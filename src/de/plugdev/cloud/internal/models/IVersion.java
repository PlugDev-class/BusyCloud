package de.plugdev.cloud.internal.models;

public interface IVersion {

	String getVersion();
	String getURL();
	boolean isAvailable();
	void setAvailable(boolean isAvailable);
	void download();
	boolean isProxy();
}
