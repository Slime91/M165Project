package M165.Objects;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bestellung {
    private Integer Bestellnummer;
    private Date Bestelldatum;
    private double Total;
    private List<Bestellposition> bestellpositionList;
    private ObjectId kundeId; // New attribute to store Kunde ID

    public Bestellung(Integer Bestellnummer, ObjectId kundeId, Date Bestelldatum, double Total, List<Bestellposition> bestellpositionList) {
        this.Bestellnummer = Bestellnummer;
        this.kundeId = kundeId;
        this.Bestelldatum = Bestelldatum;
        this.Total = Total;
        this.bestellpositionList = new ArrayList<>();
    }

    // Getter and setter for kundeId
    public ObjectId getKundeId() {
        return kundeId;
    }

    public void setKundeId(ObjectId kundeId) {
        this.kundeId = kundeId;
    }

    public Integer getBestellnummer() {
        return Bestellnummer;
    }

    public void setBestellnummer(Integer bestellnummer) {
        Bestellnummer = bestellnummer;
    }

    public Date getBestelldatum() {
        return Bestelldatum;
    }

    public void setBestelldatum(Date bestelldatum) {
        Bestelldatum = bestelldatum;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public List<Bestellposition> getBestellpositionList() {
        return bestellpositionList;
    }

    public void setBestellpositionList(List<Bestellposition> bestellpositionList) {
        this.bestellpositionList = bestellpositionList;
    }
    public String getBestellnummerAsString() {
        return String.valueOf(Bestellnummer);
    }

    public Integer getBestellnummerAsInt() {
        return Bestellnummer;
    }
}

