package de.plugdev.cloud.api.plugins.events;

import de.plugdev.cloud.api.plugins.Event;
import de.plugdev.cloud.infrastructure.SpigotServer;

public abstract class ProxyServerStarting implements Event {

	public abstract void throwEvent(SpigotServer server);

}
