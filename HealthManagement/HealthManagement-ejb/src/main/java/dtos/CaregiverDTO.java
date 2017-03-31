package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Caregiver")
@XmlAccessorType(XmlAccessType.FIELD)
public class CaregiverDTO extends UserDTO {
    
    private String rate;

    public CaregiverDTO() {}

    public CaregiverDTO(String username, String password, String name, String mail, String rate) {
        super(username, password, name, mail);
        this.rate = rate;
    }
    
    @Override
    public void reset() {
        super.reset();
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
 

    
    
    
}
