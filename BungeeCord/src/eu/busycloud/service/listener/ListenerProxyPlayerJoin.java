package eu.busycloud.service.listener;

import eu.busycloud.service.ProxyPluginInstance;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerJoin implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerConnectEvent(ServerConnectedEvent event) {
		String cloudKey = ProxyPluginInstance.getPluginInstance().getCloudInformations().getCloudKey();
		ProxyPluginInstance.getPluginInstance().getClient().sendData("Proxy", "playerconnect", cloudKey,
				event.getPlayer().getName(), event.getPlayer().getUniqueId().toString(), event.getServer().getInfo().getName(),
				event.getPlayer().getAddress().getHostName());
	}
	
}
