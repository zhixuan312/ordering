package ManagedBean;

import AccountManagement.AccountManagementRemote;
import entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Named(value = "signUpAndLoginManagedBean")
@SessionScoped

public class SignUpAndLoginManagedBean implements Serializable {
    
    @EJB
    private AccountManagementRemote accountManagementRemote;
    
    private Employee employee;
    private String email;
    private String password;
    private String last_name;
    private String first_name;
    private String mobile;
    private String verificationCode;
    private ExternalContext ec;
    
    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }
    
    public SignUpAndLoginManagedBean() {
        employee = new Employee();
        email = "";
        password = "";
        last_name = "";
        first_name = "";
        mobile = "";
        verificationCode = "";
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
    }
    
    public void employeeLogin() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            if (accountManagementRemote.employeeLogin(email, password)) {
                if (accountManagementRemote.getEmployee().getAccountStatus().equalsIgnoreCase("Pending Verification")) {
                    ec.getSessionMap().put("login", true);
                    ec.redirect("verification.xhtml?faces-redirect=true");
                } else if (accountManagementRemote.getEmployee().getAccountStatus().equalsIgnoreCase("Suspended")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Suspended account", "Suspended account"));
                } else {
                    ec.getSessionMap().put("login", true);
                    ec.redirect("home.xhtml?faces-redirect=true");
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential", "Invalid login credential"));
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void verification() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            if (verificationCode.equals(accountManagementRemote.getEmployee().getVerificationCode())) {
                System.out.println("verificationCode = " + verificationCode + " verificationCodeTrue = " + accountManagementRemote.getEmployee().getVerificationCode());
                Employee newEmployee = accountManagementRemote.getEmployee();
                newEmployee.setAccountStatus("Verified");
                accountManagementRemote.updateEmployeeProfile(newEmployee);
                employeeLogin();
            } else {
                ec.getSessionMap().put("login", true);
                ec.redirect("verification.xhtml?faces-redirect=true");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void cancelLogin() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("login.xhtml?faces-redirect=true");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void cancelRegistration() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            employee = new Employee();
            ec.redirect("register.xhtml?faces-redirect=true");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void logout() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            accountManagementRemote.logout();
            
            ((HttpSession) ec.getSession(true)).invalidate();
            ec.invalidateSession();
            ec.redirect("index.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createEmployee() {
        employee.setCreateDate(new Date());
        employee.setAccountStatus("Pending Verification");
        String verificationCode = accountManagementRemote.createNewEmployee(employee);
        if (!verificationCode.equals("-1")) {
            this.email = employee.getEmail();
            this.password = employee.getPassword();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New customer registered successfully!", "Your registration verification code is " + verificationCode));
            this.employeeLogin();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email is been registered", "Email is been registered"));
        }
    }
    
    public void updateMyProfile() {
        accountManagementRemote.updateEmployeeProfile(employee);
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getLast_name() {
        return last_name;
    }
    
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    
    public String getFirst_name() {
        return first_name;
    }
    
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    
    public AccountManagementRemote getAccountManagementRemote() {
        return accountManagementRemote;
    }
    
    public void setAccountManagementRemote(AccountManagementRemote accountManagementRemote) {
        this.accountManagementRemote = accountManagementRemote;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    
}
