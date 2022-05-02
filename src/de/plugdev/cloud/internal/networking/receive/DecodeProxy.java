package de.plugdev.cloud.internal.networking.receive;

import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.plugdev.cloud.packets.PacketLinkServer;
import de.plugdev.cloud.packets.PacketProxyPlayerConnect;
import de.plugdev.cloud.packets.PacketProxyPlayerDisconnect;
import de.plugdev.cloud.packets.PacketProxyPlayerSwitchServer;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeProxy implements DecodeListener {

	/*
	 * @since FORK v1.03 -> v1
	 * 
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final Object receiver = event.getData().read();
		if(receiver instanceof PacketLinkServer) {
			if(!((PacketLinkServer) receiver).getHeaderList().get(0).equalsIgnoreCase("Proxy"))
				return;
			final String key = (String) ((PacketLinkServer) receiver).getObjectList().get(0);
			final IService service = ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(key).get();
			service.setConnection(event.getConnection());
			ConsoleOutput.write(ConsoleOutput.GREEN, "ProxyChannel \"" + service.getName() + "\" connected.");
		}
		if(receiver instanceof PacketProxyPlayerConnect) {
			final String proxyKey = (String) ((PacketProxyPlayerConnect) receiver).getObjectList().get(0);
			final String playerName = (String) ((PacketProxyPlayerConnect) receiver).getObjectList().get(1);
			final String playerUniqueId = (String) ((PacketProxyPlayerConnect) receiver).getObjectList().get(2);
			final String connectedServer = (String) ((PacketProxyPlayerConnect) receiver).getObjectList().get(3);
			final String address = (String) ((PacketProxyPlayerConnect) receiver).getObjectList().get(4);
			((Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(proxyKey).get()).getOnlinePlayer().add(new PlayerInfo(playerName, UUID.fromString(playerUniqueId), connectedServer, address));
		}
		if(receiver instanceof PacketProxyPlayerDisconnect) {
			final String proxyKey = (String) ((PacketProxyPlayerDisconnect) receiver).getObjectList().get(0);
			final String playerUniqueId = (String) ((PacketProxyPlayerDisconnect) receiver).getObjectList().get(1);
			((Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(proxyKey).get()).getOnlinePlayer().removeIf((predicate) -> predicate.getUniqueID() == UUID.fromString(playerUniqueId));
		}
		if(receiver instanceof PacketProxyPlayerSwitchServer) {
			final String proxyKey = (String) ((PacketProxyPlayerSwitchServer) receiver).getObjectList().get(0);
			final String playerUniqueId = (String) ((PacketProxyPlayerSwitchServer) receiver).getObjectList().get(1);
			final String from = (String) ((PacketProxyPlayerSwitchServer) receiver).getObjectList().get(2);
			final String to = (String) ((PacketProxyPlayerSwitchServer) receiver).getObjectList().get(3);
			Optional<PlayerInfo> info = ((Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceByKey(proxyKey).get()).getOnlinePlayer().stream().filter((predicate) -> predicate.getUniqueID() == UUID.fromString(playerUniqueId)).findAny();
			if(info.isPresent()) {
				info.get().setConnectedServer(to);
				ConsoleOutput.write(ConsoleOutput.GREEN, "Player " + playerUniqueId + " switched " + from + " >> " + to);
			} else {
				ConsoleOutput.write(ConsoleOutput.GREEN, "Player " + playerUniqueId + " switched ??? >> " + to);
			}
		}
	}

}
