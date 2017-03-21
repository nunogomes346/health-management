package ejbs;

import dtos.VideoDTO;
import entities.Caregiver;
import entities.Counter;
import entities.Need;
import entities.Video;
import exceptions.EntityDoesNotExistException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class VideoBean {

    @PersistenceContext
    EntityManager em;

    public void create(String description, String url) 
            throws MyConstraintViolationException {
        try {
            Video video = new Video(description, url);

            em.persist(video);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(int id, String description, String url) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Video video = em.find(Video.class, id);
            if (video == null) {
                throw new EntityDoesNotExistException("There is no Video with that id.");
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", video.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                counter.setResource(description);
                em.merge(counter);                
            }

            video.setDescription(description);
            video.setUrl(url);

            em.merge(video);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<VideoDTO> getAll() {
        try {
            List<Video> videos = (List<Video>) em.createNamedQuery("getAllVideos").getResultList();

            return videosToDTOs(videos);
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public VideoDTO getVideo(int id) throws EntityDoesNotExistException {
        try {
            Video video = em.find(Video.class, id);
            if (video == null) {
                throw new EntityDoesNotExistException("There is no Video with that id.");
            }
            
            return videoToDTO(video);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void remove(int id) throws EntityDoesNotExistException {
        try {
            Video video = em.find(Video.class, id);
            if (video == null) {
                throw new EntityDoesNotExistException("There is no Video with that id.");
            }
            
            for (Caregiver caregiver : video.getCaregivers()) {
                caregiver.removeMaterial(video);
            }
            
            for (Need need : video.getNeeds()) {
                need.removeMaterial(video);
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", video.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                em.remove(counter);
            }
            
            em.remove(video);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public VideoDTO videoToDTO(Video video) {
        return new VideoDTO(
                video.getId(),
                video.getDescription(),
                video.getUrl()
        );
    }

    public List<VideoDTO> videosToDTOs(List<Video> videos) {
        List<VideoDTO> dtos = new ArrayList<>();
        for (Video v : videos) {
            dtos.add(videoToDTO(v));
        }
        return dtos;
    }

}
