package eu.busycloud.service.listener;

import eu.busycloud.service.ProxyPluginInstance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerProxyPlayerPing implements Listener {
	
	@EventHandler
	public void onPlayerPingEvent(ProxyPingEvent event) {
		PlayerInfo[] playerInfoSample = new PlayerInfo[ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdPlayerinfo().size()];
		for(int i = 0; i < ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdPlayerinfo().size(); i++)
			playerInfoSample[i] = new PlayerInfo(ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdPlayerinfo().get(i), "");
		
		ServerPing serverPing = new ServerPing();
		serverPing.setDescriptionComponent(new TextComponent(ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdDescription()));
		serverPing.setPlayers(new Players(ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdMaxplayers(), ProxyServer.getInstance().getPlayers().size(), playerInfoSample));
		serverPing.setVersion(new Protocol(ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdProtocolString(), 0));
		event.setResponse(serverPing);
	}
	
}
