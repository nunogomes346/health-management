package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "getAllAdministrators",
    query = "SELECT a FROM Administrator a ORDER BY a.name")
})
public class Administrator extends User implements Serializable {

    public Administrator() { }
    
    public Administrator(String username, String password, String name, String mail) {
        super(username, password, name, mail, GROUP.Administrator);
    }
    
}
