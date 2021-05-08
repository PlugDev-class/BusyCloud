package de.plugdev.cloud.external.plugins.events;

import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.external.plugins.Event;

public abstract class SpigotPlayerSendMessage implements Event {
	
	public abstract void throwEvent(PlayerInfo playerInfo, String message);
	
}
