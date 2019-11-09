package redes.sockets.chat.cliente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import redes.sockets.chat.common.Utils;
import redes.sockets.chat.server.Server;

public class Login extends JFrame{
	private static final long serialVersionUID = 1L;
	
	
	private JButton botaoLogin;
	private JLabel labelUsuario, labelPorta, labelTitulo;
	private JTextField fieldUsuario, fieldPorta;
	
	public Login() {
		super("Login");
		iniciarComponentes();
		configurarComponentes();
		inserirComponentes();
		inserirAcoes();
		start();
	}
	
	public void iniciarComponentes(){
		botaoLogin = new JButton("Entrar");
		labelUsuario = new JLabel("Usuario", SwingConstants.CENTER);
		labelPorta = new JLabel("Porta", SwingConstants.CENTER);
		labelTitulo = new JLabel();
		fieldUsuario = new JTextField();
		fieldPorta = new JTextField();
		
		labelTitulo.setBounds(10, 10, 375, 100);
		ImageIcon icon = new ImageIcon("logo.png");
		labelTitulo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH)));
		
		botaoLogin.setBounds(10, 220, 375, 40);
		
		labelUsuario.setBounds(10, 120, 100, 40);
		labelUsuario.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		labelPorta.setBounds(10, 170, 100, 40);
		labelPorta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		fieldUsuario.setBounds(120, 120, 265, 40);
		fieldPorta.setBounds(120, 170, 265, 40);
		
	}
	
	public void configurarComponentes() {
		this.setLayout(null);
		this.setMinimumSize(new Dimension(400, 300));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		
	}
	
	public void inserirComponentes() {
		this.add(botaoLogin);
		this.add(labelTitulo);
		this.add(labelPorta);
		this.add(labelUsuario);
		this.add(fieldUsuario);
		this.add(fieldPorta);
	}
	
	public void inserirAcoes() {
		botaoLogin.addActionListener(event -> {
			try {
				String username = fieldUsuario.getText();
				fieldUsuario.setText("");
				int port = Integer.parseInt(fieldPorta.getText());
				fieldPorta.setText("");
				Socket connection = new Socket(Server.host, Server.port);
				String connectionInfo = (username + ":" + connection.getLocalAddress().getHostAddress() + ":" + port);
				Utils.sendMessage(connection, connectionInfo);
				if(Utils.receivedMessage(connection).equals("SUCESS")){
					new Home(connection, connectionInfo);
					this.dispose();
				}else {
					JOptionPane.showMessageDialog(null, "Algum usuario ja esta conectado com esse apelido ou nesse host e porta, tente outra porta");
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao conectar, verifique se o servidor esta em execuçao");
			}
		});
	}
	
	public void start() {
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Login login = new Login();
	}
}
