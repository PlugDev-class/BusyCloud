package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.console.ConsoleInput;

public class CommandCloud extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		setHelp("Shows the relevant cloud-commands");
		for(String commands : ConsoleInput.getCommandMap().keySet()) {
			ConsoleColors.write(ConsoleColors.GREEN, "[CONSOLE] " + commands + " - " + ConsoleInput.getCommandMap().get(commands).getHelp());
		}
	}
	
}
