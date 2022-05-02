package de.plugdev.cloud.internal.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.generate.ServerGenerator;
import de.plugdev.cloud.internal.models.IVersion;
import de.plugdev.cloud.internal.utils.FileUtils;
import de.plugdev.cloud.packets.PacketSharedRconServer;
import de.terrarier.netlistening.Connection;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class SpigotServer implements IService {

	private Process instance;

	private final UUID uniqueId = UUID.randomUUID();
	private final UUID proxyId;
	private final String name;
	private final String serverGroup;
	private int port;
	private final int maxRam;
	private final IVersion version;
	private final boolean isStatic;

	private Connection connection;
	private String key;
	
	private final boolean isLobby;

	@Override
	@SneakyThrows
	public void start() {
		this.key = "KEY_" + new Random().nextInt(Integer.MAX_VALUE);
		
		if(isStatic &! new File("server/static/" + getName()).exists())
			new ServerGenerator(version, this);
		if(isStatic && new File("server/static/" + getName()).exists()) {
			FileUtils.writeFile(new File("server/static/" + getName() + "/" + key), key);
			FileUtils.copyFile(new File("backend/downloads/SpigotCloudBridge.jar").toPath(), new File("server/static/" + getName() + "/plugins/SpigotCloudBridge.jar").toPath());
		}
		if(isStatic) {
			port = Integer.parseInt(Files.readAllLines(new File("server/static/" + name + "/server.properties").toPath()).stream().filter((predicate) -> predicate.startsWith("server-port=")).findAny().get().replaceFirst("server-port=", ""));
		}
		
		String startCommand = "java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
				+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
				+ "-Dio.netty.recycler.maxCapacity.default=0 "
				+ "-Djline.terminal=jline.UnsupportedTerminal -Xmx" + maxRam + "M -jar " + version.getVersion()
				+ ".jar";
		new Thread(() -> {
			try {
				this.instance = Runtime.getRuntime().exec(startCommand.split(" "), null, new File("server/" + (isStatic ? "static" : "temp")+ "/" + this.name));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		((Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceById(proxyId).get()).addSpigotServer(this, isLobby);
	}

	@Override
	public void stop() {
		Optional<ServerGroup> serverGroup = ApplicationInterface.getAPI().getInfrastructure().getGroupByName(this.serverGroup);
		if(serverGroup.isPresent())
			serverGroup.get().getGroupList().remove(this);
		ApplicationInterface.getAPI().getInfrastructure().getServices().removeIf((predicate) -> predicate.getUniqueId()==uniqueId);
		if(instance.isAlive())
			instance.destroyForcibly();
		if(proxyId != null)
			((Proxy) ApplicationInterface.getAPI().getInfrastructure().getServiceById(proxyId).get()).removeSpigotServer(this);
		if(isStatic)
			return;
		new Thread(() -> {
			FileUtils.deleteFolderRecursivly(new File("server/" + ("temp") + "/" + this.getName()).getPath());
			FileUtils.deleteFile("server/" + ("temp") + "/" + this.getName());
		}).start();
		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD, "Stopping SpigotServer(\"" + serverGroup + " - localhost:" + port + "\")");
	}

	public void stopServer() {

		ServerGroup prefferedGroup = null;
		for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			if (group.getGroupList().contains(this)) {
				prefferedGroup = group;
			}
		}

		if (prefferedGroup != null) {
			prefferedGroup.getGroupList().remove(this);
		}

	}

	public void doTemplate(String servergroup) {
		if (isStatic)
			return;
		File serverFolder = new File("server/temp/" + getName());
		File backendTemplates = new File("backend/templates/" + servergroup);
		if (!backendTemplates.exists()) {
			backendTemplates.mkdirs();
		} else if (backendTemplates.listFiles().length != 0) {
			FileUtils.deleteFolderRecursivly(backendTemplates.getPath());
		}
		try {
			FileUtils.copyFolder(serverFolder.toPath(), backendTemplates.toPath());
			ConsoleOutput.write(ConsoleOutput.GREEN, "Template created.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@Override
	public void rcon(String command) {
		if(connection == null)
			return;
		if(!connection.isConnected())
			return;
		connection.sendData(new PacketSharedRconServer(command));
	}

}
