package eu.busycloud.service.networking;

import de.terrarier.netlistening.Server;
import eu.busycloud.service.networking.decoder.DecodeGeneral;
import eu.busycloud.service.networking.decoder.DecodeProxy;
import eu.busycloud.service.networking.decoder.DecodeSpigotServer;

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
		
		server.registerListener(new DecodeProxy());
		server.registerListener(new DecodeSpigotServer());
		server.registerListener(new DecodeGeneral());
	}
	
	public Server getServer() {
		return server;
	}

}
