package jp.co.axa.apidemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    public static final String BASE_URL = "/api/v1/employees";

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController employeeController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .build();
    }

    @Test
    public void testGetEmployee() throws Exception {
        // given
        Employee employee1 = getEmployee();
        Employee employee2 = getEmployee();

        // when
        when(employeeService.retrieveEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        // then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL)
									.contentType(MediaType.APPLICATION_JSON))
									.andExpect(status().isOk())
									.andExpect(jsonPath("$.*", hasSize(2)))
									.andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());

    }

    @Test
    public void saveEmployee() throws Exception {
        // given
        Employee employee = getEmployee();

        // when
        when(employeeService.saveEmployee(any())).thenReturn(employee);

        // then
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL)
									.contentType(MediaType.APPLICATION_JSON)
									.content(asJsonString(employee)))
									.andExpect(status().isCreated())
									.andExpect(jsonPath("$.name", equalTo("Test1")))
									.andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void deleteEmployee() throws Exception {
		mockMvc.perform(delete(BASE_URL+"/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(employeeService,times(1)).deleteEmployee(anyLong());
    }

    @Test
    public void updateEmployee() throws Exception {
        // given
        Employee employee = getEmployee();

        // when
        when(employeeService.updateEmployee(any(),anyLong())).thenReturn(employee);

        // then
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL+"/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(employee)))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.name", equalTo("Test1")))
                                    .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    private static Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test1");
        employee.setSalary(2000);
        employee.setDepartment("Software");
        return employee;
    }

	private static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
