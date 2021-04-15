package de.plugdev.cloud.network.receive;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.infrastructure.SpigotServer;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeSpigotServer implements DecodeListener {

	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Spigot")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "linkserver": {
				final String key = event.getData().read();
				for (SpigotServer spigotServer : ApplicationInterface.getAPI().getInfrastructure()
						.getRunningServers()) {
					if (spigotServer.getRegisterKey().equals(key)) {
						spigotServer.setConnection(event.getConnection());
						ConsoleColors.write(ConsoleColors.GREEN,
								"[NETWORKING] SpigotChannel \"" + spigotServer.getId() + "\" connected!");
						break;
					}
				}
				break;
			}
			case "shutdown": {
				final String key = event.getData().read();
				for (SpigotServer spigotServer : ApplicationInterface.getAPI().getInfrastructure()
						.getRunningServers()) {
					if (spigotServer.getRegisterKey().equals(key)) {
						spigotServer.setConnection(null);
						spigotServer.stopServer();
						ConsoleColors.write(ConsoleColors.GREEN,
								"[NETWORKING] Channel \"" + spigotServer.getId() + "\" disconnected!");
						break;
					}
				}
				break;
			}
			case "adminmessage": {
				final String message = event.getData().read();
				ConsoleColors.write(ConsoleColors.RED, "[INGAME] " + message);
			}
			default:
				break;
			}
		}
	}

}
