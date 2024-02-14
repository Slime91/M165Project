package M165.Objects;

public class Adresse {
    public String Strasse;
    public Integer Postleitzahl;
    public String Ort;

    public Adresse(String Strasse, Integer Postleitzahl, String Ort) {
        this.Strasse = Strasse;
        this.Postleitzahl = Postleitzahl;
        this.Ort = Ort;
    }

    public String getStrasse() {
        return Strasse;
    }

    public void setStrasse(String strasse) {
        Strasse = strasse;
    }

    public Integer getPostleitzahl() {
        return Postleitzahl;
    }

    public void setPostleitzahl(Integer postleitzahl) {
        Postleitzahl = postleitzahl;
    }

    public String getOrt() {
        return Ort;
    }

    public void setOrt(String ort) {
        Ort = ort;
    }
}
