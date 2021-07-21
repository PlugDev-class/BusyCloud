package eu.busycloud.service.listener;

import eu.busycloud.service.ProxyPluginInstance;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerSwitchServer implements Listener {

	@EventHandler
	public void onPlayerSwitchServer(ServerSwitchEvent event) {
		String cloudKey = ProxyPluginInstance.getPluginInstance().getCloudInformations().getCloudKey();
		ProxyPluginInstance.getPluginInstance().getClient().sendData("Proxy", "playerswitchserver", cloudKey,
				event.getPlayer().getUniqueId().toString(), event.getFrom().getName(),
				event.getPlayer().getServer().getInfo().getName());
	}

}
