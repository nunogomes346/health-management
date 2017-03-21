package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
    @NamedQuery(name = "getAllCaregivers",
            query = "SELECT c FROM Caregiver c ORDER BY c.name")
})
public class Caregiver extends User implements Serializable {
   
    @OneToMany(mappedBy = "caregiver")
    private List<Patient> patients;

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.REMOVE)
    private List<Proceeding> proceedings;
   
    @ManyToMany
    @JoinTable(name = "CAREGIVERS_MATERIALS", 
            joinColumns = @JoinColumn(name = "CAREGIVER_USERNAME", referencedColumnName = "USERNAME"),
            inverseJoinColumns = @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID"))
    private List<Material> materials;
    
    private String rate;
    
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.REMOVE)
    private List<Counter> counters;
    
    public Caregiver() {  
       this.patients = new LinkedList<Patient>();
       this.materials = new LinkedList<Material>();
       this.proceedings = new LinkedList<Proceeding>();
       this.counters = new LinkedList<Counter>();
    }
    
    public Caregiver(String username, String password, String name, String mail) {
        super(username, password, name, mail, GROUP.Caregiver);
        this.patients = new LinkedList<Patient>();
        this.materials = new LinkedList<Material>();
        this.proceedings = new LinkedList<Proceeding>();
        this.rate = "No rate";
        this.counters = new LinkedList<Counter>();
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void addPatient(Patient p) {
        this.patients.add(p);
    }

    public void removePatient(Patient p) {
        this.patients.remove(p);
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    
    public void addMaterial(Material material){
        this.materials.add(material);
    }
    
    public void removeMaterial(Material material){
        this.materials.remove(material);
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public List<Counter> getCounters() {
        return counters;
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
    }
    
    public void addCounter(Counter counter){
        this.counters.add(counter);
    }
    
    public void removeCounter(Counter counter){
        this.counters.remove(counter);
    }
}
