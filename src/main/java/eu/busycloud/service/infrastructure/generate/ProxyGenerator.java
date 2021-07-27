package eu.busycloud.service.infrastructure.generate;

import java.io.File;
import java.nio.file.Paths;

import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.utils.FileUtils;

public class ProxyGenerator extends ServerGenerator {

	private transient ProxyServer proxyServer;
	private transient String proxyServerPath;

	public ProxyGenerator(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
		generateServer();
	}

	@Override
	public void generateServer() {
		proxyServerPath = "server/temp/" + proxyServer.getProxyName();
		FileUtils.mkdirs(proxyServerPath);
		FileUtils.copyFolder(Paths.get("saves/templates", "proxy"), Paths.get(proxyServerPath, "."), false);
		FileUtils.copyFile(new File("saves/environments/" + proxyServer.getSoftware().getVersionName() + ".jar").toPath(),
				new File(proxyServerPath + "/" + proxyServer.getSoftware().getVersionName() + ".jar").toPath());
		FileUtils.mkdirs(proxyServerPath + "/plugins");
		FileUtils.copyFile(Paths.get("saves/environment-plugins", "CloudAPI.jar"), Paths.get(proxyServerPath + "/plugins", "CloudAPI.jar"));
	}

}
