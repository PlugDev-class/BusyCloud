package de.plugdev.cloud.api.plugins.events;

public abstract class PacketStacktrace {
	
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
