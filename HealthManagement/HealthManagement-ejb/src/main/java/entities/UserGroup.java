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
@Table(name = "USERS_GROUPS")
public class UserGroup implements Serializable {

    public enum GROUP {
        Administrator, HealthcarePro, Caregiver, Patient
    }
    
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="GROUP_NAME")
    private GROUP groupName;
    
    @Id
    @OneToOne
    @JoinColumn(name = "USERNAME")
    private User user;

    public UserGroup() { }
    
    public UserGroup(GROUP groupName, User user) {
        this.groupName = groupName;
        this.user = user;
    }

    public GROUP getGroupName() {
        return groupName;
    }

    public void setGroupName(GROUP groupName) {
        this.groupName = groupName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
