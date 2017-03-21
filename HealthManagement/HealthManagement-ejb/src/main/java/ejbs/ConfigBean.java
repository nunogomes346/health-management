package ejbs;

import exceptions.EntityAlreadyExistsException;
import exceptions.MyConstraintViolationException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ConfigBean {

    @EJB
    AdministratorBean administratorBean;
    @EJB
    HealthcareProBean healthcareProBean;
    @EJB
    CaregiverBean caregiverBean;
    @EJB
    PatientBean patientBean;
    
    @EJB
    NeedBean needBean;
    @EJB
    ProceedingBean proceedingBean;

    @EJB
    EmergencyContactBean emergencyBean;
    @EJB
    FaqBean faqBean;
    @EJB
    TutorialBean tutorialBean;
    @EJB
    TextBean textBean;
    @EJB
    VideoBean videoBean;

    @PostConstruct
    public void populateDB() {
        try {
            administratorBean.create("nunogomes", "adminpw", "Nuno Gomes", "nunogomes@gmail.com");
            administratorBean.create("joaocaroco", "adminpw", "João Caroço", "joaoc2800@gmail.com");
            administratorBean.create("pedrodomingues", "adminpw", "Pedro Domingues", "pedrolcd@outlook.com");
            administratorBean.create("admin", "adminpw", "Admin", "admin@mail.com");

            healthcareProBean.create("teddy", "propw", "Bruno Fonseca", "teddy@gmail.com", "Hospital of Leiria", "Doctor");
            healthcareProBean.create("alex", "propw", "Alex Ribeiro", "alex.ribeiro@gmail.com", "Hospital of Leiria", "Gastroenterologist");
            healthcareProBean.create("healthcarePro", "propw", "Healthcare Professional", "healthcarepro@mail.com", "Hospital of Lisbon", "Doctor");
            
            caregiverBean.create("piteu", "carepw", "Bruno Anastácio", "piteu@gmail.com");
            caregiverBean.create("piteu2", "carepw", "Bruno Anastácio2", "piteu@gmail.com");
            caregiverBean.create("piteu3", "carepw", "Bruno Anastácio3", "piteu@gmail.com");
            
            // IDs -> 4 - 9
            patientBean.create("João Sousa", "sousa@gmail.com");
            patientBean.create("João Sousa1", "sousa@gmail.com");
            patientBean.create("João Sousa2", "sousa@gmail.com");
            patientBean.create("João Sousa3", "sousa@gmail.com");
            patientBean.create("João Sousa4", "sousa@gmail.com");
            patientBean.create("João Sousa5", "sousa@gmail.com");
            
            // NEEDS: IDs -> 10 - 12
            needBean.create("Change gastric tube");
            needBean.create("Do the wound dressing");
            needBean.create("Do endoscopy");
            
            needBean.associateNeedToPatient(Long.parseLong("10"), Long.parseLong("8"));
            needBean.associateNeedToPatient(Long.parseLong("11"), Long.parseLong("8"));
            needBean.associateNeedToPatient(Long.parseLong("12"), Long.parseLong("7"));
            needBean.associateNeedToPatient(Long.parseLong("10"), Long.parseLong("6"));
            needBean.associateNeedToPatient(Long.parseLong("11"), Long.parseLong("5"));
            needBean.associateNeedToPatient(Long.parseLong("10"), Long.parseLong("5"));
            needBean.associateNeedToPatient(Long.parseLong("10"), Long.parseLong("4"));
            needBean.associateNeedToPatient(Long.parseLong("12"), Long.parseLong("4"));
            
            patientBean.associatePatientToCaregiver(Long.parseLong("4"), "piteu");
            patientBean.associatePatientToCaregiver(Long.parseLong("5"), "piteu2");
            
            // MATERIALS: IDs -> 13 - 28
            emergencyBean.create("112 Number", "National Emergency Number", "112");
            emergencyBean.create("Emergency of Leiria", "General Urgency of the Hospital Unit of Leiria", "244 817 078");
            emergencyBean.create("Health 24", "Health 24", "808 242 424");
            emergencyBean.create("Red Cross", "Red Cross", "219 421 111");
            emergencyBean.create("Commands", "Commands", "213 924 700");

            faqBean.create("FAQ on what are ulcers", "What are pressure ulcers?",
                    "Pressure ulcer can be defined as a skin injury caused by disruption "
                    + "of blood in a particular area, which develops due to increased pressure "
                    + "for an extended period. It is also known as decubitus ulcer, eschar or "
                    + "decubitus eschar. The term eschar should be used when there is a necrotic "
                    + "part or black crust in the lesion.");

            faqBean.create("FAQ on how ulcers develop", "How do pressure ulcers develop?",
                    "The pressure ulcer develops when there is a soft tissue compression between a "
                    + "bone prominence and a hard surface for an extended period. The most frequent "
                    + "place for its development is in the sacral region, calcaneus, buttocks, trochanters, "
                    + "elbows and trunk.");

            faqBean.create("FAQ on the causes of dehydration", "What are the main causes of dehydration?",
                    "We dehydrate when the water balance is negative, that is, when the water losses "
                    + "are not restored. This situation is more frequent when water needs are increased "
                    + "(heat, physical exercise, vomiting, diarrhea, ...), and special care should be "
                    + "taken with children and the elderly, since their ability to detect the state of "
                    + "dehydration and / or Responding to your signals may be diminished.");

            tutorialBean.create("Wound Dressing Tutorial", 
                    "Cleaning: Clean the wounds initially and each dressing change.");
            tutorialBean.create("Ulcers Tutorial", 
                    "Use a non-traumatic technique using minimal mechanical force when cleaning "
                    + "the ulcer with gauze, compresses, or sponges.");
            tutorialBean.create("Swab Culture Tutorial", 
                    "Do not do swab cultures to diagnose wound infection, as all PU's are colonized. "
                    + "Swab cultures detect only surface colonization and may not truly reflect "
                    + "the microorganism that is causing tissue infection.");
            
            textBean.create("Text on obstructions", 
                    "Sometimes an obstruction resolves itself without further treatment, especially "
                    + "if it is due to the presence of adhesions. To deal with some disorders such as "
                    + "twisting a segment of the lower part of the colon, an endoscope may be inserted "
                    + "through the anus or an enema with barium potato, which causes that portion of "
                    + "the intestine to inflate and the obstruction to be resolved By the pressure exerted. "
                    + "However, the most common is to have surgery as soon as possible. During the same, "
                    + "the blocked segment of the intestine can be excised and the two free ends can be "
                    + "joined together again.");
            textBean.create("Text on painful areas", 
                    "The doctor examines the abdomen for painful areas and deformations of the wall or "
                    + "masses. Normal abdominal sounds (intestinal sounds), which can be heard through "
                    + "a stethoscope, may be increased and very sharp, or not heard. If the perforation "
                    + "has caused peritonitis, the patient will feel pain with the pressure of the abdomen, "
                    + "which increases when the doctor suddenly withdraws the hand (positive sign of "
                    + "decompression).");
            
            videoBean.create("Video on how to do Endoscopy Exam", "https://www.youtube.com/watch?v=-vSXINtEPpE");
            videoBean.create("Video on how to treat sunburn", "https://www.youtube.com/watch?v=NzV6h9K8ApQ");
            videoBean.create("Video on care in nasogastric tube feeding", "https://www.youtube.com/watch?v=W3EHXiuktgM");
            
            caregiverBean.associateMaterial("piteu", 13, Long.parseLong("10"));
            caregiverBean.associateMaterial("piteu", 18, Long.parseLong("10"));
            caregiverBean.associateMaterial("piteu", 19, Long.parseLong("10"));
            caregiverBean.associateMaterial("piteu", 26, Long.parseLong("10"));
            caregiverBean.associateMaterial("piteu", 22, Long.parseLong("10"));
            caregiverBean.associateMaterial("piteu", 26, Long.parseLong("12"));
            caregiverBean.associateMaterial("piteu", 22, Long.parseLong("12"));
            caregiverBean.associateMaterial("piteu", 25, Long.parseLong("12"));
            caregiverBean.associateMaterial("piteu2", 27, Long.parseLong("11"));
            caregiverBean.associateMaterial("piteu2", 26, Long.parseLong("10"));
            caregiverBean.associateMaterial("piteu2", 21, Long.parseLong("11"));
            
            proceedingBean.create(22, Long.parseLong("4"), Long.parseLong("12"), "piteu", "12-10-2016 00:01:30", null);
            proceedingBean.create(25, Long.parseLong("4"), Long.parseLong("12"), "piteu", "23-10-2016 10:10:20", null);
            proceedingBean.create(18, Long.parseLong("4"), Long.parseLong("10"), "piteu", "15-11-2016 16:20:54", null);
            proceedingBean.create(22, Long.parseLong("4"), Long.parseLong("10"), "piteu", "17-11-2016 22:27:20", null);
            proceedingBean.create(22, Long.parseLong("4"), Long.parseLong("10"), "piteu", "08-12-2016 23:55:21", null);
            proceedingBean.create(26, Long.parseLong("4"), Long.parseLong("10"), "piteu", "10-12-2016 17:20:50", null);
            proceedingBean.create(26, Long.parseLong("5"), Long.parseLong("10"), "piteu2", "17-10-2016 16:27:47", null);
            proceedingBean.create(21, Long.parseLong("5"), Long.parseLong("11"), "piteu2", "23-10-2016 15:10:55", null);
            proceedingBean.create(27, Long.parseLong("5"), Long.parseLong("11"), "piteu2", "08-11-2016 15:36:22", null);
            
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
