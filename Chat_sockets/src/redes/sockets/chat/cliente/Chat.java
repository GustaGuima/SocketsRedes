package redes.sockets.chat.cliente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Chat extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JLabel tittle;
	private JEditorPane messages;
	private JTextField sendMessages;
	private JButton messagesButton;
	private JPanel panel;
	private JScrollPane scroll;
	
	
	private ArrayList<String> messageList;
	private String connectionInfo;
	
	public Chat(String connectionInfo, String tittle) {
		super("Chat - " + tittle);
		this.connectionInfo = connectionInfo;
		iniciarComponentes();
		configurarComponentes();
		inserirComponentes();
		inserirAcoes();
		start();
	}
	
	public void iniciarComponentes(){
		messageList = new ArrayList<String>();
		tittle = new JLabel(connectionInfo.split(":")[0], SwingConstants.CENTER);
		messages = new JEditorPane();
		sendMessages = new JTextField();
		messagesButton = new JButton("Enviar");
		panel = new JPanel(new BorderLayout());
		scroll = new JScrollPane(messages);
	}
	
	public void configurarComponentes() {
		this.setMinimumSize(new Dimension(480, 720));
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messages.setContentType("text/html");
		messages.setEditable(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messagesButton.setSize(100, 40);
	}
	
	public void inserirComponentes() {
		this.add(tittle, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		panel.add(sendMessages, BorderLayout.CENTER);
		panel.add(messagesButton, BorderLayout.EAST);
		

	}
	
	public void inserirAcoes() {
		messagesButton.addActionListener(event -> send());
		sendMessages.addKeyListener(new KeyListener() {
		
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				}
			}
			
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void appendMessage(String received) {
		messageList.add(received);
		String message = "";
		for(String str : messageList) {
			message += str;
		}
		messages.setText(message);
		
	}
	
	private void send() {
		if(sendMessages.getText().length() > 0) {
		DateFormat df = new SimpleDateFormat("hh:mm:ss");
		appendMessage("<b>["+ df.format(new Date())+"] Eu: </b><i>"+ sendMessages.getText() + "</i><br>");
		sendMessages.setText("");
		}
	}
	
	
	public void start() {
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Chat chat = new Chat("Gustavo:127.0.0.1", "Ali");
	}
	

}
