package web;

import dtos.EmergencyContactDTO;
import dtos.FaqDTO;
import dtos.MaterialDTO;
import dtos.NeedDTO;
import dtos.PatientDTO;
import dtos.ProceedingDTO;
import dtos.TextDTO;
import dtos.TutorialDTO;
import dtos.VideoDTO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

@ManagedBean
@Named(value="caregiverManager")
@SessionScoped
public class CaregiverManager implements Serializable {

    @Inject
    private UserManager userManager;

    private PatientDTO currentPatient;
    private List<PatientDTO> filteredPatients;
    
    private NeedDTO currentNeed;

    private EmergencyContactDTO currentEmergencyContact;
    private FaqDTO currentFaq;
    private TutorialDTO currentTutorial;
    private TextDTO currentText;
    private VideoDTO currentVideo;
    
    private ProceedingDTO newProceeding;
    private ProceedingDTO currentProceeding;
    private List<ProceedingDTO> filteredProceedings;
    
    private int materialId;

    private HttpAuthenticationFeature feature;
    private Client client;
    private final String baseUri = "http://localhost:8080/HealthManagement-war/webapi";
    private UIComponent component;
    private static final Logger LOGGER = Logger.getLogger("web.CaregiverManager");
    
    public CaregiverManager() {
        this.newProceeding = new ProceedingDTO();
        client = ClientBuilder.newClient();        
        feature = null;
    }
    
    @PostConstruct
    public void initClient() {
        feature = HttpAuthenticationFeature.basic(userManager.getUsername(), userManager.getPassword());
        client.register(feature);
    }
    
