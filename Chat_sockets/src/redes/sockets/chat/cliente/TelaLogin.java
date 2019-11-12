package redes.sockets.chat.cliente;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import redes.sockets.chat.common.Utils;
import redes.sockets.chat.server.Server;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaLogin extends JFrame{
	private static final long serialVersionUID = 1L;
	private static TelaLogin window = new TelaLogin();
	private JFrame frame;
	private JTextField fieldUsuario;
	private JTextField fieldPorta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);		
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 293, 483);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSign = new JLabel("Sign in");
		lblSign.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
		lblSign.setHorizontalAlignment(SwingConstants.CENTER);
		lblSign.setBounds(103, 160, 71, 22);
		frame.getContentPane().add(lblSign);
		
		fieldUsuario = new JTextField();
		fieldUsuario.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fieldUsuario.setText("");
			}
		});
		fieldUsuario.setText("Username");
		fieldUsuario.setBounds(41, 212, 197, 20);
		frame.getContentPane().add(fieldUsuario);
		fieldUsuario.setColumns(10);
		
		fieldPorta = new JTextField();
		fieldPorta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fieldPorta.setText("");
			}
		});
		fieldPorta.setText("Port");
		fieldPorta.setBounds(41, 243, 197, 20);
		frame.getContentPane().add(fieldPorta);
		fieldPorta.setColumns(10);
		
		JLabel label_1 = new JLabel("");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setIcon(new ImageIcon("C:\\Users\\SAC4 - PROGAT\\Desktop\\Chat_sockets\\Imagens\\logo_msn.png"));
		label_1.setBounds(0, 42, 277, 157);
		frame.getContentPane().add(label_1);
		
		JButton btnSignIn = new JButton("Sign in");
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String username = fieldUsuario.getText();
					fieldUsuario.setText("");
					int port = Integer.parseInt(fieldPorta.getText());
					fieldPorta.setText("");
					Socket connection = new Socket(Server.host, Server.port);
					String connectionInfo = (username + ":" + connection.getLocalAddress().getHostAddress() + ":" + port);
					Utils.sendMessage(connection, connectionInfo);
					if(Utils.receivedMessage(connection).equals("SUCESS")){
						new TelaHome(connection, connectionInfo);
						window.frame.dispose();
					}else {
						JOptionPane.showMessageDialog(null, "Algum usuario ja esta conectado com esse apelido ou nesse host e porta, tente outra porta");
					}
				} catch (IOException i) {
					JOptionPane.showMessageDialog(null, "Erro ao conectar, verifique se o servidor esta em execuçao");
				}
			}
		});
		btnSignIn.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.CYAN));
		btnSignIn.setBackground(Color.WHITE);
		btnSignIn.setBounds(85, 295, 89, 23);
		frame.getContentPane().add(btnSignIn);
		
		JLabel lblVersionBeta = new JLabel("Version Beta");
		lblVersionBeta.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblVersionBeta.setForeground(Color.GRAY);
		lblVersionBeta.setBounds(10, 419, 71, 14);
		frame.getContentPane().add(lblVersionBeta);
		
		JLabel label = new JLabel("");
		label.setBackground(Color.LIGHT_GRAY);
		label.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		label.setBounds(29, 197, 219, 87);
		frame.getContentPane().add(label);
	}
}
