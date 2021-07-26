package eu.busycloud.plugin;

import java.io.File;

import de.terrarier.netlistening.Client;
import eu.busycloud.plugin.commands.CommandProxyHub;
import eu.busycloud.plugin.listener.ListenerProxyPlayerJoin;
import eu.busycloud.plugin.listener.ListenerProxyPlayerPing;
import eu.busycloud.plugin.listener.ListenerProxyPlayerQuit;
import eu.busycloud.plugin.listener.ListenerProxyPlayerSwitchServer;
import eu.busycloud.plugin.networking.DecodeCloud;
import eu.busycloud.plugin.networking.DecodeProxyCloud;
import eu.busycloud.plugin.networking.DecodeProxyRcon;
import eu.busycloud.plugin.networking.ListenerProxyTimeout;
import eu.busycloud.plugin.utils.ProxyCloudInformations;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyPlugin extends Plugin {

	private Client client;
	private ProxyCloudInformations cloudInformations = new ProxyCloudInformations();
	private static ProxyPlugin pluginInstance;
	
	@Override
	public void onEnable() {
		client = new Client.Builder("localhost", 1130).timeout(15000).build();
		File con = null;
		for (File file : new File(".").listFiles())
			if ((file.getName()).startsWith("KEY_") && file.isFile()) {
				cloudInformations.setCloudKey(file.getName());
				con = file;
			}
		if (con != null)
			con.delete();

		ProxyServer.getInstance().getPluginManager().registerListener(pluginInstance, new ListenerProxyPlayerJoin());
		ProxyServer.getInstance().getPluginManager().registerListener(pluginInstance, new ListenerProxyPlayerPing());
		ProxyServer.getInstance().getPluginManager().registerListener(pluginInstance, new ListenerProxyPlayerQuit());
		ProxyServer.getInstance().getPluginManager().registerListener(pluginInstance, new ListenerProxyPlayerSwitchServer());
		ProxyServer.getInstance().getPluginManager().registerCommand(pluginInstance, new CommandProxyHub("hub"));
		
		client.registerListener(new DecodeProxyCloud());
		client.registerListener(new DecodeProxyRcon());
		client.registerListener(new DecodeCloud());
		client.registerListener(new ListenerProxyTimeout());
		client.sendData("Proxy", "onEnable()", cloudInformations.getCloudKey());
	}

	@Override
	public void onDisable() {
		client.sendData("Proxy", "onDisable()", cloudInformations.getCloudKey());
		super.onDisable();
	}
	
	public static ProxyPlugin getPluginInstance() {
		return pluginInstance;
	}
	
	public ProxyCloudInformations getCloudInformations() {
		return cloudInformations;
	}
	
	public Client getClient() {
		return client;
	}

}
