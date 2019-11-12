package redes.sockets.chat.cliente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import redes.sockets.chat.common.Utils;

public class Home extends JFrame{
	/*private static final long serialVersionUID = 1L;

	private ArrayList<String> connectedUsers;
	private ArrayList<String> openedChats;
	private Map<String, ClientListener> connectedListeners;
	
	private String connectionInfo;
	private Socket connection;
	private ServerSocket server;
	private boolean running;
	
	private JLabel tittle;
	private JButton getConnected, startTalk;
	private JList list;
	private JScrollPane scroll;
	
	public Home(Socket connection ,String connectionInfo) {
		super("Chat Home");
		this.connectionInfo = connectionInfo;
		this.connection = connection;
		iniciarComponentes();
		configurarComponentes();
		inserirComponentes();
		inserirAcoes();
		start();
	}

	public void iniciarComponentes(){
		server = null;
		running = false;
		connectedListeners = new HashMap<String, ClientListener>();
		openedChats = new ArrayList<>();
		connectedUsers = new ArrayList<>();
		tittle = new JLabel("Usuario:  "+ connectionInfo.split(":")[0],SwingConstants.CENTER);
		getConnected = new JButton("Atualizar Contatos");
		startTalk = new JButton("Iniciar Conversa");
		list = new JList<String>();
		scroll = new JScrollPane(list);
	}
	
	public void configurarComponentes() {
		this.setLayout(null);
		this.setMinimumSize(new Dimension(600,480));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		
		tittle.setBounds(10, 10, 370, 40);
		tittle.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		getConnected.setBounds(400, 10, 180, 40);
		getConnected.setFocusable(false);
		
		startTalk.setBounds(10, 400, 575, 40);
		startTalk.setFocusable(false);
		
		list.setBorder(BorderFactory.createTitledBorder("Usuarios Online"));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scroll.setBounds(10, 60, 575, 335);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(null);
		
	}
	
	public void inserirComponentes() {
		this.add(tittle);
		this.add(getConnected);
		this.add(startTalk);
		this.add(scroll);
	}
	
	public void inserirAcoes() {
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				running = false;
				Utils.sendMessage(connection, "QUIT");
				System.out.println("Conexao Encerrada.");
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		getConnected.addActionListener(event -> getConnectedUser());
		startTalk.addActionListener(event -> openChat());
	}
	
	public void start() {
		this.pack();
		this.setVisible(true);
		startServer(this, Integer.parseInt(connectionInfo.split(":")[2]));
	}
	
	public ArrayList<String> getConnectedUsers() {
		return connectedUsers;
	}

	public void setConnectedUsers(ArrayList<String> connectedUsers) {
		this.connectedUsers = connectedUsers;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public JLabel getTittle() {
		return tittle;
	}

	public void setTittle(JLabel tittle) {
		this.tittle = tittle;
	}

	public JButton getGetConnected() {
		return getConnected;
	}

	public void setGetConnected(JButton getConnected) {
		this.getConnected = getConnected;
	}

	public JButton getStartTalk() {
		return startTalk;
	}

	public void setStartTalk(JButton startTalk) {
		this.startTalk = startTalk;
	}

	public JList getList() {
		return list;
	}

	public void setList(JList list) {
		this.list = list;
	}

	public JScrollPane getScroll() {
		return scroll;
	}

	public void setScroll(JScrollPane scroll) {
		this.scroll = scroll;
	}

	public void setOpenedChats(ArrayList<String> openedChats) {
		this.openedChats = openedChats;
	}

	public void setConnectedListeners(Map<String, ClientListener> connectedListeners) {
		this.connectedListeners = connectedListeners;
	}

	public void setConnectionInfo(String connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public String getConnectionInfo() {
		return connectionInfo;
	}

	private void getConnectedUser() {
		Utils.sendMessage(connection, "GET_CONNECTED_USERS");
		String response = Utils.receivedMessage(connection);
		list.removeAll();
		connectedUsers.clear();
		for(String info : response.split(";")) {
			if(!info.equals(connectionInfo)) {
				connectedUsers.add(info);
			}	
		}
		list.setListData(connectedUsers.toArray());
	}
	
	public ArrayList<String> getOpenedChats() {
		return openedChats;
	}

	public Map<String, ClientListener> getConnectedListeners() {
		return connectedListeners;
	}

	private void openChat() {
		
		int index = list.getSelectedIndex();
		if(index != -1) {
			String connectionInfo = list.getSelectedValue().toString();
			String[] splited = connectionInfo.split(":");
			if(!openedChats.contains(connectionInfo)) {
				try {
					Socket connection = new Socket(splited[1], Integer.parseInt(splited[2]));
					Utils.sendMessage(connection, "OPEN_CHAT;" + this.connectionInfo);
					ClientListener cl = new ClientListener(this, connection);
					cl.setChat(new Chat(this, connection, connectionInfo, this.connectionInfo.split(":")[0]));
					cl.setChatOpen(true);
					connectedListeners.put(connectionInfo, cl);
					openedChats.add(connectionInfo);
					new Thread(cl).start();
					} catch (NumberFormatException | IOException e) {
					System.err.println("[Home: OpenChat] -> " + e.getMessage());
				}
			}
		}
	}
	
	private void startServer(Home home, int port) {
		new Thread() {
			@Override
			public void run() {
				running = true;
				try {
					server = new ServerSocket(port);
					System.out.println("Servidor Cliente iniciado na porta "+ port);
					while(running) {
						Socket connection = server.accept();
						ClientListener cl = new ClientListener(home, connection);
						new Thread(cl).start();
					}
				}catch(IOException e) {
					System.err.println("[Home: StartServer] -> "+ e.getMessage());
				}
			}
		}.start();
	}*/
}
