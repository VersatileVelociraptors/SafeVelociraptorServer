package com.github.versatilevelociraptor.safevelociraptorserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class RobotController extends Client{
	public static final int ID = 118;
	private InetAddress address;
	private DatagramSocket sock;
	private int port;
	public RobotController(Socket socket, InetAddress add, int port) {
		super(socket);
		try {
			sock = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.port = port;
		address = add;
	}
	
	/**
	 * @param arr the byte array to send
	 */
	public void sendBytes(byte[] arr) {
		DatagramPacket packet = new DatagramPacket(arr, 1, address, port);
		try {
			sock.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.github.versatilevelociraptor.safevelociraptorserver.Client#sendInt(int)
	 */
	@Override
	public void sendInt(int num) {
		byte[] arr = new byte[1];
		arr[0] = (byte) num;
		DatagramPacket packet = new DatagramPacket(arr, 1, address, 32);
		try {
			sock.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.github.versatilevelociraptor.safevelociraptorserver.Client#sendCommand(java.lang.String)
	 */
	@Override
	public void sendCommand(String command) {
		super.sendCommand(command);
	}
	
	

}
