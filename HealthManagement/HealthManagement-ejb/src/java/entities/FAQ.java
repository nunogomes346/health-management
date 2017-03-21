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
    @NamedQuery(name = "getAllFAQs",
    query = "SELECT f FROM FAQ f ORDER BY f.id")
})
public class FAQ extends Material implements Serializable {
    
    @Lob @Basic(fetch=EAGER)
    @NotNull
    private String question;
    
    @Lob @Basic(fetch=EAGER)
    @NotNull
    private String answer;
    
    public FAQ(){
    }
    
    public FAQ(String description, String question, String answer){
        super(description, GROUP.Faq);
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    
    
}
