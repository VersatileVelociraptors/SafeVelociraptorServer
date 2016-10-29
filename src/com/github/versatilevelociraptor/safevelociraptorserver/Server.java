package com.github.versatilevelociraptor.safevelociraptorserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public static final int PORT = 8005;
	private ServerSocket serverSocket;
	private Controller controller;
	private RobotController roboController;
	public static final int MAX_CLIENTS = 2;
	public Server() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeConnections();
	}
	
	public void initializeConnections() {
		Socket client;
		int connectedClients = 0;
		while(connectedClients < MAX_CLIENTS) {
			try {
				client = serverSocket.accept();
				connectedClients++;
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String ID = reader.readLine();
				if(Integer.parseInt(ID) == Controller.ID) {
					controller = new Controller(client);
					connectedClients++;
				} else if(Integer.parseInt(ID) == Controller.ID) {
					roboController = new RobotController(client);
					connectedClients++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	public static void main(String[] args) {
		Server server = new Server();
	}

}
