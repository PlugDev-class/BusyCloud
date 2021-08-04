package eu.busycloud.service.networking.easypackets;

import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.utils.SingleServerInstance;

public abstract class EasyPacketListener {

	private EasyPacketHeader easyPacketHeader = EasyPacketHeader.UNKNOWN;
	
	public void trigger(final SingleServerInstance singleServerInstance, final EasyPacket easyPacket) {}
	public void trigger(final ProxyServer proxyServer, final EasyPacket easyPacket) {}
	
	public EasyPacketListener(EasyPacketHeader receiver) {
		this.easyPacketHeader = receiver;
	}
	
	public EasyPacketHeader getEasyPacketHeader() {
		return easyPacketHeader;
	}
	
}
