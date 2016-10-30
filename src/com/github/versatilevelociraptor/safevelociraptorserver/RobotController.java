package com.github.versatilevelociraptor.safevelociraptorserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class RobotController extends Client{
	public static final int ID = 118;
	private InetAddress address;
	private DatagramSocket sock;
	public RobotController(Socket socket, InetAddress add) {
		super(socket);
		try {
			sock = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		address = add;
	}
	/* (non-Javadoc)
	 * @see com.github.versatilevelociraptor.safevelociraptorserver.Client#sendInt(int)
	 */
	@Override
	public void sendInt(int num) {
		//DatagramPacket packet = new Data
		super.sendInt(num);
	}
	/* (non-Javadoc)
	 * @see com.github.versatilevelociraptor.safevelociraptorserver.Client#sendCommand(java.lang.String)
	 */
	@Override
	public void sendCommand(String command) {
		super.sendCommand(command);
	}
	
	

}
