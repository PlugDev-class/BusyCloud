package de.plugdev.cloud.network.receive;

import de.plugdev.cloud.console.ConsoleColors;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeGeneral implements DecodeListener {
	
	@Override
	public void trigger(DecodeEvent event) {
		String command = event.getData().read();
		if(command.equalsIgnoreCase("returnping")) {
			long start = event.getData().read();
			long end = event.getData().read();
			
			ConsoleColors.write(ConsoleColors.YELLOW, "[NETWORKING] Ping answer: " + (end-start) + "ms.");
		}
	}
	
}
