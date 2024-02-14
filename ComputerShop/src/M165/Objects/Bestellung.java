package M165.Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bestellung {
    private Integer Bestellnummer;
    private Kunde kunde;
    private Date Bestelldatum;
    private double Total;
    private List<Computer> computerList;

    public Bestellung(Integer Bestellnummer, Kunde kunde, Date Bestelldatum, double Total) {
        this.Bestellnummer = Bestellnummer;
        this.Bestelldatum = Bestelldatum;
        this.kunde = kunde;
        this.Total = Total;
        this.computerList = new ArrayList<>();
    }

    public Integer getBestellnummer() {
        return Bestellnummer;
    }

    public void setBestellnummer(Integer bestellnummer) {
        Bestellnummer = bestellnummer;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
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

    public List<Computer> getComputerList() {
        return computerList;
    }

    public void setComputerList(List<Computer> computerList) {
        this.computerList = computerList;
    }
}
