package com.github.versatilevelociraptor.safevelociraptorserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client {
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	public Client(Socket socket) {
		this.socket = socket;
		try {
			if(socket != null) {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int recieveByte() throws IOException {
		return reader.read();
	}

	public byte[] getBytes() throws IOException {
		byte[] bytes = new byte[3];
		socket.getInputStream().read(bytes);
		return bytes;
	}

	public void sendChars(int num) {
		writer.print((char)num);
	}

	public String recieveCommand() {
		String command = "";
		try {
			command = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return command;
	}

	public void sendInt(int num) {
		writer.print(num);
	}

	public void sendCommand(String command) {
		writer.println(command);
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}
	/**
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return reader;
	}

}
