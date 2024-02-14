package M165.Objects;

import java.util.ArrayList;
import java.util.List;

public class Schnittstellentyp {
    private String name;
    private Integer integer;

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Schnittstellentyp(String name, Integer integer) {
        this.name = name;
        this.integer = integer;
    }

    public Schnittstellentyp(String schnittstellentyp) {
    }

    public String getName() {
        return name;
    }

    public void setName(String updatedName) {
        this.name = name;
    }


}

