package eu.busycloud.plugin.listener;

import org.bukkit.event.player.PlayerQuitEvent;

import eu.busycloud.plugin.ProxyPlugin;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		String cloudKey = ProxyPlugin.getPluginInstance().getCloudInformations().getCloudKey();
		ProxyPlugin.getPluginInstance().getClient().sendData("Proxy", "playerdisconnect", cloudKey,
				event.getPlayer().getUniqueId().toString());
	}

}
