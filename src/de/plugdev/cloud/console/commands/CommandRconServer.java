package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.infrastructure.Proxy;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class CommandRconServer extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length > 1) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				Proxy proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId);

				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					stringBuilder.append(args[i] + " ");
				}
				proxy.sendRCON(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
				ConsoleOutput.write(ConsoleOutput.CYAN,
						"[CORE] Sent rcon \""
								+ stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)
								+ "\" to Proxy " + proxy.getProxyName());

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {
				SpigotServer spigotServer = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId);

				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					stringBuilder.append(args[i] + " ");
				}
				spigotServer.sendRCON(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
				ConsoleOutput.write(ConsoleOutput.CYAN,
						"[CORE] Sent rcon \""
								+ stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)
								+ "\" to Spigot " + spigotServer.getServerName());
			} else {
				ConsoleOutput.write(ConsoleOutput.CYAN, "[CORE] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
