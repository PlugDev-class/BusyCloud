package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandGroupStartServer extends ConsoleCommand {
	
	/* /startserver <Group> <ProxyID> */
	
	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			String servergroup = args[1];
			ServerGroup prefferedGroup = null;
			for(ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
				if(group.getGroupName().equalsIgnoreCase(servergroup)) {
					prefferedGroup = group;
					break;
				}
			}
			
			if(prefferedGroup == null) {
				ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] No Group with specific name found.");
				return;
			}
			
			prefferedGroup.startServer(true, prefferedGroup.getGroupList().size()+1);
			
		}
	}
	
}
