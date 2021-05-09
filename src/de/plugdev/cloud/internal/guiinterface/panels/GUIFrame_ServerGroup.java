package de.plugdev.cloud.internal.guiinterface.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;

public class GUIFrame_ServerGroup extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4578194845184855276L;

	public GUIFrame_ServerGroup(ServerGroup serverGroup) {
		setTitle("BusyCloud | Groupcontrol: " + serverGroup.getGroupName());
		setIconImage(new ImageIcon(
				getClass().getResource("/de/plugdev/cloud/internal/guiinterface/extfiles/cloud96.png")).getImage());
		setVisible(true);
		setBounds(0, 0, 720, 480);
		setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		getContentPane().setBackground(getBackground());
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("\u00BB Servergroup: \"" + serverGroup.getGroupName() + "\" \u00AB");
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
				"Servergroupname: " + serverGroup.getGroupName()
				+ "\r\nServergroupid: " + serverGroup.getGroupID()
				+ "\r\nServergroup MRES: " + serverGroup.getMaxRamEachServer()
				+ "\r\nServergroup Startport: " + serverGroup.getStartPort()
				+ "\r\nServergroup instance: " + serverGroup.getVersion().getVersion()
				+ "\r\nCurrent player total: " + serverGroup.getOnlinePlayers());
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

		JLabel lblRam = new JLabel("Random Access Memory | Workload | Maximum: " + serverGroup.getMaxRamEachServer()*serverGroup.getGroupList().size() + "MB");
		lblRam.setForeground(Color.WHITE);
		lblRam.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRam.setBounds(287, 49, 407, 14);
		getContentPane().add(lblRam);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(SpigotServer server : serverGroup.getGroupList()) {
			model.addElement("Spigotinstance: " + server.getServerName());
		}
		JList<String> jList = new JList<>(model);
		jList.setBounds(10, 203, 684, 206);
		jList.setForeground(Color.WHITE);
		jList.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByName(jList.getSelectedValue().replaceAll("Spigotinstance: ", ""));
					new GUIFrame_Server(server);
				}
				super.mousePressed(e);
			}
			
		});
		jList.setLayoutOrientation(JList.VERTICAL);

		JScrollPane jsp = new JScrollPane(jList);
		jsp.setBounds(10, 203, 684, 206);
		jsp.setAutoscrolls(true);
		getContentPane().add(jsp);
	}
	
}
