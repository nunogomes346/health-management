package ejbs;

import dtos.NeedDTO;
import dtos.PatientDTO;
import entities.Caregiver;
import entities.Need;
import entities.Patient;
import exceptions.CaregiverAssociatedException;
import exceptions.CaregiverDiassociatedException;
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
public class PatientBean {
    
    @PersistenceContext
    EntityManager em;
    
    public void create(String name, String mail) 
            throws MyConstraintViolationException{
        try {     
            Patient patient = new Patient(name, mail);

            em.persist(patient);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(Long id, String name, String mail)
             throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Patient patient = em.find(Patient.class, id);
            if (patient == null) {
                 throw new EntityDoesNotExistException("There is no patient with that id.");
            }
            
            patient.setName(name);
            patient.setMail(mail);
            
            em.merge(patient);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<PatientDTO> getAll() {
        try {
            List<Patient> patients = (List<Patient>) em.createNamedQuery("getAllPatients").getResultList();
            
            return patientsToDTOs(patients); 
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public PatientDTO getPatient(Long id) throws EntityDoesNotExistException {
        try {
            Patient patient = em.find(Patient.class, id);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that id.");
            }
            
            return patientToDTO(patient);  
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(Long id) throws EntityDoesNotExistException {
        try {
            Patient patient = em.find(Patient.class, id);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that id.");
            }
            
            if (patient.getCaregiver() != null) {
                patient.getCaregiver().removePatient(patient);
            }
            
            for (Need need : patient.getNeeds()) {
                need.removePatient(patient);
            }
            
            em.remove(patient);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void associatePatientToCaregiver(Long patientId, String username) 
            throws EntityDoesNotExistException, CaregiverAssociatedException {
        try {
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that code.");
            }
            
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            if (patient.getCaregiver() != null) {
                throw new CaregiverAssociatedException("Patient already associated with another caregiver.");
            }

            patient.setCaregiver(caregiver);
            caregiver.addPatient(patient);

        } catch (EntityDoesNotExistException | CaregiverAssociatedException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void diassociatePatientFromCaregiver(Long patientId, String username) 
            throws EntityDoesNotExistException, CaregiverDiassociatedException {
        try {
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new EntityDoesNotExistException("There is no patient with that code.");
            }
            
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }

            if (patient.getCaregiver() == null) {
                throw new CaregiverDiassociatedException("Patient already diassociated from a caregiver.");
            }         
            
            patient.setCaregiver(null);
            caregiver.removePatient(patient);

        } catch (EntityDoesNotExistException | CaregiverDiassociatedException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<PatientDTO> getCaregiverPatients(String username) throws EntityDoesNotExistException{
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }            
            
            List<Patient> patients = (List<Patient>) caregiver.getPatients();
            
            return patientsToDTOs(patients);
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<PatientDTO> getCaregiverNotPatients(String username, List<PatientDTO> allPatients) throws EntityDoesNotExistException{
        try {
            List<PatientDTO> diassociatedPatients = new ArrayList<PatientDTO>();
            
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }        
            
            for (PatientDTO patient : allPatients) {
                if (patient.getCaregiverUsername() == null) {
                    diassociatedPatients.add(patient);
                }
            }
            
            return diassociatedPatients;
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    public PatientDTO patientToDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getMail(),
                (patient.getCaregiver() == null) ? null : patient.getCaregiver().getUsername());
    }
    
    
    public List<PatientDTO> patientsToDTOs(List<Patient> patients) {
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(patientToDTO(p));
        }
        return dtos;
    }
}
