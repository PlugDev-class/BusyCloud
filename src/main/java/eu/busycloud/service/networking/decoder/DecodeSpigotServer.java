package eu.busycloud.service.networking.decoder;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.tasks.ServerDisableCheckTask;
import eu.busycloud.service.utils.SingleServerInstance;

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
				for (SingleServerInstance serverInstance : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
					if (serverInstance.getRegisterKey().equalsIgnoreCase(key)) {
						serverInstance.setConnection(event.getConnection());
						final ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyByName("Proxy-1");
						CloudInstance.LOGGER.info("SpigotChannel \"" + serverInstance.getId() + "\" connected!");
						proxyServer.getConnection().sendData("pushInformations", "pushServers", 
									serverInstance.getPartServerName(), "localhost", serverInstance.getPort(), serverInstance.isLobbyServer());
						break;
					}
				}
				break;
			}
			case "ondisable()": {
				final String key = event.getData().read();
				for (SingleServerInstance spigotServer : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
					if (spigotServer.getRegisterKey().equalsIgnoreCase(key)) {
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
