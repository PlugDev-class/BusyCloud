package eu.busycloud.plugin.networking;

import org.bukkit.Bukkit;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import de.terrarier.netlistening.internals.AssumeNotNull;
import eu.busycloud.plugin.SpigotPlugin;

public class DecodeSpigotRcon implements DecodeListener {
	
	@Override
	public void trigger(@AssumeNotNull DecodeEvent event) {
		final String receiver = event.getData().read();
		if(receiver.equalsIgnoreCase("rcon")) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPlugin.getPluginInstance(), new Runnable() {
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getData().read());
				}
			});
		}
	}
	
}
