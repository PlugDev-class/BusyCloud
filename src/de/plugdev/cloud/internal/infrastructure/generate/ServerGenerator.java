package de.plugdev.cloud.internal.infrastructure.generate;

import java.io.File;
import java.io.IOException;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.infrastructure.MinecraftVersion;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;
import de.plugdev.cloud.internal.utils.FileUtils;
import de.plugdev.cloud.lang.ApiStatus.Between;

public class ServerGenerator {

	private String path = null;
	
	@Between
	public ServerGenerator(MinecraftVersion minecraftVersion, SpigotServer spigotServer) {
		path = "server/" + (spigotServer.isStatic() ? "static" : "temp") + "/" + spigotServer.getServerName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		if (!spigotServer.isStatic()) {
			File templateFolder = new File("backend/templates/" + spigotServer.getServerGroup());
			if (templateFolder.exists()) {
				try {
					FileUtils.copyFolder(templateFolder.toPath(), folder.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileUtils.copyFile(new File("backend/downloads/" + minecraftVersion.getVersion() + ".jar").toPath(),
					new File(path + "/" + minecraftVersion.getVersion() + ".jar").toPath());
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

	@Between
	public ServerGenerator(MinecraftVersion minecraftVersion, Proxy proxy) {

		path = "server/" + ("temp") + "/" + proxy.getProxyName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		writeServerproperties(minecraftVersion, proxy.getProxyName(), proxy.getPort());
		File templateFolder = new File("backend/templates/proxy");
		if (templateFolder.exists()) {
			if (templateFolder.listFiles().length != 0) {
				try {
					FileUtils.copyFolder(templateFolder.toPath(), folder.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		FileUtils.copyFile(new File("backend/downloads/" + minecraftVersion.getVersion() + ".jar").toPath(),
				new File(path + "/" + minecraftVersion.getVersion() + ".jar").toPath());

		File pluginFolder = new File(path + "/plugins");
		if (!pluginFolder.exists()) {
			pluginFolder.mkdir();
		}

		FileUtils.copyFile(new File("backend/downloads/BungeeCloudBridge.jar").toPath(),
				new File(path + "/plugins/BungeeCloudBridge.jar").toPath());

		FileUtils.writeFile(new File(path + "/" + proxy.getKey()), proxy.getKey());

	}
	
	private void writeServerproperties(MinecraftVersion version, String servername, int port) {
		StringBuilder builder = new StringBuilder();
		if (!version.isProxy()) {
			builder.append("server-ip=localhost\n");
			builder.append("server-port=" + port + "\n");
			builder.append("network-compression-threshold=256\n");
			builder.append("online-mode=false\n\n\n");
			FileUtils.writeFile(new File(path + "/server.properties"), builder.toString());
			FileUtils.writeFile(new File(path + "/bukkit.yml"), "connection-throttle: -1");
		}
	}

}
