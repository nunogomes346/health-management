package ejbs;

import dtos.TutorialDTO;
import entities.Caregiver;
import entities.Counter;
import entities.Need;
import entities.Tutorial;
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
public class TutorialBean {

    @PersistenceContext
    EntityManager em;

    public void create(String description, String text) 
            throws MyConstraintViolationException {
        try {
            Tutorial tutorial = new Tutorial(description, text);

            em.persist(tutorial);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(int id, String description, String text) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Tutorial tutorial = em.find(Tutorial.class, id);
            if (tutorial == null) {
                throw new EntityDoesNotExistException("There is no Tutorial with that id.");
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", tutorial.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                counter.setResource(description);
                em.merge(counter);                
            }

            tutorial.setDescription(description);
            tutorial.setText(text);

            em.merge(tutorial);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<TutorialDTO> getAll() {
        try {
            List<Tutorial> tutorials = (List<Tutorial>) em.createNamedQuery("getAllTutorials").getResultList();

            return tutorialsToDTOs(tutorials);
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public TutorialDTO getTutorial(int id) throws EntityDoesNotExistException {
        try {
            Tutorial tutorial = em.find(Tutorial.class, id);
            if (tutorial == null) {
                throw new EntityDoesNotExistException("There is no Tutorial with that id.");
            }

            return tutorialToDTO(tutorial);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void remove(int id) throws EntityDoesNotExistException {
        try {
            Tutorial tutorial = em.find(Tutorial.class, id);
            if (tutorial == null) {
                throw new EntityDoesNotExistException("There is no Tutorial with that id.");
            }

            for (Caregiver caregiver : tutorial.getCaregivers()) {
                caregiver.removeMaterial(tutorial);
            }
            
            for (Need need : tutorial.getNeeds()) {
                need.removeMaterial(tutorial);
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", tutorial.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                em.remove(counter);
            }
            
            em.remove(tutorial);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    public TutorialDTO tutorialToDTO(Tutorial tutorial) {
        return new TutorialDTO(
                tutorial.getId(),
                tutorial.getDescription(),
                tutorial.getText()
        );
    }

    public List<TutorialDTO> tutorialsToDTOs(List<Tutorial> tutorials) {
        List<TutorialDTO> dtos = new ArrayList<>();
        for (Tutorial t : tutorials) {
            dtos.add(tutorialToDTO(t));
        }
        return dtos;
    }

}
