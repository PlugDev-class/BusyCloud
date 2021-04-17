package de.plugdev.cloud.api.plugins.events;

import de.plugdev.cloud.api.plugins.Event;

public abstract class PacketReceive implements Event {
	
	public abstract void throwEvent();
	
}
