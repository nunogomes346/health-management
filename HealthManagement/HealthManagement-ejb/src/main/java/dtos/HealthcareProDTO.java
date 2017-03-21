package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HealthcarePro")
@XmlAccessorType(XmlAccessType.FIELD)
public class HealthcareProDTO extends UserDTO {
    
    private String facility;
    
    private String job;
    
    public HealthcareProDTO() { }

    public HealthcareProDTO(String username, String password, String name, String mail, String facility, String job) {
        super(username, password, name, mail);
        this.facility = facility;
        this.job = job;
    }
    
    @Override
    public void reset() {
        super.reset();
        setFacility(null);
        setJob(null);
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
