package de.plugdev.cloud.internal.console;

import de.plugdev.cloud.lang.ApiStatus.Between;
import de.plugdev.cloud.lang.ApiStatus.Internal;

@Internal
public abstract class ConsoleCommand {
	
	@Between
	private String help;
	
	@Between
	public abstract void runCommand(String command, String[] args);
	
	@Between
	public void setHelp(String help) {
		this.help = help;
	}
	
	@Between
	public String getHelp() {
		return help;
	}
	
}
