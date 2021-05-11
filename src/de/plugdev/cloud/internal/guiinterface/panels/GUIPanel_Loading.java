package de.plugdev.cloud.internal.guiinterface.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.guiinterface.interfaces.Messageable;
import de.plugdev.cloud.lang.ApiStatus.Experimental;
import de.plugdev.cloud.lang.ApiStatus.Internal;

@Experimental
@Internal
public class GUIPanel_Loading extends JPanel implements Messageable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6185462549731217530L;
	JLabel lblStatus1;
	public GUIPanel_Loading() {
		setSize(450 * 2, 300 * 2);
		setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
		setLayout(null);

		JLabel lblBusyCloud = new JLabel("BusyCloud");
		lblBusyCloud.setIcon(new ImageIcon(
				GUIPanel_Loading.class.getResource("/de/plugdev/cloud/internal/guiinterface/extfiles/cloud96.png")));
		lblBusyCloud.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
		lblBusyCloud.setForeground((ApplicationInterface.getAPI().getGuiInterface().getColorById(5)));
		lblBusyCloud.setHorizontalAlignment(SwingConstants.CENTER);
		lblBusyCloud.setBounds(0, 0, 900, 500);
		add(lblBusyCloud);

		lblStatus1 = new JLabel("tempStatus");
		lblStatus1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStatus1.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
		lblStatus1.setForeground((ApplicationInterface.getAPI().getGuiInterface().getColorById(5)));
		lblStatus1.setBounds(302, 286, 299, 14);
		add(lblStatus1);

		JLabel lblCodedBy = new JLabel("Developed by PlugDev, SumoKadaver");
		lblCodedBy.setBounds(302, 300, 299, 14);
		lblCodedBy.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
		lblCodedBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCodedBy.setForeground((ApplicationInterface.getAPI().getGuiInterface().getColorById(5)));
		add(lblCodedBy);

		JLabel lblVersion = new JLabel("BusyCloud | v1.0" + CloudInstance.currentSubversion);
		lblVersion.setBounds(302, 314, 299, 14);
		lblVersion.setFont(new Font("Segoe UI Black", Font.BOLD, 10));
		lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersion.setForeground((ApplicationInterface.getAPI().getGuiInterface().getColorById(5)));
		add(lblVersion);
		
		ApplicationInterface.getAPI().getGuiInterface().switchPanel(new GUIPanel_Main());
	}

	@Override
	public void pushMessage(String message) {
		lblStatus1.setText(message);
	}
	
	public final int MAX_RGB_VALUE = 255;
	public Color invert(Color c) {
        int a = c.getAlpha();
        int r = MAX_RGB_VALUE - c.getRed();
        int g = MAX_RGB_VALUE - c.getGreen();
        int b = MAX_RGB_VALUE - c.getBlue();

        if ((r + g + b > 740) || (r + g + b < 20)) {
            return new Color(MAX_RGB_VALUE, MAX_RGB_VALUE, 40, a);
        } else {
            return new Color(r, g, b, a);
        }
    }
}
