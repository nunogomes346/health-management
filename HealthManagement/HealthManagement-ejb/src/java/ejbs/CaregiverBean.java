package ejbs;

import dtos.CaregiverDTO;
import dtos.CounterDTO;
import dtos.EmergencyContactDTO;
import dtos.FaqDTO;
import dtos.MaterialDTO;
import dtos.NeedDTO;
import dtos.PatientDTO;
import dtos.ProceedingDTO;
import dtos.TextDTO;
import dtos.TutorialDTO;
import dtos.VideoDTO;
import entities.Caregiver;
import entities.Counter;
import entities.EmergencyContact;
import entities.FAQ;
import entities.Material;
import entities.MaterialGroup;
import entities.Need;
import entities.Patient;
import entities.Proceeding;
import entities.Text;
import entities.Tutorial;
import entities.Video;
import exceptions.CaregiverPatientsDoesNotHaveNeedException;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistException;
import exceptions.MaterialNotAssociatedToCaregiverException;
import exceptions.MyConstraintViolationException;
import exceptions.PatientNotInPatientsException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/caregivers")
public class CaregiverBean {
    
    @PersistenceContext
    EntityManager em;
    
    @EJB
    private PatientBean patientBean;
    
    @EJB
    private NeedBean needBean;
    
    @EJB
    private EmergencyContactBean emergencyContactBean;
    @EJB
    private FaqBean faqBean;
    @EJB
    private TutorialBean tutorialBean;
    @EJB
    private TextBean textBean;
    @EJB
    private VideoBean videoBean;
    
    @EJB
    private ProceedingBean proceedingBean;
    
    public void create(String username, String password, String name, String mail) 
            throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (em.find(Caregiver.class, username) != null) {
                throw new EntityAlreadyExistsException("A Caregiver with that username already exists.");
            }
            
            Caregiver caregiver = new Caregiver(username, password, name, mail);

            em.persist(caregiver);
            
            Counter counter = new Counter("login", 0, caregiver);

            em.persist(counter);
            
