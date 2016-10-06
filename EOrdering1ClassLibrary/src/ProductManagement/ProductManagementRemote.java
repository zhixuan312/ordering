package ProductManagement;

import entity.CartLineItem;
import entity.Product;
import entity.ProductCategory;
import java.util.List;

public interface ProductManagementRemote {
    
    public Long createProduct(Product product);
    
    public Boolean updateProduct(Product product);
    
    public List<Product> viewAllProduct();
    
    public Boolean deleteProduct(Long productId);
    
    public Long createCartLineItem(CartLineItem cartLineItem);
    
    public Boolean updateCartLineItem(CartLineItem cartLineItem);
    
    public List<CartLineItem> viewAllCartLineItemByEmployeeId(Long employeeId);
    
    public Boolean deleteCartLineItem(Long cartLineItemId);
    
    public CartLineItem getCartLineItem();
    
    public void setCartLineItem(CartLineItem cartLineItem);
    
    public Boolean deleteProductCategory(Long productCategoryId);
    
    public List<ProductCategory> viewAllProductCategory();
    
    public Boolean updateProductCategory(ProductCategory productCategory);
    
    public Long createProductCategory(ProductCategory productCategory);
    
    public ProductCategory viewProductCategoryByProductCategoryId(Long productCategoryId);
    
    public Product viewProductByProductId(Long productId);
    
    public CartLineItem viewCartLineItemByCartLineItemId(Long cartLineItemId);
}
