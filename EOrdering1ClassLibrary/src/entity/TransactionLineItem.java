
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TransactionLineItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionLineItemId;
    private Integer expectQuantity;
    private Integer actualQuantity;
    private Double unitCharge;
    private Double totalCharge;
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualDelivery;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Transaction transaction;
    
    public TransactionLineItem() {
    }
    
    public Long getTransactionLineItemId() {
        return transactionLineItemId;
    }
    
    public void setTransactionLineItemId(Long transactionLineItemId) {
        this.transactionLineItemId = transactionLineItemId;
    }

    public Integer getExpectQuantity() {
        return expectQuantity;
    }

    public void setExpectQuantity(Integer expectQuantity) {
        this.expectQuantity = expectQuantity;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }
    
    public Transaction getTransaction() {
        return transaction;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Double getUnitCharge() {
        return unitCharge;
    }
    
    public void setUnitCharge(Double unitCharge) {
        this.unitCharge = unitCharge;
    }
    
    public Double getTotalCharge() {
        return totalCharge;
    }
    
    public void setTotalCharge(Double totalCharge) {
        this.totalCharge = totalCharge;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Date getActualDelivery() {
        return actualDelivery;
    }
    
    public void setActualDelivery(Date actualDelivery) {
        this.actualDelivery = actualDelivery;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionLineItemId != null ? transactionLineItemId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionLineItemId fields are not set
        if (!(object instanceof TransactionLineItem)) {
            return false;
        }
        TransactionLineItem other = (TransactionLineItem) object;
        if ((this.transactionLineItemId == null && other.transactionLineItemId != null) || (this.transactionLineItemId != null && !this.transactionLineItemId.equals(other.transactionLineItemId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entity.TransactionLineItem[ id=" + transactionLineItemId + " ]";
    }
    
}
