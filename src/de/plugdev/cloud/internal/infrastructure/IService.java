package de.plugdev.cloud.internal.infrastructure;

import java.util.UUID;

import de.plugdev.cloud.internal.models.IVersion;
import de.terrarier.netlistening.Connection;

public interface IService {
	
	String getKey();
	UUID getUniqueId();
	String getName();
	void start();
	void stop();
	void rcon(String command);
	void setConnection(Connection object);
	Connection getConnection();
	IVersion getVersion();
	int getPort();
	
}
