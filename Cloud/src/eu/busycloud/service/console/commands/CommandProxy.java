package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;

public class CommandProxy extends ConsoleCommand {
	
	public CommandProxy(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length != 2) {
			CloudInstance.LOGGER.info("/proxy template");
			return;
		}
		
		if(args[1].equalsIgnoreCase("template")) {
			ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().get(0).doTemplate();
			CloudInstance.LOGGER.info("Did template.");
		}
	}
	
	/*
	 * proxy list
	 * proxy control <> info
	 * proxy control <> rcon
	 * proxy control <> ping
	 * proxy control <> stop
	 * proxy setup create
	 * proxy setup edit
	 * proxy setup delete
	 */
	
}
