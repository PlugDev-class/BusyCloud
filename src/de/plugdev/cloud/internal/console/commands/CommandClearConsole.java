package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.lang.LanguageManager;

public class CommandClearConsole implements ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 1) {
			for(int i = 0; i < 255; i++) {
				ConsoleOutput.write(ConsoleOutput.RESET, " ");
			}
			ConsoleOutput.write(ConsoleOutput.CYAN, LanguageManager.getVar("plugin.default.command.clear"));
		}
	}

	@Override
	public String getHelp() {
		return LanguageManager.getVar("plugin.default.command.clear.helpMessage");
	}
	
}
