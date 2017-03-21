package dtos;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Material")
@XmlAccessorType(XmlAccessType.FIELD)
public class MaterialDTO implements Serializable{
    
    private int id;
    private String description;
    private String type;
    
    public MaterialDTO() { }
    
    public MaterialDTO(int id, String description, String type) { 
        this.id = id;
        this.description = description;
        this.type = type;
    }
    
    public void reset(){
        this.setId(0);
        this.setDescription(null);
        this.setType(null);
    };
    
    public int getId(){
        return this.id;
    }
    
    public void setId(int id){
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
