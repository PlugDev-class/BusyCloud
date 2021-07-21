package eu.busycloud.service.networking;

import de.terrarier.netlistening.api.event.ConnectionTimeoutEvent;
import de.terrarier.netlistening.api.event.ConnectionTimeoutListener;
import de.terrarier.netlistening.internals.AssumeNotNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ListenerProxyTimeout implements ConnectionTimeoutListener {

	@Override
	public void trigger(@AssumeNotNull ConnectionTimeoutEvent event) {
		for(ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers())
			proxiedPlayer.disconnect(new TextComponent("Connection to BusyCloud lost! Please try again later."));
	}

}
