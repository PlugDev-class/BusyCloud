package eu.busycloud.service.console.screens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.infrastructure.Boot;
import eu.busycloud.service.utils.BootContainer;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.TextUtils;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;

public class ConsoleCloudSetup implements ConsoleScreen {

	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	private CloudSetupContainer[] cloudSetupContainers = {

			(new CloudSetupContainer("Are you accepting our current guidelines?", "Guidelines-Accept",
					AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Are you accepting any other by BusyCloud used guidelines?", "TP-Accept",
					AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Please type in your License-Key:", "License-Key", AnswerType.STRING, true)),
			(new CloudSetupContainer("Will you host a Minecraft-Java Server?", "Java-Server", AnswerType.BOOLEAN,
					true)),
			(new CloudSetupContainer("How much memory do you want to use for the busycloud in MB?", "RAM-in-MB",
					AnswerType.INTEGER, false)),
			(new CloudSetupContainer("If you are permitted to, do you want to use the latest ViaVersion?", "ViaVersion",
					AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("What's the name of your network?", "Network-Name", AnswerType.STRING, false)),
			(new CloudSetupContainer(
					"Do you want to use nibble-compression and varInt-compression for internal networking?"
					+ " It increases cpu-usage very low but safety very strong as well!",
					"Nibble-Networking", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Should BusyCloud preinstall some preferred proxy and spigot/nukkit-server",
					"Preinstall?", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Do you want to make the servers cross-compatible? (Java <-> Bedrock)",
					"Java <=> Bedrock", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Did you answer this questions truthfully and you're sure about it?",
					"Truthful answered", AnswerType.BOOLEAN, false))

	};

	int position = 0;

	public ConsoleCloudSetup() throws IOException {
		CloudInstance.LOGGER.info("You can skip *optional* questions with: 'skip'");
		CloudSetupContainer container = cloudSetupContainers[0];
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") " + container.question
				+ " [Type: " + container.answerType.toString() + ", Optional: " + container.optional + "]");
	}

	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("(Re/Force)installing BusyCloud, please wait a moment...");
		CloudInstance.LOGGER.warning("(Re/Force)installing BusyCloud: iterating through some files...");
		for (File file : new File[] { new File("configurations"), new File("developer"), new File("saves"),
				new File("configurations/cloudconfig.json"), new File("configurations/servergroups.json"),
				new File("developer/logs"), new File("developer/-= ..only for experts.. =-"), new File("saves/backups"),
				new File("saves/environment-plugins"), new File("saves/environments"), new File("saves/templates") }) {
			if (file.getName().endsWith(".json") || file.getName().endsWith(".check")) {
				file.createNewFile();
			} else {
				file.mkdir();
			}
		}
		CloudInstance.LOGGER.warning("(Re/Force)installing BusyCloud, finished");
		CloudInstance.LOGGER.info("Please install some software with '/install'");

		BootContainer bootContainer = new BootContainer();
		bootContainer.setAcceptGuidelines((boolean) cloudSetupContainers[0].getAnswer());
		bootContainer.setAccept3Guidelines((boolean) cloudSetupContainers[1].getAnswer());
		bootContainer.setLicenseKey((String) cloudSetupContainers[2].getAnswer());
		bootContainer.setMinecraftJavaUse((boolean) cloudSetupContainers[3].getAnswer());
		bootContainer.setMaxRam((int) cloudSetupContainers[4].getAnswer());
		bootContainer.setUseViaVersion((boolean) cloudSetupContainers[5].getAnswer());
		bootContainer.setNetworkName((String) cloudSetupContainers[6].getAnswer());
		bootContainer.setNibbleCompression((boolean) cloudSetupContainers[7].getAnswer());
		bootContainer.setPreinstall((boolean) cloudSetupContainers[8].getAnswer());
		bootContainer.setCrossCompatible((boolean) cloudSetupContainers[9].getAnswer());
		new Boot(bootContainer);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Path p = Paths.get("developer/logs", format.format(new Date(System.currentTimeMillis())) + ".log");
			if (!Files.exists(p.getParent()))
				Files.createDirectory(p.getParent());
			CloudInstance.LOGGER.addHandler(new FileHandler(
					"developer/logs/" + format.format(new Date(System.currentTimeMillis())) + ".log", true));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);

	}

	@Override
	public void scanLine(String input) {
		CloudSetupContainer question = cloudSetupContainers[position];
		if (!question.validateAnswer(input)) {
			CloudInstance.LOGGER.info("We couldn't recognize your input, please check the answertype.");
			return;
		}
		if (cloudSetupContainers.length == position + 1) {

			TextUtils.sendFatLine();
			CloudInstance.LOGGER.info("We're gonna install BusyCloud with this pre-set:");
			for (CloudSetupContainer container : cloudSetupContainers)
				CloudInstance.LOGGER.info(container.shortQuestion + ": " + container.answer);
			TextUtils.sendFatLine();

			try {
				completeInstallation();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CloudSetupContainer container = cloudSetupContainers[position += 1];
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") " + container.question
				+ " [Type: " + container.answerType.toString() + ", Optional: " + container.optional + "]");
	}

}
