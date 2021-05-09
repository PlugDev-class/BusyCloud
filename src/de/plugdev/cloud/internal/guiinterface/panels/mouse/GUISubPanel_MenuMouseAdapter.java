package de.plugdev.cloud.internal.guiinterface.panels.mouse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.guiinterface.panels.GUIPanel_Main;
import de.plugdev.cloud.internal.guiinterface.panels.sub.GUISubPanel_Dashboard;
import de.plugdev.cloud.internal.guiinterface.panels.sub.GUISubPanel_Groups;
import de.plugdev.cloud.internal.guiinterface.panels.sub.GUISubPanel_Modules;
import de.plugdev.cloud.internal.guiinterface.panels.sub.GUISubPanel_Proxies;
import de.plugdev.cloud.internal.guiinterface.panels.sub.GUISubPanel_Servers;

public class GUISubPanel_MenuMouseAdapter extends MouseAdapter {

	private SubPanel panel;
	private GUIPanel_Main mainPanel;

	public GUISubPanel_MenuMouseAdapter(GUIPanel_Main mainPanel, SubPanel subPanel) {
		this.panel = subPanel;
		this.mainPanel = mainPanel;
	}

	public static enum SubPanel {
		DASHBOARD, PROXIES, GROUPS, SERVERS, MODULES;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mainPanel.lblDashboard.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		mainPanel.lblProxies.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		mainPanel.lblGroups.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		mainPanel.lblServers.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		mainPanel.lblModules.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));

		if (mainPanel.subPanel != null) {
			mainPanel.remove(mainPanel.subPanel);
		}

		switch (this.panel) {
		case DASHBOARD:
			mainPanel.lblDashboard.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
			mainPanel.subPanel = new GUISubPanel_Dashboard();
			break;
		case PROXIES:
			mainPanel.lblProxies.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
			mainPanel.subPanel = new GUISubPanel_Proxies();
			break;
		case GROUPS:
			mainPanel.lblGroups.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
			mainPanel.subPanel = new GUISubPanel_Groups();
			break;
		case SERVERS:
			mainPanel.lblServers.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
			mainPanel.subPanel = new GUISubPanel_Servers();
			break;
		case MODULES:
			mainPanel.lblModules.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
			mainPanel.subPanel = new GUISubPanel_Modules();
		default:
			break;
		}


		mainPanel.subPanel.setBounds(87, 128, 722, 470);
		mainPanel.subPanel.setBorder(null);
		mainPanel.subPanel.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		mainPanel.subPanel.setLayout(null);
		mainPanel.add(mainPanel.subPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
		super.mousePressed(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		switch (this.panel) {
		case DASHBOARD:
			mainPanel.lblDashboard.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
			break;
		case PROXIES:
			mainPanel.lblProxies.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
			break;
		case GROUPS:
			mainPanel.lblGroups.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
			break;
		case SERVERS:
			mainPanel.lblServers.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
			break;
		case MODULES:
			mainPanel.lblModules.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
		default:
			break;
		}
		super.mouseEntered(e);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		switch (this.panel) {
		case DASHBOARD:
			mainPanel.lblDashboard.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
			break;
		case PROXIES:
			mainPanel.lblProxies.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
			break;
		case GROUPS:
			mainPanel.lblGroups.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
			break;
		case SERVERS:
			mainPanel.lblServers.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
			break;
		case MODULES:
			mainPanel.lblModules.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
		default:
			break;
		}
		super.mouseExited(e);
	}

}
