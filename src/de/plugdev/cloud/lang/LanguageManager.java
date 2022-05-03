package de.plugdev.cloud.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;

public class LanguageManager {
	
	private static final Map<String, String> MAP = new HashMap<>();
	
	public static String getVar(String key, String... strings) {
		String value = MAP.get(key);
		if(!value.contains("%var0%"))
			return value;
		for(int i = 0; i < 100; i++) {
			if(value.contains("%var" + i + "%"))
				value = value.replaceAll("%var" + i + "%", strings[i]);
			else
				break;
		}
		return value;
	}
	
	@SneakyThrows
	public void loadVar(File file) {
		loadVar(new FileInputStream(file));
	}
	
	@SneakyThrows
	public void loadVar(URL url) {
		loadVar(url.openStream());
	}
	
	@SneakyThrows
	private void loadVar(InputStream inputStream) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while((line = bufferedReader.readLine()) != null) {
			String[] split = line.split("; ");
			MAP.put(split[0], split[1]);
		}
		bufferedReader.close();
	}
	
}