    // ***************************************
    // ************ CAREGIVER ****************
    // *************************************** 
    public List<PatientDTO> getCaregiversPatientsREST() {
        List<PatientDTO> returnedPatients = null;
        try {
            returnedPatients = client.target(baseUri)
                .path("/caregivers/{username}/patients")
                .resolveTemplate("username", userManager.getUsername())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<PatientDTO>>() {});
        } catch (Exception e){
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }
        return returnedPatients;
    }
    
    public List<NeedDTO> getCaregiverPatientsNeedsREST() {
        List<NeedDTO> returnedNeeds = null;
        try {
            returnedNeeds = client.target(baseUri)
                    .path("/caregivers/{username}/patients/{patientId}/needs")
                    .resolveTemplate("username", userManager.getUsername())
                    .resolveTemplate("patientId", currentPatient.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<NeedDTO>>() {});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }
        return returnedNeeds;
    }
    
    public List<EmergencyContactDTO> getNeedEmergencyContactsREST() {
        List<EmergencyContactDTO> returnedEmergencyContacts = null;
        try {
            returnedEmergencyContacts = client.target(baseUri)
                    .path("/caregivers/{username}/needs/{id}/emergencyContacts")
                    .resolveTemplate("username", userManager.getUsername())
                    .resolveTemplate("id", currentNeed.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<EmergencyContactDTO>>() {});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }    
        return returnedEmergencyContacts;
    }
    
    public List<FaqDTO> getNeedFaqsREST() {
        List<FaqDTO> returnedFaqs = null;
        try {
            returnedFaqs = client.target(baseUri)
                    .path("/caregivers/{username}/needs/{id}/faqs")
                    .resolveTemplate("username", userManager.getUsername())
                    .resolveTemplate("id", currentNeed.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<FaqDTO>>() {});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }    
        return returnedFaqs;
    }
    
    public List<TextDTO> getNeedTextsREST() {
        List<TextDTO> returnedTexts = null;
        try {
            returnedTexts = client.target(baseUri)
                    .path("/caregivers/{username}/needs/{id}/texts")
                    .resolveTemplate("username", userManager.getUsername())
                    .resolveTemplate("id", currentNeed.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<TextDTO>>() {});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }    
        return returnedTexts;
    }
    
    public List<TutorialDTO> getNeedTutorialsREST() {
        List<TutorialDTO> returnedTutorials = null;
        try {
            returnedTutorials = client.target(baseUri)
                    .path("/caregivers/{username}/needs/{id}/tutorials")
                    .resolveTemplate("username", userManager.getUsername())
                    .resolveTemplate("id", currentNeed.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<TutorialDTO>>() {});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }    
        return returnedTutorials;
    }
    
    public List<VideoDTO> getNeedVideosREST() {
        List<VideoDTO> returnedVideos = null;
        try {
            returnedVideos = client.target(baseUri)
                    .path("/caregivers/{username}/needs/{id}/videos")
                    .resolveTemplate("username", userManager.getUsername())
                    .resolveTemplate("id", currentNeed.getId())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<VideoDTO>>() {});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }    
        return returnedVideos;
    }
    
    public void createProceedingREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("materialId");
            int id = Integer.parseInt(param.getValue().toString());
            
            newProceeding.setCaregiverUsername(userManager.getUsername());
            newProceeding.setPatientID(currentPatient.getId());
            newProceeding.setNeedID(currentNeed.getId());
            newProceeding.setMaterialID(id);
            
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            newProceeding.setProceedingDate(dateFormat.format(date));
        
            client.target(baseUri)
                .path("/proceedings/create")
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(newProceeding));
            
            setFilteredProceedings(null);
            
            newProceeding.reset();
        } catch (Exception e){
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }
    }
    
    public String updateProceedingREST() {
        try {
            client.target(baseUri)
                    .path("/proceedings/update")
                    .request(MediaType.APPLICATION_XML).put(Entity.xml(currentProceeding));
            
            setFilteredProceedings(null);
            
            return "caregiver_patient_proceedings?faces-redirect=true";
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }
        return "caregiver_patient_proceeding_update?faces-redirect=true";
    }

    public List<ProceedingDTO> getPatientsProceedingsREST() {
        List<ProceedingDTO> returnedProceeding = null;
        try {
            returnedProceeding = client.target(baseUri)
                .path("/caregivers/{username}/patients/{patientId}/proceedings")
                .resolveTemplate("username", userManager.getUsername())
                .resolveTemplate("patientId", currentPatient.getId())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ProceedingDTO>>() {});
        } catch (Exception e){
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }
        return returnedProceeding;
    }
    
    public void removeProceedingREST(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteProceedingId");
            Long proceedingId = Long.parseLong(param.getValue().toString());
            
            client.target(baseUri)
                    .path("/proceedings/{proceedingId}")
                    .resolveTemplate("proceedingId", proceedingId)
                    .request().delete();

        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }
    }
    
    public List<MaterialDTO> getAllMaterialsREST() {
        List<MaterialDTO> returnedMaterials = null;
        try {
            returnedMaterials = client.target(baseUri)
                .path("/caregivers/{username}/needs/{id}/materials")
                .resolveTemplate("username", userManager.getUsername())
                .resolveTemplate("id", currentProceeding.getNeedID())
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<MaterialDTO>>() {});
            
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", LOGGER);
        }    
        return returnedMaterials;
    }
        
    // **********************************
    // ************ GETS&SETS ***********
    // **********************************
    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public PatientDTO getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(PatientDTO currentPatient) {
        this.currentPatient = currentPatient;
    }

    public NeedDTO getCurrentNeed() {
        return currentNeed;
    }

    public void setCurrentNeed(NeedDTO currentNeed) {
        this.currentNeed = currentNeed;
    }

    public EmergencyContactDTO getCurrentEmergencyContact() {
        return currentEmergencyContact;
    }

    public void setCurrentEmergencyContact(EmergencyContactDTO currentEmergencyContact) {
        this.currentEmergencyContact = currentEmergencyContact;
    }

    public FaqDTO getCurrentFaq() {
        return currentFaq;
    }

    public void setCurrentFaq(FaqDTO currentFaq) {
        this.currentFaq = currentFaq;
    }

    public TutorialDTO getCurrentTutorial() {
        return currentTutorial;
    }

    public void setCurrentTutorial(TutorialDTO currentTutorial) {
        this.currentTutorial = currentTutorial;
    }

    public TextDTO getCurrentText() {
        return currentText;
    }

    public void setCurrentText(TextDTO currentText) {
        this.currentText = currentText;
    }

    public VideoDTO getCurrentVideo() {
        return currentVideo;
    }

    public void setCurrentVideo(VideoDTO currentVideo) {
        this.currentVideo = currentVideo;
    }
    
    public ProceedingDTO getNewProceeding() {
        return newProceeding;
    }

    public void setNewProceeding(ProceedingDTO newProceeding) {
        this.newProceeding = newProceeding;
    }

    public ProceedingDTO getCurrentProceeding() {
        return currentProceeding;
    }

    public void setCurrentProceeding(ProceedingDTO currentProceeding) {
        this.currentProceeding = currentProceeding;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public List<PatientDTO> getFilteredPatients() {
        return filteredPatients;
    }

    public void setFilteredPatients(List<PatientDTO> filteredPatients) {
        this.filteredPatients = filteredPatients;
    }

    public List<ProceedingDTO> getFilteredProceedings() {
        return filteredProceedings;
    }

    public void setFilteredProceedings(List<ProceedingDTO> filteredProceedings) {
        this.filteredProceedings = filteredProceedings;
    }
    
    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }
}
