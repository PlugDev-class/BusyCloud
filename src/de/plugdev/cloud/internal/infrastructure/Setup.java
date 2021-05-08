package de.plugdev.cloud.internal.infrastructure;

import java.io.IOException;
import java.util.Scanner;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class Setup {
	
	boolean agreeLicensement;
	boolean agreeStatistics;
	String servername;

	String optimizationType;
	MinecraftVersion bungeeCordType;
	String spigotType;
	MinecraftVersion spigotServerVersion;
	boolean useViaversion;
	boolean agreeSettings = false;
	String feedback = null;
	

	public Scanner ressourceScanner = new Scanner(System.in);
	
	public Setup() {
		int step = 100;
		String answer = "";

		printStep(step, feedback);

		while ((answer = ressourceScanner.nextLine()) != null || (ressourceScanner != null)) {
			switch (step) {
			case 100:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					ConsoleOutput.useColors = answer.equalsIgnoreCase("yes");
					step = 0;
				} else {
					feedback = "== Error >> I think I didn't understand your answer.. << Error ==";
				}
				break;
			case 0:
				if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
					agreeLicensement = answer.equalsIgnoreCase("yes");
					if (answer.equalsIgnoreCase("no")) {
						ConsoleOutput.write(ConsoleOutput.RED, "[CORE] You are not allowed to use this software!");
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
					step+=2;
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
					if (ApplicationInterface.getAPI().getInfrastructure().isValidVersion(answer)) {
						bungeeCordType = ApplicationInterface.getAPI().getInfrastructure().getVersionById(answer);
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
				if (ApplicationInterface.getAPI().getInfrastructure().isValidVersion(spigotType.toLowerCase() + "-" + answer)) {
					spigotServerVersion = ApplicationInterface.getAPI().getInfrastructure().getVersionById(spigotType.toLowerCase() + "-" + answer);
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
					ConsoleOutput.write(ConsoleOutput.BLUE_BACKGROUND_BRIGHT, "[CORE] Please restart the program.");
					System.exit(0);
				}

				try {
					new Boot(agreeLicensement, agreeStatistics, servername, optimizationType,
							bungeeCordType, spigotType, spigotServerVersion, useViaversion, ressourceScanner);
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
			ConsoleOutput.write(ConsoleOutput.CYAN, " ");
		}

		ConsoleOutput.write(ConsoleOutput.CYAN,
				"[SETUP] ====================================================================");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP]  _____      _               ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] /  ___|    | |              ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] \\ `--.  ___| |_ _   _ _ __  ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP]  `--. \\/ _ \\ __| | | | '_ \\ ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] /\\__/ /  __/ |_| |_| | |_) |");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] \\____/ \\___|\\__|\\__,_| .__/ ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP]                      | |    ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP]                      |_|    ");
		ConsoleOutput.write(ConsoleOutput.CYAN,
				"[SETUP] Software by PlugDev and licensed under Apache License v2.0 GPL.");
		if (string != null) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP]  ");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] " + string);
			feedback = null;
		}
		ConsoleOutput.write(ConsoleOutput.CYAN,
				"[SETUP] ====================================================================");

		switch (step) {
		case 100:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Hey! Thank you for downloading my software.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] The first question of the day..");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Do you want to use colors in the console?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] In Windows it's buggy and glitchy.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] And in Linux it works fine.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 0:
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] First of all! Thank you for using our cloud and appreciating the work behind it!");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] To let the program runs third-party software, you are agreeing the third-party license.");
			ConsoleOutput.write(ConsoleOutput.RED_BOLD_BRIGHT,
					"[SETUP] Even though you accept the latest EULA-Minecraft!");
			ConsoleOutput.write(ConsoleOutput.RED_BOLD_BRIGHT,
					"[SETUP] Every violate against the EULA does go to the end user and not to PlugDev!");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] If you do not, you can easily type 'no' and deleting the program.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 1:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Now we have another yes/no question.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Do you want to help the Developers to share some statistics.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] This statistics would include this: Activeplayers, Clouderrors, Pluginerrors.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] This statistics would not include this: Passwords, Whole Plugins, whole Files.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] This would be send anyway: Servername, ServerID.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 2:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Next we need a name, to identify your hard work!");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] If you don't have a servername, you shouldn't use a cloud publicly.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] You're free to use any No-NSFW names.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <*>");
			break;
		case 3:
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Now we need a value how much RAM in MByte do you want to use.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Total-Memory: " + (Runtime.getRuntime().totalMemory() * 0.000001) + " MByte");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Available-Memory: " + (Runtime.getRuntime().freeMemory() * 0.000001) + " MByte");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <Integervalue> (in MByte)");
			break;
		case 4:
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Do you want to optimize for 'Minigames', 'Citybuild' or 'Both'?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'MiniGames', 'CityBuild', 'Both'>");
			break;
		case 5:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Now we setup your Proxy.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Which type/fork of bungeecord do you want to use?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'BungeeCord', 'Waterfall'>");
			break;
		case 6:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Now we setup your Spigotserver.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Which type/fork of spigot do you want to use?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'Spigot', 'Paper', 'Taco'>");
			break;
		case 7:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Which default minecraft-version do you prefer?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Possible answerchoices:");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.7.10'>");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] <'1.8', '1.8.3', '1.8.4', '1.8.5', '1.8.6', '1.8.7', '1.8.8'*$>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.9', '1.9.2'$, '1.9.4'*$>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.10', '1.10.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.11', '1.11.1', '1.11.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.12', '1.12.1', '1.12.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.13', '1.13.1', '1.13.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.14', '1.14.1', '1.14.2', '1.14.3', '1.14.4'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.15', '1.15.1', '1.15.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.16.1', '1.16.2', '1.16.3', '1.16.4', '1.16.5'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] * means that paper is available too.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] $ means that taco is available too.");
			break;
		case 8:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Do you want to use the thirdparty-plugin ViaVersion?");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] This plugin will allows your player to join outdated server.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Important it only goes downwards! Server 1.8.1 << Client 1.9.1");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		case 9:
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Do you want to continue with the following settings?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Agree Licensement: " + agreeLicensement);
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Agree Statistics: " + agreeStatistics);
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Servername: " + servername);
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Server-Optimization: " + optimizationType);
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] BungeeCordFork: " + bungeeCordType.getVersion());
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Preffered Spigotversion: " + spigotServerVersion.getVersion());
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Thirdparty ViaVersion: " + useViaversion);
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"[SETUP] Some settings may aren't changeable after this confirmation!");
			ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Answerchoices: <'yes'/'no'>");
			break;
		default:
			break;
		}
	}
	
}
