package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Tutorial")
@XmlAccessorType(XmlAccessType.FIELD)
public class TutorialDTO extends MaterialDTO{
    
    private String text;

    public TutorialDTO() { }
    
    public TutorialDTO(int id, String description, String text) {
        super(id, description, "Tutorial");
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void reset() {
        super.reset();
        setText(null);
    }
}
