package eu.busycloud.service.console.screens.assistents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import com.google.gson.JsonParser;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.screens.ConsoleCloudDefault;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.ServerSoftware;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.TextUtils;

public class ConsoleAssistantProxyEdit implements ConsoleScreen {

	public ConsoleAssistantProxyEdit() {
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, true);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), false);

		TextUtils.sendFatLine();
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") "
				+ cloudSetupContainers[0].question + " [Type: " + cloudSetupContainers[0].answerType.toString()
				+ ", Optional: " + cloudSetupContainers[0].optional + "]");

	}

	private CloudSetupContainer[] cloudSetupContainers = {
			(new CloudSetupContainer("Which proxy do you want to edit?", "Proxy-Name", AnswerType.STRING, false)),
			(new CloudSetupContainer("Which port should the server use?", "Proxy-Port", AnswerType.INTEGER, false)),
			(new CloudSetupContainer("How many players can connect to the network?", "Proxy-MaxPlayers", AnswerType.INTEGER, false)),
			(new CloudSetupContainer("How much memory should the proxy take? > Answer in MB", "RAM", AnswerType.INTEGER, false)) };

	int position = 0;

	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("Validating answers...");
		if(ApplicationInterface.getAPI().getInfrastructure().getProxyByName((String) cloudSetupContainers[0].getAnswer()) == null) {
			CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. not found ~ ABORT");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. okay");
		
		ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyByName((String) cloudSetupContainers[0].getAnswer());
		if(cloudSetupContainers[1].getAnswer() != null)
			proxyServer.setPort((int) cloudSetupContainers[1].getAnswer());
		if(cloudSetupContainers[2].getAnswer() != null)
			proxyServer.setMaxPlayers((int) cloudSetupContainers[2].getAnswer());
		if(cloudSetupContainers[3].getAnswer() != null)
			proxyServer.setMaxRam((int) cloudSetupContainers[3].getAnswer());
		CloudInstance.LOGGER.warning("Writing to configurations/cloudconfig.json...");
		JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("configurations", "cloudconfig.json")), "UTF-8"));
		JSONObject array = jsonObject.getJSONObject("bungeeCord").getJSONObject("motdSettings");
		jsonObject.remove("bungeeCord");
		JSONObject newObject = new JSONObject();
		newObject.put("startport", proxyServer.getPort());
		newObject.put("maxPlayers", proxyServer.getMaxPlayers());
		newObject.put("maxRam", proxyServer.getMaxRam());
		newObject.put("motdSettings", array);
		jsonObject.put("bungeeCord", newObject);
		FileUtils.writeFile(new File("configurations/cloudconfig.json"), TextUtils.GSON.toJson(JsonParser.parseString(jsonObject.toString())));
		CloudInstance.LOGGER.warning("Closing assistant...");
		TextUtils.sendFatLine();
		
		proxyServer.stopProxy();
		ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().remove(proxyServer);
		for (ServerSoftware serverSoftware2 : ApplicationInterface.getAPI()
				.getInfrastructure().serverSoftwares)
			if (serverSoftware2.isAvailable() && serverSoftware2.getType() == ServerSoftwareType.PROXY) {
				ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().add(proxyServer);
				CloudInstance.LOGGER.info("Starting Proxy(\"" + proxyServer.getProxyName() + "\",\"" + serverSoftware2.getVersionName() + "\")");
				proxyServer.startProxy(serverSoftware2);
				break;
			}
		
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), true);
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
			CloudInstance.LOGGER.info("We're gonna edit a proxyserver with this pre-set:");
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
