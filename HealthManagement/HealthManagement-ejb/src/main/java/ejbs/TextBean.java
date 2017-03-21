package ejbs;

import dtos.TextDTO;
import entities.Caregiver;
import entities.Counter;
import entities.Need;
import entities.Text;
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
public class TextBean {

    @PersistenceContext
    EntityManager em;

    public void create(String description, String textContent)
            throws MyConstraintViolationException {
        try {
            Text text = new Text(description, textContent);

            em.persist(text);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));    
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(int id, String description, String textContent) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Text text = em.find(Text.class, id);
            if (text == null) {
                throw new EntityDoesNotExistException("There is no Text with that id.");
            }

            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", text.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                counter.setResource(description);
                em.merge(counter);                
            }
            
            text.setDescription(description);
            text.setText(textContent);

            em.merge(text);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<TextDTO> getAll() {
        try {
            List<Text> texts = (List<Text>) em.createNamedQuery("getAllTexts").getResultList();

            return textsToDTOs(texts);
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public TextDTO getText(int id) throws EntityDoesNotExistException {
        try {
            Text text = em.find(Text.class, id);
            if (text == null) {
                throw new EntityDoesNotExistException("There is no Text with that id.");
            }
            
            return textToDTO(text);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void remove(int id) throws EntityDoesNotExistException {
        try {
            Text text = em.find(Text.class, id);
            if (text == null) {
                throw new EntityDoesNotExistException("There is no Text with that id.");
            }
            
            for (Caregiver caregiver : text.getCaregivers()) {
                caregiver.removeMaterial(text);
            }
            
            for (Need need : text.getNeeds()) {
                need.removeMaterial(text);
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", text.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                em.remove(counter);
            }
            
            em.remove(text);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    //Build DTOs
    public TextDTO textToDTO(Text text) {
        return new TextDTO(
                text.getId(),
                text.getDescription(),
                text.getText()
        );
    }

    public List<TextDTO> textsToDTOs(List<Text> texts) {
        List<TextDTO> dtos = new ArrayList<>();
        for (Text t : texts) {
            dtos.add(textToDTO(t));
        }
        return dtos;
    }

}
