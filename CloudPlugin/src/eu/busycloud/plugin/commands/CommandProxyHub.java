package eu.busycloud.plugin.commands;

import eu.busycloud.plugin.ProxyPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandProxyHub extends Command {
	
	public CommandProxyHub(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		ServerInfo preferredLobbyServer = null;
		for(ServerInfo info : ProxyPlugin.getPluginInstance().getCloudInformations().getLobbyServers()) {
			if(preferredLobbyServer == null)
				preferredLobbyServer = info;
			if(preferredLobbyServer.getPlayers().size() >= info.getPlayers().size())
				preferredLobbyServer = info;
		}
		((ProxiedPlayer) commandSender).connect(preferredLobbyServer);
	}
	
}
