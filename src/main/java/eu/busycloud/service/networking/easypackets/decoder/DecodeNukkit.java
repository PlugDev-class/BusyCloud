package eu.busycloud.service.networking.easypackets.decoder;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.networking.easypackets.EasyPacket;
import eu.busycloud.service.networking.easypackets.EasyPacketHeader;
import eu.busycloud.service.networking.easypackets.EasyPacketListener;
import eu.busycloud.service.tasks.ServerDisableCheckTask;
import eu.busycloud.service.utils.SingleServerInstance;

public class DecodeNukkit extends EasyPacketListener {

	public DecodeNukkit(EasyPacketHeader receiver) {
		super(receiver);
	}

	@Override
	public void trigger(SingleServerInstance serverInstance, EasyPacket easyPacket) {
		switch ((String) easyPacket.getArguments().get(1)) {
		case "ENABLE":
		{
			final ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyById(0);
			CloudInstance.LOGGER.info("NukkitChannel \"" + serverInstance.getId() + "\" connected.");
			final EasyPacket newEasyPacket = new EasyPacket(EasyPacketHeader.CLOUD, "PUSH_SERVER",
					serverInstance.getPartServerName(), "localhost", serverInstance.getPort(), serverInstance.isLobbyServer());
			ApplicationInterface.getAPI().getNetworking().getEasyPacketHandler().sendPacket(proxyServer, newEasyPacket);
			break;
		}
		case "DISABLE":
		{
			CloudInstance.LOGGER.info("NukkitChannel \"" + serverInstance.getId() + "\" disconnected.");
			new ServerDisableCheckTask(serverInstance);
			break;
		}
		default:
			break;
		}
	}
	
}
