package web;

import dtos.AdministratorDTO;
import dtos.HealthcareProDTO;
import dtos.CaregiverDTO;
import dtos.EmergencyContactDTO;
import dtos.FaqDTO;
import dtos.MaterialDTO;
import dtos.TutorialDTO;
import dtos.TextDTO;
import dtos.VideoDTO;
import ejbs.AdministratorBean;
import ejbs.HealthcareProBean;
import ejbs.CaregiverBean;
import ejbs.EmergencyContactBean;
import ejbs.FaqBean;
import ejbs.TutorialBean;
import ejbs.TextBean;
import ejbs.VideoBean;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistException;
import exceptions.MyConstraintViolationException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@ManagedBean
@Named(value = "administratorManager")
@SessionScoped
public class AdministratorManager implements Serializable {

    @Inject
    private UserManager userManager;
    
    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private HealthcareProBean healthcareProBean;
	
    @EJB
    private CaregiverBean caregiverBean;

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
	
    private AdministratorDTO newAdministrator;
    private AdministratorDTO currentAdministrator;
    private HealthcareProDTO newHealthcarePro;
    private HealthcareProDTO currentHealthcarePro;
    private CaregiverDTO newCaregiver;
    private CaregiverDTO currentCaregiver;

    private EmergencyContactDTO newEmergencyContact;
    private EmergencyContactDTO currentEmergencyContact;
    private FaqDTO newFaq;
    private FaqDTO currentFaq;
    private TutorialDTO newTutorial;
    private TutorialDTO currentTutorial;
    private TextDTO newText;
    private TextDTO currentText;
    private VideoDTO newVideo;
    private VideoDTO currentVideo;
    
    private List<AdministratorDTO> filteredAdmins;
    private List<HealthcareProDTO> filteredHealthcarePros;
    private List<CaregiverDTO> filteredCaregivers;
    private List<MaterialDTO> filteredMaterials;
    
    private UIComponent component;
    private static final Logger LOGGER = Logger.getLogger("web.AdministratorManager");

    public AdministratorManager() {
        newAdministrator = new AdministratorDTO();
        newHealthcarePro = new HealthcareProDTO();
        newCaregiver = new CaregiverDTO();
        newEmergencyContact = new EmergencyContactDTO();
        newFaq = new FaqDTO();
        newTutorial = new TutorialDTO();
        newText = new TextDTO();
        newVideo = new VideoDTO();
    }

