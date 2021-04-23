package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleOutput;
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

				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] Serverinformations");
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Servertype: Proxy");
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Servername: " + proxy.getProxyName());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Serverport: " + proxy.getPort());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Serveraddress: localhost:" + proxy.getPort());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Version: " + proxy.getVersion().getVersion());
				ConsoleOutput.write(ConsoleOutput.WHITE,
						"[PLUGIN] - VersionURL: " + proxy.getVersion().getDownloadURL());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Spieleranzahl:" + proxy.getOnlinePlayer().size());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Verbundene Server: ");
				for (SpigotServer server : proxy.getRegisteredServer()) {
					ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN]   - " + server.getServerName() + " (localhost:"
							+ server.getPort() + ") ID::" + server.getId());
				}
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {

				SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId);

				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] Serverinformations");
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Servertype: Spigotinstance");
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Servername: " + server.getServerName());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Serverport: " + server.getPort());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Serveraddress: localhost:" + server.getPort());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] - Zugewiesene ProxyID: " + server.getProxyId());
				ConsoleOutput.write(ConsoleOutput.WHITE,
						"[PLUGIN] - Version: " + server.getMinecraftVersion().getVersion());
				ConsoleOutput.write(ConsoleOutput.WHITE,
						"[PLUGIN] - VersionURL: " + server.getMinecraftVersion().getDownloadURL());
				ConsoleOutput.write(ConsoleOutput.WHITE, "[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			} else {
				ConsoleOutput.write(ConsoleOutput.CYAN, "[CORE] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
