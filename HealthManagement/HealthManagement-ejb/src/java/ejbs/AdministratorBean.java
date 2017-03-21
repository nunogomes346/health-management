package ejbs;

import dtos.AdministratorDTO;
import entities.Administrator;
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
public class AdministratorBean {

    @PersistenceContext
    EntityManager em;
    
    public void create(String username, String password, String name, String mail) 
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Administrator.class, username) != null) {
                throw new EntityAlreadyExistsException("A administrator with that username already exists.");
            }
            
            Administrator administrator = new Administrator(username, password, name, mail);

            em.persist(administrator);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(String username, String password, String name, String mail) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Administrator administrator = em.find(Administrator.class, username);
            if (administrator == null) {
                throw new EntityDoesNotExistException("There is no administrator with that username.");
            }
            
            administrator.setPassword(password);
            administrator.setName(name);
            administrator.setMail(mail);
            
            em.merge(administrator);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<AdministratorDTO> getAll() {
        try {
            List<Administrator> administrators = (List<Administrator>) em.createNamedQuery("getAllAdministrators").getResultList();
            
            return administratorsToDTOs(administrators); 
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public AdministratorDTO getAdministrator(String username) throws EntityDoesNotExistException {
        try {
            Administrator administrator = em.find(Administrator.class, username);
            if (administrator == null) {
                throw new EntityDoesNotExistException("There is no administrator with that username.");
            }
            
            return administratorToDTO(administrator);  
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(String username) throws EntityDoesNotExistException {
        try {
            Administrator administrator = em.find(Administrator.class, username);
            if (administrator == null) {
                throw new EntityDoesNotExistException("There is no administrator with that username.");
            }
            
            em.remove(administrator);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    AdministratorDTO administratorToDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                null,
                administrator.getName(),
                administrator.getMail());
    }
    
    List<AdministratorDTO> administratorsToDTOs(List<Administrator> administrators) {
        List<AdministratorDTO> dtos = new ArrayList<>();
        for (Administrator a : administrators) {
            dtos.add(administratorToDTO(a));
        }
        return dtos;
    }
}
