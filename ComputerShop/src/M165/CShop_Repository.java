package M165;

import M165.Objects.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CShop_Repository {
    private final MongoCollection<Document> kundeCollection;
    private final MongoCollection<Document> computerCollection;
    private final MongoCollection<Document> bestellungCollection;
    private final MongoCollection<Document> adresseCollection;
    private final MongoCollection<Document> bestellCollection;

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
        kundeCollection.deleteOne(new Document("_id", id));
    }

    // Read Kunde
    public Kunde readKunde(String id) {
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
            kunden.add(new Kunde(kundeDoc.getObjectId("Id"),
                    kundeDoc.getString("Geschlecht"),
                    kundeDoc.getString("Nachname"),
                    kundeDoc.getString("Vorname"),
                    kundeDoc.getString("Telefon"),
                    kundeDoc.getString("Email"),
                    kundeDoc.getString("Sprache"),
                    kundeDoc.getDate("Geburtstag"),
                    (Adresse) kundeDoc.get("Adresse")));
        }
        return kunden;
    }

    // Methods for managing Computers

    // Add Computer
    public void addComputer(Computer computer) {
        Document computerDoc = new Document("Hersteller", computer.getHersteller())
                .append("Modell", computer.getModell())
                .append("Arbeitspeicher", computer.getArbeitspeicher())
                .append("CPU", computer.getCPU())
                .append("Massenspeicher", computer.getMassenspeicher())
                .append("Typ", computer.getTyp());
        this.computerCollection.insertOne(computerDoc);
    }

    // Update Computer
    public void updateComputer(String computerId, Computer computer) {
        Document updatedComputer = new Document("$set", new Document("Hersteller", computer.getHersteller())
                .append("Modell", computer.getModell())
                .append("Arbeitspeicher", computer.getArbeitspeicher())
                .append("CPU", computer.getCPU())
                .append("Massenspeicher", computer.getMassenspeicher())
                .append("Typ", computer.getTyp()));
        this.computerCollection.updateOne(new Document("_id", computerId), updatedComputer);
    }

    // Delete Computer
    public void deleteComputer(String computerId) {
        computerCollection.deleteOne(new Document("_id", computerId));
    }

    // Read Computer
    public Computer readComputer(String computerId) {
        Document computerDoc = computerCollection.find(new Document("_id", computerId)).first();
        if (computerDoc != null) {
            return new Computer(computerDoc.getString("Hersteller"),
                    computerDoc.getString("Modell"),
                    computerDoc.getInteger("Arbeitspeicher"),
                    computerDoc.getString("CPU"),
                    computerDoc.getInteger("Massenspeicher"),
                    computerDoc.getString("Typ"));
        }
        return null;
    }

    // Read all Computers
    public List<Computer> readAllComputers() {
        List<Computer> computers = new ArrayList<>();
        for (Document computerDoc : computerCollection.find()) {
            computers.add(new Computer(computerDoc.getString("Hersteller"),
                    computerDoc.getString("Modell"),
                    computerDoc.getInteger("Arbeitspeicher"),
                    computerDoc.getString("CPU"),
                    computerDoc.getInteger("Massenspeicher"),
                    computerDoc.getString("Typ")));
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
    public void addBestellung(Bestellung bestellung, CShop_Repository repository) {
        Document bestellungDoc = new Document("Bestellnummer", bestellung.getBestellnummer())
                .append("Kunde", bestellung.getKunde().getId(repository)) // Assuming Kunde has an ID
                .append("Bestelldatum", bestellung.getBestelldatum())
                .append("Total", bestellung.getTotal())
                .append("ComputerList", new ArrayList<>()); // Initialize empty list for ComputerList
        this.bestellungCollection.insertOne(bestellungDoc);
    }

    // Update Bestellung
    public void updateBestellung(String bestellungId, Bestellung bestellung, CShop_Repository repository) {
        Document updatedBestellung = new Document("$set", new Document("Bestellnummer", bestellung.getBestellnummer())
                .append("Kunde", bestellung.getKunde().getId(repository)) // Assuming Kunde has an ID
                .append("Bestelldatum", bestellung.getBestelldatum())
                .append("Total", bestellung.getTotal()));
        this.bestellungCollection.updateOne(new Document("_id", bestellungId), updatedBestellung);
    }

    // Delete Bestellung
    public void deleteBestellung(String bestellungId) {
        bestellungCollection.deleteOne(new Document("_id", bestellungId));
    }

    // Read Bestellung
    public Bestellung readBestellung(String bestellungId) {
        Document bestellungDoc = bestellungCollection.find(new Document("_id", bestellungId)).first();
        if (bestellungDoc != null) {
            // You need to handle Kunde and ComputerList here based on your implementation
            return new Bestellung(bestellungDoc.getInteger("Bestellnummer"),
                    null, // Kunde is not retrieved here, you need to handle it
                    bestellungDoc.getDate("Bestelldatum"),
                    bestellungDoc.getDouble("Total"));
        }
        return null;
    }

    // Read all Bestellungen
    public List<Bestellung> readAllBestellungen() {
        List<Bestellung> bestellungen = new ArrayList<>();
        for (Document bestellungDoc : bestellungCollection.find()) {
            // Similar to readBestellung, handle Kunde and ComputerList accordingly
            bestellungen.add(new Bestellung(bestellungDoc.getInteger("Bestellnummer"),
                    null, // Kunde is not retrieved here, you need to handle it
                    bestellungDoc.getDate("Bestelldatum"),
                    bestellungDoc.getDouble("Total")));
        }
        return bestellungen;
    }

    // Methods for managing Bestellpositionen

    // Add Bestellposition
    public void addBestellposition(Bestellposition bestellposition) {
        Document bestellpositionDoc = new Document("Einzelpreis", bestellposition.getEinzelpreis())
                .append("Anzahl", bestellposition.getAnzahl());
        bestellCollection.insertOne(bestellpositionDoc);
    }

    // Update Bestellposition
    public void updateBestellposition(String bestellpositionId, Bestellposition bestellposition) {
        Document updatedBestellposition = new Document("$set", new Document("Einzelpreis", bestellposition.getEinzelpreis())
                .append("Anzahl", bestellposition.getAnzahl()));
        bestellCollection.updateOne(new Document("_id", bestellpositionId), updatedBestellposition);
    }

    // Delete Bestellposition
    public void deleteBestellposition(String bestellpositionId) {
        bestellCollection.deleteOne(new Document("_id", bestellpositionId));
    }

    // Read Bestellposition
    public Bestellposition readBestellposition(String bestellpositionId) {
        Document bestellpositionDoc = bestellCollection.find(new Document("_id", bestellpositionId)).first();
        if (bestellpositionDoc != null) {
            return new Bestellposition(bestellpositionDoc.getDouble("Einzelpreis"),
                    bestellpositionDoc.getInteger("Anzahl"));
        }
        return null;
    }

    // Read all Bestellpositionen
    public List<Bestellposition> readAllBestellpositionen() {
        List<Bestellposition> bestellpositionen = new ArrayList<>();
        for (Document bestellpositionDoc : bestellCollection.find()) {
            bestellpositionen.add(new Bestellposition(bestellpositionDoc.getDouble("Einzelpreis"),
                    bestellpositionDoc.getInteger("Anzahl")));
        }
        return bestellpositionen;
    }

    // Universal save function
    public void save() {
        saveCollection(kundeCollection);
        saveCollection(computerCollection);
        saveCollection(bestellungCollection);
        saveCollection(adresseCollection);
        saveCollection(bestellCollection);
    }

    private void saveCollection(MongoCollection<Document> collection) {
        for (Document document : collection.find()) {
            String id = document.getObjectId("_id").toString();
            Document query = new Document("_id", id);
            collection.replaceOne(query, document);
        }
    }

    public int getLatestid() {
        Document latestKundeDoc = kundeCollection.find().sort(new Document("_id", -1)).limit(1).first();
        if (latestKundeDoc != null) {
            return latestKundeDoc.getInteger("_id");
        } else {
            // If no Kunde exists yet, return 0 or any other default value as per your requirement
            return 0;
        }
    }

}

