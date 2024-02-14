package M165;

import M165.Functions.BestellungFunctions;
import M165.Functions.ComputerFunctions;
import M165.Functions.KundeFunctions;
import M165.Objects.Bestellung;
import M165.Objects.Computer;
import M165.Objects.Kunde;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


import java.lang.reflect.Method;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Connect to MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("ComputerShop");

        // Initialize repository
        CShop_Repository repository = new CShop_Repository(database);

        // Initialize Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Main menu loop
        boolean exit = false;
        while (!exit) {
            System.out.println("Select an option:");
            System.out.println("1. Manage Kunden");
            System.out.println("2. Manage Computers");
            System.out.println("3. Manage Bestellungen");
            System.out.println("4. List all Collections");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageKunden(repository, scanner);
                    break;
                case 2:
                    manageComputers(repository, scanner);
                    break;
                case 3:
                    manageBestellungen(repository, scanner);
                    break;
                case 4:
                    repository.listAllCollections();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }

        // Close scanner and MongoDB client
        scanner.close();
        mongoClient.close();
    }

    private static void manageKunden(CShop_Repository repository, Scanner scanner) {
        boolean exitKunde = false;
        while (!exitKunde) {
            System.out.println("Select an option:");
            System.out.println("1. Add Kunde");
            System.out.println("2. Update Kunde");
            System.out.println("3. Delete Kunde");
            System.out.println("4. Search Kunde");
            System.out.println("5. ReadAll Kunde");
            System.out.println("6. Save");
            System.out.println("7. Main Menu");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add Kunde
                    Kunde newKunde = KundeFunctions.createKundeFromUserInput(scanner, repository);
                    repository.addKunde(newKunde);
                    break;
                case 2:
                    // Update Kunde
                    KundeFunctions.updateKunde(repository, scanner);
                    break;
                case 3:
                    KundeFunctions.KundeDelete(repository, scanner);
                    break;
                case 4:
                    KundeFunctions.searchKunde(repository, scanner);
                    break;
                case 5:
                    KundeFunctions.readAllKundeData(repository);
                    break;
                case 6:
                    CShop_Repository.save();
                    break;
                case 7:
                    exitKunde = true;
                    break;
            }
        }
    }



    private static void manageComputers(CShop_Repository repository, Scanner scanner) {
        boolean exitComputer = false;
        while (!exitComputer) {
            System.out.println("Select an option:");
            System.out.println("1. Add Computer");
            System.out.println("2. Update Computer");
            System.out.println("3. Delete Computer");
            System.out.println("4. Search Computer");
            System.out.println("5. ReadAll Computer");
            System.out.println("6. Save");
            System.out.println("7. Main Menu");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add Computer
                    Computer newComputer = ComputerFunctions.createComputerFromUserInput(scanner, repository);
                    repository.addComputer(newComputer);
                    break;
                case 2:
                    // Update Computer
                    ComputerFunctions.updateComputer(repository, scanner);
                    break;
                case 3:
                    ComputerFunctions.computerDelete(repository, scanner);
                    break;
                case 4:
                    ComputerFunctions.searchComputer(repository, scanner);
                    break;
                case 5:
                    ComputerFunctions.readAllComputerData(repository);
                    break;
                case 6:
                    CShop_Repository.save();
                    break;
                case 7:
                    exitComputer = true;
                    break;
            }
        }
    }



    private static void manageBestellungen(CShop_Repository repository, Scanner scanner) {
        boolean exitBestellung = false;
        while (!exitBestellung) {
            System.out.println("Select an option:");
            System.out.println("1. Add Bestellung");
            System.out.println("2. Update Bestellung");
            System.out.println("3. Delete Bestellung");
            System.out.println("4. Search Bestellung");
            System.out.println("5. ReadAll Bestellung");
            System.out.println("6. Display Bestellposition from a Bestellung");
            System.out.println("7. Save");
            System.out.println("8. Main Menu");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add Bestellung
                    Bestellung newBestellung = BestellungFunctions.createBestellungFromUserInput(scanner, repository);
                    break;
                case 2:
                    // Update Bestellung
                    BestellungFunctions.updateBestellung(repository, scanner);
                    break;
                case 3:
                    BestellungFunctions.deleteBestellung(repository, scanner);
                    break;
                case 4:
                    BestellungFunctions.searchBestellung(repository, scanner);
                    break;
                case 5:
                    BestellungFunctions.readAllBestellung(repository);
                    break;
                case 6:
                    BestellungFunctions.viewBestellposition(repository, scanner);
                    break;
                case 7:
                    CShop_Repository.save();
                    break;
                case 8:
                    exitBestellung = true;
                    break;
            }
        }
    }

}
