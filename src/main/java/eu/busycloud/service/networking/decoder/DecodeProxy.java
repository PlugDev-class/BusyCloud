package eu.busycloud.service.networking.decoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.api.PlayerInfo;
import eu.busycloud.service.infrastructure.ProxyServer;

@Deprecated
public class DecodeProxy implements DecodeListener {

	{
		System.out.println("Proxy|b-1");
	}
	
	/**
	 * @since 0.1
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Proxy")) {
			final String title = event.getData().read();
			System.out.println("Proxy|b1");
			switch (title.toLowerCase()) {
			case "onenable()": {
				System.out.println("Proxy|b2");
				final String key = event.getData().read();
				System.out.println("Proxy|b3 - " + key);
				ProxyServer proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(key);
				System.out.println("Proxy|b4 - " + proxy);
				proxy.setConnection(event.getConnection());
				System.out.println("Proxy|b5 - " + proxy.getConnection());
				
				System.out.println("Proxy: " + proxy);
				System.out.println("Key: " + key);
				System.out.println("Connection: " + event.getConnection());
				
				CloudInstance.LOGGER.info("ProxyChannel \"" + proxy.getProxyid() + "\" connected!");

				event.getConnection().sendData("pushInformations", "maxPlayers", getInfo("bungeeCord.maxPlayers"));
				event.getConnection().sendData("pushInformations", "motdDescription", getInfo("bungeeCord.motdLine1") + "\n" + getInfo("bungeeCord.motdLine2"));
				event.getConnection().sendData("pushInformations", "motdProtocol", getInfo("bungeeCord.motdProtocol"));
				break;
			}
			case "playerconnect": {
				String proxyKey = event.getData().read();
				String playername = event.getData().read();
				UUID playeruuid = UUID.fromString(event.getData().read());
				String conectedServer = event.getData().read();
				String address = event.getData().read();

				ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayers()
						.add(new PlayerInfo(playername, playeruuid, conectedServer, address));

				CloudInstance.LOGGER.info("Player " + playername + " connected to "
						+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName()
						+ " - " + conectedServer);

				if (ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).isMaintenance()) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
							.sendRCON("kick " + playername + " §cThe server is currently in maintenance-mode.");
				}
				break;
			}
			case "playerdisconnect": {
				String proxyKey = event.getData().read();
				UUID playerUUID = UUID.fromString(event.getData().read());

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayers())
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayers()
							.remove(info);
					CloudInstance.LOGGER.info("Player " + info.getPlayername() + " disconnected from "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			case "playerswitchserver": {
				String proxyKey = event.getData().read();
				UUID playerUUID = UUID.fromString(event.getData().read());
				String from = event.getData().read();
				String to = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayers())
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayers()
							.remove(info);
					info.setConnectedServer(to);
					CloudInstance.LOGGER.info("Player " + info.getPlayername() + " switched from " + from + " to " + to
							+ " on "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			default:
				break;
			}
		}
	}

	JSONObject jsonObject;

	private String getInfo(String key) {
		try {
			if (jsonObject == null)
				jsonObject = new JSONObject(
						new String(Files.readAllBytes(new File("configurations/cloudconfig.json").toPath()), "UTF-8"));
			JSONObject bungeeObject = jsonObject.getJSONObject("bungeeCord");
			return (String) bungeeObject.get(key);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
