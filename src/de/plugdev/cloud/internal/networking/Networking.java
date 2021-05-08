package de.plugdev.cloud.internal.networking;

import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.networking.receive.DecodeGeneral;
import de.plugdev.cloud.internal.networking.receive.DecodeProxy;
import de.plugdev.cloud.internal.networking.receive.DecodeSpigotServer;
import de.terrarier.netlistening.Server;
import de.terrarier.netlistening.api.event.ExceptionThrowListener;
import de.terrarier.netlistening.api.event.ExceptionTrowEvent;

public class Networking {

	/*
	 * The sourceclass of networking.
	 * Anyway if it's decoding or encoding.
	 * 
	 * Used API in this project -> 
	 * • Netty by netty
	 * • NetListening by terrarier2111
	 * 
	 * @since 0.1
	 * @author PlugDev, terrarier2111
	 */
	
	/*
	 * Initiating servernetworking.
	 * Registering some Listeners.
	 * 
	 * @since 0.1
	 * @author PlugDev, terrarier2111
	 */
	Server server;
	public void initNetworking() {
		server = new Server.Builder(1130).timeout(15000).encryption().build().compression()
				.nibbleCompression(true).varIntCompression(true).build().build();
		
		server.registerListener(new ExceptionThrowListener() {
			
			@Override
			public void trigger(ExceptionTrowEvent event) {
				event.setPrint(false);
				ConsoleOutput.write(ConsoleOutput.CYAN, "An exception/event happened: " + event.getException().getMessage());
			}
		});
		
		server.registerListener(new DecodeProxy());
		server.registerListener(new DecodeSpigotServer());
		server.registerListener(new DecodeGeneral());
	}
	
	public Server getServer() {
		return server;
	}

}
