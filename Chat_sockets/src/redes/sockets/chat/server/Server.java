package redes.sockets.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import redes.sockets.chat.common.Utils;

public class Server {
	
	public static final String host = "192.168.25.93";
	public static final int port = 4444;
	
	private ServerSocket server;
	private Map<String, ClienteListener> clients;
	
	public Server() {
		try {
			String connectionInfo;
			clients = new HashMap<String, ClienteListener>();
			server = new ServerSocket(port);
			System.out.println("Servidor Iniciado no HOST: "+ host + " e PORTA: "+ port);
			while(true) {
				Socket connection = server.accept();
				connectionInfo = Utils.receivedMessage(connection);
				if(checkLogin(connectionInfo)) {
					ClienteListener cl = new ClienteListener(connectionInfo, connection, this);
					clients.put(connectionInfo, cl);
					Utils.sendMessage(connection, "SUCESS");
					new Thread(cl).start();
				}else {
					Utils.sendMessage(connection, "ERROR");
				}
				
			}
		} catch (IOException e) {
			System.err.println("[ERROR: SERVER] -> "+ e.getMessage());
		}
		
	}
	
	public Map<String, ClienteListener> getClients(){
		return clients;
	}
	
	private boolean checkLogin(String connectionInfo) {
		String[] splited = connectionInfo.split(":");
		
		for(Map.Entry<String, ClienteListener> pair : clients.entrySet()) {
			String[] parts = pair.getKey().split(":");
			if(parts[0].toLowerCase().equals(splited[0].toLowerCase())) {
				return false;
			}else if((parts[1] + parts[2]).equals(splited[1] + splited[2])){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Server server = new Server();
	}

}
