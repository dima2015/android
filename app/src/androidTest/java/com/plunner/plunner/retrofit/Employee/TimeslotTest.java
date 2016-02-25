package com.plunner.plunner.retrofit.Employee;

import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.Timeslot;
import com.plunner.plunner.models.models.employee.utility.LoadResource;
import com.plunner.plunner.retrofit.RetrofitTest;

import java.util.List;

/**
 * Created by claudio on 22/02/16.
 */
public class TimeslotTest extends RetrofitTest {
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
        Calendar calendar = calendarsList.get(0);
        setResponse("[{\"id\":\"304\",\"time_start\":\"1972-11-30 04:13:00\",\"time_end\":\"1972-03-18 07:23:42\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"305\",\"time_start\":\"2005-09-02 02:24:19\",\"time_end\":\"1998-10-26 06:53:40\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"306\",\"time_start\":\"1995-06-30 16:57:37\",\"time_end\":\"1971-07-03 23:57:34\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"}]");
        Execution<ModelList<Timeslot>> execution2 = new Execution<>();
        LoadResource<ModelList<Timeslot>> timeslots = calendar.getTimeslots();
        timeslots.load(execution2.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/102/timeslots/");
        List<Timeslot> timeslotsList = timeslots.getInstance().getModels();
        assertEquals(3, timeslotsList.size()); //number of groups
        Timeslot timeslot = timeslotsList.get(0);
        assertEquals("304", timeslot.getId()); //just to verify if timeslot is loaded
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
        assertUrl("/employees/calendars/");
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        Calendar calendar = calendarsList.get(0);
        setResponse("[{\"id\":\"304\",\"time_start\":\"1972-11-30 04:13:00\",\"time_end\":\"1972-03-18 07:23:42\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"305\",\"time_start\":\"2005-09-02 02:24:19\",\"time_end\":\"1998-10-26 06:53:40\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"306\",\"time_start\":\"1995-06-30 16:57:37\",\"time_end\":\"1971-07-03 23:57:34\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"}]");
        Execution<ModelList<Timeslot>> execution2 = new Execution<>();
        LoadResource<ModelList<Timeslot>> timeslots = calendar.getTimeslots();
        timeslots.load(execution2.getCallback());
        lock();
        assertOK();
        List<Timeslot> timeslotsList = timeslots.getInstance().getModels();
        Timeslot timeslot = timeslotsList.get(0);
        setResponse("{\"id\":\"304\",\"time_start\":\"1970-11-30 04:13:00\",\"time_end\":\"1972-03-18 07:23:42\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"}");
        timeslot.setTimeStart("1970-11-30 04:13:00");
        Execution<Timeslot> execution3 = new Execution<>();
        timeslot.save(execution3.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/102/timeslots/304/");
        assertMethod("PUT");
        assertRequestBody("time_start=1970-11-30%2004%3A13%3A00&time_end=1972-03-18%2007%3A23%3A42");
        assertEquals("1970-11-30 04:13:00", execution3.getModel().getTimeStart());
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
        assertUrl("/employees/calendars/");
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        Calendar calendar = calendarsList.get(0);
        setResponse("[{\"id\":\"304\",\"time_start\":\"1972-11-30 04:13:00\",\"time_end\":\"1972-03-18 07:23:42\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"305\",\"time_start\":\"2005-09-02 02:24:19\",\"time_end\":\"1998-10-26 06:53:40\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"306\",\"time_start\":\"1995-06-30 16:57:37\",\"time_end\":\"1971-07-03 23:57:34\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"}]");
        Execution<ModelList<Timeslot>> execution2 = new Execution<>();
        LoadResource<ModelList<Timeslot>> timeslots = calendar.getTimeslots();
        timeslots.load(execution2.getCallback());
        lock();
        assertOK();
        List<Timeslot> timeslotsList = timeslots.getInstance().getModels();
        Timeslot timeslot = timeslotsList.get(0);
        setResponse("{\"id\":\"400\",\"time_start\":\"1970-11-30 04:13:00\",\"time_end\":\"1972-03-18 07:23:42\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"}");
        timeslot.setTimeStart("1970-11-30 04:13:00");
        timeslot.setId(null);
        Execution<Timeslot> execution3 = new Execution<>();
        timeslot.save(execution3.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/102/timeslots/");
        assertMethod("POST");
        assertRequestBody("time_start=1970-11-30%2004%3A13%3A00&time_end=1972-03-18%2007%3A23%3A42");
        assertEquals("1970-11-30 04:13:00", execution3.getModel().getTimeStart());
        assertEquals("400", execution3.getModel().getId());
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
        assertUrl("/employees/calendars/");
        List<Calendar> calendarsList = calendars.getInstance().getModels();
        Calendar calendar = calendarsList.get(0);
        setResponse("[{\"id\":\"304\",\"time_start\":\"1972-11-30 04:13:00\",\"time_end\":\"1972-03-18 07:23:42\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"305\",\"time_start\":\"2005-09-02 02:24:19\",\"time_end\":\"1998-10-26 06:53:40\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"},{\"id\":\"306\",\"time_start\":\"1995-06-30 16:57:37\",\"time_end\":\"1971-07-03 23:57:34\",\"calendar_id\":\"102\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\"}]");
        Execution<ModelList<Timeslot>> execution2 = new Execution<>();
        LoadResource<ModelList<Timeslot>> timeslots = calendar.getTimeslots();
        timeslots.load(execution2.getCallback());
        lock();
        assertOK();
        List<Timeslot> timeslotsList = timeslots.getInstance().getModels();
        Timeslot timeslot = timeslotsList.get(0);
        setResponse("{}");
        Execution<Timeslot> execution3 = new Execution<>();
        timeslot.delete(execution3.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/calendars/102/timeslots/304/");
        assertMethod("DELETE");
        assertNull(execution3.getModel().getId());
    }
}