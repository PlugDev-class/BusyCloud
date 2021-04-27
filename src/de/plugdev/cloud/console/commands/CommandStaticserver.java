package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.infrastructure.MinecraftVersion;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class CommandStaticserver extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length < 2) {
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Wrong syntax.");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /staticserver <Serverid> stop");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /staticserver <Serverid> rcon <command>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /staticserver <Serverid> add <Version> <Port> <MaxRamEachServer>");
			return;
		}

		String staticServername = args[1];
		if(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByName(staticServername) == null) {
			ConsoleOutput.write(ConsoleOutput.RED, "No server with specified name found!");
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
				MinecraftVersion version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]);
				int startport = Integer.parseInt(args[4]);
				int maxRam = Integer.parseInt(args[5]);

				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] ==========================================================================");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Starting staticservercreation.");
				if (ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]) == null) {
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Version not downloaded or invalid. Maybe renamed?");
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Stopping task...");
					return;
				} else {
					ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Version by ID " + version.getVersion() + " found.");
				}
				ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] RAM in MegaByte " + maxRam + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Startingport " + startport + " set.");
				
				SpigotServer server = new SpigotServer();
				server.startStaticServer(staticServername, version, true, maxRam);
				ApplicationInterface.getAPI().getInfrastructure().getRunningServers().add(server);

				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Starting ServerGroup!");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] ==========================================================================");
			}
		}
	}

}
