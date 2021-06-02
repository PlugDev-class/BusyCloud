package de.plugdev.cloud.internal.networking.receive;

import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeProxy implements DecodeListener {

	/*
	 * @since 0.1
	 * 
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Proxy")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "linkproxy": {
				final String key = event.getData().read();
				Proxy proxy;
				(proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(key))
						.setConnection(event.getConnection());
				ConsoleOutput.write(ConsoleOutput.GREEN,
						"[NETWORKING] ProxyChannel \"" + proxy.getProxyid() + "\" connected!");

				event.getConnection().sendData("changebungeeinfo", "change#motd",
						"§9BusyCloud §c| §dCloud is loading...");
			}
				break;
			case "playerconnect": {
				String proxyKey = event.getData().read();
				String playername = event.getData().read();
				UUID playeruuid = UUID.fromString(event.getData().read());
				String conectedServer = event.getData().read();
				String address = event.getData().read();

				ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
						.add(new PlayerInfo(playername, playeruuid, conectedServer, address));

				ConsoleOutput.write(ConsoleOutput.YELLOW, "[CORE] Player " + playername + " connected to "
						+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName()
						+ " - " + conectedServer);

				if (ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).isMaintenance()) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
							.sendRCON("kick " + playername + " §cThe server is currently in maintenance-mode.");
				}
			}
				break;
			case "playerdisconnect": {
				String proxyKey = event.getData().read();
				UUID playerUUID = UUID.fromString(event.getData().read());

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayer()) {
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}
				}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer().remove(info);
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[CORE] Player " + info.getPlayername()
							+ " disconnected from "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			case "playerswitchserver": {
				String proxyKey = event.getData().read();
				UUID playerUUID = UUID.fromString(event.getData().read());
				String from = event.getData().read();
				String to = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayer()) {
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}
				}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
							.remove(info);
					info.setConnectedServer(to);
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[CORE] Player " + info.getPlayername()
							+ " switched from " + from + " to " + to + " on "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			default:
				break;
			}
		}
	}

}
