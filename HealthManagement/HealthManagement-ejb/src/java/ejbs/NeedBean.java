package ejbs;

import dtos.NeedDTO;
import entities.Material;
import entities.Need;
import entities.Patient;
import exceptions.EntityDoesNotExistException;
import exceptions.MaterialAssociatedToNeedException;
import exceptions.MaterialNotAssociatedToNeedException;
import exceptions.MyConstraintViolationException;
import exceptions.NeedAssociatedToPatientException;
import exceptions.NeedNotAssociatedToPatientException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class NeedBean {

    @PersistenceContext
    EntityManager em;
    
    public void create(String description) throws MyConstraintViolationException{
        try {
            Need need = new Need(description);

            em.persist(need);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<NeedDTO> getAll() {
        try {
            List<Need> needs = (List<Need>) em.createNamedQuery("getAllNeeds").getResultList();
            
            return needsToDTOs(needs); 
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public NeedDTO getNeed(Long id) throws EntityDoesNotExistException {
        try {
            Need need = em.find(Need.class, id);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no Need with that id.");
            }
            
            return needToDTO(need);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(Long id, String description) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Need need = em.find(Need.class, id);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }

            need.setDescription(description);

            em.merge(need);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(Long id) throws EntityDoesNotExistException {
        try {
            Need need = em.find(Need.class, id);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            for (Patient patient : need.getPatients()) {
                patient.removeNeed(need);
            }
            
            for (Material material : need.getMaterials()) {
                material.removeNeed(need);
            }
            
            em.remove(need);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<NeedDTO> getPatientNeeds(Long patientId) throws EntityDoesNotExistException{
        try {
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that code.");
            }           
            
            List<Need> needs = (List<Need>) patient.getNeeds();
            
            return needsToDTOs(needs);
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<NeedDTO> getPatientNotNeeds(Long patientId) throws EntityDoesNotExistException{
        try {
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that code.");
            }           
            
            List<Need> allNeeds = (List<Need>) em.createNamedQuery("getAllNeeds").getResultList();
            List<Need> needs = (List<Need>) patient.getNeeds();
            allNeeds.removeAll(needs);
            
            return needsToDTOs(allNeeds);
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void associateNeedToPatient(Long needId, Long patientId) 
            throws EntityDoesNotExistException, NeedAssociatedToPatientException {
        try {
            Need need = em.find(Need.class, needId);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that code.");
            }

            if (patient.getNeeds().contains(need)) {
                throw new NeedAssociatedToPatientException("Need already associated to patient.");
            }
            
            need.addPatient(patient);
            patient.addNeed(need);

        } catch (EntityDoesNotExistException | NeedAssociatedToPatientException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void diassociateNeedFromPatient(Long needId, Long patientId) 
            throws EntityDoesNotExistException, NeedNotAssociatedToPatientException {
        try {
            Need need = em.find(Need.class, needId);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that code.");
            }   
            
            if (!patient.getNeeds().contains(need)) {
                throw new NeedNotAssociatedToPatientException("Need is already diassociated from patient.");
            }
            
            need.removePatient(patient);
            patient.removeNeed(need);

        } catch (EntityDoesNotExistException | NeedNotAssociatedToPatientException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void associateMaterialToNeed(int materialId, Long needId) 
            throws EntityDoesNotExistException, MaterialAssociatedToNeedException {
        try {
            Need need = em.find(Need.class, needId);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            Material material = em.find(Material.class, materialId);
            if (material == null) {
                throw new EntityDoesNotExistException("There is no material with that id.");
            }

            if (need.getMaterials().contains(material)) {
                throw new MaterialAssociatedToNeedException("Material already associated to need.");
            }
            
            need.addMaterial(material);
            material.addNeed(need);

        } catch (EntityDoesNotExistException | MaterialAssociatedToNeedException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void diassociateMaterialFromNeed(int materialId, Long needId) 
            throws EntityDoesNotExistException, MaterialNotAssociatedToNeedException {
        try {
            Need need = em.find(Need.class, needId);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            Material material = em.find(Material.class, materialId);
            if (material == null) {
                throw new EntityDoesNotExistException("There is no material with that id.");
            }    
            
            if (!need.getMaterials().contains(material)) {
                throw new MaterialNotAssociatedToNeedException("Material is already diassociated from need.");
            }
            
            need.removeMaterial(material);
            material.removeNeed(need);

        } catch (EntityDoesNotExistException | MaterialNotAssociatedToNeedException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    public NeedDTO needToDTO(Need need) {
        return new NeedDTO(
                need.getId(),
                need.getDescription());
    }
    
    public List<NeedDTO> needsToDTOs(List<Need> needs) {
        List<NeedDTO> dtos = new ArrayList<>();
        for (Need n : needs) {
            dtos.add(needToDTO(n));
        }
        return dtos;
    }
    
    
}
