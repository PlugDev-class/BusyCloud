package eu.busycloud.service.infrastructure.generate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.SpigotServer;
import eu.busycloud.service.utils.FileUtils;

public class SpigotGenerator extends ServerGenerator {

	private transient SpigotServer spigotServer;
	private transient String spigotServerPath;

	public SpigotGenerator(SpigotServer spigotServer) {
		this.spigotServer = spigotServer;
		generateServer();
	}

	@Override
	public void generateServer() {
		spigotServerPath = "server/" + (spigotServer.isStatic() ? "static" : "temp") + "/"
				+ spigotServer.getServerName();
		FileUtils.mkdirs(spigotServerPath);
		if (!spigotServer.isStatic())
			FileUtils.copyFolder(Paths.get("saves/templates/", spigotServer.getServerGroup()),
					Paths.get(spigotServerPath, "."), false);
		FileUtils.checkFolder(spigotServerPath + "/plugins");
		if (ApplicationInterface.getAPI().getInfrastructure().useViaVersion) {
			if (!new File("saves/environment-plugins/ViaVersion-latest.jar").exists())
				FileUtils.download(
						"https://github.com/ViaVersion/ViaVersion/releases/download/4.0.1/ViaVersion-4.0.1.jar",
						"saves/environment-plugins/ViaVersions-latest.jar");
			FileUtils.copyFile(new File("saves\\environment-plugins\\ViaVersions-latest.jar").toPath(),
					new File(spigotServerPath + "/plugins/ViaVersion-latest.jar").toPath());
		}
		FileUtils.copyFile(
				new File("saves/environments/" + spigotServer.getServerSoftware().getVersionName() + ".jar").toPath(),
				new File(spigotServerPath + "/" + spigotServer.getServerSoftware().getVersionName() + ".jar").toPath());
		FileUtils.writeFile(new File(spigotServerPath + "/" + spigotServer.getRegisterKey()),
				spigotServer.getRegisterKey());
		FileUtils.mkdirs(spigotServerPath + "/plugins");
		FileUtils.copyFile(Paths.get("saves/environment-plugins", "CloudAPI.jar"), Paths.get(spigotServerPath + "/plugins", "CloudAPI.jar"));
		generateSpigotProperties();
	}

	@Override
	public void generateSpigotProperties() {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("server-ip=localhost\n");
			builder.append("server-port=" + spigotServer.getPort() + "\n");
			builder.append("network-compression-threshold=256\n");
			builder.append("online-mode=false\n\n\n");
			Files.write(Paths.get(spigotServerPath, "server.properties"), builder.toString().getBytes(),
					StandardOpenOption.CREATE_NEW);
			Files.write(Paths.get(spigotServerPath, "bukkit.yml"), "connection-throttle: -1".getBytes(),
					StandardOpenOption.CREATE_NEW);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
