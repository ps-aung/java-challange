package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    EmployeeService service;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        service = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    public void testRetrieveEmployees() throws Exception {
        // given
        Employee employee1 = getEmployee();
        Employee employee2 = getEmployee();

        // when
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));
        List<Employee> employeeList = service.retrieveEmployees();

        // then
        assertEquals(2, employeeList.size());
    }


    @Test
    public void testGetEmployee() {
        // given
        Employee employee = getEmployee();

        // when
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        Employee result = service.getEmployee(1L);

        // then
        assertEquals("Test1", result.getName());

    }


    @Test
    public void testSaveEmployee() {
        // given
        Employee employee = getEmployee();

        // when
        when(employeeRepository.save(any())).thenReturn(employee);

        // then
        Employee result = employeeRepository.save(employee);
        assertEquals("Test1",result.getName());
        assertEquals("Software",result.getDepartment());
        verify(employeeRepository, times(1)).save(employee);
    }


    @Test
    public void testDeleteEmployee() {
        // given
        Long id = 1L;

        employeeRepository.deleteById(id);

        // then
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testUpdateEmployee() {
        // given
        Employee employee = getEmployee();

        // when
        when(employeeRepository.save(any())).thenReturn(employee);

        // then
        Employee result = employeeRepository.save(employee);
        assertEquals("Test1",result.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    // Dummy Employee
    private static Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test1");
        employee.setSalary(2000);
        employee.setDepartment("Software");
        return employee;
    }
}