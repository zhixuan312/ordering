package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Employee implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String accountStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee")
    private List<Transaction> transactions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee")
    private List<Address> addresses;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee")
    private List<CartLineItem> cartLineItems;
    private Boolean isAdmin;
    private String verificationCode;
    
    public Employee() {
        email = "";
        password = "";
        firstName = "";
        lastName = "";
        accountStatus = "";
        isAdmin = false;
        verificationCode = "";
        createDate = new Date();
        this.transactions = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.cartLineItems = new ArrayList<>();
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getAccountStatus() {
        return accountStatus;
    }
    
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    public List<Address> getAddresses() {
        return addresses;
    }
    
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    public List<CartLineItem> getCartLineItems() {
        return cartLineItems;
    }
    
    public void setCartLineItems(List<CartLineItem> cartLineItems) {
        this.cartLineItems = cartLineItems;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }
    
}
