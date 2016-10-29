package com.github.versatilevelociraptor.safevelociraptorserver;

import java.net.Socket;

public class RobotController extends Client{
	
	public static final int ID = 118;
	
	public RobotController(Socket socket) {
		super(socket);
	}

}
