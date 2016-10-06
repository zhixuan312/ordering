package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Transaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectDelivery;
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualDelivery;
    private String status;
    private BigDecimal totalCharge;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Employee employee;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transaction")
    private List<TransactionLineItem> transactionLineItems;
    
    public Transaction() {
        transactionDateTime = new Date();
        expectDelivery = new Date();
        actualDelivery = new Date();
        totalCharge = new BigDecimal(0);
        employee = new Employee();
        status = "";
        this.transactionLineItems = new ArrayList<>();
    }
    
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public Date getTransactionDateTime() {
        return transactionDateTime;
    }
    
    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
    
    public BigDecimal getTotalCharge() {
        return totalCharge;
    }
    
    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public List<TransactionLineItem> getTransactionLineItems() {
        return transactionLineItems;
    }
    
    public void setTransactionLineItems(List<TransactionLineItem> transactionLineItems) {
        this.transactionLineItems = transactionLineItems;
    }

    public Date getExpectDelivery() {
        return expectDelivery;
    }

    public void setExpectDelivery(Date expectDelivery) {
        this.expectDelivery = expectDelivery;
    }

    public Date getActualDelivery() {
        return actualDelivery;
    }

    public void setActualDelivery(Date actualDelivery) {
        this.actualDelivery = actualDelivery;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionId fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entity.Transaction[ id=" + transactionId + " ]";
    }
    
}
