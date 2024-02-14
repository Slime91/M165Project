package M165;

import M165.Functions.KundeFunctions;
import M165.Objects.Kunde;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


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
            System.out.println("3. Manage Adressen");
            System.out.println("4. Manage Bestellungen");
            System.out.println("5. Manage Bestellpositionen");
            System.out.println("6. Exit");

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
                    manageBestellpositionen(repository, scanner);
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
            System.out.println("4. Read Kunde");
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
                    System.out.println("Kunde added successfully.");
                    break;
                case 2:
                    // Update Kunde
                    KundeFunctions.updateKunde(repository, scanner);
                    break;
                case 3:

                    break;
                case 4:
                    break;
                case 5:
                    KundeFunctions.readAllKundeData(repository);
                    break;
                case 6:
                    break;
                case 7:
                    break;
            }
        }
    }



    private static void manageComputers(CShop_Repository repository, Scanner scanner) {
        // Implement CRUD operations for Computers
        // Example:
        // repository.addComputer(new Computer(...));
        // repository.updateComputer(computerId, updatedComputer);
        // repository.deleteComputer(computerId);
        // repository.readComputer(computerId);
        // repository.readAllComputers();
    }



    private static void manageBestellungen(CShop_Repository repository, Scanner scanner) {
        // Implement CRUD operations for Bestellungen
        // Example:
        // repository.addBestellung(new Bestellung(...));
        // repository.updateBestellung(bestellungId, updatedBestellung);
        // repository.deleteBestellung(bestellungId);
        // repository.readBestellung(bestellungId);
        // repository.readAllBestellungen();
    }

    private static void manageBestellpositionen(CShop_Repository repository, Scanner scanner) {
        // Implement CRUD operations for Bestellpositionen
        // Example:
        // repository.addBestellposition(new Bestellposition(...));
        // repository.updateBestellposition(bestellpositionId, updatedBestellposition);
        // repository.deleteBestellposition(bestellpositionId);
        // repository.readBestellposition(bestellpositionId);
        // repository.readAllBestellpositionen();
    }

}
