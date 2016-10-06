package ManagedBean;

import ProductManagement.ProductManagementRemote;
import entity.CartLineItem;
import entity.Employee;
import entity.Product;
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
import org.primefaces.event.SelectEvent;

@Named(value = "cartManagedBean")
@SessionScoped
public class CartManagedBean implements Serializable {
    
    @EJB
    private ProductManagementRemote productManagementRemote;
    
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    
    private Employee employee;
    private List<CartLineItem> cartLineItems;
    private double totalPrice;
    private CartLineItem newCartLineItem;
    private CartLineItem cartLineItemToRemove;
    private CartLineItem cartLineItemAfterEdit;
    private List<CartLineItem> readyToPayCartItems;
    private String orderCompleteDate;
    private Date deliveryDateTime;
    private int deliveryHour;
    private Date today;
    private String validateString;
    
    public CartManagedBean() {
        employee = new Employee();
        cartLineItems = new ArrayList<>();
        totalPrice = 0;
        readyToPayCartItems = new ArrayList<>();
        newCartLineItem = new CartLineItem();
        cartLineItemToRemove = new CartLineItem();
        cartLineItemAfterEdit = new CartLineItem();
        orderCompleteDate = "";
        today = new Date();
        
        validateString = "";
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
        System.out.println("#### new cartManaged");
        employee = signUpAndLoginManagedBean.getAccountManagementRemote().getEmployee();
        if (productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId()) != null) {
            cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        }
        orderCompleteDate = orderDate();
    }
    
    public String orderDate(){
        Date date = new Date();
        int day = date.getDate();
        int month = date.getMonth()+1;
        String monthString;
        String dayString;
        if (month < 10) {
            monthString = "0" +month;
        } else {
            monthString = month +"";
        }
        
        if (day < 10) {
            dayString = "0" +day;
        } else {
            dayString = day +"";
        }
        int year = date.getYear()+1900;
        return dayString + "/" + monthString + "/" + year + "" + "\n 10:00 PM";
    }
    
    public List<CartLineItem> refreshList() {
        if (productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId()) != null) {
            cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
            for (int j = 0; j < cartLineItems.size(); j++) {
                if (cartLineItems.get(j).getQuantity() == 0) {
                    productManagementRemote.deleteCartLineItem(cartLineItems.get(j).getCartLineItemId());
                }
            }
        }
        return cartLineItems;
    }
    
    public void addProductToCart(Product product) {
        Boolean isThere = false;
        cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        if (cartLineItems != null && !cartLineItems.isEmpty()) {
            for (int i = 0; i < cartLineItems.size(); i++) {
                if (cartLineItems.get(i).getProduct().equals(product)) {
                    CartLineItem newCartLineItem = new CartLineItem();
                    newCartLineItem.setCartLineItemId(cartLineItems.get(i).getCartLineItemId());
                    newCartLineItem.setEmployee(employee);
                    newCartLineItem.setProduct(product);
                    int quantity = cartLineItems.get(i).getQuantity() + 1;
                    newCartLineItem.setQuantity(quantity);
                    productManagementRemote.updateCartLineItem(newCartLineItem);
                    cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
                    isThere = true;
                }
            }
            if (!isThere) {
                newCartLineItem.setProduct(product);
                newCartLineItem.setEmployee(employee);
                newCartLineItem.setQuantity(1);
                productManagementRemote.createCartLineItem(newCartLineItem);
                cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
            }
        } else {
            newCartLineItem.setProduct(product);
            newCartLineItem.setEmployee(employee);
            newCartLineItem.setQuantity(1);
            productManagementRemote.createCartLineItem(newCartLineItem);
            cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        }
    }
    
    public void deductProductFromCart(Product product) {
        cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        if (cartLineItems != null && !cartLineItems.isEmpty()) {
            for (int i = 0; i < cartLineItems.size(); i++) {
                if (cartLineItems.get(i).getProduct().equals(product)) {
                    CartLineItem newCartLineItem = new CartLineItem();
                    newCartLineItem.setCartLineItemId(cartLineItems.get(i).getCartLineItemId());
                    newCartLineItem.setEmployee(employee);
                    newCartLineItem.setProduct(product);
                    int quantity = cartLineItems.get(i).getQuantity() - 1;
                    if (quantity < 0) {
                        quantity = 0;
                    }
                    newCartLineItem.setQuantity(quantity);
                    productManagementRemote.updateCartLineItem(newCartLineItem);
                }
            }
        }
        cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
    }
    
    public void updateQuantity(Long cartLineItemId, Integer newValue){
        CartLineItem updateItem = productManagementRemote.viewCartLineItemByCartLineItemId(cartLineItemId);
        updateItem.setQuantity(newValue);
        productManagementRemote.updateCartLineItem(updateItem);
    }
    
    public void addCartItem(Product product){
        Boolean isThere = false;
        cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        if (cartLineItems != null && !cartLineItems.isEmpty()) {
            for (int i = 0; i < cartLineItems.size(); i++) {
                if (cartLineItems.get(i).getProduct().equals(product)) {
                    CartLineItem newCartLineItem = new CartLineItem();
                    newCartLineItem.setCartLineItemId(cartLineItems.get(i).getCartLineItemId());
                    newCartLineItem.setEmployee(employee);
                    newCartLineItem.setProduct(product);
                    int quantity = cartLineItems.get(i).getQuantity() + 1;
                    newCartLineItem.setQuantity(quantity);
                    productManagementRemote.updateCartLineItem(newCartLineItem);
                    cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
                    isThere = true;
                }
            }
            if (!isThere) {
                newCartLineItem.setProduct(product);
                newCartLineItem.setEmployee(employee);
                newCartLineItem.setQuantity(1);
                productManagementRemote.createCartLineItem(newCartLineItem);
                cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
            }
        } else {
            newCartLineItem.setProduct(product);
            newCartLineItem.setEmployee(employee);
            newCartLineItem.setQuantity(1);
            productManagementRemote.createCartLineItem(newCartLineItem);
            cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
        }
    }
    
    public void dateValidation(SelectEvent event){
        Date newDate = new Date();
        newDate.setHours(0);
        newDate.setMinutes(0);
        newDate.setSeconds(0);
        System.out.println("time = " + (deliveryDateTime.getTime() - newDate.getTime()));
        System.out.println("time = " + validateString);
        if (deliveryDateTime.getTime() - newDate.getTime() <= 172800000){
            validateString = "Your transaction requires approval since the date is too near. Are you sure?";
        } else {
            validateString = "Are you sure?";
        }
        System.out.println("time2 = " + validateString);
    }
    
    public void deleteCartLineItem(Long cartLineItemId){
        productManagementRemote.deleteCartLineItem(cartLineItemId);
        cartLineItems = productManagementRemote.viewAllCartLineItemByEmployeeId(employee.getEmployeeId());
    }
    
    public List<CartLineItem> getCartLineItems() {
        return cartLineItems;
    }
    
    public void setCartLineItems(List<CartLineItem> cartLineItems) {
        this.cartLineItems = cartLineItems;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalAmount) {
        this.totalPrice = totalAmount;
    }
    
    public CartLineItem getNewCartLineItem() {
        return newCartLineItem;
    }
    
    public void setNewCartLineItem(CartLineItem newCartLineItem) {
        this.newCartLineItem = newCartLineItem;
    }
    
    public CartLineItem getCartLineItemToRemove() {
        return cartLineItemToRemove;
    }
    
    public void setCartLineItemToRemove(CartLineItem cartLineItemToRemove) {
        this.cartLineItemToRemove = cartLineItemToRemove;
    }
    
    public CartLineItem getCartLineItemAfterEdit() {
        return cartLineItemAfterEdit;
    }
    
    public void setCartLineItemAfterEdit(CartLineItem cartLineItemAfterEdit) {
        this.cartLineItemAfterEdit = cartLineItemAfterEdit;
    }
    
    public List<CartLineItem> getReadyToPayCartItems() {
        return readyToPayCartItems;
    }
    
    public void setReadyToPayCartItems(List<CartLineItem> readyToPayCartItems) {
        this.readyToPayCartItems = readyToPayCartItems;
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
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public String getOrderCompleteDate() {
        return orderCompleteDate;
    }
    
    public void setOrderCompleteDate(String orderCompleteDate) {
        this.orderCompleteDate = orderCompleteDate;
    }
    
    public Date getDeliveryDateTime() {
        return deliveryDateTime;
    }
    
    public void setDeliveryDateTime(Date deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }
    
    public int getDeliveryHour() {
        return deliveryHour;
    }
    
    public void setDeliveryHour(int deliveryHour) {
        this.deliveryHour = deliveryHour;
    }
    
    public Date getToday() {
        return today;
    }
    
    public void setToday(Date today) {
        this.today = today;
    }
    
    public String getValidateString() {
        return validateString;
    }
    
    public void setValidateString(String validateString) {
        this.validateString = validateString;
    }
}
