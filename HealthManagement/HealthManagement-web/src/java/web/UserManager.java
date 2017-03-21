package web;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@Named(value = "userManager")
@SessionScoped
public class UserManager implements Serializable {

    private String username;
    private String password;
    private boolean loginFlag = true;
    private static final Logger logger = Logger.getLogger("web.UserManager");
    
    public UserManager() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            logger.log(Level.WARNING, e.getMessage());
            return "error?faces-redirect=true";
        }
        
        if(isUserInRole("Administrator")){
            return "/faces/administrator/admin_index?faces-redirect=true";
        }
        
        if(isUserInRole("HealthcarePro")){
            return "/faces/healthcarePro/healthcarePro_index?faces-redirect=true";
        }
        
        if(isUserInRole("Caregiver")){
            return "/faces/caregiver/caregiver_index?faces-redirect=true";
        }
        
        if(isUserInRole("Patient")){
            return "/faces/patient/patient_index?faces-redirect=true";
        }
        
        return "error?faces-redirect=true";
    }
    
    public boolean isUserInRole(String role) {
        return (isSomeUserAuthenticated() &&
            FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role));
    }
    
    public boolean isSomeUserAuthenticated() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null;
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // remove data from beans:
        for (String bean : context.getExternalContext().getSessionMap().keySet()) {
            context.getExternalContext().getSessionMap().remove(bean);
        }
        
        // destroy session:
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        
        // using faces-redirect to initiate a new request:
        return "/index_login.xhtml?faces-redirect=true";
    }
    
    public String clearLogin(){
        if(isSomeUserAuthenticated()){
            logout();
        }
        
        return "index_login.xhtml?faces-redirect=true";
    }
}
