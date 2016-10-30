package com.github.versatilevelociraptor.safevelociraptorserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

	public static final int PORT = 2585;
	private ServerSocket serverSocket;
	private Controller controller;
	private RobotController roboController;
	public static final int MAX_CLIENTS = 2;

	public Server() {
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
		int connectedClients = 0;
		boolean controlerConnected = false , robotControllerConnected = false;
		while(connectedClients < 1) {
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
					connectedClients++;
					controlerConnected = true;
				} else if(ID.equals("" + RobotController.ID) && !robotControllerConnected) {
					InetAddress address = client.getInetAddress();
					System.out.println(address.getHostAddress() + " " + address.getHostName());
					roboController = new RobotController(client, address);
					System.out.println("Client Connected!");
					//roboController.sendCommand("Michael is a gypsy");
					connectedClients++;
					robotControllerConnected = true;
				} else {
					System.out.println("NOT CORRECT ID");
				}
			} catch (IOException e) {
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

	private class ServerThread implements Runnable{
		private Client send;
		private Client recieve;

		public ServerThread(Client send, Client recieve) {
			this.send = send;
			this.recieve = recieve;
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
						System.out.println(Arrays.toString(data));
						if(recieve instanceof RobotController) {							
							if(data[0] != 0) {
								recieve.sendCommand(data[0] > 0 ? "L" : "R");
							}
							if(data[2] > 0)
								recieve.sendCommand("F");
							if(data[1] != 0)
								recieve.sendCommand(data[1] > 0 ? "D" : "U");
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