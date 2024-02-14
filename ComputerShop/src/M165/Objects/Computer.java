package M165.Objects;

import java.util.ArrayList;
import java.util.List;

public class Computer {
    private String Hersteller;
    private String Modell;
    private Integer Arbeitspeicher;
    private String CPU;
    private Integer Massenspeicher;
    private String Typ;
    private List<M165.Objects.Schnittstellentyp> Schnittstellentyp;


    public Computer(String Hersteller, String Modell, Integer Arbeitspeicher, String CPU, Integer Massenspeicher, String Typ) {
        this.Hersteller = Hersteller;
        this.Modell = Modell;
        this.Arbeitspeicher = Arbeitspeicher;
        this.CPU = CPU;
        this.Massenspeicher = Massenspeicher;
        this.Typ = Typ;
        this.Schnittstellentyp = new ArrayList<>();
    }

    public List<M165.Objects.Schnittstellentyp> getSchnittstellentyp() {
        return Schnittstellentyp;
    }

    public String getHersteller() {
        return Hersteller;
    }

    public void setHersteller(String hersteller) {
        Hersteller = hersteller;
    }

    public String getModell() {
        return Modell;
    }

    public void setModell(String modell) {
        Modell = modell;
    }

    public Integer getArbeitspeicher() {
        return Arbeitspeicher;
    }

    public void setArbeitspeicher(Integer arbeitspeicher) {
        Arbeitspeicher = arbeitspeicher;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public Integer getMassenspeicher() {
        return Massenspeicher;
    }

    public void setMassenspeicher(Integer massenspeicher) {
        Massenspeicher = massenspeicher;
    }

    public String getTyp() {
        return Typ;
    }

    public void setTyp(String typ) {
        Typ = typ;
    }

}
