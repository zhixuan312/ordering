/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ManagedBean;

import TransactionManagement.TransactionManagementLocal;
import entity.Employee;
import entity.Transaction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author XUAN
 */
@Named(value = "receivingManagedBean")
@RequestScoped
public class ReceivingManagedBean {
    
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    @EJB
    private TransactionManagementLocal transactionManagementLocal;
    
    private Employee employee;
    private List<Transaction> receivingList;
    
    public ReceivingManagedBean() {
        employee = new Employee();
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
        receivingList = receivingFunction();
    }
    
    public List<Transaction> receivingFunction(){
        List<Transaction> tempList = transactionManagementLocal.viewAllTransaction();
        List<Transaction> tempList2 = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i ++){
            if (tempList.get(i).getStatus().equals("Received")){
                tempList2.add(tempList.get(i));
            }
        }
        return tempList2;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public SignUpAndLoginManagedBean getSignUpAndLoginManagedBean() {
        return signUpAndLoginManagedBean;
    }

    public void setSignUpAndLoginManagedBean(SignUpAndLoginManagedBean signUpAndLoginManagedBean) {
        this.signUpAndLoginManagedBean = signUpAndLoginManagedBean;
    }

    public TransactionManagementLocal getTransactionManagementLocal() {
        return transactionManagementLocal;
    }

    public void setTransactionManagementLocal(TransactionManagementLocal transactionManagementLocal) {
        this.transactionManagementLocal = transactionManagementLocal;
    }

    public List<Transaction> getReceivingList() {
        return receivingList;
    }

    public void setReceivingList(List<Transaction> receivingList) {
        this.receivingList = receivingList;
    }
}
