package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name = "getAllHealthcarePros",
    query = "SELECT h FROM HealthcarePro h ORDER BY h.name")
})
public class HealthcarePro extends User implements Serializable {

    @NotNull
    private String facility;
    
    @NotNull
    private String job;
    
    public HealthcarePro() { 
    }
    
    public HealthcarePro(String username, String password, String name, String mail, String facility, String job) {
        super(username, password, name, mail, GROUP.HealthcarePro);
        this.facility = facility;
        this.job = job;
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
