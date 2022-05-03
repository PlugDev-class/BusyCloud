package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.lang.LanguageManager;

public class CommandCloud implements ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		for(String commands : ApplicationInterface.getAPI().getConsole().getCommandMap().keySet())
			ConsoleOutput.write(ConsoleOutput.GREEN, LanguageManager.getVar("plugin.default.command.help.syntax", commands, ApplicationInterface.getAPI().getConsole().getCommandMap().get(commands).getHelp()));
	}

	@Override
	public String getHelp() {
		return LanguageManager.getVar("plugin.default.command.help.helpMessage");
	}
	
}
