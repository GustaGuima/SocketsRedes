package redes.sockets.chat.cliente;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import redes.sockets.chat.common.Utils;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import java.awt.Font;

public class TelaHome{

	private JFrame msn;
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
	JScrollPane usuariosOnline;
	private JTextField textField;

	
	public TelaHome(Socket connection, String connectionInfo) {
		this.connection = connection;
		this.connectionInfo = connectionInfo;
		iniciarComponentes();
		start();
		initialize();
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
		list.setBackground(Color.WHITE);
		list.setForeground(Color.BLACK);
		usuariosOnline = new JScrollPane(list);
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
	
	public void setConnectionInfo(String connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public String getConnectionInfo() {
		return connectionInfo;
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
	
	private void startServer(TelaHome home, int port) {
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
	}
	
	public void start() {
		startServer(this, Integer.parseInt(connectionInfo.split(":")[2]));
	}
	
	
	private void initialize() {
		msn = new JFrame();
		msn.setVisible(true);
		msn.getContentPane().setBackground(Color.WHITE);
		msn.getContentPane().setLayout(null);
		msn.addWindowListener(new WindowListener() {
			
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
		
		JPanel cabecalho = new JPanel();
		cabecalho.setBackground(Color.WHITE);
		cabecalho.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		cabecalho.setBounds(10, 11, 304, 92);
		msn.getContentPane().add(cabecalho);
		cabecalho.setLayout(null);
		
		JLabel picture = new JLabel("");
		picture.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		picture.setIcon(new ImageIcon("C:\\Users\\SAC4 - PROGAT\\Desktop\\Chat_sockets\\Imagens\\no_picture.png"));
		picture.setBounds(10, 11, 64, 56);
		cabecalho.add(picture);
		
		JLabel status = new JLabel(connectionInfo.split(":")[0]+" (Online)");
		status.setBounds(84, 15, 210, 14);
		cabecalho.add(status);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 9));
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setText("<Escreva o que esta pensando>");
		textField.setBorder(null);
		textField.setBounds(84, 32, 208, 20);
		textField.addMouseListener(new MouseListener() {
		
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				textField.setText("");
			}
		});
		cabecalho.add(textField);
		textField.setColumns(10);
		
		JLabel miniLogo = new JLabel("");
		miniLogo.setHorizontalAlignment(SwingConstants.CENTER);
		miniLogo.setIcon(new ImageIcon("C:\\Users\\SAC4 - PROGAT\\Desktop\\Chat_sockets\\Imagens\\logo_pequeno.png"));
		miniLogo.setBounds(277, 100, 65, 34);
		msn.getContentPane().add(miniLogo);
		
		JPanel painelUsuarios = new JPanel();
		painelUsuarios.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		painelUsuarios.setBounds(10, 111, 304, 402);
		msn.getContentPane().add(painelUsuarios);
		painelUsuarios.setLayout(null);
		
		list.setBorder(BorderFactory.createTitledBorder("Usuarios Online"));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		usuariosOnline.setBounds(0, 0, 304, 402);
		usuariosOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		usuariosOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		usuariosOnline.setBorder(null);
		painelUsuarios.add(usuariosOnline);
		
		JButton botaoAtualizar = new JButton("Atualizar Contatos");
		botaoAtualizar.setDefaultCapable(false);
		botaoAtualizar.setFocusTraversalKeysEnabled(false);
		botaoAtualizar.setFocusPainted(false);
		botaoAtualizar.setFocusable(false);
		botaoAtualizar.setRequestFocusEnabled(false);
		botaoAtualizar.setRolloverEnabled(false);
		botaoAtualizar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.CYAN));
		botaoAtualizar.setBackground(Color.WHITE);
		botaoAtualizar.setOpaque(false);
		botaoAtualizar.setBounds(10, 524, 130, 23);
		botaoAtualizar.addActionListener(event -> getConnectedUser());
		msn.getContentPane().add(botaoAtualizar);
		
		JButton botaoIniciarConversa = new JButton("Iniciar Conversa");
		botaoIniciarConversa.setRequestFocusEnabled(false);
		botaoIniciarConversa.setRolloverEnabled(false);
		botaoIniciarConversa.setFocusable(false);
		botaoIniciarConversa.setOpaque(false);
		botaoIniciarConversa.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.CYAN));
		botaoIniciarConversa.setBackground(Color.WHITE);
		botaoIniciarConversa.setBounds(184, 524, 130, 23);
		botaoIniciarConversa.addActionListener(event -> openChat());
		msn.getContentPane().add(botaoIniciarConversa);
		
		JLabel Fundo = new JLabel("");
		Fundo.setIcon(new ImageIcon("C:\\Users\\SAC4 - PROGAT\\Desktop\\Chat_sockets\\Imagens\\Azul.jpg"));
		Fundo.setBounds(0, 0, 324, 562);
		msn.getContentPane().add(Fundo);
		msn.setBounds(100, 100, 340, 594);
		msn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	}
}
