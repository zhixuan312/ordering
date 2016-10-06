package ManagedBean;

import ProductManagement.ProductManagementRemote;
import TransactionManagement.TransactionManagementLocal;
import entity.Transaction;
import entity.TransactionLineItem;
import java.io.IOException;
import entity.Employee;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import entity.CartLineItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.inject.Inject;

@Named(value = "checkOutManagedBean")
@RequestScoped
public class CheckOutManagedBean implements Serializable {
    
    @EJB
    private TransactionManagementLocal transactionManagementLocal;
    @EJB
    private ProductManagementRemote productManagementRemote;
    
    @Inject
    private CartManagedBean CartManagedBean;
    @Inject
    private OrderManagedBean OrderManagedBean;
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    
    private Employee employee;
    private List<TransactionLineItem> transactionLineItemsForOneTransaction;
    private Transaction transaction;
    private List<Long> tempList;
    private List<Transaction> transactionList;
    
    
    public CheckOutManagedBean() {
        employee = new Employee();
        transactionLineItemsForOneTransaction = new ArrayList<>();
        transaction = new Transaction();
        tempList = new ArrayList<>();
        transactionList = new ArrayList<>();
    }
    
    @PostConstruct
    public void init() {
        System.out.println("Checkout MangedBean constructed.");
        
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
    
    public List<Transaction> sevenList(){
        List<Transaction> transactionAll = transactionManagementLocal.viewAllTransaction();
        transactionAll.sort(Comparator.comparing(Transaction::getExpectDelivery));
        List<Transaction> transactionSeven = new ArrayList<>();
        for (int i = 0; i < transactionAll.size(); i ++) {
            if(transactionAll.get(i).getExpectDelivery().after(new Date())){
                transactionSeven.add(transactionAll.get(i));
                if (transactionSeven.size() > 6 ){
                    break;
                }
            }
        }
        return transactionSeven;
    }
    
    public List<String> transactionDate(){
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i ++) {
            Date date = transactionList.get(i).getExpectDelivery();
            int day = date.getDate();
            int month = date.getMonth()+1;
            String monthString;
            if (month < 10) {
                monthString = "0" +month;
            } else {
                monthString = month +"";
            }
            dateList.add(day + "/" + monthString + ": ");
        }
        return dateList;
    }
    
    public List<String> transactionStatus(){
        List<String> statusList = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i ++) {
            statusList.add(transactionList.get(i).getStatus());
        }
        return statusList;
    }
    
    public String convertDateToStringInterval(Date deDate){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        String hourRange;
        if (deDate.getHours() < 8) {
            hourRange = "0" + (deDate.getHours() + 2);
        } else {
            hourRange = deDate.getHours() + 2 + "";
        }
        String newDate = df.format(deDate)+ " - " + hourRange + ":00 " ;
        return newDate;
    }
    
    
    
    public void checkOut() {
        System.out.println("##### checkout function called");
        List<CartLineItem> cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        transaction.setEmployee(employee);
        transaction.setTransactionDateTime(new Date());
        Date expectDeliveryDateTime = CartManagedBean.getDeliveryDateTime();
        expectDeliveryDateTime.setHours(CartManagedBean.getDeliveryHour());
        transaction.setExpectDelivery(CartManagedBean.getDeliveryDateTime());
        transaction.setActualDelivery(null);
        Date newDate = new Date();
        newDate.setHours(0);
        newDate.setMinutes(0);
        newDate.setSeconds(0);
        if (CartManagedBean.getDeliveryDateTime().getTime() - newDate.getTime() <= 172800000){
           transaction.setStatus("unconfirmed");
        } else {
           transaction.setStatus("Order Placed");
        }
        Long transactionId = transactionManagementLocal.createTransaction(transaction);
        transaction.setTransactionId(transactionId);
        
        if (!cartLineItems.isEmpty()) {
            for (int i = 0; i < cartLineItems.size(); i++) {
                TransactionLineItem transactionLineItem = new TransactionLineItem();
                transactionLineItem.setExpectQuantity(cartLineItems.get(i).getQuantity());
                transactionLineItem.setActualQuantity(0);
                transactionLineItem.setProduct(cartLineItems.get(i).getProduct());
                transactionLineItem.setTransaction(transaction);
                transactionManagementLocal.createTransactionLineItem(transactionLineItem);
                transaction.getTransactionLineItems().add(transactionLineItem);
                transactionManagementLocal.updateTransaction(transaction);
                transactionLineItemsForOneTransaction.add(transactionLineItem);
                tempList.add(cartLineItems.get(i).getCartLineItemId());
            }
            for (int i = 0; i < tempList.size(); i++) {
                productManagementRemote.deleteCartLineItem(tempList.get(i));
            }
        }
        CartManagedBean.init();
        OrderManagedBean.init();
        try{
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("product.xhtml?faces-redirect=true");
        } catch (Exception e){
            
        }
        
    }
    
    public List<TransactionLineItem> getTransactionLineItemsForOneTransaction() {
        return transactionLineItemsForOneTransaction;
    }
    
    public void setTransactionLineItemsForOneTransaction(List<TransactionLineItem> transactionLineItemsForOneTransaction) {
        this.transactionLineItemsForOneTransaction = transactionLineItemsForOneTransaction;
    }
    
    public Transaction getTransaction() {
        return transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public List<Long> getTempList() {
        return tempList;
    }
    
    public void setTempList(List<Long> tempList) {
        this.tempList = tempList;
    }
    
    public TransactionManagementLocal getTransactionManagementLocal() {
        return transactionManagementLocal;
    }
    
    public void setTransactionManagementLocal(TransactionManagementLocal transactionManagementLocal) {
        this.transactionManagementLocal = transactionManagementLocal;
    }
    
    
    public ProductManagementRemote getProductManagementRemote() {
        return productManagementRemote;
    }
    
    public void setProductManagementRemote(ProductManagementRemote productManagementRemote) {
        this.productManagementRemote = productManagementRemote;
    }
    
    public SignUpAndLoginManagedBean getSignUpAndLoginManagedBean() {
        return signUpAndLoginManagedBean;
    }
    
    public void setSignUpAndLoginManagedBean(SignUpAndLoginManagedBean signUpAndLoginManagedBean) {
        this.signUpAndLoginManagedBean = signUpAndLoginManagedBean;
    }
    
    public CartManagedBean getCartManagedBean() {
        return CartManagedBean;
    }
    
    public void setCartManagedBean(CartManagedBean CartManagedBean) {
        this.CartManagedBean = CartManagedBean;
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
    
}
