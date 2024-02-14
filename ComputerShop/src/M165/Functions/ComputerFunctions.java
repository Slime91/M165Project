package M165.Functions;

import M165.CShop_Repository;
import M165.Objects.Computer;
import M165.Objects.Schnittstellentyp;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ComputerFunctions {

    // Function to create a Computer object from user input
    public static Computer createComputerFromUserInput(Scanner scanner, CShop_Repository repository) {
        System.out.println("Please enter Computer details:");
        System.out.println("Format: Hersteller, Modell, Arbeitspeicher, CPU, Massenspeicher, Typ, Einzelpreis");
        System.out.println("Example input: Dell, Inspiron, 8, Intel Core i5, 512, Laptop, 12.99");

        String input = scanner.nextLine();
        String[] details = input.split(", ");

        // Assuming the input is correctly formatted
        ObjectId computerId = new ObjectId();
        String hersteller = details[0];
        String modell = details[1];
        Integer arbeitspeicher = Integer.parseInt(details[2]);
        String cpu = details[3];
        Integer massenspeicher = Integer.parseInt(details[4]);
        String typ = details[5];
        Double einzelpreis = Double.valueOf(details[6]);

// Prompt the user to enter Schnittstellentyp details
        List<Schnittstellentyp> schnittstellen = new ArrayList<>();
        while (true) {
            System.out.println("Enter Schnittstellentyp (or 'done' to finish):");
            System.out.println("Example input: USB-A / HDMI 2.0 / VGA / ...");

            String schnittstellentypName = scanner.nextLine();
            if (schnittstellentypName.equalsIgnoreCase("done")) {
                break;
            }
            System.out.println("Enter the amount of slots for Schnittstellentyp:");
            System.out.println("Only numbers required.");

            int schnittstellentypInteger = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            // Create a new Schnittstellentyp object with the provided name and integer
            Schnittstellentyp schnittstellentyp = new Schnittstellentyp(schnittstellentypName, schnittstellentypInteger);
            schnittstellen.add(schnittstellentyp);
        }

        // Create a new Computer object
        Computer computer = new Computer(computerId, hersteller, modell, arbeitspeicher, cpu, massenspeicher, typ, schnittstellen, einzelpreis);
        return computer;
    }

    public static void updateComputer(CShop_Repository repository, Scanner scanner) {
        // Retrieve all existing Computers
        List<Computer> computers = repository.readAllComputers();

        // Display the list of Computers with their numbered IDs to the user
        System.out.println("Existing Computers:");
        for (int i = 0; i < computers.size(); i++) {
            Computer currentComputer = computers.get(i);
            ObjectId computerId = currentComputer.getId(repository);
            System.out.println((i + 1) + ". " + computerId + ": " + currentComputer.getModell() + ", " + currentComputer.getHersteller());
        }

        // Prompt the user to select the Computer they want to update
        System.out.println("Enter the number of the Computer you want to update:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (choice >= 1 && choice <= computers.size()) {
            // Retrieve the selected Computer
            Computer selectedComputer = computers.get(choice - 1);

            // Prompt the user to input the updated details for the selected Computer
            System.out.println("Enter the updated details for the Computer:");
            System.out.println("Format: Hersteller, Modell, Arbeitspeicher, CPU, Massenspeicher, Typ");
            System.out.println("Example input: Dell, Inspiron, 8, Intel Core i5, 512, Laptop");
            String input = scanner.nextLine();
            String[] details = input.split(", ");

            // Assuming the input is correctly formatted
            String hersteller = details[0];
            String modell = details[1];
            Integer arbeitspeicher = Integer.parseInt(details[2]);
            String cpu = details[3];
            Integer massenspeicher = Integer.parseInt(details[4]);
            String typ = details[5];
            Double einzelpreis = Double.valueOf(details[6]);

            // Create a new Computer object with the updated details
            Computer updatedComputer = new Computer(selectedComputer.getId(repository), hersteller, modell, arbeitspeicher, cpu, massenspeicher, typ, null, einzelpreis); // Assuming Schnittstellentyp is not provided in user input

            // Call the updateComputer method from the repository to apply the changes
            repository.updateComputer(selectedComputer.getId(repository), updatedComputer);
            System.out.println("Computer updated successfully.");

            // Prompt the user to update Schnittstellentyp if desired
            System.out.println("Do you want to update the Schnittstellentyp? (yes/no)");
            String updateSchnittstellentyp = scanner.nextLine();
            if (updateSchnittstellentyp.equalsIgnoreCase("yes")) {
                List<Schnittstellentyp> schnittstellentypList = selectedComputer.getSchnittstellen();
                if (schnittstellentypList != null && !schnittstellentypList.isEmpty()) {
                    System.out.println("Schnittstellentyps:");
                    for (int i = 0; i < schnittstellentypList.size(); i++) {
                        System.out.println((i + 1) + ". " + schnittstellentypList.get(i).getName());
                    }

                    while (true) {
                        System.out.println("Enter the number of the Schnittstellentyp you want to update (or enter 0 to finish):");
                        int schnittstellentypChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (schnittstellentypChoice == 0) {
                            break;
                        }
                        if (schnittstellentypChoice >= 1 && schnittstellentypChoice <= schnittstellentypList.size()) {
                            System.out.println("Enter the updated type for the Schnittstellentyp:");
                            String updatedName = scanner.nextLine();
                            schnittstellentypList.get(schnittstellentypChoice - 1).setName(updatedName);
                            System.out.println("Schnittstellentyp updated successfully.");
                        } else {
                            System.out.println("Invalid choice. Please enter a number within the range.");
                        }
                    }

                    // Update the computer with the modified Schnittstellentyp list
                    updatedComputer.setSchnittstellen(schnittstellentypList);
                    repository.updateComputer(selectedComputer.getId(repository), updatedComputer);
                    System.out.println("Computer updated with modified Schnittstellentyp list.");
                } else {
                    System.out.println("No Schnittstellentyps found for this Computer.");
                }
            }
        } else {
            System.out.println("Invalid choice. Please enter a number within the range.");
        }
    }

    public static void computerDelete(CShop_Repository repository, Scanner scanner) {
        // Retrieve all existing Computers
        List<Computer> computers = repository.readAllComputers();

        // Display the list of Computers with their numbered IDs to the user
        System.out.println("Existing Computers:");
        for (int i = 0; i < computers.size(); i++) {
            Computer computer = computers.get(i);
            System.out.println((i + 1) + ". " + computer.getId(repository) + ": " + computer.getModell() + ", " + computer.getHersteller());
        }

        // Prompt the user to input the ID of the Computer they want to delete
        System.out.println("Enter the number of the Computer you want to delete:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (choice >= 1 && choice <= computers.size()) {
            // Retrieve the selected Computer
            Computer selectedComputer = computers.get(choice - 1);

            // Delete the Computer with the specified ID
            repository.deleteComputer(selectedComputer.getId(repository));
            System.out.println("Computer with ID " + selectedComputer.getId(repository) + " deleted successfully.");
        } else {
            System.out.println("Invalid choice. Please enter a number within the range.");
        }
    }

    // Function to read all data from the "computer" collection
    public static void readAllComputerData(CShop_Repository repository) {
        // Obtain the "computer" collection
        MongoCollection<Document> computerCollection = repository.getComputerCollection();

        // Query the collection to retrieve all documents
        FindIterable<Document> cursor = computerCollection.find();

        // Iterate over the result set and process each document
        MongoCursor<Document> iterator = cursor.iterator();
        while (iterator.hasNext()) {
            Document computerDoc = iterator.next();
            // Process the computer document, e.g., print its contents
            System.out.println(computerDoc.toJson());
        }
    }

    public static void searchComputer(CShop_Repository repository, Scanner scanner) {
        // Prompt the user to enter the column and content to search
        System.out.println("Enter the column you want to search (e.g., Hersteller):");
        String column = scanner.nextLine();
        System.out.println("Enter the content to search:");
        String content = scanner.nextLine();

        // Retrieve Computers matching the search criteria
        Computer computer = repository.readComputer(column, content);

        // Display the search result
        if (computer != null) {
            System.out.println("Search result:");
            ObjectId computerId = computer.getId(repository);
            System.out.println(computerId + ": " + computer.getModell() + ", " + computer.getHersteller());
        } else {
            System.out.println("No Computer found matching the search criteria.");
        }
    }
}
