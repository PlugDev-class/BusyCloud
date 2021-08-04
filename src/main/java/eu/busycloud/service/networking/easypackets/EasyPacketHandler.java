package eu.busycloud.service.networking.easypackets;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.terrarier.netlistening.api.DataContainer;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import de.terrarier.netlistening.internal.AssumeNotNull;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.utils.SingleServerInstance;

public class EasyPacketHandler implements DecodeListener {

	private final List<EasyPacketListener> listeners = new LinkedList<EasyPacketListener>();
	private final Map<EasyPacketListener, Boolean> actionMap = new HashMap<EasyPacketListener, Boolean>();

	@Override
	public final void trigger(@AssumeNotNull DecodeEvent event) {
		EasyPacketHeader header = EasyPacketHeader.valueOf(event.getData().read());
		EasyPacket easyPacket = new EasyPacket(header);
		for(Object object : event.getData().readRemaining())
			easyPacket.addArgument(object);

		for (EasyPacketListener listener : actionMap.keySet())
			if (actionMap.get(listener)) {
				listeners.add(listener);
			} else {
				listeners.remove(listener);
			}

		final String key = (String) easyPacket.getArguments().get(0);
		final ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(key);
		final SingleServerInstance serverInstance = ApplicationInterface.getAPI().getInfrastructure().getServerByKey(key);
		for (final EasyPacketListener listener : listeners)
			if (listener.getEasyPacketHeader() == easyPacket.getHeader() || listener.getEasyPacketHeader() == EasyPacketHeader.ALL) {
				if(serverInstance != null)
					listener.trigger(serverInstance, (EasyPacket) easyPacket);
				if(proxyServer != null)
					listener.trigger(proxyServer, (EasyPacket) easyPacket);
			}
	}

	@Deprecated
	public final List<EasyPacketListener> getListeners() {
		return listeners;
	}

	public final Map<EasyPacketListener, Boolean> getActionMap() {
		return actionMap;
	}
	
	public final void sendPacket(SingleServerInstance serverInstance, EasyPacket easyPacket) {
		DataContainer dataContainer = new DataContainer();
		dataContainer.add(easyPacket.getHeader().toString());
		for(Object object : dataContainer.readRemaining())
			dataContainer.add(object);
		serverInstance.getConnection().sendData(dataContainer);
	}
	
	public final void sendPacket(ProxyServer proxyServer, EasyPacket easyPacket) {
		DataContainer dataContainer = new DataContainer();
		dataContainer.add(easyPacket.getHeader().toString());
		for(Object object : dataContainer.readRemaining())
			dataContainer.add(object);
		proxyServer.getConnection().sendData(dataContainer);
	}
	
}
