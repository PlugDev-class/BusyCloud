package de.plugdev.cloud.api.plugins.events;

import de.plugdev.cloud.api.PlayerInfo;
import de.plugdev.cloud.api.plugins.Event;

public abstract class SpigotPlayerSendMessage implements Event {
	
	public abstract void throwEvent(PlayerInfo playerInfo, String message);
	
}
