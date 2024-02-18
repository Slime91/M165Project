package M165;

import M165.Objects.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

public class CShop_Repository {
    private static MongoCollection<Document> kundeCollection = null;
    private static MongoCollection<Document> computerCollection = null;
    private static MongoCollection<Document> bestellungCollection = null;
    private static MongoCollection<Document> adresseCollection = null;
    private static MongoCollection<Document> bestellCollection = null;

    public MongoCollection<Document> getKundeCollection() {
        return kundeCollection;
    }

    public MongoCollection<Document> getComputerCollection() {
        return computerCollection;
    }

    public MongoCollection<Document> getBestellungCollection() {
        return bestellungCollection;
    }

    public MongoCollection<Document> getAdresseCollection() {
        return adresseCollection;
    }

    public MongoCollection<Document> getBestellCollection() {
        return bestellCollection;
    }

    public CShop_Repository(MongoDatabase database) {
        this.kundeCollection = database.getCollection("kunde");
        this.computerCollection = database.getCollection("computer");
        this.bestellungCollection = database.getCollection("bestellung");
        this.adresseCollection = database.getCollection("adresse");
        this.bestellCollection = database.getCollection("bestellposition");


    }
    // Method to list all collections and print their names
    public void listAllCollections() {
        List<String> collectionNames = new ArrayList<>();
        collectionNames.add("kunde");
        collectionNames.add("computer");
        collectionNames.add("bestellung");
        collectionNames.add("adresse");

        for (String collectionName : collectionNames) {
            System.out.println("Collection: " + collectionName);
        }
    }
    // Methods for managing Kunden

    // Add Kunde
    public void addKunde(Kunde kunde) {
        Document kundeDoc = new Document("Geschlecht", kunde.getGeschlecht())
                .append("Nachname", kunde.getNachname())
                .append("Vorname", kunde.getVorname())
                .append("Telefon", kunde.getTelefon())
                .append("Email", kunde.getEmail())
                .append("Sprache", kunde.getSprache())
                .append("Geburtstag", kunde.getGeburtstag())
                .append("Adresse", new Document("Strasse", kunde.getAdresse().getStrasse())
                        .append("Postleitzahl", kunde.getAdresse().getPostleitzahl())
                        .append("Ort", kunde.getAdresse().getOrt()))
                .append("Bestellungen", new ArrayList<>()); // Initialize empty list for bestellungen
        this.kundeCollection.insertOne(kundeDoc);
    }

    // Update Kunde
    public void updateKunde(ObjectId id, Kunde kunde) {
        Document updatedKunde = new Document("$set", new Document("Geschlecht", kunde.getGeschlecht())
                .append("Nachname", kunde.getNachname())
                .append("Vorname", kunde.getVorname())
                .append("Telefon", kunde.getTelefon())
                .append("Email", kunde.getEmail())
                .append("Sprache", kunde.getSprache())
                .append("Geburtstag", kunde.getGeburtstag())
                .append("Adresse", new Document("Strasse", kunde.getAdresse().getStrasse())
                        .append("Postleitzahl", kunde.getAdresse().getPostleitzahl())
                        .append("Ort", kunde.getAdresse().getOrt())));
        this.kundeCollection.updateOne(new Document("_id", id), updatedKunde);
    }

    // Delete Kunde
    public void deleteKunde(String id) {
        // Retrieve the Kunde document to get its ID
        Document kundeDocument = kundeCollection.find(new Document("_id", new ObjectId(id))).first();
        if (kundeDocument != null) {
            // Extract the Kunde ID
            ObjectId kundeId = kundeDocument.getObjectId("_id");

            // Delete the Kunde document
            kundeCollection.deleteOne(new Document("_id", kundeId));

            // Delete associated Bestellung documents
            bestellungCollection.deleteMany(new Document("Kunde", kundeId));

            System.out.println("Kunde with ID " + kundeId + " and associated Bestellungen deleted successfully.");
        } else {
            System.out.println("Kunde not found with ID: " + id);
        }
    }

