package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandCloud extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		setHelp("Shows the relevant cloud-commands");
		for(String commands : ApplicationInterface.getAPI().getConsole().getCommandMap().keySet()) {
			ConsoleColors.write(ConsoleColors.GREEN, "[CONSOLE] " + commands + " - " + ApplicationInterface.getAPI().getConsole().getCommandMap().get(commands).getHelp());
		}
	}
	
}
