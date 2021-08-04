package eu.busycloud.service.networking.easypackets.decoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.networking.easypackets.EasyPacket;
import eu.busycloud.service.networking.easypackets.EasyPacketHandler;
import eu.busycloud.service.networking.easypackets.EasyPacketHeader;
import eu.busycloud.service.networking.easypackets.EasyPacketListener;

public class DecodeProxy extends EasyPacketListener {

	public DecodeProxy(EasyPacketHeader receiver) {
		super(receiver);
	}
	
	@Override
	public void trigger(ProxyServer proxyServer, EasyPacket easyPacket) {
		switch ((String) easyPacket.getArguments().get(1)) {
		case "ENABLE":
		{
			CloudInstance.LOGGER.info("ProxyChannel \"" + proxyServer.getProxyName() + "\" connected");
			final JSONObject jsonObject = (JSONObject) getInfo("motdSettings");
			final String motdHeader = jsonObject.getString("motdLine1");
			final String motdFooter = jsonObject.getString("motdLine2");
			final String motdProtocol = jsonObject.getString("motdProtocol");
			final JSONArray playerInfoArray = jsonObject.getJSONArray("motdPlayerInfo");
			
			final EasyPacketHandler easyPacketHandler = ApplicationInterface.getAPI().getNetworking().getEasyPacketHandler();
			easyPacketHandler.sendPacket(proxyServer, new EasyPacket(EasyPacketHeader.CLOUD, "pushInformations", "motdHeader", motdHeader));
			easyPacketHandler.sendPacket(proxyServer, new EasyPacket(EasyPacketHeader.CLOUD, "pushInformations", "motdFooter", motdFooter));
			easyPacketHandler.sendPacket(proxyServer, new EasyPacket(EasyPacketHeader.CLOUD, "pushInformations", "motdProtocol", motdProtocol));
			playerInfoArray.forEach((object) -> {
				easyPacketHandler.sendPacket(proxyServer, new EasyPacket(EasyPacketHeader.CLOUD, "pushInformations", "motdPlayerInfo", (String) object));
			});
			break;
		}
		case "DISABLE":
		{
			CloudInstance.LOGGER.info("ProxyChannel \"" + proxyServer.getProxyName() + "\" disconnected");
			proxyServer.stopProxy();
			break;
		}
		default:
			break;
		}
	}
	
	JSONObject jsonObject;
	private Object getInfo(String key) {
		try {
			if (jsonObject == null)
				jsonObject = new JSONObject(
						new String(Files.readAllBytes(new File("configurations/cloudconfig.json").toPath()), "UTF-8"));
			JSONObject bungeeObject = jsonObject.getJSONObject("bungeeCord");
			return bungeeObject.get(key);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