    // Helper method to get Bestellnummern associated with a Kunde ID
    private List<Integer> getBestellnummernByKundeId(ObjectId kundeId) {
        List<Integer> bestellnummern = new ArrayList<>();
        FindIterable<Document> kundeWithBestellungen = kundeCollection.find(eq("_id", kundeId)).projection(include("Bestellungen.Bestellnummer"));
        for (Document doc : kundeWithBestellungen) {
            List<Document> bestellungen = doc.getList("Bestellungen", Document.class);
            for (Document bestellung : bestellungen) {
                bestellnummern.add(bestellung.getInteger("Bestellnummer"));
            }
        }
        return bestellnummern;
    }

    // Read Kunde
    public Kunde searchKunde(String id) {
        Document kundeDoc = kundeCollection.find(new Document("_id", id)).first();
        if (kundeDoc != null) {
            return new Kunde(kundeDoc.getObjectId("Id"),
                    kundeDoc.getString("Geschlecht"),
                    kundeDoc.getString("Nachname"),
                    kundeDoc.getString("Vorname"),
                    kundeDoc.getString("Telefon"),
                    kundeDoc.getString("Email"),
                    kundeDoc.getString("Sprache"),
                    kundeDoc.getDate("Geburtstag"),
                    (Adresse) kundeDoc.get("Adresse"));
        }
        return null;
    }

    // Read all Kunden
    public List<Kunde> readAllKunden() {
        List<Kunde> kunden = new ArrayList<>();
        for (Document kundeDoc : kundeCollection.find()) {
            Adresse adresse = new Adresse(kundeDoc.getString("Adresse.Strasse"),
                    kundeDoc.getInteger("Adresse.Postleitzahl"),
                    kundeDoc.getString("Adresse.Ort"));

            kunden.add(new Kunde(kundeDoc.getObjectId("_id"),
                    kundeDoc.getString("Geschlecht"),
                    kundeDoc.getString("Nachname"),
                    kundeDoc.getString("Vorname"),
                    kundeDoc.getString("Telefon"),
                    kundeDoc.getString("Email"),
                    kundeDoc.getString("Sprache"),
                    kundeDoc.getDate("Geburtstag"),
                    adresse));
        }
        return kunden;
    }
    public List<Kunde> searchKunde(String column, String content) {
        List<Kunde> kunden = new ArrayList<>();
        Document query = new Document(column, content);
        for (Document kundeDoc : kundeCollection.find(query)) {
            Kunde kunde = documentToKunde(kundeDoc);
            if (kunde != null) {
                kunden.add(kunde);
            }
        }
        return kunden;
    }
    // Helper method to convert Document to Kunde object
    private Kunde documentToKunde(Document kundeDoc) {
        if (kundeDoc == null) {
            return null;
        }

        ObjectId id = kundeDoc.getObjectId("_id");
        String geschlecht = kundeDoc.getString("Geschlecht");
        String nachname = kundeDoc.getString("Nachname");
        String vorname = kundeDoc.getString("Vorname");
        String telefon = kundeDoc.getString("Telefon");
        String email = kundeDoc.getString("Email");
        String sprache = kundeDoc.getString("Sprache");
        Date geburtstag = kundeDoc.getDate("Geburtstag");
        // Assuming the Adresse is stored as a Document within the Kunde document
        Document adresseDoc = kundeDoc.get("Adresse", Document.class);
        Adresse adresse = documentToAdresse(adresseDoc);

        // Create and return a new Kunde object
        return new Kunde(id, geschlecht, nachname, vorname, telefon, email, sprache, geburtstag, adresse);
    }

    // Helper method to convert Document to Adresse object
    private Adresse documentToAdresse(Document adresseDoc) {
        if (adresseDoc == null) {
            return null;
        }

        String strasse = adresseDoc.getString("Strasse");
        Integer postleitzahl = adresseDoc.getInteger("Postleitzahl");
        String ort = adresseDoc.getString("Ort");

        return new Adresse(strasse, postleitzahl, ort);
    }

    // Methods for managing Computers


