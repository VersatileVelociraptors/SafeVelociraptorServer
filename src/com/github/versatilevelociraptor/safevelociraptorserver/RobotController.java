package com.github.versatilevelociraptor.safevelociraptorserver;

import java.net.Socket;

public class RobotController {

	public static final int ID = 118;
	private Socket socket;
	public RobotController(Socket socket) {
		this.socket = socket;
	}
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

}
