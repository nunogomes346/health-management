package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FAQ")
@XmlAccessorType(XmlAccessType.FIELD)
public class FaqDTO extends MaterialDTO {
    
    private String question;
    private String answer;
    
    public FaqDTO() { }
    
    public FaqDTO(int id,String description, String question, String answer){
        super(id, description, "Faq");
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

    @Override
    public void reset() {
        super.reset();
        setAnswer(null);
        setQuestion(null);
    }
    
    
    
}
