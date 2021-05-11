package de.plugdev.cloud.internal.guiinterface.panels.mouse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import de.plugdev.cloud.internal.guiinterface.panels.sub.GUISubPanel_Dashboard;
import de.plugdev.cloud.lang.ApiStatus.Experimental;
import de.plugdev.cloud.lang.ApiStatus.Internal;

@Experimental
@Internal
public class GUISubPanel_PanelDashboardMouseAdapter extends MouseAdapter {
	
	private GUISubPanel_Dashboard dashboard;
	public GUISubPanel_PanelDashboardMouseAdapter(GUISubPanel_Dashboard dashboard) {
		this.dashboard = dashboard;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(dashboard.panel.getBounds().getHeight() <= 200) {
			dashboard.panel.setBounds(dashboard.panel.getX(), dashboard.panel.getY(), dashboard.panel.getWidth(), 418);
		} else {
			dashboard.panel.setBounds(dashboard.panel.getX(), dashboard.panel.getX(), dashboard.panel.getWidth(), 31);
		}
		super.mousePressed(e);
	}
	
}
