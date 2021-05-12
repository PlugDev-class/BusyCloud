package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;

public class CommandShutdown extends ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
	}
	
}