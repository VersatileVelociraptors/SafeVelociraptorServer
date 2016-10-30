package com.github.versatilevelociraptor.safevelociraptorserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

	public static final int PORT = 2585;
	public static final int UDPPORT = 2587;
	private ServerSocket serverSocket;
	private int robotControllerPort;
	private InetAddress robotAddress;
	private Controller controller;
	private RobotController roboController;
	private AtomicInteger totalClients;
	public static final int MAX_CLIENTS = 2;

	public Server() {
		totalClients = new AtomicInteger(0);
		try {
			serverSocket = new ServerSocket(PORT);
			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeConnections();
	}

	public void initializeConnections() {
		Socket client;
		boolean controlerConnected = false;
		new Thread(new AwaitUDPThread()).start();
		while(!controlerConnected) {
			try {
				System.out.println("Looking for clients..");
				client = serverSocket.accept();
				System.out.println("Loocking for clients");
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String ID = reader.readLine();
				System.out.println(ID);
				if(ID.equals("" + Controller.ID) && !controlerConnected) {
					controller = new Controller(client);
					controller.sendCommand("Michael is a gypsy");
					System.out.println("Client Connected!");
					controlerConnected = true;
					totalClients.getAndIncrement();
				}  else {
					System.out.println("NOT CORRECT ID");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		while(totalClients.get() != 2) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void restart() {
		initializeConnections();
		createCommunicationSession();
	}

	public synchronized void setController() {

	}

	public void createCommunicationSession() {
		System.out.println("Created");
		new Thread(new ServerThread(controller, roboController)).start();
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.createCommunicationSession();
	}

	private class AwaitUDPThread implements Runnable {
		private DatagramSocket socket;
		public AwaitUDPThread() {
			try {
				socket = new DatagramSocket(2587);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
		}
		@Override
		public void run() {
			byte[] buf = new byte[1];
			DatagramPacket packet = new DatagramPacket(buf, 1);
			while(buf[0] != 118) {
				try {
					System.out.println("I WANT MA PACKETS");
					socket.receive(packet);
					System.out.println("OOH BABY A TRIPLE");
					Thread.sleep(10);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Client connected!");
			robotControllerPort = packet.getPort();
			roboController = new RobotController(null , packet.getAddress(), robotControllerPort);
			byte[] arr = new byte[1];
			arr[0] = 32;
			roboController.sendBytes(arr);
			totalClients.getAndIncrement();
		}
	}

	private class ServerThread implements Runnable{
		private Client send;
		private Client recieve;

		public ServerThread(Client send, Client recieve) {
			this.send = send;
			this.recieve = recieve;
		}

		public boolean isInDeadZone(double value) {
			return value == 0;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				while(true) {
					String command = send.recieveCommand();
					int[] data;
					if(command != null) {
						data = Arrays.asList(command.split(" ")).stream().map(s -> Integer.parseInt(s)).mapToInt(Integer::intValue).toArray();
						//System.out.println(Arrays.toString(data));
						byte []arr = new byte[1];
						if(recieve instanceof RobotController) {
							if(isInDeadZone(data[0]) && isInDeadZone(data[1]) && isInDeadZone(data[2])) {
								arr[0] = 7;
								///if(!sentStop)
								System.out.println(arr[0]);
								((RobotController)recieve).sendBytes(arr);
								//recieve.sendCommand("S");
								//sentStop = true;
							}
							else {
								arr[0] = (byte)(data[0] > 0 ? 1 : 2);
								((RobotController)recieve).sendBytes(arr);
								System.out.println(arr[0]);
								//recieve.sendCommand(data[0] > 0 ? "L" : "R");
								if(data[2] > 0) {
									arr[0] = 3;
									((RobotController)recieve).sendBytes(arr);
									System.out.println(arr[0]);
								}
									
									//3 recieve.sendCommand("F");
								arr[0] = (byte)(data[1] > 0 ? 6 : 5);
								System.out.println(arr[0]);
								((RobotController)recieve).sendBytes(arr);
								//recieve.sendCommand(data[1] > 0 ? "D6" : "U5");
							}
						}
					}

					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				recieve.sendInt(2587);
				send = null;
				while(send == null) {
					try {
						Socket sock = serverSocket.accept();
						send = new Controller(sock);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} 
		}	
	}
}


//You will never resolve SD1