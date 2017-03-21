package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MATERIALS_GROUPS")
public class MaterialGroup implements Serializable {
    
    public enum GROUP {
        Faq, Text, Video, Tutorial, EmergencyContact
    }
    
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="GROUP_NAME")
    private GROUP groupName;
    
    @Id
    @OneToOne
    @JoinColumn(name = "ID")
    private Material material;
    
    public MaterialGroup() { }
    
    public MaterialGroup(GROUP groupName, Material material) {
        this.groupName = groupName;
        this.material = material;
    }
    
    public GROUP getGroupName() {
        return groupName;
    }

    public void setGroupName(GROUP groupName) {
        this.groupName = groupName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    
}
