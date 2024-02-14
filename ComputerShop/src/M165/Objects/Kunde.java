package M165.Objects;

import M165.CShop_Repository;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Kunde {
    private ObjectId id;
    private String Geschlecht;
    private String Nachname;
    private String Vorname;
    private String Telefon;
    private String Email;
    private String Sprache;
    private Date Geburtstag;
    private Adresse adresse;
    private List<Bestellung> Bestellungen;

    public Kunde(ObjectId id, String Geschlecht, String Nachname, String Vorname, String Telefon, String Email, String Sprache, Date Geburtstag,Adresse adresse) {

        this.id = id;
        this.Geschlecht = Geschlecht;
        this.Nachname = Nachname;
        this.Vorname = Vorname;
        this.Telefon = Telefon;
        this.Email = Email;
        this.Sprache = Sprache;
        this.Geburtstag = Geburtstag;
        this.adresse = adresse;
        this.Bestellungen = new ArrayList<>();
    }


    public String getGeschlecht() {
        return Geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        Geschlecht = geschlecht;
    }

    public String getNachname() {
        return Nachname;
    }

    public void setNachname(String nachname) {
        Nachname = nachname;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setVorname(String vorname) {
        Vorname = vorname;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSprache() {
        return Sprache;
    }

    public void setSprache(String sprache) {
        Sprache = sprache;
    }

    public Date getGeburtstag() {
        return Geburtstag;
    }

    public void setGeburtstag(Date geburtstag) {
        Geburtstag = geburtstag;
    }

    public List<Bestellung> getBestellungen() {
        return Bestellungen;
    }

    public void setBestellungen(List<Bestellung> bestellungen) {
        Bestellungen = bestellungen;
    }

    public ObjectId getId(CShop_Repository repository) {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    // Function to check if a Kunde ID already exists in the database
    static boolean isKundeIdExists(CShop_Repository repository, ObjectId kundeId) {
        List<Kunde> kunden = repository.readAllKunden();
        for (Kunde kunde : kunden) {
            if (kunde.getId(repository).equals(kundeId)) {
                return true;
            }
        }
        return false;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
}

