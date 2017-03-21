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
@Table(name = "COUNTERS")
@NamedQueries({
    @NamedQuery(name = "getAllCounters",
            query = "SELECT c FROM Counter c ORDER BY c.id"),
    @NamedQuery(name = "getAllCountersCaregiver",
            query = "SELECT c FROM Counter c WHERE c.caregiver.username = :caregiverUsername ORDER BY c.id"),
    @NamedQuery(name = "getAllCountersResource",
            query = "SELECT c FROM Counter c WHERE c.resource = :resource ORDER BY c.id"),
    @NamedQuery(name = "getAllCountersCaregiverResource",
            query = "SELECT c FROM Counter c WHERE c.caregiver.username = :caregiverUsername AND c.resource = :resource ORDER BY c.id")
})
public class Counter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String resource;
    
    @NotNull
    private int counter;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "CAREGIVER_USERNAME")
    private Caregiver caregiver;
    
    public Counter() { }

    public Counter(String resource, int counter, Caregiver caregiver) {
        this.resource = resource;
        this.counter = counter;
        this.caregiver = caregiver;
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
    
    public void incrementCounter() {
        this.counter++;
    }
    
    public void decrementCounter() {
        this.counter--;
    }

    public Caregiver getCaregiver() {
        return caregiver;
    }

    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }
}
