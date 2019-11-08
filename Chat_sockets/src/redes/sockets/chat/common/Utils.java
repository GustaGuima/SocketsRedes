package redes.sockets.chat.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Utils {
	
	public static boolean sendMessage(Socket connection, String message) {
		try {
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			output.writeObject(message);
			return true;
		}catch(IOException e) {
			System.err.println("[ERROR: send Message] ->" + e.getMessage());
		}
		return false;
	}
	
	public static String receivedMessage(Socket connection) {
		String response = null;
		try {
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			response = (String)input.readObject();
		} catch (Exception e) {
			System.err.println("[ERROR: received Message] ->" + e.getMessage());
		}
		return response;
	}

}
