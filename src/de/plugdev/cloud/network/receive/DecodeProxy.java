package de.plugdev.cloud.network.receive;

import java.util.UUID;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.PlayerInfo;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.infrastructure.Proxy;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeProxy implements DecodeListener {

	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Proxy")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "linkproxy":
				{
					final String key = event.getData().read();
					Proxy proxy;
					(proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(key)).setConnection(event.getConnection());
					proxy.setConnection(event.getConnection());
					ConsoleColors.write(ConsoleColors.GREEN, "[NETWORKING] ProxyChannel \"" + proxy.getProxyid() + "\" connected!");
				}
			break;
			case "playerconnect": {
				String proxyKey = event.getData().read();
				String playername = event.getData().read();
				UUID playeruuid = event.getData().read();
				String conectedServer = event.getData().read();
				String address = event.getData().read();

				ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer().add(new PlayerInfo(playername, playeruuid, conectedServer, address));

				ConsoleColors.write(ConsoleColors.YELLOW,
						"[CORE] Player " + playername + " connected to PROXY_" + proxyKey + ".");
			}
				break;
			case "playerdisconnect": {
				String proxyKey = event.getData().read();
				UUID playerUUID = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()) {
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}
				}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer().remove(info);
					ConsoleColors.write(ConsoleColors.YELLOW,
							"[CORE] Player " + info.getPlayername() + " disconnected from PROXY_" + info.getConnectedServer() + ".");
				} else {
					ConsoleColors.write(ConsoleColors.YELLOW,
							"[CORE] Player " + null + " disconnected from PROXY_" + null + ".");
				}
			}
				break;
			case "playerswitchserver": {
				String proxyKey = event.getData().read();
				UUID playerUUID = event.getData().read();
				String from = event.getData().read();
				String to = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()) {
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}
				}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer().remove(info);
					info.setConnectedServer(to);
					ConsoleColors.write(ConsoleColors.YELLOW, "[CORE] Player " + info.getPlayername()
							+ " switched from " + from + " to " + to + " on PROXY_" + proxyKey + ".");
				} else {
					ConsoleColors.write(ConsoleColors.YELLOW, "[CORE] Player " + null + " switched from " + from
							+ " to " + to + " on PROXY_" + proxyKey + ".");
				}
			}
				break;
			default:
				break;
			}
		}
	}

}
