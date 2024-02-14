package M165.Objects;

import M165.CShop_Repository;
import org.bson.types.ObjectId;

import org.bson.types.ObjectId;
import java.util.List;

public class Computer {
    private ObjectId computerId;
    private String hersteller;
    private String modell;
    private Integer arbeitspeicher;
    private String cpu;
    private Integer massenspeicher;
    private String typ;
    private List<Schnittstellentyp> schnittstellen;
    private Double Einzelpreis;

    public Computer(ObjectId computerId, String hersteller, String modell, Integer arbeitspeicher, String cpu, Integer massenspeicher, String typ, List<Schnittstellentyp> schnittstellen, Double Einzelpreis) {
        this.computerId = computerId;
        this.hersteller = hersteller;
        this.modell = modell;
        this.arbeitspeicher = arbeitspeicher;
        this.cpu = cpu;
        this.massenspeicher = massenspeicher;
        this.typ = typ;
        this.schnittstellen = schnittstellen;
        this.Einzelpreis = Einzelpreis;
    }

    // Getters and setters...


    public Double getEinzelpreis() {
        return Einzelpreis;
    }

    public void setEinzelpreis(Double einzelpreis) {
        Einzelpreis = einzelpreis;
    }

    public ObjectId getId(CShop_Repository repository) {
        return computerId;
    }

    public void setId(ObjectId id) {
        this.computerId = id;
    }

    public List<Schnittstellentyp> getSchnittstellen() {
        return schnittstellen;
    }

    public void setSchnittstellen(List<Schnittstellentyp> schnittstellen) {
        this.schnittstellen = schnittstellen;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public Integer getArbeitspeicher() {
        return arbeitspeicher;
    }

    public void setArbeitspeicher(Integer arbeitspeicher) {
        this.arbeitspeicher = arbeitspeicher;
    }

    public String getCPU() {
        return cpu;
    }

    public void setCPU(String cpu) {
        this.cpu = cpu;
    }

    public Integer getMassenspeicher() {
        return massenspeicher;
    }

    public void setMassenspeicher(Integer massenspeicher) {
        this.massenspeicher = massenspeicher;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public ObjectId getComputerId() {
        return computerId;
    }

    public void setComputerId(ObjectId computerId) {
        this.computerId = computerId;
    }

    // Function to check if a Computer ID already exists in the database
    static boolean isComputerIdExists(CShop_Repository repository, ObjectId computerId) {
        List<Computer> computers = repository.readAllComputers();
        for (Computer computer : computers) {
            if (computer.getId(repository).equals(computerId)) {
                return true;
            }
        }
        return false;
    }

}


