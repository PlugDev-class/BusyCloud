package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ServerSoftware;
import eu.busycloud.service.infrastructure.SpigotServer;

public class CommandStaticServer extends ConsoleCommand {

	public CommandStaticServer(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length < 2) {
			CloudInstance.LOGGER.info("Wrong syntax.");
			CloudInstance.LOGGER.info("/staticserver <Serverid> stop");
			CloudInstance.LOGGER.info("/staticserver <Serverid> rcon <command>");
			CloudInstance.LOGGER.info("/staticserver <Serverid> add <Version> <Port> <MaxRamEachServer>");
			return;
		}

		String staticServername = args[1];
		if(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByName(staticServername) == null) {
			CloudInstance.LOGGER.info("No server with specified name found!");
			return;
		}
		
		if(args.length == 3) {
			if(args[2].equalsIgnoreCase("stop")) {
				ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByName(staticServername).stopServer();
			}
		} else if(args.length > 3) {
			if(args[2].equalsIgnoreCase("rcon")) {
				StringBuilder builder = new StringBuilder();
				for(int i = 3; i < args.length; i++) {
					builder.append(args[i] + " ");
				}
				ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByName(staticServername).sendRCON(builder.toString());
				System.out.println(builder.toString());
			} else if(args[2].equalsIgnoreCase("add")) {
				ServerSoftware serverSoftware = ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]);
				int startport = Integer.parseInt(args[4]);
				int maxRam = Integer.parseInt(args[5]);

				CloudInstance.LOGGER.info("==========================================================================");
				CloudInstance.LOGGER.info("Starting staticservercreation.");
				if (ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]) == null) {
					CloudInstance.LOGGER.info("Version not downloaded or invalid. Maybe renamed?");
					CloudInstance.LOGGER.info("Stopping task...");
					return;
				} else {
					CloudInstance.LOGGER.info("Version by ID " + serverSoftware.getVersionName() + " found.");
				}
				CloudInstance.LOGGER.info("RAM in MegaByte " + maxRam + " set.");
				CloudInstance.LOGGER.info("Startingport " + startport + " set.");
				
				SpigotServer server = new SpigotServer();
				server.startStaticServer(staticServername, serverSoftware, true, maxRam);
				ApplicationInterface.getAPI().getInfrastructure().getRunningServers().add(server);

				CloudInstance.LOGGER.info("Starting ServerGroup!");
				CloudInstance.LOGGER.info("==========================================================================");
			}
		}
	}
	
	/*
	 * staticserver list
	 * staticserver control <> info
	 * staticserver control <> rcon
	 * staticserver control <> ping
	 * staticserver control <> stop
	 * staticserver setup create
	 * staticserver setup edit
	 * staticserver setup delete
	 */

}
