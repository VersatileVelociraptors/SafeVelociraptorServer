package com.github.versatilevelociraptor.safevelociraptorserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


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
		while(connectedClients < MAX_CLIENTS) {
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
					roboController = new RobotController(client);
					System.out.println("Client Connected!");
					roboController.sendCommand("Michael is a gypsy");
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
		new Thread(new ServerThread(roboController, controller)).start();
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
			recieve.sendCommand(send.recieveCommand());
		}	
	}
}
