package de.plugdev.cloud.internal.guiinterface.panels.sub;

import javax.swing.JPanel;

import de.plugdev.cloud.lang.ApiStatus.Experimental;
import javax.swing.JLabel;

@Experimental
public class GUISubPanel_Dashboard extends JPanel {
	
	private static final long serialVersionUID = -6594173744287309229L;

	public GUISubPanel_Dashboard() {
		setBounds(0, 0, 885, 440);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(130, 112, 440, 168);
		add(lblNewLabel);
	}
}
