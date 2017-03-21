package ejbs;

import dtos.ProceedingDTO;
import entities.Caregiver;
import entities.Counter;
import entities.Material;
import entities.Need;
import entities.Patient;
import entities.Proceeding;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/proceedings")
public class ProceedingBean {
    @PersistenceContext
    EntityManager em;
    
    public void create(int materialID, Long patientID, Long needID, String caregiverUsername, String date, String note)
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Material material = em.find(Material.class, materialID);
            if (material == null) {
                throw new EntityDoesNotExistException("There is no material with that id.");
            }
            
            Patient patient = em.find(Patient.class, patientID);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that id.");
            }
            
            Need need = em.find(Need.class, needID);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            Caregiver caregiver = em.find(Caregiver.class, caregiverUsername);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            Proceeding proceeding  = new Proceeding(material, patient, need, caregiver, date, note);
            
            em.persist(proceeding);
            
            material.addProceeding(proceeding);
            patient.addProceeding(proceeding);
            need.addProceeding(proceeding);
            caregiver.addProceeding(proceeding);
            
            List<Counter> counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .setParameter("resource", material.getDescription())
                    .getResultList();
            
            counters.get(0).incrementCounter();

            em.merge(counters.get(0));
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(Long proceedingID, String caregiverUsername, int materialID, String note)
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {          
            Proceeding proceeding = em.find(Proceeding.class, proceedingID);
            if (proceeding == null) {
                throw new EntityDoesNotExistException("There is no proceeding with that id.");
            }
            
            Material material = em.find(Material.class, materialID);
            if (material == null) {
                throw new EntityDoesNotExistException("There is no material with that id.");
            }
            
            Caregiver caregiver = em.find(Caregiver.class, caregiverUsername);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            proceeding.getMaterial().removeProceeding(proceeding);
            List<Counter> counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .setParameter("resource", proceeding.getMaterial().getDescription())
                    .getResultList();
            counters.get(0).decrementCounter();
            em.merge(counters.get(0));
            
            proceeding.setMaterial(material);
            proceeding.setNote(note);
            
            em.persist(proceeding);
            
            material.addProceeding(proceeding);
            counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .setParameter("resource", material.getDescription())
                    .getResultList();
            counters.get(0).incrementCounter();
            em.merge(counters.get(0));
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<ProceedingDTO> getAll() {
        try {
            List<Proceeding> proceedings = (List<Proceeding>) em.createNamedQuery("getAllProceeding").getResultList();
            
            return proceedingsToDTOs(proceedings); 
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public ProceedingDTO getProceeding(Long id) throws EntityDoesNotExistException {
        try {
            Proceeding proceeding = em.find(Proceeding.class, id);
            if (proceeding == null) {
                throw new EntityDoesNotExistException("There is no prooceding with that id.");
            }
            
            return proceedingToDTO(proceeding);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(Long id) throws EntityDoesNotExistException {
        try {
            Proceeding proceeding = em.find(Proceeding.class, id);
            if (proceeding == null) {
                throw new EntityDoesNotExistException("There is no procedure with that id.");
            }
            
            proceeding.getCaregiver().removeProceeding(proceeding);
            proceeding.getMaterial().removeProceeding(proceeding);
            proceeding.getPatient().removeProceeding(proceeding);
            proceeding.getNeed().removeProceeding(proceeding);
            
            List<Counter> counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", proceeding.getCaregiver().getUsername())
                    .setParameter("resource", proceeding.getMaterial().getDescription())
                    .getResultList();
            counters.get(0).decrementCounter();
            em.merge(counters.get(0));
            
            em.remove(proceeding);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("create")
    public void createProceedingREST(ProceedingDTO p) throws EntityDoesNotExistException {
        try {
            create(p.getMaterialID(), p.getPatientID(), p.getNeedID(), p.getCaregiverUsername(), p.getProceedingDate(), p.getNote());
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("update")
    public void updateProceedingREST(ProceedingDTO p) throws EntityDoesNotExistException, MyConstraintViolationException{
        try {
            update(p.getProceedingId(),p.getCaregiverUsername(), p.getMaterialID(), p.getNote());
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @DELETE
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{proceedingId}")
    public void removeProceedingREST(@PathParam("proceedingId") Long proceedingId) throws EntityDoesNotExistException {
        try {
            remove(proceedingId);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    public ProceedingDTO proceedingToDTO(Proceeding proceeding) {
        return new ProceedingDTO(
                proceeding.getId(),
                proceeding.getMaterial().getId(),
                proceeding.getMaterial().getDescription(),
                proceeding.getPatient().getId(),
                proceeding.getPatient().getName(),
                proceeding.getNeed().getId(),
                proceeding.getNeed().getDescription(),
                proceeding.getCaregiver().getUsername(),
                proceeding.getProceedingDate(),
                proceeding.getNote()
        );
    }
    
    public List<ProceedingDTO> proceedingsToDTOs(List<Proceeding> proceedings) {
        List<ProceedingDTO> dtos = new ArrayList<>();
        for (Proceeding p : proceedings) {
            dtos.add(proceedingToDTO(p));
        }
        return dtos;
    }
}
