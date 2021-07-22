package eu.busycloud.plugin.listener;

import eu.busycloud.plugin.ProxyPlugin;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerSwitchServer implements Listener {

	@EventHandler
	public void onPlayerSwitchServer(ServerSwitchEvent event) {
		String cloudKey = ProxyPlugin.getPluginInstance().getCloudInformations().getCloudKey();
		ProxyPlugin.getPluginInstance().getClient().sendData("Proxy", "playerswitchserver", cloudKey,
				event.getPlayer().getUniqueId().toString(), event.getFrom().getName(),
				event.getPlayer().getServer().getInfo().getName());
	}

}
