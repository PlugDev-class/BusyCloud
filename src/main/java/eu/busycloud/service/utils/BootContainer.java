package eu.busycloud.service.utils;

public class BootContainer {
	
	private boolean acceptGuidelines, accept3Guidelines, crossCompatible;
	private boolean useViaVersion, minecraftJavaUse, nibbleCompression, preinstall;
	private String networkName, licenseKey;
	private int maxRam;
	
	/**
	 * @return the acceptGuidelines
	 */
	public final boolean isAcceptGuidelines() {
		return acceptGuidelines;
	}
	/**
	 * @return the accept3Guidelines
	 */
	public final boolean isAccept3Guidelines() {
		return accept3Guidelines;
	}
	/**
	 * @return the crossCompatible
	 */
	public final boolean isCrossCompatible() {
		return crossCompatible;
	}
	/**
	 * @return the useViaVersion
	 */
	public final boolean isUseViaVersion() {
		return useViaVersion;
	}
	/**
	 * @return the minecraftJavaUse
	 */
	public final boolean isMinecraftJavaUse() {
		return minecraftJavaUse;
	}
	/**
	 * @return the networkName
	 */
	public final String getNetworkName() {
		return networkName;
	}
	/**
	 * @return the licenseKey
	 */
	public final String getLicenseKey() {
		return licenseKey;
	}
	/**
	 * @return the maxRam
	 */
	public final int getMaxRam() {
		return maxRam;
	}
	/**
	 * @param acceptGuidelines the acceptGuidelines to set
	 */
	public final void setAcceptGuidelines(boolean acceptGuidelines) {
		this.acceptGuidelines = acceptGuidelines;
	}
	/**
	 * @param accept3Guidelines the accept3Guidelines to set
	 */
	public final void setAccept3Guidelines(boolean accept3Guidelines) {
		this.accept3Guidelines = accept3Guidelines;
	}
	/**
	 * @param crossCompatible the crossCompatible to set
	 */
	public final void setCrossCompatible(boolean crossCompatible) {
		this.crossCompatible = crossCompatible;
	}
	/**
	 * @param useViaVersion the useViaVersion to set
	 */
	public final void setUseViaVersion(boolean useViaVersion) {
		this.useViaVersion = useViaVersion;
	}
	/**
	 * @param minecraftJavaUse the minecraftJavaUse to set
	 */
	public final void setMinecraftJavaUse(boolean minecraftJavaUse) {
		this.minecraftJavaUse = minecraftJavaUse;
	}
	/**
	 * @param networkName the networkName to set
	 */
	public final void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	/**
	 * @param licenseKey the licenseKey to set
	 */
	public final void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}
	/**
	 * @param maxRam the maxRam to set
	 */
	public final void setMaxRam(int maxRam) {
		this.maxRam = maxRam;
	}
	/**
	 * @return the nibbleCompression
	 */
	public boolean isNibbleCompression() {
		return nibbleCompression;
	}
	/**
	 * @param nibbleCompression the nibbleCompression to set
	 */
	public void setNibbleCompression(boolean nibbleCompression) {
		this.nibbleCompression = nibbleCompression;
	}
	
	public boolean isPreinstall() {
		return preinstall;
	}
	
	public void setPreinstall(boolean preinstall) {
		this.preinstall = preinstall;
	}
	
}
