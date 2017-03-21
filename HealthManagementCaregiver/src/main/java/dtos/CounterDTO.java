package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Counter")
@XmlAccessorType(XmlAccessType.FIELD)
public class CounterDTO {

    private Long id;
    private String resource;
    private int counter;
    private String caregiverUsername;
    
    public CounterDTO() { }

    public CounterDTO(Long id, String resource, int counter, String caregiverUsername) {
        this.id = id;
        this.resource = resource;
        this.counter = counter;
        this.caregiverUsername = caregiverUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getCaregiverUsername() {
        return caregiverUsername;
    }

    public void setCaregiverUsername(String caregiverUsername) {
        this.caregiverUsername = caregiverUsername;
    }
    
    public void reset() {
        setId(Long.parseLong("0"));
        setResource(null);
        setCounter(0);
        setCaregiverUsername(null);
    }
    
}
