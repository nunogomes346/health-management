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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "NEEDS", 
    uniqueConstraints = 
        @UniqueConstraint(columnNames = {"DESCRIPTION"}))
@NamedQueries({
    @NamedQuery(name = "getAllNeeds",
    query = "SELECT n FROM Need n ORDER BY n.id ")
})
public class Need implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String description;
    
    @ManyToMany
    @JoinTable(name = "NEEDS_PATIENTS", 
            joinColumns = @JoinColumn(name = "NEED_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID"))
    private List<Patient> patients;
    
    @ManyToMany
    @JoinTable(name = "NEEDS_MATERIALS", 
            joinColumns = @JoinColumn(name = "NEED_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID"))
    private List<Material> materials;
    
    @OneToMany(mappedBy = "need", cascade = CascadeType.REMOVE)
    private List<Proceeding> proceedings;

    public Need(){
        this.patients = new LinkedList<Patient>();
        this.materials = new LinkedList<Material>();
        this.proceedings = new LinkedList<Proceeding>();
    }
    
    public Need(String description) {
        this.description = description;
        this.patients = new LinkedList<Patient>();
        this.materials = new LinkedList<Material>();
        this.proceedings = new LinkedList<Proceeding>();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }
    
    public void removePatient(Patient patient) {
        this.patients.remove(patient);
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    
    public void addMaterial(Material material) {
        this.materials.add(material);
    }
    
    public void removeMaterial(Material material) {
        this.materials.remove(material);
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