            caregiver.addCounter(counter);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<CaregiverDTO> getAll() {
        try {
            List<Caregiver> caregivers = (List<Caregiver>) em.createNamedQuery("getAllCaregivers").getResultList();
            
            return caregiversToDTOs(caregivers); 
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public CaregiverDTO getCaregiver(String username) throws EntityAlreadyExistsException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (em.find(Caregiver.class, username) != null) {
                throw new EntityAlreadyExistsException("A Caregiver with that username already exists.");
            }
            
            return caregiverToDTO(caregiver);  
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void update(String username, String password, String name, String mail) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            caregiver.setPassword(password);
            caregiver.setName(name);
            caregiver.setMail(mail);
            
            em.merge(caregiver);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void remove(String username) throws EntityDoesNotExistException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            for (Patient patient : caregiver.getPatients()) {
                patient.setCaregiver(null);
            }
            
            for (Material material : caregiver.getMaterials()) {
                material.removeCaregiver(caregiver);
            }
            
            em.remove(caregiver);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public int getLoginCounter(String username) 
            throws EntityDoesNotExistException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            List<Counter> counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .setParameter("resource", "login")
                    .getResultList();
                    
            return counters.get(0).getCounter();
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<CounterDTO> getCaregiverProceedingsPerformedCounter(String username) 
            throws EntityDoesNotExistException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiver")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .getResultList();
            List<Counter> countersProceedingsPerformed = new LinkedList<Counter>();
            for (Counter counter : allCounters) {
                if (counter.getResource().compareTo("login") != 0) {
                    countersProceedingsPerformed.add(counter);
                }
            }
            
            return countersToDTOs(countersProceedingsPerformed);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void rate(String username, String rate) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            caregiver.setRate(rate);
            
            em.merge(caregiver);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<NeedDTO> getCaregiverPatientsNeeds(String username)
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            List<Need> caregiverPatientsNeeds = new ArrayList<Need>();
            
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            for (Patient patient : caregiver.getPatients()) {
                for (Need need : patient.getNeeds()) {
                    if(!caregiverPatientsNeeds.contains(need)) {
                        caregiverPatientsNeeds.add(need);
                    }
                }
            }
            
            return needBean.needsToDTOs(caregiverPatientsNeeds); 
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<MaterialDTO> getCaregiverMaterials(String username) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            List<EmergencyContactDTO> emergencyContacts = new LinkedList<EmergencyContactDTO>();
            List<FaqDTO> faqs = new LinkedList<FaqDTO>();
            List<TextDTO> texts = new LinkedList<TextDTO>();
            List<TutorialDTO> tutorials = new LinkedList<TutorialDTO>();
            List<VideoDTO> videos = new LinkedList<VideoDTO>();
            
            for (Material material : caregiver.getMaterials()) {
                switch (material.getGroup().getGroupName()) {
                    case EmergencyContact:
                        emergencyContacts.add(emergencyContactBean.emergencyContactToDTO((EmergencyContact) material));
                        break;
                    case Faq:
                        faqs.add(faqBean.faqToDTO((FAQ) material));
                        break;
                    case Text:
                        texts.add(textBean.textToDTO((Text) material));
                        break;
                    case Tutorial:
                        tutorials.add(tutorialBean.tutorialToDTO((Tutorial) material));
                        break;
                    case Video:
                        videos.add(videoBean.videoToDTO((Video) material));
                        break;
                }
            }
            
            List<MaterialDTO> materials = new LinkedList<MaterialDTO>();
            materials.addAll(emergencyContacts);
            materials.addAll(faqs);
            materials.addAll(texts);
            materials.addAll(tutorials);
            materials.addAll(videos);
            
            return materials;
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<MaterialDTO> getCaregiverPatientsNeedsMaterial(String username, Long needId) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            Need need = em.find(Need.class, needId);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            List<EmergencyContactDTO> emergencyContacts = new LinkedList<EmergencyContactDTO>();
            List<FaqDTO> faqs = new LinkedList<FaqDTO>();
            List<TextDTO> texts = new LinkedList<TextDTO>();
            List<TutorialDTO> tutorials = new LinkedList<TutorialDTO>();
            List<VideoDTO> videos = new LinkedList<VideoDTO>();
            
            for (Material material : caregiver.getMaterials()) {
                for(Material needMaterial : need.getMaterials()) {
                    if(material == needMaterial) {
                        switch (material.getGroup().getGroupName()) {
                            case EmergencyContact:
                                emergencyContacts.add(emergencyContactBean.emergencyContactToDTO((EmergencyContact) material));
                                break;
                            case Faq:
                                faqs.add(faqBean.faqToDTO((FAQ) material));
                                break;
                            case Text:
                                texts.add(textBean.textToDTO((Text) material));
                                break;
                            case Tutorial:
                                tutorials.add(tutorialBean.tutorialToDTO((Tutorial) material));
                                break;
                            case Video:
                                videos.add(videoBean.videoToDTO((Video) material));
                                break;
                        }
                        
                        break;
                    }
                }
            }
            
            List<MaterialDTO> materials = new LinkedList<MaterialDTO>();
            materials.addAll(emergencyContacts);
            materials.addAll(faqs);
            materials.addAll(texts);
            materials.addAll(tutorials);
            materials.addAll(videos);
            
            return materials;
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void associateMaterial(String username, int materialId, Long needId)
            throws EntityDoesNotExistException, MyConstraintViolationException, CaregiverPatientsDoesNotHaveNeedException {
        try {
            boolean canInsert = true;
            
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            Material material = em.find(Material.class, materialId);
            if (material == null) {
                throw new EntityDoesNotExistException("There is no material with that id.");
            }
            
            Need need = em.find(Need.class, needId);
            if (need == null) {
                throw new EntityDoesNotExistException("There is no need with that id.");
            }
            
            List<Need> allCaregiverPatientsNeeds = new ArrayList<Need>();
            for (Patient patient : caregiver.getPatients()) {
                for (Need patientNeed : patient.getNeeds()) {
                    if(!allCaregiverPatientsNeeds.contains(patientNeed)) {
                        allCaregiverPatientsNeeds.add(patientNeed);
                    }
                }
            }
            if (!allCaregiverPatientsNeeds.contains(need)) {
                throw new CaregiverPatientsDoesNotHaveNeedException("Caregiver patients does not have that need.");
            }
            
            for (Material caregiverMaterial : caregiver.getMaterials()) {
                if(caregiverMaterial == material) {
                    canInsert = false;
                    break;
                }
                canInsert = true;
            }
            
            if(canInsert) {
                caregiver.addMaterial(material);
                material.addCaregiver(caregiver);
                
                Counter counter = new Counter(material.getDescription(), 0, caregiver);

                em.persist(counter);
                
                caregiver.addCounter(counter);
            }
            
            for (Material needMaterial : need.getMaterials()) {
                if(needMaterial == material) {
                    canInsert = false;
                    break;
                }
                canInsert = true;
            }
            
            if(canInsert) {
                material.addNeed(need);
                need.addMaterial(material);
            }
        } catch (EntityDoesNotExistException | CaregiverPatientsDoesNotHaveNeedException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public void diassociateMaterialFromCaregiver(String username, int materialId) 
            throws EntityDoesNotExistException, MyConstraintViolationException, MaterialNotAssociatedToCaregiverException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }   
            
            Material material = em.find(Material.class, materialId);
            if (caregiver == null) {
                throw new EntityDoesNotExistException("There is no material with that id.");
            }  
            
            if (!caregiver.getMaterials().contains(material)) {
                throw new MaterialNotAssociatedToCaregiverException("Material is already diassociated from caregiver.");
            }
            
            material.removeCaregiver(caregiver);
            caregiver.removeMaterial(material);
            
            List <Proceeding> proceedings = new LinkedList<Proceeding>(caregiver.getProceedings());
            for (Proceeding proceeding : proceedings) {
                if (proceeding.getMaterial() == material) {
                    proceeding.getCaregiver().removeProceeding(proceeding);
                    proceeding.getNeed().removeProceeding(proceeding);
                    proceeding.getPatient().removeProceeding(proceeding);
                    proceeding.getMaterial().removeProceeding(proceeding);
                    em.remove(proceeding);
                }
            }
            
            List<Counter> counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .setParameter("resource", material.getDescription())
                    .getResultList();

            em.remove(counters.get(0));
        } catch (EntityDoesNotExistException | MaterialNotAssociatedToCaregiverException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch(EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/login")
    public void caregiverLoginREST(@PathParam("username") String username) throws EntityDoesNotExistException{
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }            
            
            List<Counter> counters = (List<Counter>) em.createNamedQuery("getAllCountersCaregiverResource")
                    .setParameter("caregiverUsername", caregiver.getUsername())
                    .setParameter("resource", "login")
                    .getResultList();
            
            counters.get(0).incrementCounter();

            em.merge(counters.get(0));
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/patients")
    public List<PatientDTO> getCaregiverPatientsREST(@PathParam("username") String username) throws EntityDoesNotExistException{
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }
            
            List<Patient> patients = (List<Patient>) caregiver.getPatients();
            
            return patientBean.patientsToDTOs(patients);
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/patients/{patientId}/needs")
    public List<NeedDTO> getPatientsNeedsREST (@PathParam("username") String username, @PathParam("patientId") Long patientId) 
            throws EntityDoesNotExistException, PatientNotInPatientsException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }  
            
            Patient patient = em.find(Patient.class, patientId);
            if(patient == null){
                throw new EntityDoesNotExistException("There is no Patient with that id.");
            }
            
            if (!caregiver.getPatients().contains(patient)) {
                throw new PatientNotInPatientsException("Patient does not exist on caregiver patients list.");
            }
            
            List<Need> needs = (List<Need>) patient.getNeeds();
            
            return needBean.needsToDTOs(needs);
        }catch (EntityDoesNotExistException | PatientNotInPatientsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/needs/{id}/materials")
    public List<MaterialDTO> getCaregiverMaterialsREST(@PathParam("username") String username, @PathParam("id") Long needId) 
            throws EntityDoesNotExistException{
        try {
            Need need = em.find(Need.class, needId);
            if(need == null){
                throw new EntityDoesNotExistException("There is no need with that id.");
            } 
            
            List<MaterialDTO> materials = new ArrayList<MaterialDTO>();
            List<MaterialDTO> caregiverMaterials = getCaregiverMaterials(username);
            List<Material> needMaterials = need.getMaterials();
            
            for (MaterialDTO caregiverMaterial : caregiverMaterials) {
                for (Material needMaterial : needMaterials) {
                    if(caregiverMaterial.getId() == needMaterial.getId()) {
                        materials.add(caregiverMaterial);
                        break;
                    }
                }
            }
            
            return materials;
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/needs/{id}/emergencyContacts")
    public List<EmergencyContactDTO> getEmergencyContactsREST (@PathParam("username") String username, @PathParam("id") Long id)  throws EntityDoesNotExistException {
        try{
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }   
            
            Need need = em.find(Need.class, id);
            if(need == null){
                throw new EntityDoesNotExistException("There is no Need with that id.");
            }
            
            List<Material> caregiverMaterials = caregiver.getMaterials();
            List<Material> needMaterials = need.getMaterials();
            List<EmergencyContact> emergencyContacts = new ArrayList<>();
            
            for (Material caregiverMaterial : caregiverMaterials) {
                for(Material needMaterial : needMaterials){
                    if(caregiverMaterial == needMaterial &&
                        caregiverMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.EmergencyContact &&
                        needMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.EmergencyContact){
                        emergencyContacts.add((EmergencyContact) caregiverMaterial);
                        break;
                    }
                }
            }
            
            return emergencyContactBean.emergencyContactsToDTOs(emergencyContacts);
        } catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/needs/{id}/faqs")
    public List<FaqDTO> getFaqsREST (@PathParam("username") String username, @PathParam("id") Long id)  throws EntityDoesNotExistException {
        try{
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }  
            
            Need need = em.find(Need.class, id);
            if(need == null){
                throw new EntityDoesNotExistException("There is no Need with that id.");
            }
            
            List<Material> caregiverMaterials = caregiver.getMaterials();
            List<Material> needMaterials = need.getMaterials();
            List<FAQ> faqs = new ArrayList<>();
            
            for (Material caregiverMaterial : caregiverMaterials) {
                for(Material needMaterial : needMaterials){
                    if(caregiverMaterial == needMaterial &&
                        caregiverMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Faq &&
                        needMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Faq){
                        faqs.add((FAQ) caregiverMaterial);
                        break;
                    }
                }
            } 
            
            return faqBean.faqsToDTOs(faqs);
        }catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/needs/{id}/texts")
    public List<TextDTO> getTextsREST (@PathParam("username") String username, @PathParam("id") Long id)  throws EntityDoesNotExistException {
        try{
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }  
            
            Need need = em.find(Need.class, id);
            if(need == null){
                throw new EntityDoesNotExistException("There is no Need with that id.");
            }
            
            List<Material> caregiverMaterials = caregiver.getMaterials();
            List<Material> needMaterials = need.getMaterials();
            List<Text> texts = new ArrayList<>();
            
            for (Material caregiverMaterial : caregiverMaterials) {
                for(Material needMaterial : needMaterials){
                    if(caregiverMaterial == needMaterial &&
                        caregiverMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Text &&
                        needMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Text){
                        texts.add((Text) caregiverMaterial);
                        break;
                    }
                }
            }
            
            return textBean.textsToDTOs(texts);
        }catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/needs/{id}/tutorials")
    public List<TutorialDTO> getTutorialsREST (@PathParam("username") String username, @PathParam("id") Long id)  throws EntityDoesNotExistException {
        try{
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }  
            
            Need need = em.find(Need.class, id);
            if(need == null){
                throw new EntityDoesNotExistException("There is no Need with that id.");
            }
            
            List<Material> caregiverMaterials = caregiver.getMaterials();
            List<Material> needMaterials = need.getMaterials();
            List<Tutorial> tutorials = new ArrayList<>();
            
            for (Material caregiverMaterial : caregiverMaterials) {
                for(Material needMaterial : needMaterials){
                    if(caregiverMaterial == needMaterial &&
                        caregiverMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Tutorial &&
                        needMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Tutorial){
                        tutorials.add((Tutorial) caregiverMaterial);
                        break;
                    }
                }
            }
            
            return tutorialBean.tutorialsToDTOs(tutorials);
        }catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/needs/{id}/videos")
    public List<VideoDTO> getVideosREST (@PathParam("username") String username, @PathParam("id") Long id)  throws EntityDoesNotExistException {
        try{
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }  
            
            Need need = em.find(Need.class, id);
            if(need == null){
                throw new EntityDoesNotExistException("There is no Need with that id.");
            }
            
            List<Material> caregiverMaterials = caregiver.getMaterials();
            List<Material> needMaterials = need.getMaterials();
            List<Video> videos = new ArrayList<>();
            
            for (Material caregiverMaterial : caregiverMaterials) {
                for(Material needMaterial : needMaterials){
                    if(caregiverMaterial == needMaterial &&
                        caregiverMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Video &&
                        needMaterial.getGroup().getGroupName() == MaterialGroup.GROUP.Video){
                        videos.add((Video) caregiverMaterial);
                        break;
                    }
                }
            }
            
            return videoBean.videosToDTOs(videos);
        }catch (EntityDoesNotExistException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/patients/{patientId}/proceedings")
    public List<ProceedingDTO> getPatientsProceedingsREST(@PathParam("username") String username, @PathParam("patientId") Long patientId) 
            throws EntityDoesNotExistException, PatientNotInPatientsException {
        try {
            Caregiver caregiver = em.find(Caregiver.class, username);
            if(caregiver == null){
                throw new EntityDoesNotExistException("There is no caregiver with that username.");
            }  
            
            Patient patient = em.find(Patient.class, patientId);
            if(patient == null){
                throw new EntityDoesNotExistException("There is no patient with that id.");
            }
            
            if (!caregiver.getPatients().contains(patient)) {
                throw new PatientNotInPatientsException("Patient does not exist on caregiver patients list.");
            }
            
            List<Proceeding> proceedings = (List<Proceeding>) patient.getProceedings();
            
            return proceedingBean.proceedingsToDTOs(proceedings);
        } catch (EntityDoesNotExistException | PatientNotInPatientsException e) {
            throw e;             
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    //Build DTOs
    CaregiverDTO caregiverToDTO(Caregiver caregiver) {
        return new CaregiverDTO(
                caregiver.getUsername(),
                null,
                caregiver.getName(),
                caregiver.getMail(),
                caregiver.getRate());
    }
    
    List<CaregiverDTO> caregiversToDTOs(List<Caregiver> caregivers) {
        List<CaregiverDTO> dtos = new ArrayList<>();
        for (Caregiver c : caregivers) {
            dtos.add(caregiverToDTO(c));
        }
        return dtos;
    }
    
    CounterDTO counterToDTO(Counter counter) {
        return new CounterDTO(
                counter.getId(),
                counter.getResource(),
                counter.getCounter(),
                counter.getCaregiver().getUsername());
    }
    
    List<CounterDTO> countersToDTOs(List<Counter> counters) {
        List<CounterDTO> dtos = new ArrayList<>();
        for (Counter c : counters) {
            dtos.add(counterToDTO(c));
        }
        return dtos;
    }
}
