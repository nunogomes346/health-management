package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Proceeding")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProceedingDTO {

    private Long proceedingId;
    private int materialID;
    private String materialDescription;
    private Long patientID;
    private String patientName;
    private Long needID;
    private String needDescription;
    private String caregiverUsername;
    private String proceedingDate;
    private String note;

    public ProceedingDTO() { }
    
    public ProceedingDTO(Long proceedingId, int materialID, String materialDescription, Long patientID, 
            String patientName, Long needID, String needDescription, String caregiverUsername, 
            String proceedingDate, String note) {
        this.proceedingId = proceedingId;
        this.materialID = materialID;
        this.materialDescription = materialDescription;
        this.patientID = patientID;
        this.patientName = patientName;
        this.needID = needID;
        this.needDescription = needDescription;
        this.caregiverUsername = caregiverUsername;
        this.proceedingDate = proceedingDate;
        this.note = note;
    }

    public Long getProceedingId() {
        return proceedingId;
    }

    public void setProceedingId(Long proceedingId) {
        this.proceedingId = proceedingId;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getNeedID() {
        return needID;
    }

    public void setNeedID(Long needID) {
        this.needID = needID;
    }

    public String getNeedDescription() {
        return needDescription;
    }

    public void setNeedDescription(String needDescription) {
        this.needDescription = needDescription;
    }

    public String getCaregiverUsername() {
        return caregiverUsername;
    }

    public void setCaregiverUsername(String caregiverUsername) {
        this.caregiverUsername = caregiverUsername;
    }

    public String getProceedingDate() {
        return proceedingDate;
    }

    public void setProceedingDate(String proceedingDate) {
        this.proceedingDate = proceedingDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void reset() {
        setProceedingId(null);
        setMaterialID(0);
        setMaterialDescription(null);
        setPatientID(null);
        setPatientName(null);
        setNeedID(null);
        setNeedDescription(null);
        setCaregiverUsername(null);
        setProceedingDate(null);
        setNote(null);
    }
}
