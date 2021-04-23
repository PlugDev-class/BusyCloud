package de.plugdev.cloud.api.plugins.events;

import de.plugdev.cloud.api.plugins.Event;

public abstract class PacketStacktrace implements Event {
	
	boolean sendMessage = true;
	public void throwEvent(Exception exception) {
		if(sendMessage) {
			exception.printStackTrace();
		}
	}
	
	public void setSendMessage(boolean sendMessage) {
		this.sendMessage = sendMessage;
	}
	
	public boolean isSendMessage() {
		return sendMessage;
	}
	
}
