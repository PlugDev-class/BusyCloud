package eu.busycloud.service.networking.decoder;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.SpigotServer;
import eu.busycloud.service.tasks.ServerDisableCheckTask;

public class DecodeSpigotServer implements DecodeListener {

	/**
	 * @since 0.1
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Spigot")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "onenable()": {
				final String key = event.getData().read();
				for (SpigotServer spigotServer : ApplicationInterface.getAPI().getInfrastructure()
						.getRunningServers()) {
					if (spigotServer.getRegisterKey().equals(key)) {
						spigotServer.setConnection(event.getConnection());
						CloudInstance.LOGGER.info("SpigotChannel \"" + spigotServer.getId() + "\" connected!");
						ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().get(0).getConnection().sendData("changebungeeinfo",
								"change#motd", "§9BusyCloud §c| §aCloud loaded successfully.");
						break;
					}
				}
				break;
			}
			case "ondisable()": {
				final String key = event.getData().read();
				for (SpigotServer spigotServer : ApplicationInterface.getAPI().getInfrastructure()
						.getRunningServers()) {
					if (spigotServer.getRegisterKey().equals(key)) {
						spigotServer.setConnection(null);
						new ServerDisableCheckTask(spigotServer);
						CloudInstance.LOGGER.info("SpigotChannel \"" + spigotServer.getId() + "\" disconnected! ~Check in 15 seconds for reconnect.");
						break;
					}
				}
				break;
			}
			case "adminmessage": {
				CloudInstance.LOGGER.info("Adminmessage: " + event.getData().read());
			}
			default:
				break;
			}
		}
	}

}
