package eu.busycloud.service.networking.decoder;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import eu.busycloud.service.CloudInstance;

public class DecodeGeneral implements DecodeListener {
	
	/*
	 * @since 0.1
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		String command = event.getData().read();
		if(command.equalsIgnoreCase("returnping")) {
			long start = event.getData().read();
			long end = event.getData().read();
			
			CloudInstance.LOGGER.info("Ping answer: " + (end-start) + "ms.");
		}
	}
	
}
