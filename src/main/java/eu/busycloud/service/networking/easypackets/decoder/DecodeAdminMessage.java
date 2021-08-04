package eu.busycloud.service.networking.easypackets.decoder;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.networking.easypackets.EasyPacket;
import eu.busycloud.service.networking.easypackets.EasyPacketHeader;
import eu.busycloud.service.networking.easypackets.EasyPacketListener;
import eu.busycloud.service.utils.SingleServerInstance;

public class DecodeAdminMessage extends EasyPacketListener {

	public DecodeAdminMessage(EasyPacketHeader receiver) {
		super(receiver);
	}

	@Override
	public void trigger(ProxyServer proxyServer, EasyPacket easyPacket) {
		CloudInstance.LOGGER.info("AdminMessage × " + easyPacket.getArguments().get(1));
	}
	
	@Override
	public void trigger(SingleServerInstance serverInstance, EasyPacket easyPacket) {
		CloudInstance.LOGGER.info("AdminMessage × " + easyPacket.getArguments().get(1));
	}

}
