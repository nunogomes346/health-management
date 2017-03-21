package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Video")
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoDTO extends MaterialDTO {
    
    private String url;

    public VideoDTO() { }
    
    public VideoDTO(int id, String description, String url){
        super(id, description, "Video");
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void reset() {
        super.reset();
        setUrl(null);
    }
    
    
}
