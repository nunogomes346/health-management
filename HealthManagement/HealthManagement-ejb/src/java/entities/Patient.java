package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PATIENTS")
@NamedQueries({
    @NamedQuery(name = "getAllPatients",
            query = "SELECT p FROM Patient p ORDER BY p.name")
})
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String mail;

    @ManyToOne
    @JoinColumn(name = "CAREGIVER_USERNAME")
    private Caregiver caregiver;
    
    @ManyToMany(mappedBy = "patients")
    private List<Need> needs;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<Proceeding> proceedings;

    public Patient() {
        this.needs = new LinkedList<Need>();
        this.proceedings = new LinkedList<Proceeding>();
    }

    public Patient(String name, String mail) {
        this.name = name;
        this.mail = mail;
        this.caregiver = null;
        this.needs = new LinkedList<Need>();
        this.proceedings = new LinkedList<Proceeding>();
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

    public Caregiver getCaregiver() {
        return caregiver;
    }

    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }

    public List<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(List<Need> needs) {
        this.needs = needs;
    }
    
    public void addNeed(Need need) {
        this.needs.add(need);
    }
    
    public void removeNeed(Need need) {
        this.needs.remove(need);
    }

    public List<Proceeding> getProceedings() {
        return proceedings;
    }

    public void setProceedings(List<Proceeding> proceedings) {
        this.proceedings = proceedings;
    }
    
    public void addProceeding(Proceeding proceeding){
        this.proceedings.add(proceeding);
    }
    
    public void removeProceeding(Proceeding proceeding){
        this.proceedings.remove(proceeding);
    }
}
