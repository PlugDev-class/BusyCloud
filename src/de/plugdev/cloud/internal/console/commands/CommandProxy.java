package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.Proxy;

public class CommandProxy implements ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length != 2) {
			ConsoleOutput.write(ConsoleOutput.RED, "Wrong syntax.");
			ConsoleOutput.write(ConsoleOutput.RED, "/proxy template");
			return;
		}
		
		if(args[1].equalsIgnoreCase("template")) {
			((Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceByName("Proxy-1").get()).doTemplate();
			ConsoleOutput.write(ConsoleOutput.GREEN, "Did template.");
		}
	}

	@Override
	public String getHelp() {
		return "Manages the running proxy.";
	}
	
}
