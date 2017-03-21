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
    @NamedQuery(name = "getAllTexts",
    query = "SELECT t FROM Text t ORDER BY t.id")
})
public class Text extends Material implements Serializable {
    
    @Lob @Basic(fetch=EAGER)
    @NotNull
    private String text;
    
    public Text(){
    }
    
    public Text(String description, String text){
        super(description, GROUP.Text);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
}
