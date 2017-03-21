package entities;

import entities.MaterialGroup.GROUP;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MATERIALS", 
    uniqueConstraints = 
        @UniqueConstraint(columnNames = {"DESCRIPTION"}))
public class Material implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "material")
    protected MaterialGroup group;
    
    @NotNull
    private String description;
    
    @ManyToMany(mappedBy = "materials")
    private List<Need> needs;
    
    @ManyToMany(mappedBy = "materials")
    private List<Caregiver> caregivers;

    @OneToMany(mappedBy = "material", cascade = CascadeType.REMOVE)
    private List<Proceeding> proceedings;
    
    public Material(){
        this.needs = new LinkedList<Need>();
        this.caregivers = new LinkedList<Caregiver>();
        this.proceedings = new LinkedList<Proceeding>();
    }
    
    public Material(String description, GROUP group){
        this.description = description;
        this.group = new MaterialGroup(group, this);
        this.needs = new LinkedList<Need>();
        this.caregivers = new LinkedList<Caregiver>();
        this.proceedings = new LinkedList<Proceeding>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MaterialGroup getGroup() {
        return group;
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

    public List<Caregiver> getCaregivers() {
        return caregivers;
    }

    public void setCaregivers(List<Caregiver> caregivers) {
        this.caregivers = caregivers;
    }
    
    public void addCaregiver(Caregiver caregiver) {
        this.caregivers.add(caregiver);
    }
    
    public void removeCaregiver(Caregiver caregiver) {
        this.caregivers.remove(caregiver);
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
