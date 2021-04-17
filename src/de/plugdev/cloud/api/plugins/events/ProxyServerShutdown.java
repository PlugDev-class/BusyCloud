package de.plugdev.cloud.api.plugins.events;

import de.plugdev.cloud.api.plugins.Event;
import de.plugdev.cloud.infrastructure.SpigotServer;

public abstract class ProxyServerShutdown implements Event {
	
	public abstract void throwEvent(SpigotServer serverInfo);
	
}
