package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantProxyEdit;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.utils.TextUtils;

public class CommandProxy extends ConsoleCommand {
	
	public CommandProxy(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2)
			if(args[1].equalsIgnoreCase("list")) {
				for(ProxyServer proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies())
					CloudInstance.LOGGER.info(proxy.getProxyName() + " [" + proxy.getProxyid() + "]");
				if(ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0)
					CloudInstance.LOGGER.info("No proxy is running. Do you need help with cloudsetup? /introduction");
				return;
			}

		if(args.length < 3) {
			printHelp();
			return;
		}
		if(args[1].toLowerCase().equals("control")) {
			if(args.length < 4) {
				printHelp();
				return;
			}
		}
		
		
		switch (args[1].toLowerCase()) {
		case "control":

			if (ApplicationInterface.getAPI().getInfrastructure().getProxyByName(args[2]) == null) {
				CloudInstance.LOGGER.warning("Proxyserver not found!");
				printHelp();
				return;
			}
			ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyByName(args[2]);

			switch (args[3].toLowerCase()) {
			case "rcon":
				if (args.length >= 4) {
					TextUtils.sendFatLine();
					StringBuilder builder = new StringBuilder();
					for (int i = 4; i < args.length; i++)
						builder.append(args[i] + " ");
					CloudInstance.LOGGER.info("Send command '" + builder.substring(0, builder.toString().length() - 1)
					+ "' to '" + proxyServer.getProxyName() + "'.");
					proxyServer.sendRCON(builder.substring(0, builder.toString().length() - 1));
					TextUtils.sendFatLine();
					return;
				}
				printHelp();
				return;
			case "stop":
				TextUtils.sendFatLine();
				proxyServer.stopProxy();
				TextUtils.sendFatLine();
				return;
			case "info":
				TextUtils.sendFatLine();
				proxyServer.printInfo();
				TextUtils.sendFatLine();
				return;
			}

			printHelp();
			break;
		case "setup":
			switch (args[2].toLowerCase()) {
			case "edit":
				new ConsoleAssistantProxyEdit();
				return;
			}
		default:
			printHelp();
			break;
		}
	}
	
	private void printHelp() {
		CloudInstance.LOGGER.info("/proxy list");
		CloudInstance.LOGGER.info("/proxy control <servername> rcon <command>");
		CloudInstance.LOGGER.info("/proxy control <servername> stop");
		CloudInstance.LOGGER.info("/proxy control <servername> info");
		CloudInstance.LOGGER.info("/proxy setup edit");
	}
	
	/*
	 * proxy list
	 * proxy control <> info
	 * proxy control <> rcon
	 * proxy control <> ping
	 * proxy control <> stop
	 * proxy control <> template
	 * proxy setup create
	 * proxy setup edit
	 * proxy setup delete
	 */
	
}
