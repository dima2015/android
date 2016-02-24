package com.plunner.plunner.retrofit;

import com.plunner.plunner.models.models.employee.Employee;

/**
 * Created by claudio on 22/02/16.
 */
public class EmployeeTest extends RetrofitTest {
    public void testAsync() throws InterruptedException {
        InterceptorClient interceptorClient = initialise("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).get(executionE.getCallback());
        lockClass.lock();
        assertTrue(executionE.isExecuted());
        assertEquals(0, executionE.getStatus());
        assertFalse(executionE.isError());
        assertEquals("testEmp", executionE.getModel().getName());
        assertEquals("testEmp@test.com", executionE.getModel().getEmail());
        assertEquals("11", executionE.getModel().getCompanyId());
        assertEquals("34", executionE.getModel().getId());
        assertEquals("2015-12-30 21:31:43", executionE.getModel().getCreatedAt());
        assertEquals("2015-12-30 21:31:43", executionE.getModel().getUpdatedAt());
        assertEquals("/employees/employee/", interceptorClient.getUri().getPath());
        //TODO improve calling one method that perform all checks
    }
}