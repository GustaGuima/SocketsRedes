package redes.sockets.chat.cliente;

import java.io.IOException;
import java.net.Socket;

import redes.sockets.chat.common.Utils;

public class ClientListener implements Runnable{

	private boolean running;
	private Socket connection;
	private TelaHome home;
	private boolean chatOpen;
	private String connectionInfo;
	private Chat chat;
	
	public ClientListener(TelaHome telaHome, Socket connection) {
		this.home = telaHome;
		this.connection = connection;
		this.chatOpen = false;
		this.running = false;
		this.connectionInfo = null;
		this.chat = null;
	}
	
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}

	public Socket getConnection() {
		return connection;
	}
	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public TelaHome getHome() {
		return home;
	}
	public void setHome(TelaHome home) {
		this.home = home;
	}

	public boolean isChatOpen() {
		return chatOpen;
	}
	public void setChatOpen(boolean chatOpen) {
		this.chatOpen = chatOpen;
	}

	public String getConnectionInfo() {
		return connectionInfo;
	}
	public void setConnectionInfo(String connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public Chat getChat() {
		return chat;
	}
	public void setChat(Chat chat) {
		this.chat = chat;
	}


	@Override
	public void run() {
		running = true;
		String message;
		while(running) {
			message = Utils.receivedMessage(connection);
			if(message == null || message.equals("CHAT_CLOSED")) {
				if(chatOpen) {
					home.getOpenedChats().remove(connectionInfo);
					home.getConnectedListeners().remove(connectionInfo);
					chatOpen = false;
					try {
						connection.close();
					}catch(IOException e) {
						System.err.println("[ClientListener:Run] " + e.getMessage());
					}
					chat.dispose();
				}
				running = false;
			}else {
				String[] fields = message.split(";");
				if(fields.length > 1) {
					if(fields[0].equals("OPEN_CHAT")) {
						String[] splited = fields[1].split(":");
						connectionInfo = fields[1];
						if(!chatOpen) {
							System.out.println("Chat Aberto");
							home.getOpenedChats().add(connectionInfo);
							home.getConnectedListeners().put(connectionInfo, this);
							chatOpen = true;
							chat = new Chat(home, connection, connectionInfo, home.getConnectionInfo());
						}
					}else if(fields[0].equals("MESSAGE")) {
						String msg = "";
						for(int i = 1; i < fields.length; i++) {
							msg +=fields[i];
							if(i > 1) {
								msg += ";";
							}
						}
						chat.appendMessage(msg);
					}
				}
			} 
			System.out.println(">> Mensagem: "+ message);
		}
		
	}

}
