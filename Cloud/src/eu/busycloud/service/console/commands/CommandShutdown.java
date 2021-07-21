package eu.busycloud.service.console.commands;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;

public class CommandShutdown extends ConsoleCommand {
	
	public CommandShutdown(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
	}
	
}
