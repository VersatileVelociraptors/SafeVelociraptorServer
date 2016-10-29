package com.github.versatilevelociraptor.safevelociraptorserver;

import java.net.Socket;

public class Controller {
	public static final int ID = 2585;
	private Socket socket;
	
	public Controller(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

}