    // Add Computer
    public void addComputer(Computer computer) {
        // Generate a new ObjectId for the computer
        ObjectId computerId = new ObjectId();

        // Set the computerId for the Computer Object
        computer.setId(computerId);

        // Create a document for the computer data
        Document computerDoc = new Document("_id", computerId)
                .append("Hersteller", computer.getHersteller())
                .append("Modell", computer.getModell())
                .append("Arbeitspeicher", computer.getArbeitspeicher())
                .append("CPU", computer.getCPU())
                .append("Massenspeicher", computer.getMassenspeicher())
                .append("Typ", computer.getTyp())
                .append("Einzelpreis", computer.getEinzelpreis());

        // Embed Schnittstellen documents
        if (computer.getSchnittstellen() != null) {
            List<Document> schnittstellenDocs = new ArrayList<>();
            for (Schnittstellentyp schnittstellen : computer.getSchnittstellen()) {
                Document schnittstellentypDoc = new Document()
                        .append("Typ", schnittstellen.getName())
                        .append("Amount of Slots", schnittstellen.getInteger());
                schnittstellenDocs.add(schnittstellentypDoc);
            }
            computerDoc.append("Schnittstellen", schnittstellenDocs);
        }

        // Insert the document into the computer collection
        this.computerCollection.insertOne(computerDoc);
    }

    public void updateComputer(ObjectId computerId, Computer computer) {
        Document updatedComputer = new Document("$set", new Document("Hersteller", computer.getHersteller())
                .append("Modell", computer.getModell())
                .append("Arbeitspeicher", computer.getArbeitspeicher())
                .append("CPU", computer.getCPU())
                .append("Massenspeicher", computer.getMassenspeicher())
                .append("Typ", computer.getTyp())
                .append("Einzelpreis", computer.getEinzelpreis()));

        // If Schnittstellen are provided, update them as well
        List<Schnittstellentyp> schnittstellen = computer.getSchnittstellen();
        if (schnittstellen != null && !schnittstellen.isEmpty()) {
            List<Document> schnittstellenDocuments = new ArrayList<>();
            for (Schnittstellentyp schnittstellentyp : schnittstellen) {
                Document schnittstellenDocument = new Document()
                        .append("Typ", schnittstellentyp.getName())
                        .append("Amount of Slots", schnittstellentyp.getInteger());
                schnittstellenDocuments.add(schnittstellenDocument);
            }
            // Append the new Schnittstellen array to the updatedComputer document
            updatedComputer.append("$set", new Document("Schnittstellen", schnittstellenDocuments));
        }

        // Update the computer document
        this.computerCollection.updateOne(new Document("_id", computerId), updatedComputer);
    }



    // Delete Computer
    public void deleteComputer(ObjectId computerId) {
        computerCollection.deleteOne(new Document("_id", computerId));
    }

    // Search Computer


    public static Computer readComputer(String column, String content) {
        // Create a query document to find the computer based on the specified column and content
        Document query = new Document(column, content);

        // Find the computer document based on the query
        Document computerDoc = computerCollection.find(query).first();

        if (computerDoc != null) {
            List<Schnittstellentyp> schnittstellentypList = new ArrayList<>();
            List<Document> schnittstellenDocs = computerDoc.getList("schnittstellen", Document.class);
            if (schnittstellenDocs != null) {
                for (Document schnittstellenDoc : schnittstellenDocs) {
                    schnittstellentypList.add(new Schnittstellentyp(
                            schnittstellenDoc.getString("Typ"),
                            schnittstellenDoc.getInteger("Amount of Slots")));
                }
            }
            // Retrieve other fields from the document
            ObjectId id = computerDoc.getObjectId("_id");
            String hersteller = computerDoc.getString("Hersteller");
            String modell = computerDoc.getString("Modell");
            Integer arbeitspeicher = computerDoc.getInteger("Arbeitspeicher");
            String cpu = computerDoc.getString("CPU");
            Integer massenspeicher = computerDoc.getInteger("Massenspeicher");
            String typ = computerDoc.getString("Typ");
            Double einzelpreis = computerDoc.getDouble("Einzelpreis");

            return new Computer(id, hersteller, modell, arbeitspeicher, cpu, massenspeicher, typ, schnittstellentypList, einzelpreis);
        }
        return null;
    }

