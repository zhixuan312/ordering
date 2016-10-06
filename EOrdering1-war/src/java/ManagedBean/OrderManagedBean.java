package ManagedBean;

import ProductManagement.ProductManagementRemote;
import TransactionManagement.TransactionManagementLocal;
import entity.Employee;
import entity.Transaction;
import entity.TransactionLineItem;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named(value = "orderManagedBean")
@SessionScoped
public class OrderManagedBean implements Serializable {
    
    @EJB
    private ProductManagementRemote productManagementRemote;
    @EJB
    private TransactionManagementLocal transactionManagementLocal;
    
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    
    private Employee employee;
    private List<Transaction> transactionList;
    private List<String> statusList;
    private Transaction selectedTransaction;
    
    public OrderManagedBean() {
        employee = new Employee();
        transactionList = new ArrayList<>();
        statusList = new ArrayList<>();
        statusList.add("Delivered");
        statusList.add("In Transit");
        statusList.add("Order Placed");
        selectedTransaction = new Transaction();
    }
    
    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        
        try {
            if (ec.getSessionMap().get("login") == null) {
                ec.redirect("index.xhtml?faces-redirect=true");
            } else {
                if (ec.getSessionMap().get("login").equals(false)) {
                    ec.redirect("index.xhtml?faces-redirect=true");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        employee = signUpAndLoginManagedBean.getAccountManagementRemote().getEmployee();
        transactionList = transactionManagementLocal.viewAllTransaction();
    }
    
    public void updateTrnasactionAndTransactionLineItem(){
        List<TransactionLineItem> itemList = selectedTransaction.getTransactionLineItems();
        for(int i =0; i < itemList.size(); i ++){
            transactionManagementLocal.updateTransactionLineItem(itemList.get(i));
        }
        if (selectedTransaction.getStatus().equals("Delivered")){
            selectedTransaction.setStatus("Received");
            selectedTransaction.setActualDelivery(new Date());
            transactionManagementLocal.updateTransaction(selectedTransaction);
        }
    }
    
    public void updateTrnasactionStatus(){
        transactionManagementLocal.updateTransaction(selectedTransaction);
    }
    
    public ProductManagementRemote getProductManagementRemote() {
        return productManagementRemote;
    }
    
    public void setProductManagementRemote(ProductManagementRemote productManagementRemote) {
        this.productManagementRemote = productManagementRemote;
    }
    
    public TransactionManagementLocal getTransactionManagementLocal() {
        return transactionManagementLocal;
    }
    
    public void setTransactionManagementLocal(TransactionManagementLocal transactionManagementLocal) {
        this.transactionManagementLocal = transactionManagementLocal;
    }
    
    public SignUpAndLoginManagedBean getSignUpAndLoginManagedBean() {
        return signUpAndLoginManagedBean;
    }
    
    public void setSignUpAndLoginManagedBean(SignUpAndLoginManagedBean signUpAndLoginManagedBean) {
        this.signUpAndLoginManagedBean = signUpAndLoginManagedBean;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public List<Transaction> getTransactionList() {
        return transactionList;
    }
    
    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
    
    public List<String> getStatusList() {
        return statusList;
    }
    
    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }
    
    public Transaction getSelectedTransaction() {
        return selectedTransaction;
    }
    
    public void setSelectedTransaction(Transaction selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }
    
}
