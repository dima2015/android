package com.plunner.plunner.retrofit.Employee;

import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.retrofit.RetrofitTest;

/**
 * Created by claudio on 22/02/16.
 */
public class EmployeeTest extends RetrofitTest {
    public void testGet() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/employee/");
        assertEquals("testEmp", executionE.getModel().getName());
        assertEquals("testEmp@test.com", executionE.getModel().getEmail());
        assertEquals("11", executionE.getModel().getCompanyId());
        assertEquals("34", executionE.getModel().getId());
        assertEquals("2015-12-30 21:31:43", executionE.getModel().getCreatedAt());
        assertEquals("2015-12-30 21:31:43", executionE.getModel().getUpdatedAt());
    }

    public void testFactoryPlanner() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/employee/");
        assertTrue(executionE.getModel() instanceof Planner);
    }

    public void testFactoryEmployee() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":false}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/employee/");
        assertFalse(executionE.getModel() instanceof Planner);
    }
}
