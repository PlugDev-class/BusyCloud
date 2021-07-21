package eu.busycloud.service.console.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;

public class CommandCloud extends ConsoleCommand {

	public CommandCloud(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		List<String> list = null;
		Collections.sort(list = new ArrayList<>(ApplicationInterface.getAPI().getConsole().getConsoleDefault().getCommandMap().keySet()));
		for(String commands : list) {
			CloudInstance.LOGGER.info(commands + " | " + ApplicationInterface.getAPI().getConsole().getConsoleDefault().getCommandMap().get(commands).getHelp());
		}
	}
	
}
