package redes.sockets.chat.cliente;

import java.awt.Color;
import java.awt.Dimension;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class Home extends JFrame{
	private static final long serialVersionUID = 1L;

	private String connectionInfo;
	private Socket connection;
	
	private JLabel tittle;
	private JButton getConnected, startTalk;
	private JList<?> list;
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
	}
	
	public void start() {
		this.pack();
		this.setVisible(true);
	}
	
}
