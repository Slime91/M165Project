package M165.Functions;

import M165.CShop_Repository;
import M165.Objects.Bestellposition;
import M165.Objects.Bestellung;
import M165.Objects.Kunde;
import M165.Objects.Computer;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.*;

public class BestellungFunctions {

    public static Bestellung createBestellungFromUserInput(Scanner scanner, CShop_Repository repository) {
        // Retrieve all Kunde data from the repository
        List<Kunde> kunden = repository.readAllKunden();

        // Display all Kunde data with numbers for selection
        System.out.println("Choose a Kunde for this Bestellung:");
        for (int i = 0; i < kunden.size(); i++) {
            Kunde currentKunde = kunden.get(i);
            System.out.println((i + 1) + ". " + currentKunde.getVorname() + " " + currentKunde.getNachname());
            // Display more details as needed
        }

        // Prompt the user to choose a Kunde
        System.out.println("Enter the number of the Kunde:");
        int kundeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (kundeChoice >= 1 && kundeChoice <= kunden.size()) {
            // Retrieve the selected Kunde's ObjectId directly from MongoDB
            ObjectId selectedKundeId = repository.getKundeObjectId(kundeChoice - 1);

            // Generate a random 5-digit bestellnummer
            Random random = new Random();
            Integer bestellnummer;
            do {
                bestellnummer = random.nextInt(90000) + 10000; // Generate a random 5-digit number
            } while (repository.existsBestellnummer(bestellnummer)); // Check if bestellnummer already exists

            // Prompt the user to enter Bestelldatum
            System.out.println("Enter the Bestelldatum (YYYY-MM-DD):");
            String bestelldatumString = scanner.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bestelldatum;
            try {
                bestelldatum = dateFormat.parse(bestelldatumString);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                return null;
            }

            // Create Bestellung object with the selected Kunde
            Bestellung bestellung = new Bestellung(bestellnummer, selectedKundeId, bestelldatum, 0, new ArrayList<>());

            // Prompt for adding Bestellposition
            List<Bestellposition> bestellpositionList = new ArrayList<>(); // Create a list to hold Bestellposition objects
            boolean continueAdding = true;
            while (continueAdding) {
                Bestellposition bestellposition = createBestellpositionFromUserInput(scanner, repository);
                bestellpositionList.add(bestellposition); // Add the current Bestellposition to the list

                // Ask the user if they want to add more Bestellpositionen
                System.out.println("Do you want to add another Bestellposition? (yes/no)");
                String choice = scanner.nextLine();
                if (!choice.equalsIgnoreCase("yes")) {
                    continueAdding = false;
                }
            }

            // Set the list of Bestellpositionen to the Bestellung object
            bestellung.setBestellpositionList(bestellpositionList);

            // Calculate total
            double total = calculateTotal(bestellpositionList);
            bestellung.setTotal(total);

            // Add the created Bestellung to the repository
            repository.addBestellung(bestellung);

            // Update the Kunde with the new Bestellung
            repository.updateKundeForBestellung(selectedKundeId, bestellung);

            System.out.println("Bestellung added successfully.");
            return bestellung;
        }
        return null;
    }

    public static Bestellposition createBestellpositionFromUserInput(Scanner scanner, CShop_Repository repository) {
        // Retrieve all Computer data from the repository
        List<Computer> computers = repository.readAllComputers();

        // Display all Computer data with numbers for selection
        System.out.println("Choose a Computer for this Bestellposition:");
        for (int i = 0; i < computers.size(); i++) {
            Computer currentComputer = computers.get(i);
            System.out.println((i + 1) + ". " + currentComputer.getHersteller() + " " + currentComputer.getModell() + ", Einzelpreis: " + currentComputer.getEinzelpreis());
            // Display more details as needed
        }

        // Prompt the user to choose a Computer
        System.out.println("Enter the number of the Computer:");
        int computerChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (computerChoice >= 1 && computerChoice <= computers.size()) {
            // Retrieve the ObjectId of the selected Computer
            ObjectId computerId = repository.getComputerIdByIndex(computerChoice - 1);

            // Prompt the user to enter Anzahl
            System.out.println("Enter the Anzahl of the Bestellposition:");
            int anzahl = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Create Bestellposition object with the selected Computer's articleId and Einzelpreis
            Bestellposition bestellposition = new Bestellposition(computerId, anzahl, computers.get(computerChoice - 1).getEinzelpreis());

            return bestellposition;
        } else {
            System.out.println("Invalid choice.");
            return null;
        }
    }


