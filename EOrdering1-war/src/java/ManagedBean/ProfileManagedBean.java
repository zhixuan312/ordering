/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ManagedBean;

import entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author XUAN
 */
@Named(value = "profileManagedBean")
@RequestScoped
public class ProfileManagedBean implements Serializable  {
    
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    
    private Employee employee;
    private String oldPassword;
    private String newPassword;
    
    public ProfileManagedBean() {
        employee = new Employee();
        oldPassword = "";
        newPassword = "";
    }
    
    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        
        try {
            if (ec.getSessionMap().get("login") == null) {
                ec.redirect("index.xhtml?faces-redirect=true");
            } else if (ec.getSessionMap().get("login").equals(false)){
                ec.redirect("index.xhtml?faces-redirect=true");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        employee = signUpAndLoginManagedBean.getAccountManagementRemote().getEmployee();
        System.out.println("###### Employee: " + employee.getEmail());
    }
    
    public void updateEmployeeProfile() {
        Employee updateEmployee = signUpAndLoginManagedBean.getAccountManagementRemote().retrieveEmployeeByEmail(employee.getEmail());
        updateEmployee.setFirstName(employee.getFirstName());
        updateEmployee.setLastName(employee.getLastName());
        updateEmployee.setEmail(employee.getEmail());
        if (updateEmployee.getPassword().equals(oldPassword)){
            updateEmployee.setPassword(newPassword);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful", "Update Successful"));
        }  else if (!oldPassword.isEmpty() && !updateEmployee.getPassword().equals(oldPassword)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password not correct", "old Password not correct"));
        } else {
            System.out.println("Not correct");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Successful", "Update Successful"));
        }
        signUpAndLoginManagedBean.getAccountManagementRemote().updateEmployeeProfile(updateEmployee);
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public String getOldPassword() {
        return oldPassword;
    }
    
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
