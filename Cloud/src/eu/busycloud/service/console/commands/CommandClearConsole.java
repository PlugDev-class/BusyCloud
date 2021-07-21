package eu.busycloud.service.console.commands;

import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.utils.TextUtils;

public class CommandClearConsole extends ConsoleCommand {
	
	public CommandClearConsole(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		TextUtils.clearScreen();
	}
	
}
