package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Text")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextDTO extends MaterialDTO{
    private String text;

    public TextDTO() { }
    
    public TextDTO (int id, String description, String text){
        super(id, description, "Text");
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
