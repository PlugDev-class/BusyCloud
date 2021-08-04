package eu.busycloud.service.networking.easypackets.decoder;

import eu.busycloud.service.networking.easypackets.EasyPacketHeader;
import eu.busycloud.service.networking.easypackets.EasyPacketListener;

public class DecoderPing extends EasyPacketListener {

	public DecoderPing(EasyPacketHeader receiver) {
		super(receiver);
	}
	
}
