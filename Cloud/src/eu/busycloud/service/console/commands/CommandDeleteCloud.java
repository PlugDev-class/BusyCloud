package eu.busycloud.service.console.commands;

import java.util.Random;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.ServerGroup;
import eu.busycloud.service.infrastructure.SpigotServer;
import eu.busycloud.service.utils.FileUtils;

public class CommandDeleteCloud extends ConsoleCommand {
	
	private boolean needToConfirm = true;
	private String deletecode = null;
	
	public CommandDeleteCloud(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(needToConfirm) {
			CloudInstance.LOGGER.warning("ARE YOU SURE TO RESET THE CLOUD? THIS WILL AFFECT ALL YOUR DATA ~NOT ONLY THE GROUPS!");
			CloudInstance.LOGGER.warning("YOU WON'T GET ANY SUPPORT IF YOU LOSE SOMETHING YOU DOESN'T WANTED TO!");
			CloudInstance.LOGGER.warning("-> if the less part of your brain thinks, you're ready, type in: */deletecloud confirm* <-");
			needToConfirm = false;
			return;
		}
		if(!needToConfirm && deletecode == null) {
			if(args.length != 2) {
				CloudInstance.LOGGER.warning("You've made an typo. '/deletecloud confirm'");
				return;
			}
			if(!args[1].equalsIgnoreCase("confirm")) {
				CloudInstance.LOGGER.warning("You've made an typo. '/deletecloud confirm'");
				return;
			}
			CloudInstance.LOGGER.warning("WOAH! WAIT! THINK ABOUT IT! THE CLOUD CAN WAIT, BUT DO YOU REALLY WANT TO LOSE EVERYTHING YOU BUILD UP?");
			CloudInstance.LOGGER.warning("-blushes- The 'backups'-Folder will be deleted as well! Save it, before it's to late!");
			CloudInstance.LOGGER.warning("THE LAST QUESTION: ARE YOU SURE? THEN TYPE IN: */deletecloud <Networkname> <Deletioncode>*");
			CloudInstance.LOGGER.warning("The deletecode will be displayed in 3 Seconds...");
			new Thread() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					CloudInstance.LOGGER.warning("Networkname: " + ApplicationInterface.getAPI().getInfrastructure().serverName);
					CloudInstance.LOGGER.warning("Deletioncode: " + (deletecode = generateRandomCode(6)));
				}
			}.run();
			return;
		}
		if(!needToConfirm && deletecode != null) {
			if(args.length == 3) {
				CloudInstance.LOGGER.warning("Okay.. If you want it so badly.. Yeah.. We'll do what I've to do.");
				deleteCloud(args[1], args[2]);
			} else {
				CloudInstance.LOGGER.warning("You've made an typo. '/deletecloud <Networkname> <Deletioncode>'");
			}
			return;
		}
	}
	
	private String generateRandomCode(int length) {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(length);
	    for (int i = 0; i < length; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    return buffer.toString();
	}
	
	private void deleteCloud(String networkName, String deletionCode) {
		CloudInstance.LOGGER.warning("========================= {Part 1 | Checking} =========================");
		System.out.print("Checking networkname ..");
		if(!ApplicationInterface.getAPI().getInfrastructure().serverName.toLowerCase().equals(networkName.toLowerCase())) {
			System.out.print(" failed! ~ Aborting deletion!\n");
			needToConfirm = true;
			deletecode = null;
			return;
		}
		System.out.print(" validated\n");
		System.out.print("Checking deletioncode ..");
		if(!deletionCode.equals(deletecode)) {
			System.out.print(" failed! ~ Aborting deletion!\n");
			needToConfirm = true;
			deletecode = null;
			return;
		}
		System.out.print(" validated\n");
		CloudInstance.LOGGER.warning("========================= {Part 2 | Stopping} =========================");
		for (ServerGroup runningGroups : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			CloudInstance.LOGGER.info("Stop group: " + runningGroups.getServerGroupContainer().getGroupName());
			runningGroups.stopServers();
		}
		
		for (ProxyServer runningProxies : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
			CloudInstance.LOGGER.info("Stop proxy: " + runningProxies.getProxyName());
			ApplicationInterface.getAPI().getInfrastructure().stopProxyServer(true, runningProxies.getProxyid());
		}

		for (SpigotServer runningServers : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
			CloudInstance.LOGGER.info("Stop spigotinstance: " + runningServers.getServerName());
			ApplicationInterface.getAPI().getInfrastructure().stopSpigotServer(runningServers.getId());
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		CloudInstance.LOGGER.warning("========================= {Part 3 | Deletion} =========================");
		System.out.print("Delete every file...");
		FileUtils.deleteFolderRecursivly(".");
		System.out.print(" done!\n");
		CloudInstance.LOGGER.warning("========================= {Part 4 | INTERRUPTION} =========================");
		CloudInstance.LOGGER.warning("Here's a message from the developer PlugDev, who developed this software:");
		CloudInstance.LOGGER.warning("Hey.. Thank you for using this program.. I really don't know why you're");
		CloudInstance.LOGGER.warning("doing this, but if the warnings are less valuable than my words, then");
		CloudInstance.LOGGER.warning("should it be. But for nowon, I wish you the best in the life");
		CloudInstance.LOGGER.warning("and if somethings happens not like you want to, think: you could do it better and");
		CloudInstance.LOGGER.warning("you are better! No one could do it better, than you!");
		CloudInstance.LOGGER.warning("Anyway. It's time to say goodbye and thanks for support me! Bye! <3");
		CloudInstance.LOGGER.warning("Maybe you write me a postcard on my mails: \"plugdev@outlook.com\"");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		CloudInstance.LOGGER.warning("========================= {Part 5 | Confirmation} =========================");
		CloudInstance.LOGGER.warning("Okay. Now you can close the application. If you want, you can start the jar again");
		CloudInstance.LOGGER.warning("and reinstall the cloud.");
		ApplicationInterface.getAPI().getConsole().getConsoleDefault().getCommandMap().clear();
		ApplicationInterface.getAPI().getConsole().getConsoleDefault().getCommandMap().put("shutdown", new CommandShutdown("~"));
		ApplicationInterface.getAPI().getConsole().getConsoleDefault().getCommandMap().put("help", new CommandHelp("~"));
	}
	
	
	
}
