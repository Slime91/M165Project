package M165.Objects;
import M165.*;
import java.util.ArrayList;
import java.util.*;

public class Schnittstellen {
    public static List<Schnittstellentyp> schnittstellentypen;

    public static List<Schnittstellentyp> getSchnittstellentypen() {
        return schnittstellentypen;
    }

    public void setSchnittstellentypen(List<Schnittstellentyp> schnittstellentypen) {
        this.schnittstellentypen = schnittstellentypen;
    }

    public void addSchnittstellentyp(Schnittstellentyp schnittstellentyp) {
        if (schnittstellentypen == null) {
            schnittstellentypen = new ArrayList<>();
        }
        schnittstellentypen.add(schnittstellentyp);
    }

}