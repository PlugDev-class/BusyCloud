package eu.busycloud.service.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.screens.ConsoleDefault;
import eu.busycloud.service.console.screens.ConsoleSetup;
import eu.busycloud.service.utils.TextUtils;

public class ConsoleInstance {

	private List<ConsoleScreen> consoleScreens = new ArrayList<ConsoleScreen>();
	private Map<ConsoleScreen, Boolean> queueMap = new HashMap<ConsoleScreen, Boolean>();
	private ConsoleDefault consoleDefault = null;

	public ConsoleInstance(boolean startWithSetup) {
		ApplicationInterface.getAPI().setConsole(this);

		if (startWithSetup) {
			try {
				consoleScreens.add(new ConsoleSetup());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			TextUtils.sendHeader();
			consoleScreens.add(new ConsoleDefault());
		}
		System.out.print("BusyCloud-v2@Input: ");
		Scanner scanner = new Scanner(System.in);
		String input = "";
		while ((input = scanner.nextLine()) != null) {
			for (ConsoleScreen screen : queueMap.keySet())
				if (queueMap.get(screen)) {
					consoleScreens.add(screen);
				} else {
					consoleScreens.remove(screen);
				}
			queueMap.clear();
			for (ConsoleScreen screen2 : consoleScreens)
				screen2.scanLine(input);

			System.out.print("BusyCloud-v2@Input: ");
		}

		scanner.close();
	}

	public void setConsoleDefault(ConsoleDefault consoleDefault) {
		this.consoleDefault = consoleDefault;
	}

	public ConsoleDefault getConsoleDefault() {
		return consoleDefault;
	}

	public List<ConsoleScreen> getConsoleScreens() {
		return consoleScreens;
	}

	public Map<ConsoleScreen, Boolean> getQueueMap() {
		return queueMap;
	}

}
