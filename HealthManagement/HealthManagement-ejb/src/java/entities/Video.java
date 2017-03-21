package entities;

import entities.MaterialGroup.GROUP;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name = "getAllVideos",
    query = "SELECT v FROM Video v ORDER BY v.id")
})
public class Video extends Material implements Serializable {

    @NotNull
    private String url;
    
    public Video(){
    }
    
    public Video(String description, String url){
        super(description, GROUP.Video);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
