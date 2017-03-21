
package ejbs;

import dtos.HealthcareProDTO;
import entities.HealthcarePro;
import exceptions.EntityAlreadyExistsException;
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
public class HealthcareProBean {

    @PersistenceContext
    EntityManager em;
    
    public void create(String username, String password, String name, String mail, String facility, String job) 
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(HealthcarePro.class, username) != null) {
                throw new EntityAlreadyExistsException("A healthcarePro with that username already exists.");
            }
            HealthcarePro healthcarepro = new HealthcarePro(username, password, name, mail, facility, job);

            em.persist(healthcarepro);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(String username, String password, String name, String mail, String facility, String job) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            HealthcarePro healthcarePro = em.find(HealthcarePro.class, username);
            if (healthcarePro == null) {
                throw new EntityDoesNotExistException("There is no healthcarePro with that username.");
            }
            
            healthcarePro.setPassword(password);
            healthcarePro.setName(name);
            healthcarePro.setMail(mail);
            healthcarePro.setFacility(facility);
            healthcarePro.setJob(job);
            
            em.merge(healthcarePro);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<HealthcareProDTO> getAll() {
        try {
            List<HealthcarePro> healthcarePros = (List<HealthcarePro>) em.createNamedQuery("getAllHealthcarePros").getResultList();
            
            return HealthcareprosToDTOs(healthcarePros);  
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public HealthcareProDTO getHealthcarePro(String username) throws EntityDoesNotExistException {
        try {
            HealthcarePro healthcarePro = em.find(HealthcarePro.class, username);
            if (healthcarePro == null) {
                throw new EntityDoesNotExistException("There is no healthcarePro with that username.");
            }
            
            return healthcareproToDTO(healthcarePro);  
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(String username) throws EntityDoesNotExistException {
        try {
            HealthcarePro healthcarePro = em.find(HealthcarePro.class, username);
            if (healthcarePro == null) {
                throw new EntityDoesNotExistException("There is no healthcarePro with that username.");
            }
            
            em.remove(healthcarePro);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    HealthcareProDTO healthcareproToDTO(HealthcarePro healthcarePro) {
        return new HealthcareProDTO(
                healthcarePro.getUsername(),
                null,
                healthcarePro.getName(),
                healthcarePro.getMail(),
                healthcarePro.getFacility(),
                healthcarePro.getJob());
    }
    
    List<HealthcareProDTO> HealthcareprosToDTOs(List<HealthcarePro> healthcarePros) {
        List<HealthcareProDTO> dtos = new ArrayList<>();
        for (HealthcarePro h : healthcarePros) {
            dtos.add(healthcareproToDTO(h));
        }
        return dtos;
    }
}
