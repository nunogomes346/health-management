package ejbs;

import dtos.EmergencyContactDTO;
import entities.Caregiver;
import entities.Counter;
import entities.EmergencyContact;
import entities.Need;
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
public class EmergencyContactBean {

    @PersistenceContext
    EntityManager em;

    public void create(String description, String name, String telephoneNumber)
            throws MyConstraintViolationException {
        try {
            EmergencyContact emergencyContact = new EmergencyContact(description, name, telephoneNumber);

            em.persist(emergencyContact);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(int id, String description, String name, String telephoneNumber)
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            EmergencyContact emergencyContact = em.find(EmergencyContact.class, id);
            if (emergencyContact == null) {
                throw new EntityDoesNotExistException("There is no Emergency Contact with that id.");
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", emergencyContact.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                counter.setResource(description);
                em.merge(counter);                
            }
            
            emergencyContact.setDescription(description);
            emergencyContact.setName(name);
            emergencyContact.setTelephoneNumber(telephoneNumber);

            em.merge(emergencyContact);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<EmergencyContactDTO> getAll() {
        try {
            List<EmergencyContact> emergencyContacts = (List<EmergencyContact>) em.createNamedQuery("getAllEmergencyContacts").getResultList();

            return emergencyContactsToDTOs(emergencyContacts);
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public EmergencyContactDTO getEmergencyContact(int id) throws EntityDoesNotExistException {
        try {
            EmergencyContact emergencyContact = em.find(EmergencyContact.class, id);
            if (emergencyContact == null) {
                throw new EntityDoesNotExistException("There is no Emergency Contact with that id.");
            }

            return emergencyContactToDTO(emergencyContact);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void remove(int id) throws EntityDoesNotExistException {
        try {
            EmergencyContact emergencyContact = em.find(EmergencyContact.class, id);
            if (emergencyContact == null) {
                throw new EntityDoesNotExistException("There is no Emergency Contact with that id.");
            }

            for (Caregiver caregiver : emergencyContact.getCaregivers()) {
                caregiver.removeMaterial(emergencyContact);
            }
            
            for (Need need : emergencyContact.getNeeds()) {
                need.removeMaterial(emergencyContact);
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", emergencyContact.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                em.remove(counter);
            }
            
            em.remove(emergencyContact);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    //Build DTOs
    public EmergencyContactDTO emergencyContactToDTO(EmergencyContact emergencyContact) {
        return new EmergencyContactDTO(
                emergencyContact.getId(),
                emergencyContact.getDescription(),
                emergencyContact.getName(),
                emergencyContact.getTelephoneNumber()
        );
    }

    public List<EmergencyContactDTO> emergencyContactsToDTOs(List<EmergencyContact> emergencyContacts) {
        List<EmergencyContactDTO> dtos = new ArrayList<>();
        for (EmergencyContact ec : emergencyContacts) {
            dtos.add(emergencyContactToDTO(ec));
        }
        return dtos;
    }

}
