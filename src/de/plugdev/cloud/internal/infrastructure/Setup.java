package de.plugdev.cloud.internal.infrastructure;

import java.io.IOException;
import java.util.Scanner;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.models.IVersion;

public class Setup {
	
	boolean agreeLicensement;
	boolean agreeStatistics;
	String servername;

	String optimizationType;
	IVersion bungeeCordType;
	String spigotType;
	IVersion spigotServerVersion;
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
						ConsoleOutput.write(ConsoleOutput.RED, "You are not allowed to use this software!");
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
						bungeeCordType = ApplicationInterface.getAPI().getInfrastructure().getVersionById(answer).get();
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
					spigotServerVersion = ApplicationInterface.getAPI().getInfrastructure().getVersionById(spigotType.toLowerCase() + "-" + answer).get();
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
					ConsoleOutput.write(ConsoleOutput.BLUE_BACKGROUND_BRIGHT, "Please restart the program.");
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
				"====================================================================");
		ConsoleOutput.write(ConsoleOutput.CYAN, " _____      _               ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "/  ___|    | |              ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "\\ `--.  ___| |_ _   _ _ __  ");
		ConsoleOutput.write(ConsoleOutput.CYAN, " `--. \\/ _ \\ __| | | | '_ \\ ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "/\\__/ /  __/ |_| |_| | |_) |");
		ConsoleOutput.write(ConsoleOutput.CYAN, "\\____/ \\___|\\__|\\__,_| .__/ ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "                     | |    ");
		ConsoleOutput.write(ConsoleOutput.CYAN, "                     |_|    ");
		ConsoleOutput.write(ConsoleOutput.CYAN,
				"Software by PlugDev and licensed under Apache License v2.0 GPL.");
		if (string != null) {
			ConsoleOutput.write(ConsoleOutput.CYAN, " ");
			ConsoleOutput.write(ConsoleOutput.CYAN, "" + string);
			feedback = null;
		}
		ConsoleOutput.write(ConsoleOutput.CYAN,
				"====================================================================");

		switch (step) {
		case 100:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Hey! Thank you for downloading my software.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "The first question of the day..");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Do you want to use colors in the console?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "In Windows it's buggy and glitchy.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "And in Linux it works fine.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'yes'/'no'>");
			break;
		case 0:
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"First of all! Thank you for using our cloud and appreciating the work behind it!");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"To let the program runs third-party software, you are agreeing the third-party license.");
			ConsoleOutput.write(ConsoleOutput.RED_BOLD_BRIGHT,
					"Even though you accept the latest EULA-Minecraft!");
			ConsoleOutput.write(ConsoleOutput.RED_BOLD_BRIGHT,
					"Every violate against the EULA does go to the end user and not to PlugDev!");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"If you do not, you can easily type 'no' and deleting the program.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'yes'/'no'>");
			break;
		case 1:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Now we have another yes/no question.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Do you want to help the Developers to share some statistics.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"This statistics would include this: Activeplayers, Clouderrors, Pluginerrors.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"This statistics would not include this: Passwords, Whole Plugins, whole Files.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "This would be send anyway: Servername, ServerID.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'yes'/'no'>");
			break;
		case 2:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Next we need a name, to identify your hard work!");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"If you don't have a servername, you shouldn't use a cloud publicly.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "You're free to use any No-NSFW names.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <*>");
			break;
		case 3:
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Now we need a value how much RAM in MByte do you want to use.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Total-Memory: " + (Runtime.getRuntime().totalMemory() * 0.000001) + " MByte");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Available-Memory: " + (Runtime.getRuntime().freeMemory() * 0.000001) + " MByte");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <Integervalue> (in MByte)");
			break;
		case 4:
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Do you want to optimize for 'Minigames', 'Citybuild' or 'Both'?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'MiniGames', 'CityBuild', 'Both'>");
			break;
		case 5:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Now we setup your Proxy.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Which type/fork of bungeecord do you want to use?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'BungeeCord', 'Waterfall'>");
			break;
		case 6:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Now we setup your Spigotserver.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Which type/fork of spigot do you want to use?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'Spigot', 'Paper', 'Taco'>");
			break;
		case 7:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Which default minecraft-version do you prefer?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Possible answerchoices:");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.7.10'>");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"<'1.8', '1.8.3', '1.8.4', '1.8.5', '1.8.6', '1.8.7', '1.8.8'*$>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.9', '1.9.2'$, '1.9.4'*$>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.10', '1.10.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.11', '1.11.1', '1.11.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.12', '1.12.1', '1.12.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.13', '1.13.1', '1.13.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.14', '1.14.1', '1.14.2', '1.14.3', '1.14.4'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.15', '1.15.1', '1.15.2'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.16.1', '1.16.2', '1.16.3', '1.16.4', '1.16.5'*>");
			ConsoleOutput.write(ConsoleOutput.CYAN, "* means that paper is available too.");
			ConsoleOutput.write(ConsoleOutput.CYAN, "$ means that taco is available too.");
			break;
		case 8:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Do you want to use the thirdparty-plugin ViaVersion?");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"This plugin will allows your player to join outdated server.");
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Important it only goes downwards! Server 1.8.1 << Client 1.9.1");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'yes'/'no'>");
			break;
		case 9:
			ConsoleOutput.write(ConsoleOutput.CYAN, "Do you want to continue with the following settings?");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Agree Licensement: " + agreeLicensement);
			ConsoleOutput.write(ConsoleOutput.CYAN, "Agree Statistics: " + agreeStatistics);
			ConsoleOutput.write(ConsoleOutput.CYAN, "Servername: " + servername);
			ConsoleOutput.write(ConsoleOutput.CYAN, "Server-Optimization: " + optimizationType);
			ConsoleOutput.write(ConsoleOutput.CYAN, "BungeeCordFork: " + bungeeCordType.getVersion());
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Preffered Spigotversion: " + spigotServerVersion.getVersion());
			ConsoleOutput.write(ConsoleOutput.CYAN, "Thirdparty ViaVersion: " + useViaversion);
			ConsoleOutput.write(ConsoleOutput.CYAN,
					"Some settings may aren't changeable after this confirmation!");
			ConsoleOutput.write(ConsoleOutput.CYAN, "Answerchoices: <'yes'/'no'>");
			break;
		default:
			break;
		}
	}
	
}
