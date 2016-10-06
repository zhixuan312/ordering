package ManagedBean;

import ProductManagement.ProductManagementRemote;
import entity.Employee;
import entity.Product;
import entity.ProductCategory;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.model.UploadedFile;

@Named(value = "productManagedBean")
@RequestScoped

public class ProductManagedBean implements Serializable {
    
    @EJB
    private ProductManagementRemote productManagmentRemote;
    @Inject
    private SignUpAndLoginManagedBean signUpAndLoginManagedBean;
    
    private Employee employee;
    private Product product;
    private ProductCategory productCategory;
    private String productCat;
    private List<String> catName;
    private List<Product> productsTotal;
    private List<ProductCategory> productCategoriesTotal;
    private UploadedFile picture;
    
    public ProductManagedBean() {
        employee = new Employee();
        product = new Product();
        productCat = "";
        productCategory = new ProductCategory();
        productsTotal = new ArrayList<>();
        productCategoriesTotal = new ArrayList<>();
        catName = new ArrayList<>();
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
        if (!employee.getIsAdmin()){
            try {
                ec.redirect("index.xhtml?faces-redirect=true");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        productsTotal = productManagmentRemote.viewAllProduct();
        productCategoriesTotal = productManagmentRemote.viewAllProductCategory();
        for (int i = 0; i < productCategoriesTotal.size(); i ++) {
            catName.add("Category" +(i+1) + ": " +productCategoriesTotal.get(i).getName());
        }
    }

    
    public void createProduct() {
        if (employee.getIsAdmin()) {
            String id = productCat.substring(8, 9);
            product.setProductCategory(productManagmentRemote.viewProductCategoryByProductCategoryId(Long.valueOf(id)));
            long productId = productManagmentRemote.createProduct(product);
            productsTotal.add(productManagmentRemote.viewProductByProductId(productId));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully!", "New product created successfully!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please login with admin", "Please login with admin"));
        }
    }
    
    public void cancelCreateProduct(ActionEvent event) {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            
            ec.redirect("product.xhtml?faces-redirect=true");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateProduct(ActionEvent event) {
        if (employee.getIsAdmin()) {
            if (productManagmentRemote.updateProduct(product)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Success!"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please login with admin", "Please login with admin"));
            }
        }
    }
    
    public void deleteProduct(ActionEvent event) {
        if (employee.getIsAdmin()) {
            Product productToDelete = (Product) event.getComponent().getAttributes().get("productToDelete");
            if (productManagmentRemote.deleteProduct(productToDelete.getProductId())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Success!"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fail to update", "Fail to update"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please login with admin", "Please login with admin"));
        }
    }
    
    public void createProductCategory(ActionEvent event) {
        if (employee.getIsAdmin()) {
            long productCategoryId = productManagmentRemote.createProductCategory(productCategory);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product category created successfully!", "New product category created successfully!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please login with admin", "Please login with admin"));
        }
    }
    
    public void cancelCreateProductCategory(ActionEvent event) {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            
            ec.redirect("product.xhtml?faces-redirect=true");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateProductCategory(ActionEvent event) {
        if (employee.getIsAdmin()) {
            if (productManagmentRemote.updateProductCategory(productCategory)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Success!"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please login with admin", "Please login with admin"));
            }
        }
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Employee getAdmin() {
        return employee;
    }
    
    public void setAdmin(Employee admin) {
        this.employee = admin;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public ProductManagementRemote getProductManagmentRemote() {
        return productManagmentRemote;
    }
    
    public void setProductManagmentRemote(ProductManagementRemote productManagmentRemote) {
        this.productManagmentRemote = productManagmentRemote;
    }
    
    public SignUpAndLoginManagedBean getSignUpAndLoginManagedBean() {
        return signUpAndLoginManagedBean;
    }
    
    public void setSignUpAndLoginManagedBean(SignUpAndLoginManagedBean signUpAndLoginManagedBean) {
        this.signUpAndLoginManagedBean = signUpAndLoginManagedBean;
    }
    
    public List<Product> getProductsTotal() {
        return productsTotal;
    }
    
    public void setProductsTotal(List<Product> productsTotal) {
        this.productsTotal = productsTotal;
    }
    
    public ProductCategory getProductCategory() {
        return productCategory;
    }
    
    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
    
    public List<ProductCategory> getProductCategoriesTotal() {
        return productCategoriesTotal;
    }
    
    public void setProductCategoriesTotal(List<ProductCategory> productCategoriesTotal) {
        this.productCategoriesTotal = productCategoriesTotal;
    }
    
    public String getProductCat() {
        return productCat;
    }
    
    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }
    
    public List<String> getCatName() {
        return catName;
    }
    
    public void setCatName(List<String> catName) {
        this.catName = catName;
    }
    
    public UploadedFile getPicture() {
        return picture;
    }
    
    public void setPicture(UploadedFile picture) {
        this.picture = picture;
    }
}