    // Read all Computers
    public List<Computer> readAllComputers() {
        List<Computer> computers = new ArrayList<>();
        for (Document computerDoc : computerCollection.find()) {
            List<Schnittstellentyp> schnittstellentypList = new ArrayList<>();
            List<Document> schnittstellenDocs = computerDoc.getList("Schnittstellen", Document.class);
            if (schnittstellenDocs != null) {
                for (Document schnittstellenDoc : schnittstellenDocs) {
                    schnittstellentypList.add(new Schnittstellentyp(
                            schnittstellenDoc.getString("Typ"),
                            schnittstellenDoc.getInteger("Amount of Slots")));
                }
            }
            computers.add(new Computer(
                    computerDoc.getObjectId("_id"),
                    computerDoc.getString("Hersteller"),
                    computerDoc.getString("Modell"),
                    computerDoc.getInteger("Arbeitspeicher"),
                    computerDoc.getString("CPU"),
                    computerDoc.getInteger("Massenspeicher"),
                    computerDoc.getString("Typ"),
                    schnittstellentypList,
                    computerDoc.getDouble("Einzelpreis")));

        }
        return computers;
    }

    // Methods for managing Adressen

    // Add Adresse
    public void addAdresse(Adresse adresse) {
        Document adresseDoc = new Document("Strasse", adresse.getStrasse())
                .append("Postleitzahl", adresse.getPostleitzahl())
                .append("Ort", adresse.getOrt());
        this.adresseCollection.insertOne(adresseDoc);
    }

    // Update Adresse
    public void updateAdresse(String adresseId, Adresse adresse) {
        Document updatedAdresse = new Document("$set", new Document("Strasse", adresse.getStrasse())
                .append("Postleitzahl", adresse.getPostleitzahl())
                .append("Ort", adresse.getOrt()));
        this.adresseCollection.updateOne(new Document("_id", adresseId), updatedAdresse);
    }

    // Delete Adresse
    public void deleteAdresse(String adresseId) {
        adresseCollection.deleteOne(new Document("_id", adresseId));
    }

    // Read Adresse
    public Adresse readAdresse(String adresseId) {
        Document adresseDoc = adresseCollection.find(new Document("_id", adresseId)).first();
        if (adresseDoc != null) {
            return new Adresse(adresseDoc.getString("Strasse"),
                    adresseDoc.getInteger("Postleitzahl"),
                    adresseDoc.getString("Ort"));
        }
        return null;
    }

    // Read all Adressen
    public List<Adresse> readAllAdressen() {
        List<Adresse> adressen = new ArrayList<>();
        for (Document adresseDoc : adresseCollection.find()) {
            adressen.add(new Adresse(adresseDoc.getString("Strasse"),
                    adresseDoc.getInteger("Postleitzahl"),
                    adresseDoc.getString("Ort")));
        }
        return adressen;
    }

    // Methods for managing Bestellungen

    // Add Bestellung
    public void addBestellung(Bestellung bestellung) {
        // Create a document for Bestellung
        Document bestellungDoc = new Document("Bestellnummer", bestellung.getBestellnummerAsInt())
                .append("Kunde", bestellung.getKundeId()) // Assuming Kunde ID is stored directly
                .append("Bestelldatum", bestellung.getBestelldatum())
                .append("Total", bestellung.getTotal());

        // Convert BestellpositionList to a list of documents and embed it into the Bestellung document
        List<Document> bestellpositionDocs = new ArrayList<>();
        for (Bestellposition bestellposition : bestellung.getBestellpositionList()) {
            Document bestellpositionDoc = new Document("articleId", bestellposition.getArticleId()) // Include articleId
                    .append("Einzelpreis", bestellposition.getEinzelpreis())
                    .append("Anzahl", bestellposition.getAnzahl());
            bestellpositionDocs.add(bestellpositionDoc);
        }
        bestellungDoc.append("BestellpositionList", bestellpositionDocs);

        // Insert the document into the collection
        this.bestellungCollection.insertOne(bestellungDoc);
    }



    // Update Bestellung
    public void updateBestellung(String bestellungId, Bestellung bestellung, CShop_Repository repository) {
        Document updatedBestellung = new Document("$set", new Document("Bestellnummer", bestellung.getBestellnummer())
                .append("Kunde", bestellung.getKundeId()) // Assuming Kunde ID is stored directly
                .append("Bestelldatum", bestellung.getBestelldatum())
                .append("Total", bestellung.getTotal()));
        this.bestellungCollection.updateOne(new Document("_id", bestellungId), updatedBestellung);
    }

