package AccountManagement;

import entity.Employee;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateful
@Local(AccountManagementLocal.class)
@Remote(AccountManagementRemote.class)
public class AccountManagement implements AccountManagementRemote, AccountManagementLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    private Employee employee;
    
    public AccountManagement(){
        employee = new Employee();
    }
    
    @Override
    public boolean employeeLogin(String email, String password) {
        employee = retrieveEmployeeByEmail(email);
        
        if (employee != null) {
            if (employee.getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public Boolean logout() {
        employee = null;
        return true;
    }
    
    @Override
    public String createNewEmployee(Employee employee) {
        boolean sameEmail = false;
        List<Employee> list = new ArrayList<>();
        try {
            String jpql = "SELECT e FROM Employee e";
            Query query = em.createQuery(jpql);
            list = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmail().equals(employee.getEmail())) {
                sameEmail = true;
            }
        }
        if (!sameEmail) {
            String verificationCode = Integer.toString(1000 + (int) (Math.random() * 9999));
            employee.setVerificationCode(verificationCode);
            employee.setAccountStatus("Pending Verification");
            employee.setCreateDate(new Date());
            em.persist(employee);
            em.flush();
            
            return verificationCode;
        } else {
            return "-1";
        }
    }
    
    @Override
    public Employee retrieveEmployeeByEmail(String email) {
        try {
            String jpql = "SELECT e FROM Employee e WHERE e.email = " + "'" + email + "'";
            Query query = em.createQuery(jpql);
            return (Employee) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new Employee();
        }
    }
    
    @Override
    public Employee retrieveEmployeeByEmployeeId (Long employeeId) {
        try {
            String jpql = "SELECT e FROM Employee e WHERE e.employeeId = " + employeeId;
            Query query = em.createQuery(jpql);
            return (Employee) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new Employee();
        }
    }
    
    @Override
    public Boolean updateEmployeeProfile(Employee employee) {
        try {
            em.merge(employee);
            em.flush();
            if (this.employee.getEmployeeId().equals(employee.getEmployeeId())){
                this.employee = employee;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Employee> viewAllRecordedEmployee() {
        List<Employee> employees = null;
        try {
            String jpql = "SELECT e FROM Employee e";
            Query query = em.createQuery(jpql);
            employees = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return employees;
    }
    
    @Override
    public Boolean deleteEmployee(Long employeeId) {
        try {
            Employee employee = em.find(Employee.class, employeeId);
            em.remove(employee);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
