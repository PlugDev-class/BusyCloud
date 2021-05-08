package de.plugdev.cloud.external.plugins;

import java.util.Collection;
import java.util.LinkedList;

import de.plugdev.cloud.external.ApplicationInterface;

public class Application {
	
	private Collection<Event> eventList = new LinkedList<Event>();
	
	public void onEnable(String[] args) {
		ApplicationInterface.getAPI().getPlugins().add(this);
	}
	
	public void onDisable(String[] args) {
		ApplicationInterface.getAPI().getPlugins().remove(this);
	}
	
	public void registerEvent(Event event) {
		eventList.add(event);
	}
	
	public void registerCommand(Event event) {
		eventList.remove(event);
	}
	
	public ApplicationInterface getAPI() {
		return ApplicationInterface.getAPI();
	}
	
	public Collection<Event> getEventList() {
		return eventList;
	}
	
}