    // ***************************************
    // ************ ADMINISTRATOR ************
    // ***************************************
    public String createAdministrator() {
        try {
            administratorBean.create(
                    newAdministrator.getUsername(),
                    newAdministrator.getPassword(),
                    newAdministrator.getName(),
                    newAdministrator.getMail());
            
            setFilteredAdmins(null);
            
            newAdministrator.reset();
            
            return "admin_index?faces-redirect=true";
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_administrator_create?faces-redirect=true";
    }

    public List<AdministratorDTO> getAllAdministrators() {
        try {
            return administratorBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }
    
    public String updateAdministrator() {
        try {
            administratorBean.update(
                    currentAdministrator.getUsername(),
                    currentAdministrator.getPassword(),
                    currentAdministrator.getName(),
                    currentAdministrator.getMail());

            setFilteredAdmins(null);
            
            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_administrator_update?faces-redirect=true";
    }

    public void removeAdministrator(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteAdministratorId");
            String id = param.getValue().toString();

            administratorBean.remove(id);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }

    // ***************************************
    // ************ HEALTHCAREPRO ************
    // ***************************************
    public String createHealthcarePro() {
        try {
            healthcareProBean.create(
                    newHealthcarePro.getUsername(),
                    newHealthcarePro.getPassword(),
                    newHealthcarePro.getName(),
                    newHealthcarePro.getMail(),
                    newHealthcarePro.getFacility(),
                    newHealthcarePro.getJob());

            setFilteredHealthcarePros(null);
            
            newHealthcarePro.reset();

            return "admin_index?faces-redirect=true";
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_healthcarepro_create?faces-redirect=true";
    }

    public List<HealthcareProDTO> getAllHealthcarePros() {
        try {
            return healthcareProBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }

    public String updateHealthcarePro() {
        try {
            healthcareProBean.update(
                    currentHealthcarePro.getUsername(),
                    currentHealthcarePro.getPassword(),
                    currentHealthcarePro.getName(),
                    currentHealthcarePro.getMail(),
                    currentHealthcarePro.getFacility(),
                    currentHealthcarePro.getJob());
            
            setFilteredHealthcarePros(null);

            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_healthcarepro_update?faces-redirect=true";
    }

    public void removeHealthcarePro(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteHealthcareProId");
            String id = param.getValue().toString();

            healthcareProBean.remove(id);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    } 
    
    // ***************************************
    // ************ CAREGIVER ****************
    // ***************************************
    public String createCaregiver() {
        try {
            caregiverBean.create(
                    newCaregiver.getUsername(),
                    newCaregiver.getPassword(),
                    newCaregiver.getName(),
                    newCaregiver.getMail());
            
            setFilteredCaregivers(null);

            newCaregiver.reset();

            return "admin_index?faces-redirect=true";
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_caregiver_create?faces-redirect=true";
    }
    
    public List<CaregiverDTO> getAllCaregivers() {
        try {
            return caregiverBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }
    
    public String updateCaregiver() {
        try {
            caregiverBean.update(
                    currentCaregiver.getUsername(),
                    currentCaregiver.getPassword(),
                    currentCaregiver.getName(), 
                    currentCaregiver.getMail());
            
            setFilteredCaregivers(null);

            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_caregiver_update?faces-redirect=true";
    }
    
    public void removeCaregiver(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteCaregiverId");
            String id = param.getValue().toString();

            caregiverBean.remove(id);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }
    
    public boolean disableRemoveButton(String username) {
        return (username.compareTo(userManager.getUsername()) == 0);
    }
    
    // ***************************************
    // ************ MATERIALS ****************
    // ***************************************
    public List<MaterialDTO> getAllMaterials() {
        try {
            List<MaterialDTO> materials = new LinkedList<MaterialDTO>();
            
            materials.addAll(emergencyContactBean.getAll());
            materials.addAll(faqBean.getAll());
            materials.addAll(textBean.getAll());
            materials.addAll(tutorialBean.getAll());
            materials.addAll(videoBean.getAll());
            
            return materials;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }
    
    public String detailsView(MaterialDTO material) {
        switch(material.getType()) {
            case "EmergencyContact": 
                setCurrentEmergencyContact((EmergencyContactDTO) material);
                return "admin_emergencyContact_details?faces-redirect=true";
            case "Faq": 
                setCurrentFaq((FaqDTO) material);
                return "admin_faq_details?faces-redirect=true";
            case "Text": 
                setCurrentText((TextDTO) material);
                return "admin_text_details?faces-redirect=true";
            case "Tutorial": 
                setCurrentTutorial((TutorialDTO) material);
                return "admin_tutorial_details?faces-redirect=true";
            case "Video": 
                setCurrentVideo((VideoDTO) material);
                return "admin_video_details?faces-redirect=true";
        }
        
        return null;
    }
    
    public String updateView(MaterialDTO material) {
        switch(material.getType()) {
            case "EmergencyContact": 
                setCurrentEmergencyContact((EmergencyContactDTO) material);
                return "admin_emergencyContact_update?faces-redirect=true";
            case "Faq": 
                setCurrentFaq((FaqDTO) material);
                return "admin_faq_update?faces-redirect=true";
            case "Text": 
                setCurrentText((TextDTO) material);
                return "admin_text_update?faces-redirect=true";
            case "Tutorial": 
                setCurrentTutorial((TutorialDTO) material);
                return "admin_tutorial_update?faces-redirect=true";
            case "Video": 
                setCurrentVideo((VideoDTO) material);
                return "admin_video_update?faces-redirect=true";
        }
        
        return null;
    }
    
    public void removeMaterial(MaterialDTO material) {
        switch(material.getType()) {
            case "EmergencyContact": 
                removeEmergencyContact(material.getId());
                break;
            case "Faq": 
                removeFaq(material.getId());
                break;
            case "Text": 
                removeText(material.getId());
                break;
            case "Tutorial": 
                removeTutorial(material.getId());
                break;
            case "Video": 
                removeVideo(material.getId());
                break;
        }
    }
	
    // ***********************************************
    // ************ EMERGENCY CONTACT ****************
    // ***********************************************
    public String createEmergencyContact() {
        try {
            emergencyContactBean.create(
                    newEmergencyContact.getDescription(),
                    newEmergencyContact.getName(),
                    newEmergencyContact.getTelephoneNumber());

            setFilteredMaterials(null);
            
            newEmergencyContact.reset();

            return "admin_index?faces-redirect=true";
        } catch (MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_emergencyContact_create?faces-redirect=true";
    }

    public List<EmergencyContactDTO> getAllEmergencyContacts() {
        try {
            return emergencyContactBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }

    public String updateEmergengyContact() {
        try {

            emergencyContactBean.update(
                    currentEmergencyContact.getId(),
                    currentEmergencyContact.getDescription(),
                    currentEmergencyContact.getName(),
                    currentEmergencyContact.getTelephoneNumber()
            );
            
            setFilteredMaterials(null);

            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_emergencyContact_update?faces-redirect=true";
    }

    public void removeEmergencyContact(int emergencyContactId) {
        try {
            emergencyContactBean.remove(emergencyContactId);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }

    // *********************************
    // ************ FAQ ****************
    // *********************************
    public String createFaq() {
        try {
            faqBean.create(
                    newFaq.getDescription(),
                    newFaq.getQuestion(),
                    newFaq.getAnswer()
            );
            
            setFilteredMaterials(null);

            newFaq.reset();

            return "admin_index?faces-redirect=true";
        } catch (MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_faq_create?faces-redirect=true";
    }

    public List<FaqDTO> getAllFaqs() {
        try {
            return faqBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }

    public String updateFaq() {
        try {
            faqBean.update(
                    currentFaq.getId(),
                    currentFaq.getDescription(),
                    currentFaq.getQuestion(),
                    currentFaq.getAnswer());
            
            setFilteredMaterials(null);

            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_faq_update?faces-redirect=true";
    }

    public void removeFaq(int faqId) {
        try {
            faqBean.remove(faqId);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }

    // **************************************
    // ************ TUTORIAL ****************
    // **************************************
    public String createTutorial() {
        try {
            tutorialBean.create(newTutorial.getDescription(), newTutorial.getText());

            setFilteredMaterials(null);
            
            newTutorial.reset();

            return "admin_index?faces-redirect=true";
        } catch (MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_tutorials_create?faces-redirect=true";
    }

    public List<TutorialDTO> getAllTutorials() {
        try {
            return tutorialBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }

    public String updateTutorial() {
        try {
            tutorialBean.update(
                    currentTutorial.getId(),
                    currentTutorial.getDescription(),
                    currentTutorial.getText()
            );
            
            setFilteredMaterials(null);

            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_tutorial_update?faces-redirect=true";
    }

    public void removeTutorial(int tutorialId) {
        try {
            tutorialBean.remove(tutorialId);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }
    
    // **********************************
    // ************ TEXT ****************
    // **********************************
    public String createText() {
        try {
            textBean.create(
                    newText.getDescription(),
                    newText.getText()
            );
            
            setFilteredMaterials(null);

            newText.reset();

            return "admin_index?faces-redirect=true";
        } catch (MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_text_create?faces-redirect=true";
    }

    public List<TextDTO> getAllTexts() {
        try {
            return textBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }

    public String updateText() {
        try {
            textBean.update(
                    currentText.getId(),
                    currentText.getDescription(),
                    currentText.getText()
            );
            
            setFilteredMaterials(null);
            
            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_text_update?faces-redirect=true";
    }

    public void removeText(int textId) {
        try {
            textBean.remove(textId);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }

    // ***********************************
    // ************ VIDEO ****************
    // ***********************************
    public String createVideo() {
        try {
            videoBean.create(
                    newVideo.getDescription(),
                    newVideo.getUrl()
            );

            setFilteredMaterials(null);
            
            newVideo.reset();

            return "admin_index?faces-redirect=true";
        } catch (MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_video_create?faces-redirect=true";
    }

    public List<VideoDTO> getAllVideos() {
        try {
            return videoBean.getAll();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return null;
    }

    public String updateVideo() {
        try {
            videoBean.update(
                    currentVideo.getId(), 
                    currentVideo.getDescription(),
                    currentVideo.getUrl()
            );
            
            setFilteredMaterials(null);

            return "admin_index?faces-redirect=true";
        } catch (EntityDoesNotExistException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }

        return "admin_video_update?faces-redirect=true";
    }

    public void removeVideo(int videoId) {
        try {
            videoBean.remove(videoId);
        } catch (EntityDoesNotExistException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, LOGGER);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, LOGGER);
        }
    }
    
    // *******************************************
    // ************ GETTERS & SETTERS ************
    // *******************************************  
    public AdministratorDTO getNewAdministrator() {
        return newAdministrator;
    }

    public void setNewAdministrator(AdministratorDTO newAdministrator) {
        this.newAdministrator = newAdministrator;
    }

    public AdministratorDTO getCurrentAdministrator() {
        return currentAdministrator;
    }

    public void setCurrentAdministrator(AdministratorDTO currentAdministrator) {
        this.currentAdministrator = currentAdministrator;
    }

    public HealthcareProDTO getNewHealthcarePro() {
        return newHealthcarePro;
    }

    public void setNewHealthcarePro(HealthcareProDTO newHealthcarePro) {
        this.newHealthcarePro = newHealthcarePro;
    }

    public HealthcareProDTO getCurrentHealthcarePro() {
        return currentHealthcarePro;
    }

    public void setCurrentHealthcarePro(HealthcareProDTO currentHealthcarePro) {
        this.currentHealthcarePro = currentHealthcarePro;
    }

    public CaregiverDTO getNewCaregiver() {
        return newCaregiver;
    }

    public void setNewCaregiver(CaregiverDTO newCaregiver) {
        this.newCaregiver = newCaregiver;
    }

    public CaregiverDTO getCurrentCaregiver() {
        return currentCaregiver;
    }

    public void setCurrentCaregiver(CaregiverDTO currentCaregiver) {
        this.currentCaregiver = currentCaregiver;
    }

    public EmergencyContactDTO getNewEmergencyContact() {
        return newEmergencyContact;
    }

    public void setNewEmergencyContact(EmergencyContactDTO newEmergencyContact) {
        this.newEmergencyContact = newEmergencyContact;
    }

    public EmergencyContactDTO getCurrentEmergencyContact() {
        return currentEmergencyContact;
    }

    public void setCurrentEmergencyContact(EmergencyContactDTO currentEmergencyContact) {
        this.currentEmergencyContact = currentEmergencyContact;
    }

    public FaqDTO getNewFaq() {
        return newFaq;
    }

    public void setNewFaq(FaqDTO newFaq) {
        this.newFaq = newFaq;
    }

    public FaqDTO getCurrentFaq() {
        return currentFaq;
    }

    public void setCurrentFaq(FaqDTO currentFaq) {
        this.currentFaq = currentFaq;
    }

    public TutorialDTO getNewTutorial() {
        return newTutorial;
    }

    public void setNewTutorial(TutorialDTO newTutorial) {
        this.newTutorial = newTutorial;
    }

    public TutorialDTO getCurrentTutorial() {
        return currentTutorial;
    }

    public void setCurrentTutorial(TutorialDTO currentTutorial) {
        this.currentTutorial = currentTutorial;
    }

    public TextDTO getNewText() {
        return newText;
    }

    public void setNewText(TextDTO newText) {
        this.newText = newText;
    }

    public TextDTO getCurrentText() {
        return currentText;
    }

    public void setCurrentText(TextDTO currentText) {
        this.currentText = currentText;
    }

    public VideoDTO getNewVideo() {
        return newVideo;
    }

    public void setNewVideo(VideoDTO newVideo) {
        this.newVideo = newVideo;
    }

    public VideoDTO getCurrentVideo() {
        return currentVideo;
    }

    public void setCurrentVideo(VideoDTO currentVideo) {
        this.currentVideo = currentVideo;
    }
    
    public List<AdministratorDTO> getFilteredAdmins() {
        return filteredAdmins;
    }
        
    public void setFilteredAdmins(List<AdministratorDTO> filteredAdmins) {    
        this.filteredAdmins = filteredAdmins;
    }

    public List<HealthcareProDTO> getFilteredHealthcarePros() {
        return filteredHealthcarePros;
    }

    public void setFilteredHealthcarePros(List<HealthcareProDTO> filteredHealthcarePros) {
        this.filteredHealthcarePros = filteredHealthcarePros;
    }

    public List<CaregiverDTO> getFilteredCaregivers() {
        return filteredCaregivers;
    }

    public void setFilteredCaregivers(List<CaregiverDTO> filteredCaregivers) {
        this.filteredCaregivers = filteredCaregivers;
    }

    public List<MaterialDTO> getFilteredMaterials() {
        return filteredMaterials;
    }

    public void setFilteredMaterials(List<MaterialDTO> filteredMaterials) {
        this.filteredMaterials = filteredMaterials;
    }
    
    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

}
