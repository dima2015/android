package com.plunner.plunner.retrofit.Employee;

import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.models.models.employee.utils.LoadResource;
import com.plunner.plunner.retrofit.RetrofitTest;

import java.util.List;

/**
 * Created by claudio on 22/02/16.
 */
public class MeetingsTest extends RetrofitTest {
    public void testGet() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"134\",\"title\":\"2Msad1\",\"description\":\"1312312321312\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 06:15:00\",\"duration\":\"900\",\"created_at\":\"2015-12-31 15:17:17\",\"updated_at\":\"2016-01-11 20:15:05\"},{\"id\":\"136\",\"title\":\"sadas\",\"description\":\"sadasd\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:41:02\",\"updated_at\":\"2016-01-09 17:23:35\"},{\"id\":\"138\",\"title\":\"12312\",\"description\":\"312312\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:42:51\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"346\",\"title\":\"Nuovo Meeting\",\"description\":\"hjsakdjhsa\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-11 19:34:55\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"460\",\"title\":\"Meeting\",\"description\":\"asdasd\",\"group_id\":\"45\",\"start_time\":\"2016-02-06 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-30 10:38:39\",\"updated_at\":\"2016-01-31 00:00:22\"}]");
        Execution<ModelList<Meeting>> executionM = new Execution<>();
        LoadResource<ModelList<Meeting>> meetings = executionE.getModel().getMeetings();
        meetings.load(executionM.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/meetings/");
        List<Meeting> meetingsList = meetings.getInstance().getModels();
        assertEquals(5, meetingsList.size()); //number of groups
        Meeting meeting = meetingsList.get(0);
        assertEquals("134", meeting.getId()); //just to verify if meeting is loaded
    }
}
