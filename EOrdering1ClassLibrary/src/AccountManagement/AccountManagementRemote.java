package AccountManagement;

import entity.Employee;
import java.util.List;

public interface AccountManagementRemote {
    
    public boolean employeeLogin(String email, String password);
    
    public Boolean logout();
    
    public String createNewEmployee(Employee employee);
    
    public Employee retrieveEmployeeByEmail(String email);
    
    public Boolean updateEmployeeProfile(Employee employee) ;
    
    public Employee retrieveEmployeeByEmployeeId (Long employeeId);
    
    public List<Employee> viewAllRecordedEmployee();
    
    public Boolean deleteEmployee(Long employeeId);
    
    public Employee getEmployee();
    
    public void setEmployee(Employee employee);
    
}
