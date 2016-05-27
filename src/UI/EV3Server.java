package UI;

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import BE.MyRobot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class EV3Server {

	DataInputStream dis;
	DataOutputStream dos;
	Socket socket;
	ServerSocket serverSocket;
	
	boolean done = false;
	boolean followPath = false;
	float posX, posY, heading;

	MyRobot robot;

	public EV3Server(int serverSocketPort) {
		robot = new MyRobot();

		try {
			serverSocket = new ServerSocket(serverSocketPort);

			LCD.clear();
			LCD.drawString("Waiting for client connection...", 0, 0);

			socket = serverSocket.accept();

			LCD.clear();
			LCD.drawString("Client connected", 0, 0);

			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			waitForRequests();

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void waitForRequests() {
		while (!done || !Button.ESCAPE.isDown()) {
			readStringMessage();
		}
	}

	private void readStringMessage() {
		try {
			String clientMessage = dis.readUTF();
			if (followPath) {
				if (isCordinates(clientMessage)) {
					goToDestination();
				}
			}
			if (clientMessage.equalsIgnoreCase("follow")) {
				followPath = true;
			} else if (clientMessage.equalsIgnoreCase("stop")) {
				followPath = false;
			} else if (clientMessage.equalsIgnoreCase("quit")) {
				close();
			}else {
				writeToLCD("I'm not allowed to move!!!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void writeToLCD(String message) {
		LCD.clear();
		LCD.drawString(message, 0, 0);
	}

	private boolean isCordinates(String message) {
		String[] cordinates = message.split("\\|");

		try {
			posX = Float.parseFloat(cordinates[0]);
			posY = Float.parseFloat(cordinates[1]);
			heading = Float.parseFloat(cordinates[2]);
		
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void close() throws IOException{
		done = true;
		robot.close();
		socket.close();
		serverSocket.close();
		dis.close();
		dos.close();
		System.exit(0);
	}
	
	private void goToDestination(){
		writeToLCD("Positioning at: ");
		System.out.println("X: " + posX);
		System.out.println("Y: " + posY);
		robot.goToDestination(posX, posY, heading);
		
	}

}