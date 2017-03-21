package entities;

import entities.MaterialGroup.GROUP;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name = "getAllTutorials",
    query = "SELECT t FROM Tutorial t ORDER BY t.id")
})
public class Tutorial extends Material implements Serializable {
    
    @Lob @Basic(fetch=EAGER)
    @NotNull
    private String text;

    public Tutorial(){
    }
    
    public Tutorial(String description, String text) {
        super(description, GROUP.Tutorial);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
  
}
