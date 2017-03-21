package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PROCEEDINGS")
@NamedQueries({
    @NamedQuery(name = "getAllProceeding",
            query = "SELECT p FROM Proceeding p ORDER BY p.id ")
    }
)
public class Proceeding implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "MATERIAL_ID")
    @NotNull
    private Material material;
    
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "NEED_ID")
    @NotNull
    private Need need;
    
    @ManyToOne
    @JoinColumn(name = "CAREGIVER_ID")
    @NotNull
    private Caregiver caregiver;
    
    @NotNull
    private String proceedingDate;
    
    private String note;

    public Proceeding() { }
    
    public Proceeding(Material material, Patient patient, Need need, Caregiver caregiver, String proceedingDate, String note) {
        this.material = material;
        this.patient = patient;
        this.need = need;
        this.caregiver = caregiver;
        this.proceedingDate = proceedingDate;
        this.note = note;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public Caregiver getCaregiver() {
        return caregiver;
    }

    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }

    public String getProceedingDate() {
        return proceedingDate;
    }

    public void setProceedingDate(String proceedingDate) {
        this.proceedingDate = proceedingDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
