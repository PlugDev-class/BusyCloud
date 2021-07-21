package eu.busycloud.service.networking;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import de.terrarier.netlistening.internals.AssumeNotNull;
import eu.busycloud.service.ProxyPluginInstance;

public class DecodeProxyCloud implements DecodeListener {

	@Override
	public void trigger(@AssumeNotNull DecodeEvent event) {
		if (((String) event.getData().read()).equalsIgnoreCase("pushInformations")) {
			switch ((String) event.getData().read()) {
			case "maxPlayers":
				ProxyPluginInstance.getPluginInstance().getCloudInformations()
						.setMotdMaxplayers(event.getData().read());
				break;
			case "motdPlayerInfo":
				String information;
				while((information = event.getData().read()) != null)
					ProxyPluginInstance.getPluginInstance().getCloudInformations().getMotdPlayerinfo().add(information);
				break;
			case "motdProtocol":
				ProxyPluginInstance.getPluginInstance().getCloudInformations().setMotdProtocolString(event.getData().read());
				break;
			case "motdDescription":
				ProxyPluginInstance.getPluginInstance().getCloudInformations().setMotdDescription(event.getData().read());
				break;
			default:
				break;
			}
		}
	}

}
