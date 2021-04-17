package de.plugdev.cloud;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.infrastructure.Infrastructure;
import de.plugdev.cloud.infrastructure.MinecraftVersion;
import de.plugdev.cloud.infrastructure.Setup;

public class Cloud {

	private Infrastructure infrastructure;

	public static void main(String[] args) {
		ConsoleColors.write(ConsoleColors.CYAN, "[CORE] ServerCloud by PlugDev - 0.1");
		ApplicationInterface applicationInterface = new ApplicationInterface();
		Cloud cloud = new Cloud();
		applicationInterface.initializeInterface(cloud);
		cloud.preLoad();
	}

	public void preLoad() {
		infrastructure = new Infrastructure();
		if (new File("local/settings.json").exists()) {
			try {
				new Setup();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setup();
		}
	}

	boolean agreeLicensement;
	boolean agreeStatistics;
	String servername;
	int ramUsageInMbyte;

	String optimizationType;
	MinecraftVersion bungeeCordType;
	String spigotType;
	MinecraftVersion spigotServerVersion;
	boolean useViaversion;
	boolean agreeSettings = false;

	String feedback = null;

	public Scanner ressourceScanner = new Scanner(System.in);

	private void setup() {
		int step = 100;
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] Setup - Version: 0.1");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] Welcome to BusyCloud by PlugDev - 0.1");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] We detected a new installation or update to your program.");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] Please answer our questions and the server will do the rest");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		String answer = "";

		printStep(step, feedback);

		while ((answer = ressourceScanner.nextLine()) != null || (ressourceScanner != null)) {
			switch (step) {
			case 100:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					ConsoleColors.useColors = answer.equalsIgnoreCase("yes");
					step = 0;
				} else {
					feedback = "== Error >> I think I didn't understand your answer.. << Error ==";
				}
				break;
			case 0:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					agreeLicensement = answer.equalsIgnoreCase("yes");
					if (answer.equalsIgnoreCase("no")) {
						ConsoleColors.write(ConsoleColors.RED, "[CORE] You are not allowed to use this software!");
						System.exit(0);
					}
					step++;
				}
				break;
			case 1:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					agreeStatistics = answer.equalsIgnoreCase("yes");
					step++;
				}
				break;
			case 2:
				if (answer.length() != 0) {
					servername = answer;
					step++;
				}
				break;
			case 3:
				if (answer.length() != 0) {
					int tempInt = Integer.parseInt(answer);
					if (tempInt <= (Runtime.getRuntime().totalMemory() * 0.000001)) {
						if (tempInt >= (Runtime.getRuntime().freeMemory() * 0.000001)) {
							feedback = "== Warning >> You inserted RAM is greater than the current free Memory | Continuing program << Warning ==";
						}
						ramUsageInMbyte = tempInt;
						step++;
					} else {
						feedback = "== Error >> You don't have this much RAM in MByte << Error ==";
					}
				}
				break;
			case 4:
				if (answer.equalsIgnoreCase("minigames") || answer.equalsIgnoreCase("citybuild")
						|| answer.equalsIgnoreCase("both")) {
					optimizationType = answer;
					step++;
				} else {
					feedback = "== Error >> Optimizationtype not found. Maybe Deprecated? << Error ==";
				}
				break;
			case 5:
				if (answer.equalsIgnoreCase("BungeeCord") || answer.equalsIgnoreCase("Waterfall")) {
					if (getInfrastructure().isValidVersion(answer)) {
						bungeeCordType = getInfrastructure().getVersionById(answer);
						step++;
					} else {
						feedback = ("== Error >> Version not found! Please check the nature of your fork. << Error ==");
					}
				}
				break;
			case 6:
				if (answer.equalsIgnoreCase("Spigot") || answer.equalsIgnoreCase("Paper")
						|| answer.equalsIgnoreCase("Taco")) {
					spigotType = answer;
					step++;
				}
				break;
			case 7:
				if (getInfrastructure().isValidVersion(spigotType.toLowerCase() + "-" + answer)) {
					spigotServerVersion = getInfrastructure().getVersionById(spigotType.toLowerCase() + "-" + answer);
					step++;
				} else {
					feedback = ("== Error >> Version not found! Please check the nature of your fork. << Error ==");
				}
				break;
			case 8:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					useViaversion = answer.equalsIgnoreCase("yes");
					step++;
				}
				break;
			case 9:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					agreeSettings = answer.equalsIgnoreCase("yes");
				}

				if (!agreeSettings) {
					ConsoleColors.write(ConsoleColors.BLUE_BACKGROUND_BRIGHT, "[CORE] Please restart the program.");
					System.exit(0);
				}

				try {
					new Setup(agreeLicensement, agreeStatistics, servername, ramUsageInMbyte, optimizationType,
							bungeeCordType, spigotType, spigotServerVersion, useViaversion);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			default:
				break;
			}

			printStep(step, feedback);
		}
	}

	private void printStep(int step, String string) {
		for (int i = 0; i <= 250; i++) {
			ConsoleColors.write(ConsoleColors.CYAN, " ");
		}

		ConsoleColors.write(ConsoleColors.CYAN,
				"[SETUP] ====================================================================");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP]  _____      _               ");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] /  ___|    | |              ");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] \\ `--.  ___| |_ _   _ _ __  ");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP]  `--. \\/ _ \\ __| | | | '_ \\ ");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] /\\__/ /  __/ |_| |_| | |_) |");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] \\____/ \\___|\\__|\\__,_| .__/ ");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP]                      | |    ");
		ConsoleColors.write(ConsoleColors.CYAN, "[SETUP]                      |_|    ");
		ConsoleColors.write(ConsoleColors.CYAN,
				"[SETUP] Software by PlugDev and licensed under Apache License v2.0 GPL.");
		if (string != null) {
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP]  ");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] " + string);
			feedback = null;
		}
		ConsoleColors.write(ConsoleColors.CYAN,
				"[SETUP] ====================================================================");

		switch (step) {
		case 100:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Hey! Thank you for downloading my software.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] The first question of the day..");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Do you want to use colors in the console?");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] In Windows it's buggy and glitchy.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] And in Linux it works fine.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 0:
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] First of all! Thank you for using our cloud and appreciating the work behind it!");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] To let the program runs third-party software, you are agreeing the third-party license.");
			ConsoleColors.write(ConsoleColors.RED_BOLD_BRIGHT,
					"[SETUP] Even though you accept the latest EULA-Minecraft!");
			ConsoleColors.write(ConsoleColors.RED_BOLD_BRIGHT,
					"[SETUP] Every violate against the EULA does go to the end user and not to PlugDev!");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] If you do not, you can easily type 'no' and deleting the program.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 1:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Now we have another yes/no question.");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Do you want to help the Developers to share some statistics.");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] This statistics would include this: Activeplayers, Clouderrors, Pluginerrors.");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] This statistics would not include this: Passwords, Whole Plugins, whole Files.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] This would be send anyway: Servername, ServerID.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 2:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Next we need a name, to identify your hard work!");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] If you don't have a servername, you shouldn't use a cloud publicly.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] You're free to use any No-NSFW names.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <*>");
			break;
		case 3:
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Now we need a value how much RAM in MByte do you want to use.");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Total-Memory: " + (Runtime.getRuntime().totalMemory() * 0.000001) + " MByte");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Available-Memory: " + (Runtime.getRuntime().freeMemory() * 0.000001) + " MByte");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <Integervalue> (in MByte)");
			break;
		case 4:
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Do you want to optimize for 'Minigames', 'Citybuild' or 'Both'?");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'MiniGames', 'CityBuild', 'Both'>");
			break;
		case 5:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Now we setup your Proxy.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Which type/fork of bungeecord do you want to use?");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'BungeeCord', 'Waterfall'>");
			break;
		case 6:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Now we setup your Spigotserver.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Which type/fork of spigot do you want to use?");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'Spigot', 'Paper', 'Taco'>");
			break;
		case 7:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Which default minecraft-version do you prefer?");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Possible answerchoices:");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.7.10'>");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] <'1.8', '1.8.3', '1.8.4', '1.8.5', '1.8.6', '1.8.7', '1.8.8'*$>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.9', '1.9.2'$, '1.9.4'*$>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.10', '1.10.2'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.11', '1.11.1', '1.11.2'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.12', '1.12.1', '1.12.2'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.13', '1.13.1', '1.13.2'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.14', '1.14.1', '1.14.2', '1.14.3', '1.14.4'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.15', '1.15.1', '1.15.2'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] <'1.16.1', '1.16.2', '1.16.3', '1.16.4', '1.16.5'*>");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] * means that paper is available too.");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] $ means that taco is available too.");
			break;
		case 8:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Do you want to use the thirdparty-plugin ViaVersion?");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] This plugin will allows your player to join outdated server.");
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Important it only goes downwards! Server 1.8.1 << Client 1.9.1");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 9:
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Do you want to continue with the following settings?");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Agree Licensement: " + agreeLicensement);
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Agree Statistics: " + agreeStatistics);
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Servername: " + servername);
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Percentage of RAM-use: " + ramUsageInMbyte);
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Server-Optimization: " + optimizationType);
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] BungeeCordFork: " + bungeeCordType.getVersion());
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Preffered Spigotversion: " + spigotServerVersion.getVersion());
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Thirdparty ViaVersion: " + useViaversion);
			ConsoleColors.write(ConsoleColors.CYAN,
					"[SETUP] Some settings may aren't changeable after this confirmation!");
			ConsoleColors.write(ConsoleColors.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		default:
			break;
		}
	}

	public Infrastructure getInfrastructure() {
		return infrastructure;
	}

}
