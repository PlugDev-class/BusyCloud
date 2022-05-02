package de.plugdev.cloud.internal.networking.receive;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.packets.PacketLinkServer;
import de.plugdev.cloud.packets.PacketSharedInfo;
import de.plugdev.cloud.packets.PacketSpigotDisconnect;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeSpigotServer implements DecodeListener {
	
	/*
	 * @since FORK v1.03 -> v1
	 * 
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final Object receiver = event.getData().read();
		if(receiver instanceof PacketLinkServer) {
			if(((PacketLinkServer) receiver).getHeaderList().get(0).equalsIgnoreCase("Proxy"))
				return;
			final String key = (String) ((PacketLinkServer) receiver).getObjectList().get(0);
			final IService service = ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(key).get();
			service.setConnection(event.getConnection());
			ConsoleOutput.write(ConsoleOutput.GREEN, "SpigotChannel \"" + service.getName() + "\" connected.");
		}
		if(receiver instanceof PacketSpigotDisconnect) {
			final String spigotKey = (String) ((PacketSpigotDisconnect) receiver).getObjectList().get(0);
			final String reason = (String) ((PacketSpigotDisconnect) receiver).getObjectList().get(1);

			final IService service = ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(spigotKey).get();
			service.setConnection(null);
			ConsoleOutput.write(ConsoleOutput.GREEN, "SpigotChannel \"" + service.getName() + "\" disconnected. '" + reason + "'");
		}
		if(receiver instanceof PacketSharedInfo) {
			final String proxyKey = (String) ((PacketSharedInfo) receiver).getObjectList().get(0);
			final String message = (String) ((PacketSharedInfo) receiver).getObjectList().get(1);
			final IService service = ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(proxyKey).get();
			ConsoleOutput.write(ConsoleOutput.GREEN, service.getName() + " >> " + message);
		}
	}

}
