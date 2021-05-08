package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandProxy extends ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length != 2) {
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Wrong syntax.");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /proxy template");
			return;
		}
		
		if(args[1].equalsIgnoreCase("template")) {
			ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().get(0).doTemplate();
			ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Did template.");
		}
	}
	
}
