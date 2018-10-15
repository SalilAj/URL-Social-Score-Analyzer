package NewsWhip.socialInteractionAnalyzer;

import java.util.Scanner;
import NewsWhip.socialInteractionAnalyzer.QueryService;


public class App {

	public static void printDefault() {
		System.out.println("\nEnter a command:\n" + "To Add a URL enter: ADD <url> <score>\n"
				+ "To Remove a URL enter: REMOVE <url>\n" + "To Export URL statistics enter: EXPORT\n"
				+ "To Exit the program enter: EXIT\n");
	}

	public static void main(String[] args) {

		MongoConnection mongoConn = new MongoConnection();

		Scanner reader = new Scanner(System.in);
		String input = "";
		boolean run = true;

		System.out.println("!!! Welcome to NewsWhip !!!");

		while (run) {
			printDefault();
			input = reader.nextLine();
			String[] inputArr = input.split(" ");

			switch (inputArr[0].toUpperCase()) {
			case "ADD":
				if (inputArr.length >= 3) {
					if (QueryService.addURL(mongoConn, inputArr[1], inputArr[2])) {
						System.out.println("\nSuccessfully Added entry\n");
					} else {
						System.out.println("\nFailed to add entry\n");
					}
				} else {
					System.out.println("\nInvalid number of parameters!");
				}
				break;
			case "REMOVE":
				if (inputArr.length >= 2) {
					if (QueryService.removeURL(mongoConn, inputArr[1])) {
						System.out.println("\nSuccessfully Removed entry\n");
					} else {
						System.out.println("\nFailed to remove entry\n");
					}
				} else {
					System.out.println("\nInvalid number of parameters!");
				}
				break;
			case "EXPORT":
				QueryService.getURLStats(mongoConn);
				break;
			case "EXIT":
				System.out.println("\nExiting program!");
				run = false;
				break;
			default:
				System.out.println("!!! Invalid Option !!!");
				break;
			}
		}
		reader.close();

	}
}
