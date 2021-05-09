package de.plugdev.cloud.internal.guiinterface.panels.sub;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.guiinterface.panels.GUIFrame_ServerGroup;
import de.plugdev.cloud.internal.guiinterface.panels.mouse.GUISubPanel_PanelDashboardMouseAdapter;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.plugdev.cloud.lang.ApiStatus.Experimental;

@Experimental
public class GUISubPanel_Dashboard extends JPanel {
	
	private static final long serialVersionUID = -6594173744287309229L;
	
	public JPanel panel;
	public JLabel lblArrow;
	
	public GUISubPanel_Dashboard() {
		setLayout(null);
		setBounds(0, 0, 722, 470);
		
		panel = new JPanel();
		panel.setBounds(224, 5, 1, 1);
		panel.setBorder(new LineBorder(ApplicationInterface.getAPI().getGuiInterface().getColorById(8), 2));
		panel.setBounds(10, 11, 702, 418);
		panel.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		panel.setLayout(null);
		add(panel);
		
		addMouseListener(new GUISubPanel_PanelDashboardMouseAdapter(this));
		
		JLabel lblNewLabel = new JLabel("BusyCloud | v1.0" + CloudInstance.currentSubversion + " | *click to open*");
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 2, 646, 26);
		lblNewLabel.setForeground(Color.WHITE);
		panel.add(lblNewLabel);
		
		pushGeneralInformations();
		pushProxy();
		pushServergroup();
	}
	
	public void pushGeneralInformations() {
		JLabel lblTitle1 = new JLabel("General informations");
		lblTitle1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTitle1.setForeground(Color.WHITE);
		lblTitle1.setBounds(10, 40, 682, 14);
		panel.add(lblTitle1);
		
		JLabel lblCloudname = new JLabel("Cloudname: BusyCloud >> fork NoFork");
		lblCloudname.setBounds(20, 56, 672, 14);
		lblCloudname.setForeground(Color.WHITE);
		panel.add(lblCloudname);
		
		JLabel lblCloudauthor = new JLabel("Cloudauthor: PlugDev, SumoKadaver");
		lblCloudauthor.setForeground(Color.WHITE);
		lblCloudauthor.setBounds(20, 84, 672, 14);
		panel.add(lblCloudauthor);
		
		JLabel lblCloudversion = new JLabel("Cloudversion: v1.0" + CloudInstance.currentSubversion);
		lblCloudversion.setForeground(Color.WHITE);
		lblCloudversion.setBounds(20, 70, 672, 14);
		panel.add(lblCloudversion);
		
		JLabel lblCloudnetworkname = new JLabel("Specific networkname:");
		lblCloudnetworkname.setForeground(Color.WHITE);
		lblCloudnetworkname.setBounds(20, 98, 672, 14);
		panel.add(lblCloudnetworkname);
		
		JLabel lblTotalProxies = new JLabel("Total running Proxies: " + ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size());
		lblTotalProxies.setForeground(Color.WHITE);
		lblTotalProxies.setBounds(20, 122, 672, 14);
		panel.add(lblTotalProxies);
		
		JLabel lblTotalGroups = new JLabel("Total active Servergroups: " + ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().size());
		lblTotalGroups.setForeground(Color.WHITE);
		lblTotalGroups.setBounds(20, 136, 672, 14);
		panel.add(lblTotalGroups);
		
		JLabel lblTotalServer = new JLabel("Total running Servers: " + ApplicationInterface.getAPI().getInfrastructure().getRunningServers().size());
		lblTotalServer.setForeground(Color.WHITE);
		lblTotalServer.setBounds(20, 150, 672, 14);
		panel.add(lblTotalServer);
		
	}

	int currentY = 192;
	public void pushProxy() {
		JLabel lblTitle2 = new JLabel("Proxies (in total " + ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + "):");
		lblTitle2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTitle2.setBounds(10, 176, 682, 14);
		lblTitle2.setForeground(Color.WHITE);
		panel.add(lblTitle2);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(Proxy proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
			model.addElement("Proxyname: " + proxy.getProxyName() 
			+ " | ProxyId: " + proxy.getProxyid() 
			+ " | ProxyNetKey: " + proxy.getKey() 
			+ " | Reg. servers: " + proxy.getRegisteredServer().size()
			+ " | Connected players: " + proxy.getOnlinePlayer().size());
		}
		
		JList<String> jList = new JList<>(model);
		jList.setForeground(Color.WHITE);
		jList.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setBounds(10, currentY, 682, ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size()*22);
		
		JScrollPane pane = new JScrollPane(jList);
		pane.setBounds(10, currentY, 682, ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size()*22);
		
		currentY += (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size()*22);
		
		panel.add(pane);
	}
	public void pushServergroup() {
		JLabel lblTitle2 = new JLabel("Servergroups (in total " + ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + "):");
		lblTitle2.setForeground(Color.WHITE);
		lblTitle2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTitle2.setBounds(10, currentY+=12, 682, 14);
		panel.add(lblTitle2);
		currentY += 16;
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			model.addElement("Groupname: " + group.getGroupName());
		}
		
		JList<String> jList = new JList<>(model);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setForeground(Color.WHITE);
		jList.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		jList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount() == 2) {
					String groupName = jList.getSelectedValue().replaceAll("Groupname: ", "");
					new GUIFrame_ServerGroup(ApplicationInterface.getAPI().getInfrastructure().getGroupbyName(groupName));
				}
			}
			
		});
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setBounds(10, currentY, 682, (ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().size()*22));
		
		JScrollPane pane = new JScrollPane(jList);
		pane.setBounds(10, currentY, 682, (ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().size()*22));
		
		currentY += (ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().size()*22);
		
		panel.add(pane);
	}
}
