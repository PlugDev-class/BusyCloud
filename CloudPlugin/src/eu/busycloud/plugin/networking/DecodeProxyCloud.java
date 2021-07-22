package eu.busycloud.plugin.networking;

import java.net.InetSocketAddress;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import de.terrarier.netlistening.internals.AssumeNotNull;
import eu.busycloud.plugin.ProxyPlugin;
import net.md_5.bungee.api.ProxyServer;

public class DecodeProxyCloud implements DecodeListener {

	@Override
	public void trigger(@AssumeNotNull DecodeEvent event) {
		if (((String) event.getData().read()).equalsIgnoreCase("pushInformations")) {
			switch ((String) event.getData().read()) {
			case "maxPlayers":
				ProxyPlugin.getPluginInstance().getCloudInformations()
						.setMotdMaxplayers(event.getData().read());
				break;
			case "motdPlayerInfo":
				String information;
				while((information = event.getData().read()) != null)
					ProxyPlugin.getPluginInstance().getCloudInformations().getMotdPlayerinfo().add(information);
				break;
			case "motdProtocol":
				ProxyPlugin.getPluginInstance().getCloudInformations().setMotdProtocolString(event.getData().read());
				break;
			case "motdDescription":
				ProxyPlugin.getPluginInstance().getCloudInformations().setMotdDescription(event.getData().read());
				break;
			case "pushServers":
				String serverName = event.getData().read();
				InetSocketAddress address = new InetSocketAddress((String) event.getData().read(), (int) event.getData().read());
				String motd = "§9BusyCloud-Service §8| §cSpigotServer";
				ProxyServer.getInstance().getServers().put(serverName, ProxyServer.getInstance().constructServerInfo(serverName, address, motd, false));
				break;
			default:
				break;
			}
		}
	}

}
