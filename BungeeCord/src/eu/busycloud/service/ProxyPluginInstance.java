package eu.busycloud.service;

import java.io.File;

import de.terrarier.netlistening.Client;
import eu.busycloud.service.cloud.CloudInformations;
import eu.busycloud.service.listener.ListenerProxyPlayerJoin;
import eu.busycloud.service.listener.ListenerProxyPlayerPing;
import eu.busycloud.service.listener.ListenerProxyPlayerQuit;
import eu.busycloud.service.listener.ListenerProxyPlayerSwitchServer;
import eu.busycloud.service.networking.DecodeProxyCloud;
import eu.busycloud.service.networking.DecodeProxyRcon;
import eu.busycloud.service.networking.ListenerProxyTimeout;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyPluginInstance extends Plugin {

	private Client client;
	private CloudInformations cloudInformations = new CloudInformations();
	private static ProxyPluginInstance pluginInstance;
	
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
		
		client.registerListener(new DecodeProxyCloud());
		client.registerListener(new DecodeProxyRcon());
		client.registerListener(new ListenerProxyTimeout());
		client.sendData("Proxy", "onEnable()", cloudInformations.getCloudKey());
	}

	@Override
	public void onDisable() {
		client.sendData("Proxy", "onDisable()", cloudInformations.getCloudKey());
		super.onDisable();
	}

	@Override
	public void onLoad() {
	}
	
	public static ProxyPluginInstance getPluginInstance() {
		return pluginInstance;
	}
	
	public CloudInformations getCloudInformations() {
		return cloudInformations;
	}
	
	public Client getClient() {
		return client;
	}

}
