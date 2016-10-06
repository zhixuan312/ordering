/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ManagedBean;

import entity.Employee;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author XUAN
 */
@Named(value = "adminManagedBean")
@RequestScoped
public class AdminManagedBean {
    
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    
    private Employee employee;
    private Employee selectedEmployee;
    private List<Employee> employees;
    
    public AdminManagedBean() {
        employee = new Employee();
        selectedEmployee = new Employee();
        employees = new ArrayList<>();
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
        if (!employee.getIsAdmin()){
            try {
                ec.redirect("index.xhtml?faces-redirect=true");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        employees = signUpAndLoginManagedBean.getAccountManagementRemote().viewAllRecordedEmployee();
    }
    
    public String adminStatusConvert(boolean status){
        if (status){
            return "Yes";
        } else {
            return "No";
        }
    }
    
    public void updateEmployeeProfile() {
        Employee updateEmployee = signUpAndLoginManagedBean.getAccountManagementRemote().retrieveEmployeeByEmail(selectedEmployee.getEmail());
        updateEmployee.setFirstName(selectedEmployee.getFirstName());
        updateEmployee.setLastName(selectedEmployee.getLastName());
        updateEmployee.setIsAdmin(selectedEmployee.getIsAdmin());
        updateEmployee.setAccountStatus(selectedEmployee.getAccountStatus());
        System.out.println("Employee name = " + updateEmployee.getFirstName());
        signUpAndLoginManagedBean.getAccountManagementRemote().updateEmployeeProfile(updateEmployee);
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }
    
    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    } 
}
