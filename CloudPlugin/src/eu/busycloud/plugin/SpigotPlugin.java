package eu.busycloud.plugin;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import de.terrarier.netlistening.Client;
import eu.busycloud.plugin.networking.DecodeCloud;
import eu.busycloud.plugin.networking.DecodeSpigotRcon;
import eu.busycloud.plugin.networking.ListenerSpigotTimeout;

public class SpigotPlugin extends JavaPlugin {
	
	private Client client;
	private String cloudKey;
	private static SpigotPlugin pluginInstance;

	@Override
	public void onEnable() {
		pluginInstance = this;
		
		client = new Client.Builder("localhost", 1130).timeout(15000).build();
		File con = null;
		for(File file : new File(".").listFiles()) 
			if((cloudKey = file.getName()).startsWith("KEY_") && file.isFile()) {
				con = file;
			}
		if(con != null)
			con.delete();

		client.registerListener(new DecodeCloud());
		client.registerListener(new DecodeSpigotRcon());
		client.registerListener(new ListenerSpigotTimeout());
		client.sendData("Spigot", "onEnable()", cloudKey);
	}

	@Override
	public void onDisable() {
		pluginInstance = null;
		client.sendData("Spigot", "onDisable()", cloudKey);
	}

	@Override
	public void onLoad() {
		pluginInstance = this;
		client.sendData("Spigot", "onLoad()", cloudKey);
	}
	
	public static SpigotPlugin getPluginInstance() {
		return pluginInstance;
	}
	
}
