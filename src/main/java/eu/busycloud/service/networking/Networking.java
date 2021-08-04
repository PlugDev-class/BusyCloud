package eu.busycloud.service.networking;

import de.terrarier.netlistening.Server;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.networking.decoder.DecodeGeneral;
import eu.busycloud.service.networking.decoder.DecodeNukkit;
import eu.busycloud.service.networking.decoder.DecodeProxyServer;
import eu.busycloud.service.networking.decoder.DecodeSpigotServer;
import eu.busycloud.service.networking.easypackets.EasyPacketHandler;

/**
 * The sourceclass of networking.
 * Anyway if it's decoding or encoding.
 * 
 * Used API in this project -> 
 *  Netty by netty
 *  NetListening by terrarier2111
 * 
 * @since 0.1
 * @author PlugDev
 */
public class Networking {

	/**
	 * Initiating servernetworking.
	 * Registering some listeners.
	 * 
	 * @since 0.1
	 * @author PlugDev
	 */
	private Server server;
	private EasyPacketHandler easyPacketHandler;
	public void initNetworking() {
		server = new Server.Builder(1130).timeout(15000).encryption().build().compression()
				.nibbleCompression(ApplicationInterface.getAPI().getInfrastructure().nibbleCompression)
				.varIntCompression(true)
				.build().build();
		
		new Runnable() {
			
			@Override
			public void run() {
				server.registerListener(new EasyPacketHandler());
			}
		};
		
		server.registerListener(new DecodeProxyServer());
		server.registerListener(new DecodeSpigotServer());
		server.registerListener(new DecodeNukkit());
		server.registerListener(new DecodeGeneral());
	}
	
	public Server getServer() {
		return server;
	}
	
	public EasyPacketHandler getEasyPacketHandler() {
		return easyPacketHandler;
	}

}
