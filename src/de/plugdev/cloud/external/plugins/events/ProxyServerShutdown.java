package de.plugdev.cloud.external.plugins.events;

import de.plugdev.cloud.external.plugins.Event;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;

public abstract class ProxyServerShutdown implements Event {
	
	public abstract void throwEvent(SpigotServer serverInfo);
	
}