    // Delete Bestellung
    public void deleteBestellung(String bestellungId) {
        bestellungCollection.deleteOne(new Document("_id", bestellungId));
    }

    public List<Bestellung> searchBestellung(String columnName, String searchData) {
        List<Bestellung> matchingBestellungen = new ArrayList<>();
        List<Document> bestellungenDocuments = bestellungCollection.find().into(new ArrayList<>());

        for (Document doc : bestellungenDocuments) {
            Bestellung bestellung = documentToBestellung(doc);
            if (bestellungMatchesSearchCriteria(bestellung, columnName, searchData)) {
                matchingBestellungen.add(bestellung);
            }
        }

        return matchingBestellungen;
    }

    private Bestellung documentToBestellung(Document doc) {
        // Convert Document to Bestellung object (implement this method based on your existing code)
        return null;
    }

    private boolean bestellungMatchesSearchCriteria(Bestellung bestellung, String columnName, String searchData) {
        switch (columnName.toLowerCase()) {
            case "bestellnummer":
                return String.valueOf(bestellung.getBestellnummer()).equals(searchData);
            case "bestelldatum":
                return bestellung.getBestelldatum().toString().equals(searchData);
            // Add cases for other columns as needed
            default:
                // Column name doesn't match any known attribute of Bestellung
                return false;
        }
    }

    public List<Bestellung> readAllBestellungen(CShop_Repository repository) {
        List<Bestellung> bestellungen = new ArrayList<>();
        for (Document bestellungDoc : bestellungCollection.find()) {
            Integer bestellnummer = bestellungDoc.getInteger("Bestellnummer"); // Get the Bestellnummer from the document

            // Assuming "Bestellnummer" is the field name for Bestellung Bestellnummer
            if (bestellnummer != null) {
                List<Bestellposition> bestellpositionList = repository.readBestellpositionenOfBestellung(bestellnummer); // Fetch Bestellpositionen based on Bestellnummer
                ObjectId kundeId = bestellungDoc.getObjectId("Kunde"); // Assuming "KundeId" is the field name for Kunde ObjectId
                Bestellung bestellung = new Bestellung(
                        bestellnummer,
                        kundeId,
                        bestellungDoc.getDate("Bestelldatum"),
                        bestellungDoc.getDouble("Total"),
                        bestellpositionList
                );
                bestellungen.add(bestellung);
            } else {
                // Handle case when Bestellnummer is null, if needed
                System.out.println("Failed to fetch Bestellung with Bestellnummer: " + bestellnummer);
            }
        }
        return bestellungen;
    }



    public List<Bestellposition> readBestellpositionenOfBestellung(Integer bestellnummer) {
        List<Bestellposition> bestellpositionList = new ArrayList<>();

        // Find the Bestellung document by Bestellnummer
        Document bestellungDoc = bestellungCollection.find(new Document("Bestellnummer", bestellnummer)).first();

        if (bestellungDoc != null) {
            // Retrieve the list of Bestellposition documents associated with the Bestellung
            List<Document> bestellpositionDocs = (List<Document>) bestellungDoc.get("BestellpositionList");

            if (bestellpositionDocs != null) {
                // Iterate over the Bestellposition documents and construct Bestellposition objects
                for (Document bestellpositionDoc : bestellpositionDocs) {
                    ObjectId articleId = bestellpositionDoc.getObjectId("articleId");
                    Integer anzahl = bestellpositionDoc.getInteger("Anzahl"); // Corrected field name
                    Double einzelpreis = bestellpositionDoc.getDouble("Einzelpreis"); // Corrected field name
                    Bestellposition bestellposition = new Bestellposition(articleId, anzahl, einzelpreis);
                    bestellpositionList.add(bestellposition);
                }
            }
        }

        return bestellpositionList;
    }




