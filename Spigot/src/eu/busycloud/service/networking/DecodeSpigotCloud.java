package eu.busycloud.service.networking;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import de.terrarier.netlistening.internals.AssumeNotNull;

public class DecodeSpigotCloud implements DecodeListener {

	@Override
	public void trigger(@AssumeNotNull DecodeEvent event) {
		switch ((String) event.getData().read()) {
		case "ping":
			event.getConnection().sendData("returnping", event.getData().read(), System.currentTimeMillis());
			break;
		default:
			break;
		}
	}

}
