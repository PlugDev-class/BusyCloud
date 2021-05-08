package de.plugdev.cloud.internal.guiinterface.interfaces;

import de.plugdev.cloud.lang.ApiStatus.Experimental;

@Experimental
public interface Messageable {
	
	public void pushMessage(String message);
	
}
