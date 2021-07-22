package eu.busycloud.plugin.listener;

import eu.busycloud.plugin.ProxyPlugin;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerJoin implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerConnectEvent(ServerConnectEvent event) {
		
		ServerInfo preferredLobbyServer = null;
		for(ServerInfo info : ProxyPlugin.getPluginInstance().getCloudInformations().getLobbyServers()) {
			if(preferredLobbyServer == null)
				preferredLobbyServer = info;
			if(preferredLobbyServer.getPlayers().size() >= info.getPlayers().size())
				preferredLobbyServer = info;
		}
		
		event.setTarget(preferredLobbyServer);
		
		String cloudKey = ProxyPlugin.getPluginInstance().getCloudInformations().getCloudKey();
		ProxyPlugin.getPluginInstance().getClient().sendData("Proxy", "playerconnect", cloudKey,
				event.getPlayer().getName(), event.getPlayer().getUniqueId().toString(), event.getTarget().getName(),
				event.getPlayer().getAddress().getHostName());
	}
	
}
