package de.plugdev.cloud.console;

public abstract class ConsoleCommand {
	
	private String help;
	
	public abstract void runCommand(String command, String[] args);
	
	public void setHelp(String help) {
		this.help = help;
	}
	
	public String getHelp() {
		return help;
	}
	
}