    public static double calculateTotal(List<Bestellposition> bestellpositionen) {
        double total = 0.0;
        for (Bestellposition bestellposition : bestellpositionen) {
            total += bestellposition.getEinzelpreis() * bestellposition.getAnzahl();
        }
        return total;
    }

    public static void updateBestellung(CShop_Repository repository, Scanner scanner) {
        // Retrieve all existing Bestellungen
        List<Bestellung> bestellungen = repository.readAllBestellungen();

        // Display the list of Bestellungen with their details
        System.out.println("Existing Bestellungen:");
        for (int i = 0; i < bestellungen.size(); i++) {
            Bestellung currentBestellung = bestellungen.get(i);
            System.out.println((i + 1) + ". Bestellnummer: " + currentBestellung.getBestellnummer() + ", Total: " + currentBestellung.getTotal());
            // Display more details as needed
        }

        // Prompt the user to select the Bestellung they want to update
        System.out.println("Enter the number of the Bestellung you want to update:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (choice >= 1 && choice <= bestellungen.size()) {
            // Retrieve the selected Bestellung
            Bestellung selectedBestellung = bestellungen.get(choice - 1);

            // Retrieve Bestellpositions for the selected Bestellung using the repository method
            List<Bestellposition> bestellpositions = repository.readBestellpositionenOfBestellung(selectedBestellung.getBestellnummer());

            // Display Bestellpositions for the selected Bestellung
            System.out.println("Bestellpositions:");
            for (int i = 0; i < bestellpositions.size(); i++) {
                Bestellposition position = bestellpositions.get(i);
                System.out.println((i + 1) + ". Artikel ID: " + position.getArticleId() + ", Einzelpreis: " + position.getEinzelpreis() + ", Anzahl: " + position.getAnzahl());
            }

            // Prompt the user to select the Bestellposition they want to update
            System.out.println("Enter the number of the Bestellposition you want to update:");
            int positionChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Ensure the user entered a valid choice
            if (positionChoice >= 1 && positionChoice <= bestellpositions.size()) {
                // Retrieve the selected Bestellposition
                Bestellposition selectedPosition = bestellpositions.get(positionChoice - 1);

                // Retrieve all computers and display them for the user to choose
                List<Computer> computers = repository.readAllComputers();
                System.out.println("Available Computers:");
                for (int i = 0; i < computers.size(); i++) {
                    Computer computer = computers.get(i);
                    System.out.println((i + 1) + ". " + computer.getHersteller() + ", " + computer.getModell() + ", Preis: " + computer.getEinzelpreis());
                }

                // Prompt the user to choose a computer
                System.out.println("Choose a computer by entering its number:");
                int computerChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Ensure the user entered a valid choice
                if (computerChoice >= 1 && computerChoice <= computers.size()) {
                    // Retrieve the selected computer
                    Computer selectedComputer = computers.get(computerChoice - 1);

                    // Update the selected position with the new article ID (computer ID) and Einzelpreis (computer price)
                    selectedPosition.setArticleId(selectedComputer.getComputerId());
                    selectedPosition.setEinzelpreis(selectedComputer.getEinzelpreis());

                    // Prompt the user to input the new Anzahl
                    System.out.println("Enter the new Anzahl:");
                    int newAnzahl = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Update the Anzahl of the selected position
                    selectedPosition.setAnzahl(newAnzahl);

                    // Update the selected Bestellposition in the Bestellung
                    selectedBestellung.getBestellpositionList().set(positionChoice - 1, selectedPosition);

                    // Calculate the updated total based on the modified Bestellung
                    double updatedTotal = calculateTotal(selectedBestellung.getBestellpositionList());
                    selectedBestellung.setTotal(updatedTotal);

                    // Update the Bestellung in the repository
                    repository.updateBestellung(String.valueOf(selectedBestellung.getBestellnummer()), selectedBestellung);

                    System.out.println("Bestellposition updated successfully.");
                } else {
                    System.out.println("Invalid choice for computer. Please enter a number within the range.");
                }
            } else {
                System.out.println("Invalid choice for Bestellposition. Please enter a number within the range.");
            }
        } else {
            System.out.println("Invalid choice for Bestellung. Please enter a number within the range.");
        }
    }





