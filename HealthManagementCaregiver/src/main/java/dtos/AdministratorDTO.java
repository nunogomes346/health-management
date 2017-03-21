package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Administrator")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministratorDTO extends UserDTO implements Serializable{
   
    public AdministratorDTO(){ }

    public AdministratorDTO(String username, String password, String name, String mail) {
        super(username, password, name, mail);
    }
    
    @Override
    public void reset() {
        super.reset();
    }
}
