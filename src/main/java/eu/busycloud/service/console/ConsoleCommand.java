package eu.busycloud.service.console;

public abstract class ConsoleCommand {

	private String help;
	
	public ConsoleCommand(String help) {
		this.help = help;
	}
	
	public abstract void runCommand(String command, String[] args);
	
	public String getHelp() {
		return help;
	}
	
}
