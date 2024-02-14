package M165.Functions;

import M165.CShop_Repository;
import M165.Objects.Adresse;
import M165.Objects.Kunde;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class KundeFunctions {

    // Function to create a Kunde object from user input
    public static Kunde createKundeFromUserInput(Scanner scanner, CShop_Repository repository) {
        System.out.println("Please enter Kunde details:");
        System.out.println("Format: Geschlecht, Nachname, Vorname, Telefon, Email, Sprache, Geburtstag, Straße, Postleitzahl, Ort");
        System.out.println("Example input: M, Smith, John, 123456789, john@example.com, German, 1990-01-01, Main St, 12345, City");

        String input = scanner.nextLine();
        String[] details = input.split(", ");

        // Assuming the input is correctly formatted
        ObjectId kundeId = new ObjectId();
        String geschlecht = details[0];
        String nachname = details[1];
        String vorname = details[2];
        String telefon = details[3];
        String email = details[4];
        String sprache = details[5];
        Date geburtstag = null;
        try {
            geburtstag = new SimpleDateFormat("yyyy-MM-dd").parse(details[6]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strasse = details[7];
        Integer postleitzahl = Integer.parseInt(details[8]);
        String ort = details[9];

        // Create a new Adresse object
        Adresse adresse = new Adresse(strasse, postleitzahl, ort);

        // Create a new Kunde object with the Adresse object
        Kunde kunde = new Kunde(kundeId, geschlecht, nachname, vorname, telefon, email, sprache, geburtstag, adresse);
        return kunde;
    }



    public static void updateKunde(CShop_Repository repository, Scanner scanner) {
        // Retrieve all existing Kunden
        List<Kunde> kunden = repository.readAllKunden();

        // Display the list of Kunden with their numbered IDs to the user
        System.out.println("Existing Kunden:");
        for (int i = 0; i < kunden.size(); i++) {
            System.out.println((i + 1) + ". " + kunden.get(i).getId(repository) + ": " + kunden.get(i).getNachname() + ", " + kunden.get(i).getVorname());
        }

        // Prompt the user to select the Kunde they want to update
        System.out.println("Enter the number of the Kunde you want to update:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the user entered a valid choice
        if (choice >= 1 && choice <= kunden.size()) {
            // Retrieve the selected Kunde
            Kunde selectedKunde = kunden.get(choice - 1);

            // Prompt the user to input the updated details for the selected Kunde
            System.out.println("Enter the updated details for the Kunde:");
            System.out.println("Format: Geschlecht, Nachname, Vorname, Telefon, Email, Sprache, Geburtstag (yyyy-MM-dd), Straße, Postleitzahl, Ort");
            System.out.println("Example input: M, Smith, John, 123456789, john@example.com, German, 1990-01-01, Main St, 12345, City");
            String input = scanner.nextLine();
            String[] details = input.split(", ");

            // Assuming the input is correctly formatted
            String geschlecht = details[0];
            String nachname = details[1];
            String vorname = details[2];
            String telefon = details[3];
            String email = details[4];
            String sprache = details[5];
            Date geburtstag = null;
            try {
                geburtstag = new SimpleDateFormat("yyyy-MM-dd").parse(details[6]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String strasse = details[7];
            Integer postleitzahl = Integer.parseInt(details[8]);
            String ort = details[9];

            Adresse adresse = new Adresse(strasse, postleitzahl, ort);

            // Create a new Kunde object with the updated details
            Kunde updatedKunde = new Kunde(selectedKunde.getId(repository), geschlecht, nachname, vorname, telefon, email, sprache, geburtstag, adresse);

            // Call the updateKunde method from the repository to apply the changes
            repository.updateKunde(selectedKunde.getId(repository), updatedKunde);
            System.out.println("Kunde updated successfully.");
        } else {
            System.out.println("Invalid choice. Please enter a number within the range.");
        }
    }
        // Function to display all Kunden and delete the one with the specified ID
        static void displayAndDeleteKunde(CShop_Repository repository, Scanner scanner) {
            // Retrieve all existing Kunden
            List<Kunde> kunden = repository.readAllKunden();

            // Display the list of Kunden with their IDs to the user
            System.out.println("Existing Kunden:");
            for (Kunde kunde : kunden) {
                System.out.println(kunde.getId(repository) + ": " + kunde.getNachname() + ", " + kunde.getVorname());
            }

            // Prompt the user to input the ID of the Kunde they want to delete
            System.out.println("Enter the ID of the Kunde you want to delete:");
            String idString = scanner.nextLine();
            ObjectId id = new ObjectId(idString);

            // Check if the provided ID exists
            boolean idExists = false;
            for (Kunde kunde : kunden) {
                if (kunde.getId(repository).equals(id)) {
                    idExists = true;
                    break;
                }
            }

            if (idExists) {
                // Delete the Kunde with the specified ID
                repository.deleteKunde(idString);
                System.out.println("Kunde with ID " + idString + " deleted successfully.");
            } else {
                System.out.println("Kunde with the provided ID does not exist.");
            }
        }


        // Function to read all data from the "kunde" collection
        public static void readAllKundeData(CShop_Repository repository) {
            // Obtain the "kunde" collection
            MongoCollection<Document> kundeCollection = repository.getKundeCollection();

            // Query the collection to retrieve all documents
            FindIterable<Document> cursor = kundeCollection.find();

            // Iterate over the result set and process each document
            MongoCursor<Document> iterator = cursor.iterator();
            while (iterator.hasNext()) {
                Document kundeDoc = iterator.next();
                // Process the kunde document, e.g., print its contents
                System.out.println(kundeDoc.toJson());
            }
        }

}