    public static void deleteBestellung(CShop_Repository repository, Scanner scanner) {
        // Retrieve all existing Bestellungen
        List<Bestellung> bestellungen = repository.readAllBestellungen();

        // Display the list of Bestellungen with their details
        System.out.println("Existing Bestellungen:");
        for (int i = 0; i < bestellungen.size(); i++) {
            Bestellung currentBestellung = bestellungen.get(i);
            System.out.println((i + 1) + ". Bestellnummer: " + currentBestellung.getBestellnummer() + ", Total: " + currentBestellung.getTotal());
            // Display more details as needed
        }

        // Prompt the user to select the Bestellung they want to delete
        System.out.println("Enter the number of the Bestellung you want to delete:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (choice >= 1 && choice <= bestellungen.size()) {
            // Retrieve the selected Bestellung
            Bestellung selectedBestellung = bestellungen.get(choice - 1);

            // Delete the selected Bestellung
            repository.deleteBestellung(selectedBestellung.getBestellnummerAsString());
            System.out.println("Bestellung with Bestellnummer " + selectedBestellung.getBestellnummerAsString() + " deleted successfully.");

            // Delete the associated Bestellnummer from the respective Kunde
            repository.deleteBestellnummerFromKunde(selectedBestellung.getKundeId(), selectedBestellung.getBestellnummerAsInt());
            System.out.println("Bestellnummer deleted from associated Kunde.");
        } else {
            System.out.println("Invalid choice. Please enter a number within the range.");
        }
    }

    public static void searchBestellung(CShop_Repository repository, Scanner scanner) {
        System.out.println("Enter the column name to search for (e.g., Bestellnummer):");
        String columnName = scanner.nextLine();
        System.out.println("Enter the data associated with the column:");
        String searchData = scanner.nextLine();

        List<Bestellung> matchingBestellungen = repository.searchBestellung(columnName, searchData);

        if (matchingBestellungen.isEmpty()) {
            System.out.println("No matching Bestellungen found.");
        } else {
            System.out.println("Matching Bestellungen:");
            for (Bestellung bestellung : matchingBestellungen) {
                System.out.println(bestellung);
            }
        }
    }

    public static void readAllBestellung(CShop_Repository repository) {
        List<Bestellung> allBestellungen = repository.readAllBestellungen();
        System.out.println("All Bestellungen:");
        for (Bestellung bestellung : allBestellungen) {
            System.out.println(bestellung);
        }
    }

    public static void viewBestellposition(CShop_Repository repository, Scanner scanner) {
        List<Bestellung> allBestellungen = repository.readAllBestellungen();

        if (!allBestellungen.isEmpty()) {
            System.out.println("Available Bestellungen:");
            for (int i = 0; i < allBestellungen.size(); i++) {
                Bestellung currentBestellung = allBestellungen.get(i);
                System.out.println((i + 1) + ". Bestellnummer: " + currentBestellung.getBestellnummer());
            }

            System.out.println("Enter the number of the Bestellung to view Bestellpositions:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice >= 1 && choice <= allBestellungen.size()) {
                Bestellung selectedBestellung = allBestellungen.get(choice - 1);
                Integer bestellnummer = selectedBestellung.getBestellnummer(); // Get the Bestellnummer of the selected Bestellung
                List<Bestellposition> bestellpositionList = repository.readBestellpositionenOfBestellung(bestellnummer);

                if (!bestellpositionList.isEmpty()) {
                    System.out.println("Bestellpositions for Bestellung with Bestellnummer " + bestellnummer + ":");
                    for (int i = 0; i < bestellpositionList.size(); i++) {
                        Bestellposition bestellposition = bestellpositionList.get(i);
                        System.out.println("Bestellposition " + (i + 1) + ":");
                        System.out.println("Article ID: " + bestellposition.getArticleId());
                        System.out.println("Einzelpreis: " + bestellposition.getEinzelpreis());
                        System.out.println("Anzahl: " + bestellposition.getAnzahl());
                    }
                } else {
                    System.out.println("No Bestellpositions found for the selected Bestellung.");
                }
            } else {
                System.out.println("Invalid choice. Please enter a number within the range.");
            }
        } else {
            System.out.println("No Bestellungen found.");
        }
    }
}
