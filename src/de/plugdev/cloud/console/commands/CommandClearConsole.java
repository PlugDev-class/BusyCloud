package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandClearConsole extends ConsoleCommand{
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 1) {
			for(int i = 0; i < 255; i++) {
				ConsoleColors.write(ConsoleColors.RESET, " ");
			}
			ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] Console is cleared.");
		}
	}
	
}
