package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CartLineItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartLineItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Employee employee;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;
    
    
    public CartLineItem() {
        quantity = new Integer(0);
        unitPrice = new BigDecimal(0);
        subTotal = new BigDecimal(0);
        employee = new Employee();
        product = new Product();
    }
    
    public Long getCartLineItemId() {
        return cartLineItemId;
    }
    
    public void setCartLineItemId(Long cartLineItemId) {
        this.cartLineItemId = cartLineItemId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal getSubTotal() {
        return subTotal;
    }
    
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cartLineItemId != null ? cartLineItemId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cartLineItemId fields are not set
        if (!(object instanceof CartLineItem)) {
            return false;
        }
        CartLineItem other = (CartLineItem) object;
        if ((this.cartLineItemId == null && other.cartLineItemId != null) || (this.cartLineItemId != null && !this.cartLineItemId.equals(other.cartLineItemId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entity.CartLineItem[ id=" + cartLineItemId + " ]";
    }
    
}
