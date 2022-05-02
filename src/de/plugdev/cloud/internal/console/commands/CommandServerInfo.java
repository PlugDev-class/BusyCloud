package de.plugdev.cloud.internal.console.commands;

import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.internal.infrastructure.Proxy;

public class CommandServerInfo implements ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length != 2) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Wrong syntax. /serverinfo <ServiceId>");
			return;
		}
		UUID uuid = UUID.fromString(args[1]);
		if(uuid == null) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Service not found! UniqueId invalid.");
			return;
		}
		Optional<IService> iService = ApplicationInterface.getAPI().getInfrastructure().getServiceById(uuid);
		if(!iService.isPresent()) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Service not found!");
			return;
		}
		ConsoleOutput.write(ConsoleOutput.WHITE, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleOutput.write(ConsoleOutput.WHITE, "Serverinformations");
		ConsoleOutput.write(ConsoleOutput.WHITE, "- Servertype: " + ((iService.get().getVersion().isProxy()) ? "Proxy" : "Spigot"));
		ConsoleOutput.write(ConsoleOutput.WHITE, "- Servername: " + iService.get().getName());
		ConsoleOutput.write(ConsoleOutput.WHITE, "- Serverport: " + iService.get().getPort());
		ConsoleOutput.write(ConsoleOutput.WHITE, "- Serveraddress: localhost:" + iService.get().getPort());
		ConsoleOutput.write(ConsoleOutput.WHITE, "- Version: " + iService.get().getVersion().getVersion());
		ConsoleOutput.write(ConsoleOutput.WHITE, "- VersionURL: " + iService.get().getVersion().getURL());
		if(iService.get().getVersion().isProxy()) {
			ConsoleOutput.write(ConsoleOutput.WHITE, "- Verbundene Server: ");
			for (IService server : ((Proxy) iService.get()).getRegisteredServer()) {
				ConsoleOutput.write(ConsoleOutput.WHITE, "  - " + server.getName() + " (localhost:" + server.getPort() + ") ID::" + server.getUniqueId());
			}
		}
	}

	@Override
	public String getHelp() {
		return "Shows some service-infos.";
	}

}
