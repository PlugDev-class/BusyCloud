package eu.busycloud.service.listener;

import org.bukkit.event.player.PlayerQuitEvent;

import eu.busycloud.service.ProxyPluginInstance;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		String cloudKey = ProxyPluginInstance.getPluginInstance().getCloudInformations().getCloudKey();
		ProxyPluginInstance.getPluginInstance().getClient().sendData("Proxy", "playerdisconnect", cloudKey,
				event.getPlayer().getUniqueId().toString());
	}

}
