package M165.Objects;

public class Bestellposition {
    private Double Einzelpreis;
    private Integer Anzahl;

    public Bestellposition(Double Einzelpreis, Integer Anzahl) {
        this.Einzelpreis = Einzelpreis;
        this.Anzahl = Anzahl;
    }

    public Double getEinzelpreis() {
        return Einzelpreis;
    }

    public void setEinzelpreis(Double einzelpreis) {
        Einzelpreis = einzelpreis;
    }

    public Integer getAnzahl() {
        return Anzahl;
    }

    public void setAnzahl(Integer anzahl) {
        Anzahl = anzahl;
    }
}