package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.infrastructure.Proxy;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class CommandServerInfo extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				Proxy proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId);

				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] Serverinformations");
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Servertype: Proxy");
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Servername: " + proxy.getProxyName());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Serverport: " + proxy.getPort());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Serveraddress: localhost:" + proxy.getPort());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Version: " + proxy.getVersion().getVersion());
				ConsoleColors.write(ConsoleColors.WHITE,
						"[PLUGIN] - VersionURL: " + proxy.getVersion().getDownloadURL());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Spieleranzahl:" + proxy.getOnlinePlayer().size());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Verbundene Server: ");
				for (SpigotServer server : proxy.getRegisteredServer()) {
					ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN]   - " + server.getServerName() + " (localhost:"
							+ server.getPort() + ") ID::" + server.getId());
				}
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {

				SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId);

				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] Serverinformations");
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Servertype: Spigotinstance");
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Servername: " + server.getServerName());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Serverport: " + server.getPort());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Serveraddress: localhost:" + server.getPort());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] - Zugewiesene ProxyID: " + server.getProxyId());
				ConsoleColors.write(ConsoleColors.WHITE,
						"[PLUGIN] - Version: " + server.getMinecraftVersion().getVersion());
				ConsoleColors.write(ConsoleColors.WHITE,
						"[PLUGIN] - VersionURL: " + server.getMinecraftVersion().getDownloadURL());
				ConsoleColors.write(ConsoleColors.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			} else {
				ConsoleColors.write(ConsoleColors.CYAN, "[CORE] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
