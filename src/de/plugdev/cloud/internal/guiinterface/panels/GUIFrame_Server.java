package de.plugdev.cloud.internal.guiinterface.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;
import de.plugdev.cloud.lang.ApiStatus.Experimental;
import de.plugdev.cloud.lang.ApiStatus.Internal;

@Experimental
@Internal
public class GUIFrame_Server extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1645304334028526657L;
	private JTextField textField;

	public GUIFrame_Server(SpigotServer spigotServer) {
		setTitle("BusyCloud | Servercontrol: " + spigotServer.getServerName());
		setResizable(false);
		setIconImage(new ImageIcon(
				getClass().getResource("/de/plugdev/cloud/internal/guiinterface/extfiles/cloud96.png")).getImage());
		setVisible(true);
		setBounds(0, 0, 720, 480);
		setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		getContentPane().setBackground(getBackground());
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("\u00BB Server: \"" + spigotServer.getServerName() + "\" \u00AB");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 704, 38);
		getContentPane().add(lblNewLabel);

		JPanel line = new JPanel();
		line.setBackground(Color.WHITE);
		line.setBounds(0, 39, 704, 4);
		getContentPane().add(line);

		JTextPane txtpnInformationenServernameServerid = new JTextPane();
		txtpnInformationenServernameServerid
				.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		txtpnInformationenServernameServerid.setForeground(Color.WHITE);
		txtpnInformationenServernameServerid.setEditable(false);
		txtpnInformationenServernameServerid.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		txtpnInformationenServernameServerid.setText(
				"Servername: " + spigotServer.getServerName() 
				+ "\r\nServerid: " + spigotServer.getId()
				+ "\r\nServergroup: " + spigotServer.getServerGroup()
				+ "\r\nServerinstance: " + spigotServer.getMinecraftVersion().getVersion()
				+ "\r\nServerport: " + spigotServer.getPort()
				+ "\r\nCurrent player total: " + -1);
		txtpnInformationenServernameServerid.setBounds(10, 65, 253, 112);
		getContentPane().add(txtpnInformationenServernameServerid);

		JLabel lblInformations = new JLabel("Informations:");
		lblInformations.setForeground(Color.WHITE);
		lblInformations.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblInformations.setBounds(10, 49, 253, 14);
		getContentPane().add(lblInformations);

		JPanel line2 = new JPanel();
		line2.setBackground(Color.WHITE);
		line2.setBounds(0, 188, 704, 4);
		getContentPane().add(line2);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(272, 39, 5, 153);
		getContentPane().add(panel);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBackground(Color.GREEN);
		progressBar.setForeground(Color.RED);
		progressBar.setBounds(287, 73, 407, 21);
		getContentPane().add(progressBar);

		JLabel lblRam = new JLabel("Random Access Memory | Workload | Maximum: " + spigotServer.getMaxRam() + "MB");
		lblRam.setForeground(Color.WHITE);
		lblRam.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRam.setBounds(287, 49, 407, 14);
		getContentPane().add(lblRam);

		JTextPane consolePane = new JTextPane();
		consolePane.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		consolePane.setForeground(Color.WHITE);
		consolePane.setEditable(false);
		consolePane.setBorder(new LineBorder(new Color(0, 0, 0)));
		consolePane.setBounds(10, 203, 684, 206);

		JScrollPane jsp = new JScrollPane(consolePane);
		jsp.setBounds(10, 203, 684, 206);
		jsp.setAutoscrolls(true);
		getContentPane().add(jsp);

		textField = new JTextField();
		textField.setBounds(10, 410, 684, 20);
		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (textField.getText().length() == 0) {
						return;
					} else {
						spigotServer.sendRCON(textField.getText());
						consolePane.setText(consolePane.getText() + "[Cloud] Sent rcon \"" + textField.getText()
								+ "\" to \"" + spigotServer.getServerName() + "\".\n");
						textField.setText("");
					}
				}
				super.keyPressed(e);
			}

		});
		getContentPane().add(textField);
		textField.setColumns(10);

		readLog(consolePane, spigotServer);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			int internalCounter = 500;

			@Override
			public void run() {
				if (isShowing()) {
					readLog(consolePane, spigotServer);
					internalCounter = 0;
				} else {
					internalCounter++;
				}
				if (internalCounter >= 5) {
					timer.cancel();
				}
			}

		}, 500, 500);
	}

	public void readLog(JTextPane pane, SpigotServer server) {
		try {
			List<String> strings = Files.readAllLines(server.getLatestLog().toPath());
			for (String line : strings) {
				if (!pane.getText().contains(line)) {
					if (pane.getText().length() == 0) {
						pane.setText(line + "\n");
					} else {
						pane.setText(pane.getText() + line + "\n");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
