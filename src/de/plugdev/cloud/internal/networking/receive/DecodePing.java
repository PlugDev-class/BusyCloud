package de.plugdev.cloud.internal.networking.receive;

import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.packets.Packet;
import de.plugdev.cloud.packets.PacketSharedPing;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodePing implements DecodeListener {

	/*
	 * @since FORK v1.03 -> v1
	 * 
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		Object read = event.getData().read();
		if (!(read instanceof PacketSharedPing))
			return;
		Packet readPacket = (PacketSharedPing) read;
		long start = (long) readPacket.getObjectList().get(0);
		long end = (long) readPacket.getObjectList().get(1);
		ConsoleOutput.write(ConsoleOutput.YELLOW, "Ping: " + (end - start) + "ms.");
	}

}
