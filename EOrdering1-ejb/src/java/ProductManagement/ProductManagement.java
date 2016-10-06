package ProductManagement;

import entity.CartLineItem;
import entity.Product;
import entity.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(ProductManagementLocal.class)
@Remote(ProductManagementRemote.class)
public class ProductManagement implements ProductManagementRemote, ProductManagementLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    private CartLineItem cartLineItem;
    
    @Override
    public Long createProduct(Product product) {
        try {
            em.persist(product);
            em.flush();
            return product.getProductId();
        } catch (Exception e) {
            e.printStackTrace();
            return new Long(-1);
        }
    }
    
    @Override
    public Boolean updateProduct(Product product) {
        try {
            em.merge(product);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Product> viewAllProduct() {
        List<Product> products = null;
        try {
            String jpql = "SELECT p FROM Product p";
            Query query = em.createQuery(jpql);
            products = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return products;
    }
    
    @Override
    public Product viewProductByProductId(Long productId) {
        try {
            String jpql = "SELECT p FROM Product p WHERE p.productId = " + productId;
            Query query = em.createQuery(jpql);
            return (Product) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new Product();
        }
    }
    
    @Override
    public Boolean deleteProduct(Long productId) {
        try {
            Product product = em.find(Product.class, productId);
            em.remove(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Long createProductCategory(ProductCategory productCategory) {
        try {
            em.persist(productCategory);
            em.flush();
            return productCategory.getProductCategoryId();
        } catch (Exception e) {
            e.printStackTrace();
            return new Long(-1);
        }
    }
    
    @Override
    public Boolean updateProductCategory(ProductCategory productCategory) {
        try {
            em.merge(productCategory);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<ProductCategory> viewAllProductCategory() {
        List<ProductCategory> productCategories = null;
        try {
            String jpql = "SELECT pc FROM ProductCategory pc";
            Query query = em.createQuery(jpql);
            productCategories = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return productCategories;
    }
    
    @Override
    public ProductCategory viewProductCategoryByProductCategoryId(Long productCategoryId) {
        try {
            String jpql = "SELECT pc FROM ProductCategory pc WHERE pc.productCategoryId = " + productCategoryId;
            Query query = em.createQuery(jpql);
            return (ProductCategory) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new ProductCategory();
        }
    }
    
    @Override
    public Boolean deleteProductCategory(Long productCategoryId) {
        try {
            ProductCategory productCategory = em.find(ProductCategory.class, productCategoryId);
            em.remove(productCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Long createCartLineItem(CartLineItem cartLineItem) {
        try {
            em.persist(cartLineItem);
            em.flush();
            return cartLineItem.getCartLineItemId();
        } catch (Exception e) {
            e.printStackTrace();
            return new Long(-1);
        }
    }
    
    @Override
    public Boolean updateCartLineItem(CartLineItem cartLineItem) {
        try {
            em.merge(cartLineItem);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<CartLineItem> viewAllCartLineItemByEmployeeId(Long employeeId) {
        List<CartLineItem> cartLineItems = new ArrayList<>();
        try {
            String jpql = "SELECT cli FROM CartLineItem cli WHERE cli.employee.employeeId = '" + employeeId + "'";
            Query query = em.createQuery(jpql);
            cartLineItems = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return cartLineItems;
    }
    
    @Override
    public CartLineItem viewCartLineItemByCartLineItemId(Long cartLineItemId) {
        try {
            String jpql = "SELECT cli FROM CartLineItem cli WHERE cli.cartLineItemId = " + cartLineItemId;
            Query query = em.createQuery(jpql);
            return (CartLineItem) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new CartLineItem();
        }
    }
    
    @Override
    public Boolean deleteCartLineItem(Long cartLineItemId) {
        try {
            CartLineItem cartLineItem = em.find(CartLineItem.class, cartLineItemId);
            em.remove(cartLineItem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CartLineItem getCartLineItem() {
        return cartLineItem;
    }
    
    @Override
    public void setCartLineItem(CartLineItem cartLineItem) {
        this.cartLineItem = cartLineItem;
    }
}
