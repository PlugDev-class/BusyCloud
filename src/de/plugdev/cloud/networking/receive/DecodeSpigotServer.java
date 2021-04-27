package de.plugdev.cloud.networking.receive;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.infrastructure.SpigotServer;
import de.terrarier.netlistening.api.DataContainer;
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
						DataContainer container = new DataContainer();
						container.add("changebungeeinfo");
						container.add("change#motd");
						container.add("§9BusyCloud §c| §aCloud loaded successfully.");
						ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().get(0).getConnection().sendData(container);
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
