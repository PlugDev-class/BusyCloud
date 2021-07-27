package eu.busycloud.service.infrastructure.generate;

import java.io.File;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.ServerSoftware;
import eu.busycloud.service.infrastructure.SpigotServer;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;
import eu.busycloud.service.utils.FileUtils;

@Deprecated
public class ServerGenerator2 {

	private String path = null;
	
	public ServerGenerator2(ServerSoftware minecraftVersion, SpigotServer spigotServer) {
		path = "server/" + (spigotServer.isStatic() ? "static" : "temp") + "/" + spigotServer.getServerName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		if (!spigotServer.isStatic()) {
			File templateFolder = new File("backend/templates/" + spigotServer.getServerGroup());
			if (templateFolder.exists()) {
				FileUtils.copyFolder(templateFolder.toPath(), folder.toPath());
			}
			FileUtils.copyFile(new File("backend/downloads/" + minecraftVersion.getVersionName() + ".jar").toPath(),
					new File(path + "/" + minecraftVersion.getVersionName() + ".jar").toPath());
		}
		
		writeServerproperties(minecraftVersion, spigotServer.getServerName(), spigotServer.getPort());

		File pluginFolder = new File(path + "/plugins");
		FileUtils.checkFolder(pluginFolder);

		if (ApplicationInterface.getAPI().getInfrastructure().useViaVersion) {
			if(!new File("backend/downloads/ViaVersion-3.2.1.jar").exists()) {
				FileUtils.download("https://github.com/ViaVersion/ViaVersion/releases/download/3.2.1/ViaVersion-3.2.1.jar", "backend/downloads/ViaVersion-3.2.1.jar");
			}
			FileUtils.copyFile(new File("backend/downloads/ViaVersion-3.2.1.jar").toPath(), new File(path + "/plugins/ViaVersion-3.2.1.jar").toPath());
		}
		FileUtils.copyFile(new File("backend/downloads/SpigotCloudBridge.jar").toPath(), new File(path + "/plugins/SpigotCloudBridge.jar").toPath());
		FileUtils.writeFile(new File(path + "/" + spigotServer.getRegisterKey()), spigotServer.getRegisterKey());
	}

	public ServerGenerator2(ServerSoftware serverSoftware, ProxyServer proxy) {

		path = "server/" + ("temp") + "/" + proxy.getProxyName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		writeServerproperties(serverSoftware, proxy.getProxyName(), proxy.getPort());
		File templateFolder = new File("backend/templates/proxy");
		if (templateFolder.exists()) {
			if (templateFolder.listFiles().length != 0) {
				FileUtils.copyFolder(templateFolder.toPath(), folder.toPath());
			}
		}
		FileUtils.copyFile(new File("backend/downloads/" + serverSoftware.getVersionName() + ".jar").toPath(),
				new File(path + "/" + serverSoftware.getVersionName() + ".jar").toPath());

		File pluginFolder = new File(path + "/plugins");
		if (!pluginFolder.exists()) {
			pluginFolder.mkdir();
		}

		FileUtils.copyFile(new File("backend/downloads/BungeeCloudBridge.jar").toPath(),
				new File(path + "/plugins/BungeeCloudBridge.jar").toPath());

		FileUtils.writeFile(new File(path + "/" + proxy.getKey()), proxy.getKey());

	}
	
	private void writeServerproperties(ServerSoftware serverSoftware, String servername, int port) {
		StringBuilder builder = new StringBuilder();
		if (!(serverSoftware.getType() == ServerSoftwareType.PROXY)) {
			builder.append("server-ip=localhost\n");
			builder.append("server-port=" + port + "\n");
			builder.append("network-compression-threshold=256\n");
			builder.append("online-mode=false\n\n\n");
			FileUtils.writeFile(new File(path + "/server.properties"), builder.toString());
			FileUtils.writeFile(new File(path + "/bukkit.yml"), "connection-throttle: -1");
		}
	}

}
