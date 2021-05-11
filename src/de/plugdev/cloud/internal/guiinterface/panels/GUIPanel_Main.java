package de.plugdev.cloud.internal.guiinterface.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.guiinterface.panels.mouse.GUISubPanel_MenuMouseAdapter;
import de.plugdev.cloud.internal.guiinterface.panels.mouse.GUISubPanel_MenuMouseAdapter.SubPanel;
import de.plugdev.cloud.lang.ApiStatus.Experimental;
import de.plugdev.cloud.lang.ApiStatus.Internal;

@Experimental
@Internal
public class GUIPanel_Main extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5189595416954810595L;

	public JLabel lblDashboard;
	public JLabel lblProxies;
	public JLabel lblGroups;
	public JLabel lblServers;
	public JLabel lblModules;
	
	public JPanel subPanel;
	
	@Internal
	public GUIPanel_Main() {
		setSize(450 * 2, 300 * 2);
		setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		setLayout(null);
		labelPanel.setBounds(0, 0, 900, 92);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		menuPanel.setBounds(160, 80, 577, 36);
		add(menuPanel);
		menuPanel.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
		menuPanel.setLayout(null);
		
		labelPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		labelPanel.setLayout(null);
		labelPanel.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(7));
		add(labelPanel);
		
		JLabel lblBusyCloud = new JLabel("BusyCloud");
		lblBusyCloud.setIcon(new ImageIcon(getClass().getResource("/de/plugdev/cloud/internal/guiinterface/extfiles/cloud96.png")));
		lblBusyCloud.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
		lblBusyCloud.setForeground((ApplicationInterface.getAPI().getGuiInterface().getColorById(5)));
		lblBusyCloud.setHorizontalAlignment(SwingConstants.CENTER);
		lblBusyCloud.setBounds(0, 10, 900, 75);
		labelPanel.add(lblBusyCloud);
		
		lblDashboard = new JLabel("Dashboard");
		lblDashboard.setOpaque(true);
		lblDashboard.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblDashboard.setHorizontalAlignment(SwingConstants.CENTER);
		lblDashboard.addMouseListener(new GUISubPanel_MenuMouseAdapter(this, SubPanel.DASHBOARD));
		lblDashboard.setBounds(0, 1, 139, 34);
		menuPanel.add(lblDashboard);
		
		lblProxies = new JLabel("Proxies");
		lblProxies.setOpaque(true);
		lblProxies.setHorizontalAlignment(SwingConstants.CENTER);
		lblProxies.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblProxies.addMouseListener(new GUISubPanel_MenuMouseAdapter(this, SubPanel.PROXIES));
		lblProxies.setBounds(138, 1, 102, 34);
		menuPanel.add(lblProxies);
		
		lblGroups = new JLabel("Groups");
		lblGroups.setOpaque(true);
		lblGroups.setHorizontalAlignment(SwingConstants.CENTER);
		lblGroups.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblGroups.setBounds(239, 1, 104, 34);
		lblGroups.addMouseListener(new GUISubPanel_MenuMouseAdapter(this, SubPanel.GROUPS));
		menuPanel.add(lblGroups);
		
		lblServers = new JLabel("Servers");
		lblServers.setOpaque(true);
		lblServers.setHorizontalAlignment(SwingConstants.CENTER);
		lblServers.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblServers.setBounds(342, 1, 121, 34);
		lblServers.addMouseListener(new GUISubPanel_MenuMouseAdapter(this, SubPanel.SERVERS));
		menuPanel.add(lblServers);
		
		lblModules = new JLabel("Modules");
		lblModules.setOpaque(true);
		lblModules.setHorizontalAlignment(SwingConstants.CENTER);
		lblModules.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblModules.setBounds(462, 1, 115, 34);
		lblModules.addMouseListener(new GUISubPanel_MenuMouseAdapter(this, SubPanel.MODULES));
		menuPanel.add(lblModules);
		
		subPanel = new RoundedPanel(null, 200);
		subPanel.setBounds(87, 128, 722, 470);
		subPanel.setBorder(null);
		subPanel.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(5));
		subPanel.setLayout(null);
		add(subPanel);

		lblDashboard.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(9));
		lblProxies.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		lblGroups.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		lblServers.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		lblModules.setForeground(ApplicationInterface.getAPI().getGuiInterface().getColorById(8));
		
		lblDashboard.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(6));
		lblProxies.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
		lblGroups.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
		lblServers.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
		lblModules.setBackground(ApplicationInterface.getAPI().getGuiInterface().getColorById(4));
		
		revalidate();
		repaint();
	}
	
	public final int MAX_RGB_VALUE = 255;
	private final JPanel labelPanel = new JPanel();
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
	
	ImageIcon resizeIcon(ImageIcon icon, int targetWidth, int targetHeight) {
		try {
			BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		    Graphics2D graphics2D = resizedImage.createGraphics();
		    graphics2D.drawImage(icon.getImage(), 0, 0, targetWidth, targetHeight, null);
		    graphics2D.dispose();
		    return new ImageIcon(resizedImage);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	    return null;
	}
}
