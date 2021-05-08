package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandCloud extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		setHelp("Shows the relevant cloud-commands");
		for(String commands : ApplicationInterface.getAPI().getConsole().getCommandMap().keySet()) {
			ConsoleOutput.write(ConsoleOutput.GREEN, "[CONSOLE] " + commands + " - " + ApplicationInterface.getAPI().getConsole().getCommandMap().get(commands).getHelp());
		}
	}
	
}
