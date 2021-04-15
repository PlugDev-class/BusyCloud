package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandShutdown extends ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
	}
	
}
