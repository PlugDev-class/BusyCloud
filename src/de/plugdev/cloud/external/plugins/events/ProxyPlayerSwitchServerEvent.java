package de.plugdev.cloud.external.plugins.events;

import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.external.plugins.Event;

public abstract class ProxyPlayerSwitchServerEvent implements Event {
	
	public abstract void throwEvent(PlayerInfo playerInfo);
	
}
