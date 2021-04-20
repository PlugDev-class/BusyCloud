package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandProxy extends ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length != 2) {
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Wrong syntax.");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /proxy template");
			return;
		}
		
		if(args[1].equalsIgnoreCase("template")) {
			ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().get(0).doTemplate();
		}
	}
	
}
