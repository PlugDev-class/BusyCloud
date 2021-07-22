package eu.busycloud.plugin.networking;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.terrarier.netlistening.api.event.ConnectionTimeoutEvent;
import de.terrarier.netlistening.api.event.ConnectionTimeoutListener;
import de.terrarier.netlistening.internals.AssumeNotNull;
import eu.busycloud.plugin.SpigotPlugin;

public class ListenerSpigotTimeout implements ConnectionTimeoutListener {

	@Override
	public void trigger(@AssumeNotNull ConnectionTimeoutEvent event) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPlugin.getPluginInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.kickPlayer("Connection to BusyCloud lost! Please try again later.");
				}
				Bukkit.shutdown();
			}
		});
	}

}
