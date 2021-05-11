package de.plugdev.cloud.internal.networking.receive;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeSpigotServer implements DecodeListener {

	/*
	 * @since 0.1
	 * @author PlugDev
	 */
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
						ConsoleOutput.write(ConsoleOutput.GREEN,
								"[NETWORKING] SpigotChannel \"" + spigotServer.getId() + "\" connected!");
						ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().get(0).getConnection().sendData("changebungeeinfo",
								"change#motd", "§9BusyCloud §c| §aCloud loaded successfully.");
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
						ConsoleOutput.write(ConsoleOutput.GREEN, "[NETWORKING] Channel \"" + spigotServer.getId() + "\" disconnected!");
						break;
					}
				}
				break;
			}
			case "adminmessage": {
				final String message = event.getData().read();
				ConsoleOutput.write(ConsoleOutput.RED, "[INGAME] " + message);
			}
			default:
				break;
			}
		}
	}

}
