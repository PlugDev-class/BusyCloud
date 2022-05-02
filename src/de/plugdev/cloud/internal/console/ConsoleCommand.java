package de.plugdev.cloud.internal.console;

import de.plugdev.cloud.lang.ApiStatus.Between;
import de.plugdev.cloud.lang.ApiStatus.Internal;

@Internal
public interface ConsoleCommand {
	
	@Between
	void runCommand(String command, String[] args);
	
	@Between
	String getHelp();
}
