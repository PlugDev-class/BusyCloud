package eu.busycloud.service.settings;

import java.util.HashMap;
import java.util.Map;

public class SettingsInstance {
	
	private Map<Class<?>, SettingsClass> cacheMap = new HashMap<Class<?>, SettingsClass>();
	
	public Map<Class<?>, SettingsClass> getCacheMap() {
		return cacheMap;
	}
	
}
