package de.plugdev.cloud.api.plugins.events;

import de.plugdev.cloud.api.PlayerInfo;
import de.plugdev.cloud.api.plugins.Event;

public abstract class ProxyPlayerLoginEvent implements Event {

	public abstract void throwEvent(PlayerInfo playerInfo);
	
}