    // Check if a Bestellung with the given bestellnummer already exists
    public boolean existsBestellnummer(int bestellnummer) {
        Document query = new Document("Bestellnummer", bestellnummer);
        return bestellungCollection.countDocuments(query) > 0;
    }
    public ObjectId getKundeObjectId(int kundeChoiceIndex) {
        // Retrieve the ObjectId of the selected Kunde from MongoDB
        Document kundeDoc = kundeCollection.find().skip(kundeChoiceIndex).first();
        if (kundeDoc != null) {
            return kundeDoc.getObjectId("_id");
        }
        return null;
    }
    public void updateKundeForBestellung(ObjectId kundeId, Bestellung bestellung) {
        Document kundeDoc = kundeCollection.find(new Document("_id", kundeId)).first();
        if (kundeDoc != null) {
            List<Integer> bestellungen;
            Object bestellungenObj = kundeDoc.get("Bestellungen");
            if (bestellungenObj instanceof String) {
                // If Bestellungen is stored as a String, parse it to a list of integers
                bestellungen = parseBestellungenString((String) bestellungenObj);
            } else if (bestellungenObj instanceof ArrayList) {
                // If Bestellungen is stored as an ArrayList, cast it directly
                bestellungen = (ArrayList<Integer>) bestellungenObj;
            } else {
                // Handle other cases if needed
                bestellungen = new ArrayList<>();
            }
            bestellungen.add(bestellung.getBestellnummer());
            String updatedBestellungenString = bestellungen.toString();
            kundeCollection.updateOne(new Document("_id", kundeId), new Document("$set", new Document("Bestellungen", updatedBestellungenString)));
        }
    }


    private List<Integer> parseBestellungenString(String bestellungenString) {
        List<Integer> bestellungen = new ArrayList<>();
        // Remove all non-numeric characters (except commas) from the string
        String cleanedString = bestellungenString.replaceAll("[^\\d,]", "");
        String[] bestellungenArray = cleanedString.split(",");
        for (String bestellung : bestellungenArray) {
            bestellungen.add(Integer.parseInt(bestellung.trim()));
        }
        return bestellungen;
    }


    public void deleteBestellnummerFromKunde(ObjectId kundeId, int bestellnummer) {
        // Retrieve the Kunde object associated with the provided ID
        Kunde kunde = searchKunde(String.valueOf(kundeId));

        // Check if the Kunde exists
        if (kunde != null) {
            // Get the current list of Bestellungen from the Kunde object
            List<Bestellung> bestellungen = kunde.getBestellungen();

            // Remove the specified Bestellnummer from the list of Bestellungen
            bestellungen.remove(Integer.valueOf(bestellnummer));

            // Update the Kunde object in the database with the modified list of Bestellungen
            updateBestellungenForKunde(kundeId, bestellungen);
        }
    }

    private void updateBestellungenForKunde(ObjectId kundeId, List<Bestellung> bestellungen) {
        // Convert the list of integers to a string with brackets
        String updatedBestellungenString = bestellungen.toString();

        // Update the Kunde document in the database with the modified list of Bestellungen
        kundeCollection.updateOne(new Document("_id", kundeId), new Document("$set", new Document("Bestellungen", updatedBestellungenString)));
    }

    public List<Document> listAllComputerDocuments() {
        List<Document> computerDocuments = new ArrayList<>();
        FindIterable<Document> cursor = computerCollection.find();
        for (Document doc : cursor) {
            computerDocuments.add(doc);
        }
        return computerDocuments;
    }

    // Method to get the ObjectId of a computer document by its index
    public ObjectId getComputerIdByIndex(int index) {
        List<Document> computerDocuments = listAllComputerDocuments();
        // Ensure the index is within bounds
        if (index >= 0 && index < computerDocuments.size()) {
            Document computerDoc = computerDocuments.get(index);
            return computerDoc.getObjectId("_id");
        } else {
            return null; // Or throw an exception, depending on your requirements
        }
    }

    // Universal save function
    public static void save() {
        saveCollection(kundeCollection);
        saveCollection(computerCollection);
        saveCollection(bestellungCollection);
        saveCollection(adresseCollection);
        saveCollection(bestellCollection);
    }

    private static void saveCollection(MongoCollection<Document> collection) {
        for (Document document : collection.find()) {
            String id = document.getObjectId("_id").toString();
            Document query = new Document("_id", id);
            collection.replaceOne(query, document);
        }
    }
}


