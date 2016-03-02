package com.plunner.plunner.retrofit.Employee;

import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Caldav;
import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.utils.LoadResource;
import com.plunner.plunner.retrofit.RetrofitTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by claudio on 22/02/16.
 */
public class CalendarsTest extends RetrofitTest {
    public void testGet() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"102\",\"name\":\"Dr. Neha Beier\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"caldav\":null},{\"id\":\"106\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"106\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"NotExists\",\"sync_errors\":\"calendar inserted doesn't exist\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-01-28 13:00:10\"}},{\"id\":\"108\",\"name\":\"test3\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"108\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"test3\",\"sync_errors\":\"\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-16 11:20:11\"}},{\"id\":\"109\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"109\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"personale\",\"sync_errors\":\"\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-16 10:50:05\"}},{\"id\":\"450\",\"name\":\"contact_birthdays\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"450\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"contact_birthdays\",\"sync_errors\":\"\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-01-28 13:00:11\"}},{\"id\":\"645\",\"name\":\"sdasd\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-20 17:08:42\",\"updated_at\":\"2016-01-20 17:08:42\",\"caldav\":null}]");
        Execution<ModelList<Calendar>> execution = new Execution<>();
        LoadResource<ModelList<Calendar>> calendars = executionE.getModel().getCalendars();
        calendars.load(execution.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/");
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        assertEquals(6, calendarsList.size()); //number of groups
        assertNull(calendarsList.get(0).getCaldav()); //caldav check
        assertNotNull(calendarsList.get(1).getCaldav()); //caldav check
        Calendar calendar = calendarsList.get(1);
        Caldav caldav = calendar.getCaldav();
        assertEquals("106", calendar.getId()); //just to verify if calendar is loaded
        assertEquals("106", caldav.getCalendarId()); //just to verify if caldav is loaded
    }

    public void testPut() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"102\",\"name\":\"Dr. Neha Beier\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"caldav\":null},{\"id\":\"106\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"106\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"NotExists\",\"sync_errors\":\"calendar inserted doesn't exist\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-01-28 13:00:10\"}},{\"id\":\"108\",\"name\":\"test3\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"108\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"test3\",\"sync_errors\":\"\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-16 11:20:11\"}},{\"id\":\"109\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"109\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"personale\",\"sync_errors\":\"\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-16 10:50:05\"}},{\"id\":\"450\",\"name\":\"contact_birthdays\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"450\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"contact_birthdays\",\"sync_errors\":\"\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-01-28 13:00:11\"}},{\"id\":\"645\",\"name\":\"sdasd\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-20 17:08:42\",\"updated_at\":\"2016-01-20 17:08:42\",\"caldav\":null}]");
        Execution<ModelList<Calendar>> execution = new Execution<>();
        LoadResource<ModelList<Calendar>> calendars = executionE.getModel().getCalendars();
        calendars.load(execution.getCallback());
        lock();
        assertOK();
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        Calendar calendar = calendarsList.get(0);
        setResponse("{\"id\":\"102\",\"name\":\"NewName\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"caldav\":null}");
        calendar.setName("NewName");
        Execution<Calendar> execution2 = new Execution<>();
        calendar.save(execution2.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/102/");
        assertMethod("PUT");
        Map<String, String> body = new HashMap<>();
        body.put("name", "NewName");
        body.put("enabled", "0");
        assertRequestBody(body);
        assertEquals("NewName", execution2.getModel().getName());
    }

    public void testPost() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"102\",\"name\":\"Dr. Neha Beier\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"caldav\":null},{\"id\":\"106\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"106\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"NotExists\",\"sync_errors\":\"calendar inserted doesn't exist\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-01-28 13:00:10\"}},{\"id\":\"108\",\"name\":\"test3\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"108\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"test3\",\"sync_errors\":\"\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-16 11:20:11\"}},{\"id\":\"109\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"109\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"personale\",\"sync_errors\":\"\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-16 10:50:05\"}},{\"id\":\"450\",\"name\":\"contact_birthdays\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"450\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"contact_birthdays\",\"sync_errors\":\"\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-01-28 13:00:11\"}},{\"id\":\"645\",\"name\":\"sdasd\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-20 17:08:42\",\"updated_at\":\"2016-01-20 17:08:42\",\"caldav\":null}]");
        Execution<ModelList<Calendar>> execution = new Execution<>();
        LoadResource<ModelList<Calendar>> calendars = executionE.getModel().getCalendars();
        calendars.load(execution.getCallback());
        lock();
        assertOK();
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        Calendar calendar = calendarsList.get(0);
        setResponse("{\"id\":\"400\",\"name\":\"NewName\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"caldav\":null}");
        calendar.setName("NewName");
        calendar.setId(null);
        Execution<Calendar> execution2 = new Execution<>();
        calendar.save(execution2.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/");
        assertMethod("POST");
        Map<String, String> body = new HashMap<>();
        body.put("name", "NewName");
        body.put("enabled", "0");
        assertRequestBody(body);
        assertEquals("NewName", execution2.getModel().getName());
        assertEquals("400", execution2.getModel().getId());
    }

    public void testDelete() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"102\",\"name\":\"Dr. Neha Beier\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"caldav\":null},{\"id\":\"106\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"0\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"106\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"NotExists\",\"sync_errors\":\"calendar inserted doesn't exist\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-01-28 13:00:10\"}},{\"id\":\"108\",\"name\":\"test3\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"108\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"test3\",\"sync_errors\":\"\",\"created_at\":\"2016-01-01 16:26:30\",\"updated_at\":\"2016-02-16 11:20:11\"}},{\"id\":\"109\",\"name\":\"personale\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-24 15:00:07\",\"caldav\":{\"calendar_id\":\"109\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"personale\",\"sync_errors\":\"\",\"created_at\":\"2016-01-06 23:58:48\",\"updated_at\":\"2016-02-16 10:50:05\"}},{\"id\":\"450\",\"name\":\"contact_birthdays\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-02-02 14:30:05\",\"caldav\":{\"calendar_id\":\"450\",\"url\":\"https:\\/\\/owncloud.thecsea.it\\/owncloud\\/remote.php\\/caldav\\/\",\"username\":\"testCalDav\",\"calendar_name\":\"contact_birthdays\",\"sync_errors\":\"\",\"created_at\":\"2016-01-12 10:33:45\",\"updated_at\":\"2016-01-28 13:00:11\"}},{\"id\":\"645\",\"name\":\"sdasd\",\"employee_id\":\"34\",\"enabled\":\"1\",\"created_at\":\"2016-01-20 17:08:42\",\"updated_at\":\"2016-01-20 17:08:42\",\"caldav\":null}]");
        Execution<ModelList<Calendar>> execution = new Execution<>();
        LoadResource<ModelList<Calendar>> calendars = executionE.getModel().getCalendars();
        calendars.load(execution.getCallback());
        lock();
        assertOK();
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        Calendar calendar = calendarsList.get(0);
        setResponse("{}");
        Execution<Calendar> execution2 = new Execution<>();
        calendar.delete(execution2.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/102/");
        assertMethod("DELETE");
        assertNull(execution2.getModel().getId());
    }
}
