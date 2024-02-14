package M165.Objects;
import org.bson.types.ObjectId;


public class Bestellposition {
    private ObjectId articleId;
    private Integer anzahl;
    private Double einzelpreis;

    public Bestellposition(ObjectId articleId, Integer anzahl, Double einzelpreis) {
        this.articleId = articleId;
        this.anzahl = anzahl;
        this.einzelpreis = einzelpreis;
    }

    public ObjectId getArticleId() {
        return articleId;
    }

    public void setArticleId(ObjectId articleId) {
        this.articleId = articleId;
    }

    public Integer getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(Integer anzahl) {
        this.anzahl = anzahl;
    }

    public Double getEinzelpreis() {
        return einzelpreis;
    }

    public void setEinzelpreis(Double einzelpreis) {
        this.einzelpreis = einzelpreis;
    }
}