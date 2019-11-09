package redes.sockets.chat.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import redes.sockets.chat.common.Utils;

public class ClienteListener implements Runnable{
	
	private String connectionInfo;
	private Socket connection;
	private Server server;
	private boolean running;
	
	public ClienteListener(String connectionInfo, Socket connection, Server server) {
		this.connection = connection;
		this.connectionInfo = connectionInfo;
		this.server = server;
		this.running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		running = true;
		String message;
		while(running) {
			message = Utils.receivedMessage(connection);
			if(message.equals("QUIT")) {
				server.getClients().remove(connectionInfo);
				try {
					connection.close();
				} catch (IOException e) {
					System.out.println("[ClientListener:Run] -> "  +e.getMessage());
				}
				running = false;
			}else if(message.equals("GET_CONNECTED_USERS")) {
				System.out.println("Solicitaçao de atualizar lista de contatos...");
				String response = "";
				for(Map.Entry<String, ClienteListener> pair : server.getClients().entrySet()) {
					response += (pair.getKey() + ";");
				}
				Utils.sendMessage(connection, response);
			}else {
				System.out.println("Recebido " + message);
			}
		}

	}

}
