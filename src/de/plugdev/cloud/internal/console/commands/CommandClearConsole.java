package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandClearConsole extends ConsoleCommand{
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 1) {
			for(int i = 0; i < 255; i++) {
				ConsoleOutput.write(ConsoleOutput.RESET, " ");
			}
			ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Console is cleared.");
		}
	}
	
}
