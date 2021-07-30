package eu.busycloud.service.console.screens.assistents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.JsonParser;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.screens.ConsoleCloudDefault;
import eu.busycloud.service.infrastructure.ServerGroup;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.ServerGroupContainer;
import eu.busycloud.service.utils.TextUtils;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;

public class ConsoleAssistantGroupCreate implements ConsoleScreen {

	public ConsoleAssistantGroupCreate() {
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, true);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), false);

		TextUtils.sendFatLine();
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") "
				+ cloudSetupContainers[0].question + " [Type: " + cloudSetupContainers[0].answerType.toString()
				+ ", Optional: " + cloudSetupContainers[0].optional + "]");

	}

	private CloudSetupContainer[] cloudSetupContainers = {

			(new CloudSetupContainer("Which name should your group have?", "Groupname", AnswerType.STRING, false)),
			(new CloudSetupContainer("Which serversoftware your group should run?", "ServerSoftware", AnswerType.STRING,
					false)),
			(new CloudSetupContainer("At which port should your server start? 0-65535", "Port", AnswerType.INTEGER,
					false)),
			(new CloudSetupContainer("Please name a random number for the groupid.", "GroupId", AnswerType.INTEGER,
					false)),
			(new CloudSetupContainer("How much memory should a single server take? > Answer in MB", "RAM",
					AnswerType.INTEGER, false)),
			(new CloudSetupContainer("Please type in a percentrate between 30-100, to define when a new server start?",
					"Percentrate", AnswerType.INTEGER, false)),
			(new CloudSetupContainer("How much servers should start at beginning?", "Begin", AnswerType.INTEGER,
					false)),
			(new CloudSetupContainer("Is this a lobby-servergroup?", "Lobby", AnswerType.BOOLEAN, false)) };

	int position = 0;

	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("Validating answers...");
		CloudInstance.LOGGER.info(cloudSetupContainers[0].getAnswer() + " .. okay");
		if (ApplicationInterface.getAPI().getInfrastructure()
				.isValidSoftware(((String) cloudSetupContainers[1].getAnswer()))) {
			if (!ApplicationInterface.getAPI().getInfrastructure()
					.getSoftwareById(((String) cloudSetupContainers[1].getAnswer())).isAvailable())
				ApplicationInterface.getAPI().getInfrastructure()
						.getSoftwareById((String) cloudSetupContainers[1].getAnswer()).download();
			CloudInstance.LOGGER.info(cloudSetupContainers[1].getAnswer() + " .. okay");
		} else {
			CloudInstance.LOGGER.info(cloudSetupContainers[1].getAnswer() + " .. doesn't exists! ~ ABORT");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		int port = (int) cloudSetupContainers[2].getAnswer();
		if (!(port < 0 && port > 65535)) {
			CloudInstance.LOGGER.info(port + " .. okay");
		} else {
			CloudInstance.LOGGER.info(port + " .. out of range! ~ ABORT");
			CloudInstance.LOGGER.info("Be sure to set a startport between 0 and 65535.");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		int ram = (int) cloudSetupContainers[4].getAnswer();
		if (ram < Runtime.getRuntime().maxMemory()) {
			CloudInstance.LOGGER.info(ram + "M .. okay");
		} else {
			CloudInstance.LOGGER.info(ram + "M .. you don't have that much ram! ~ ABORT");
			CloudInstance.LOGGER.info("Be sure you started your cloud with max-ram parameter, like:");
			CloudInstance.LOGGER.info("\tusage: java -jar -Xmx\"MAX-RAM\"M BusyCloud.jar");
			CloudInstance.LOGGER.info("\tex: java -jar -Xmx4096M BusyCloud.jar");
			CloudInstance.LOGGER.info("We need to know that, to calculate your total memory of your system.");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			return;
		}
		int percent = (int) cloudSetupContainers[5].getAnswer();
		if (percent > 29 && percent < 101) {
			CloudInstance.LOGGER.info(ram + "% .. okay");
		} else {
			CloudInstance.LOGGER.info(ram + "M .. out of safe range! ~ ABORT");
			CloudInstance.LOGGER.info("Be sure to set a percentrate between 30 and 100.");
		}
		CloudInstance.LOGGER.info(cloudSetupContainers[6].getAnswer() + " .. okay");
		CloudInstance.LOGGER.info(cloudSetupContainers[7].getAnswer() + " .. okay");

		CloudInstance.LOGGER.warning("Writing to servergroups.json...");
		JSONObject jsonObject = new JSONObject(
				new String(Files.readAllBytes(Paths.get("configurations", "servergroups.json")), "UTF-8"));
		Map<String, Object> serverMap = new LinkedHashMap<String, Object>();
		serverMap.put("serverSoftware", (String) cloudSetupContainers[1].getAnswer());
		serverMap.put("startPort", port);
		serverMap.put("groupId", (int) cloudSetupContainers[3].getAnswer());
		serverMap.put("maxRamEachServer", ram);
		serverMap.put("startServerByGroupstart", (int) cloudSetupContainers[6].getAnswer());
		serverMap.put("startNewServerByPercentage", percent);
		serverMap.put("lobbyState", (boolean) cloudSetupContainers[7].getAnswer());
		jsonObject.put((String) cloudSetupContainers[0].getAnswer(), serverMap);
		FileUtils.writeFile(new File("configurations/servergroups.json"),
				TextUtils.GSON.toJson(JsonParser.parseString(jsonObject.toString())));
		CloudInstance.LOGGER.warning("Writing done.");
		CloudInstance.LOGGER.warning("Closing assistant...");
		TextUtils.sendFatLine();

		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), true);

		new ServerGroup(new ServerGroupContainer((String) cloudSetupContainers[0].getAnswer()));
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
			CloudInstance.LOGGER.info("We're gonna create a servergroup with this pre-set:");
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
