package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientDTO {
    
    private Long id;
    private String name;
    private String mail;
    private String caregiverUsername;
    
    public PatientDTO() { }

    public PatientDTO(Long id, String name, String mail, String caregiverUsername) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.caregiverUsername = caregiverUsername;
    }
    
    public void reset() {
        setId(null);
        setName(null);
        setMail(null);
        setCaregiverUsername(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCaregiverUsername() {
        return caregiverUsername;
    }

    public void setCaregiverUsername(String caregiverUsername) {
        this.caregiverUsername = caregiverUsername;
    }
}
