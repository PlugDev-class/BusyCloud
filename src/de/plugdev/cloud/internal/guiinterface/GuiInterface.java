package de.plugdev.cloud.internal.guiinterface;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.internal.guiinterface.interfaces.Messageable;
import de.plugdev.cloud.internal.guiinterface.panels.GUIPanel_Loading;
import de.plugdev.cloud.internal.guiinterface.panels.GUIPanel_Main;
import de.plugdev.cloud.lang.ApiStatus.Experimental;

@Experimental
public class GuiInterface {

	private JFrame frame;
	private int colorMode = 0;
	private Map<String, Color> colorMap;

	public GuiInterface() {
		colorMap = new LinkedHashMap<>();

		colorMap.put("0:1", new Color(207, 93, 255));
		colorMap.put("0:2", new Color(202, 178, 225));
		colorMap.put("0:3", new Color(187, 25, 255));
		colorMap.put("0:4", new Color(53, 118, 255));
		colorMap.put("0:5", new Color(79, 102, 229));
		colorMap.put("0:6", new Color(123, 165, 255));
		colorMap.put("0:7", new Color(82, 189, 255));
		colorMap.put("0:8", new Color(204, 204, 204));
		colorMap.put("0:9", new Color(255, 255, 255));

//		colorMap.put("1:1", new Color(3, 4, 94));
//		colorMap.put("1:2", new Color(2, 62, 138));
//		colorMap.put("1:3", new Color(0, 119, 182));
//		colorMap.put("1:4", new Color(0, 150, 199));
//		colorMap.put("1:5", new Color(0, 180, 216));
//		colorMap.put("1:6", new Color(72, 202, 228));
//		colorMap.put("1:7", new Color(144, 224, 239));
//		colorMap.put("1:8", new Color(173, 232, 244));
//		colorMap.put("1:9", new Color(202, 240, 248));
//
//		colorMap.put("2:1", new Color(29, 52, 97));
//		colorMap.put("2:2", new Color(31, 72, 126));
//		colorMap.put("2:3", new Color(55, 105, 150));
//		colorMap.put("2:4", new Color(98, 144, 200));
//		colorMap.put("2:5", new Color(130, 156, 188));
//		colorMap.put("2:6", new Color(130, 156, 188));
//		colorMap.put("2:7", new Color(130, 156, 188));
//		colorMap.put("2:8", new Color(130, 156, 188));
//		colorMap.put("2:9", new Color(130, 156, 188));

	}

	private JPanel jPanel;
	private Messageable messageable;

	public void initGUI() {

		frame = new JFrame();
		frame.setBounds(100, 100, 913, 600);
		frame.setVisible(true);
		frame.setTitle("BusyCloud-GUI | v1.0" + CloudInstance.currentSubversion + " - GUI-(A)Interface");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		GUIPanel_Loading loading = new GUIPanel_Loading();
		this.messageable = loading;
		pushStatusMessage("Loading cloud...");

		switchPanel(loading);

		switchPanel(new GUIPanel_Main());
	}

	public void switchPanel(JPanel newJPanel) {
		if (jPanel != null) {
			frame.getContentPane().remove(jPanel);
		}
		this.jPanel = newJPanel;
		frame.getContentPane().add(newJPanel);
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();

		System.out.println(jPanel.getClass().getName());
	}

	public void pushStatusMessage(String messsage) {
		messageable.pushMessage(messsage);
	}

	public Color getColorById(int id) {
		return getColorMap().get(colorMode + ":" + id);
	}

	public Map<String, Color> getColorMap() {
		return colorMap;
	}

}
