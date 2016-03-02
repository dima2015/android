package com.plunner.plunner.retrofit.Employee.planner;

import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.planner.Group;
import com.plunner.plunner.models.models.employee.planner.Meeting;
import com.plunner.plunner.models.models.employee.planner.MeetingTimeslot;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.models.models.employee.utils.LoadResource;
import com.plunner.plunner.retrofit.RetrofitTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by claudio on 25/02/16.
 */
public class MeetingTimeslots extends RetrofitTest {
    public void testGet() throws Exception {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Planner> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"45\",\"created_at\":\"2015-12-31 14:59:14\",\"updated_at\":\"2015-12-31 14:59:14\",\"name\":\"Gruppo\",\"description\":\"ssdsadasd\",\"company_id\":\"11\",\"planner_id\":\"34\",\"planner_name\":\"testEmp\",\"meetings\":[{\"id\":\"134\",\"title\":\"2Msad1\",\"description\":\"1312312321312\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 06:15:00\",\"duration\":\"900\",\"created_at\":\"2015-12-31 15:17:17\",\"updated_at\":\"2016-01-11 20:15:05\"},{\"id\":\"136\",\"title\":\"sadas\",\"description\":\"sadasd\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:41:02\",\"updated_at\":\"2016-01-09 17:23:35\"},{\"id\":\"138\",\"title\":\"12312\",\"description\":\"312312\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:42:51\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"345\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"},{\"id\":\"346\",\"title\":\"Nuovo Meeting\",\"description\":\"hjsakdjhsa\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-11 19:34:55\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"460\",\"title\":\"Meeting\",\"description\":\"asdasd\",\"group_id\":\"45\",\"start_time\":\"2016-02-06 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-30 10:38:39\",\"updated_at\":\"2016-01-31 00:00:22\"}]}]");
        Execution<ModelList<Group>> executionG = new Execution<>();
        LoadResource<ModelList<Group>> groups = executionE.getModel().getGroupsManaged();
        groups.load(executionG.getCallback());
        lock();
        assertOK();
        List<Group> groupsList = groups.getInstance().getModels();
        Group group = groupsList.get(0);
        setResponse("[{\"id\":\"134\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"}]");
        Execution<ModelList<Meeting>> execution2 = new Execution<>();
        LoadResource<ModelList<Meeting>> meetings = group.getMeetingsManaged();
        meetings.load(execution2.getCallback());
        lock();
        assertOK();
        List<Meeting> meetingsList = meetings.getInstance().getModels();
        Meeting meeting = meetingsList.get(0);
        setResponse("[{\"id\":\"398\",\"time_start\":\"2016-01-15 06:00:00\",\"time_end\":\"2016-01-15 09:00:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:18\",\"updated_at\":\"2015-12-31 15:17:40\"},{\"id\":\"399\",\"time_start\":\"2016-01-16 08:00:00\",\"time_end\":\"2016-01-16 10:15:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:55\",\"updated_at\":\"2015-12-31 15:17:55\"}]");
        Execution<ModelList<MeetingTimeslot>> execution3 = new Execution<>();
        LoadResource<ModelList<MeetingTimeslot>> timeslots = meeting.getMeetingsTimeslotManaged();
        timeslots.load(execution3.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/planners/groups/45/meetings/134/timeslots/");
        List<MeetingTimeslot> timeslotsList = timeslots.getInstance().getModels();
        MeetingTimeslot timeslot = timeslotsList.get(0);
        assertEquals(2, timeslotsList.size()); //number of timeslots
        assertEquals("134", timeslot.getMeetingId()); //just to verify if timeslot is loaded
        assertEquals("398", timeslot.getId()); //just to verify if timeslot is loaded
    }

    public void testPut() throws Exception {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Planner> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"45\",\"created_at\":\"2015-12-31 14:59:14\",\"updated_at\":\"2015-12-31 14:59:14\",\"name\":\"Gruppo\",\"description\":\"ssdsadasd\",\"company_id\":\"11\",\"planner_id\":\"34\",\"planner_name\":\"testEmp\",\"meetings\":[{\"id\":\"134\",\"title\":\"2Msad1\",\"description\":\"1312312321312\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 06:15:00\",\"duration\":\"900\",\"created_at\":\"2015-12-31 15:17:17\",\"updated_at\":\"2016-01-11 20:15:05\"},{\"id\":\"136\",\"title\":\"sadas\",\"description\":\"sadasd\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:41:02\",\"updated_at\":\"2016-01-09 17:23:35\"},{\"id\":\"138\",\"title\":\"12312\",\"description\":\"312312\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:42:51\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"345\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"},{\"id\":\"346\",\"title\":\"Nuovo Meeting\",\"description\":\"hjsakdjhsa\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-11 19:34:55\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"460\",\"title\":\"Meeting\",\"description\":\"asdasd\",\"group_id\":\"45\",\"start_time\":\"2016-02-06 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-30 10:38:39\",\"updated_at\":\"2016-01-31 00:00:22\"}]}]");
        Execution<ModelList<Group>> executionG = new Execution<>();
        LoadResource<ModelList<Group>> groups = executionE.getModel().getGroupsManaged();
        groups.load(executionG.getCallback());
        lock();
        assertOK();
        List<Group> groupsList = groups.getInstance().getModels();
        Group group = groupsList.get(0);
        setResponse("[{\"id\":\"134\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"}]");
        Execution<ModelList<Meeting>> execution2 = new Execution<>();
        LoadResource<ModelList<Meeting>> meetings = group.getMeetingsManaged();
        meetings.load(execution2.getCallback());
        lock();
        assertOK();
        List<Meeting> meetingsList = meetings.getInstance().getModels();
        Meeting meeting = meetingsList.get(0);
        setResponse("[{\"id\":\"398\",\"time_start\":\"2016-01-15 06:00:00\",\"time_end\":\"2016-01-15 09:00:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:18\",\"updated_at\":\"2015-12-31 15:17:40\"},{\"id\":\"399\",\"time_start\":\"2016-01-16 08:00:00\",\"time_end\":\"2016-01-16 10:15:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:55\",\"updated_at\":\"2015-12-31 15:17:55\"}]");
        Execution<ModelList<MeetingTimeslot>> execution3 = new Execution<>();
        LoadResource<ModelList<MeetingTimeslot>> timeslots = meeting.getMeetingsTimeslotManaged();
        timeslots.load(execution3.getCallback());
        lock();
        assertOK();
        List<MeetingTimeslot> timeslotsList = timeslots.getInstance().getModels();
        MeetingTimeslot timeslot = timeslotsList.get(0);
        setResponse("{\"id\":\"398\",\"time_start\":\"2012-01-15 06:00:00\",\"time_end\":\"2016-01-15 09:00:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:18\",\"updated_at\":\"2015-12-31 15:17:40\"}");
        timeslot.setTimeStart("2012-01-15 06:00:00");
        Execution<MeetingTimeslot> execution4 = new Execution<>();
        timeslot.save(execution4.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/planners/groups/45/meetings/134/timeslots/398/");
        assertMethod("PUT");
        Map<String, String> body = new HashMap<>();
        body.put("time_start", "2012-01-15%2006%3A00%3A00");
        body.put("time_end", "2016-01-15%2009%3A00%3A00");
        assertRequestBody(body);
        assertEquals("2012-01-15 06:00:00", execution4.getModel().getTimeStart());
    }

    public void testPost() throws Exception {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Planner> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"45\",\"created_at\":\"2015-12-31 14:59:14\",\"updated_at\":\"2015-12-31 14:59:14\",\"name\":\"Gruppo\",\"description\":\"ssdsadasd\",\"company_id\":\"11\",\"planner_id\":\"34\",\"planner_name\":\"testEmp\",\"meetings\":[{\"id\":\"134\",\"title\":\"2Msad1\",\"description\":\"1312312321312\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 06:15:00\",\"duration\":\"900\",\"created_at\":\"2015-12-31 15:17:17\",\"updated_at\":\"2016-01-11 20:15:05\"},{\"id\":\"136\",\"title\":\"sadas\",\"description\":\"sadasd\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:41:02\",\"updated_at\":\"2016-01-09 17:23:35\"},{\"id\":\"138\",\"title\":\"12312\",\"description\":\"312312\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:42:51\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"345\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"},{\"id\":\"346\",\"title\":\"Nuovo Meeting\",\"description\":\"hjsakdjhsa\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-11 19:34:55\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"460\",\"title\":\"Meeting\",\"description\":\"asdasd\",\"group_id\":\"45\",\"start_time\":\"2016-02-06 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-30 10:38:39\",\"updated_at\":\"2016-01-31 00:00:22\"}]}]");
        Execution<ModelList<Group>> executionG = new Execution<>();
        LoadResource<ModelList<Group>> groups = executionE.getModel().getGroupsManaged();
        groups.load(executionG.getCallback());
        lock();
        assertOK();
        List<Group> groupsList = groups.getInstance().getModels();
        Group group = groupsList.get(0);
        setResponse("[{\"id\":\"134\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"}]");
        Execution<ModelList<Meeting>> execution2 = new Execution<>();
        LoadResource<ModelList<Meeting>> meetings = group.getMeetingsManaged();
        meetings.load(execution2.getCallback());
        lock();
        assertOK();
        List<Meeting> meetingsList = meetings.getInstance().getModels();
        Meeting meeting = meetingsList.get(0);
        setResponse("[{\"id\":\"398\",\"time_start\":\"2016-01-15 06:00:00\",\"time_end\":\"2016-01-15 09:00:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:18\",\"updated_at\":\"2015-12-31 15:17:40\"},{\"id\":\"399\",\"time_start\":\"2016-01-16 08:00:00\",\"time_end\":\"2016-01-16 10:15:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:55\",\"updated_at\":\"2015-12-31 15:17:55\"}]");
        Execution<ModelList<MeetingTimeslot>> execution3 = new Execution<>();
        LoadResource<ModelList<MeetingTimeslot>> timeslots = meeting.getMeetingsTimeslotManaged();
        timeslots.load(execution3.getCallback());
        lock();
        assertOK();
        List<MeetingTimeslot> timeslotsList = timeslots.getInstance().getModels();
        MeetingTimeslot timeslot = timeslotsList.get(0);
        setResponse("{\"id\":\"400\",\"time_start\":\"2012-01-15 06:00:00\",\"time_end\":\"2016-01-15 09:00:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:18\",\"updated_at\":\"2015-12-31 15:17:40\"}");
        timeslot.setTimeStart("2012-01-15 06:00:00");
        timeslot.setId(null);
        Execution<MeetingTimeslot> execution4 = new Execution<>();
        timeslot.save(execution4.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/planners/groups/45/meetings/134/timeslots/");
        assertMethod("POST");
        Map<String, String> body = new HashMap<>();
        body.put("time_start", "2012-01-15%2006%3A00%3A00");
        body.put("time_end", "2016-01-15%2009%3A00%3A00");
        assertRequestBody(body);
        assertEquals("2012-01-15 06:00:00", execution4.getModel().getTimeStart());
        assertEquals("400", execution4.getModel().getId());
    }

    public void testDelete() throws Exception {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Planner> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"45\",\"created_at\":\"2015-12-31 14:59:14\",\"updated_at\":\"2015-12-31 14:59:14\",\"name\":\"Gruppo\",\"description\":\"ssdsadasd\",\"company_id\":\"11\",\"planner_id\":\"34\",\"planner_name\":\"testEmp\",\"meetings\":[{\"id\":\"134\",\"title\":\"2Msad1\",\"description\":\"1312312321312\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 06:15:00\",\"duration\":\"900\",\"created_at\":\"2015-12-31 15:17:17\",\"updated_at\":\"2016-01-11 20:15:05\"},{\"id\":\"136\",\"title\":\"sadas\",\"description\":\"sadasd\",\"group_id\":\"45\",\"start_time\":\"2016-01-15 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:41:02\",\"updated_at\":\"2016-01-09 17:23:35\"},{\"id\":\"138\",\"title\":\"12312\",\"description\":\"312312\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 07:00:00\",\"duration\":\"900\",\"created_at\":\"2016-01-07 13:42:51\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"345\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"},{\"id\":\"346\",\"title\":\"Nuovo Meeting\",\"description\":\"hjsakdjhsa\",\"group_id\":\"45\",\"start_time\":\"2016-01-22 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-11 19:34:55\",\"updated_at\":\"2016-01-12 12:15:47\"},{\"id\":\"460\",\"title\":\"Meeting\",\"description\":\"asdasd\",\"group_id\":\"45\",\"start_time\":\"2016-02-06 06:45:00\",\"duration\":\"900\",\"created_at\":\"2016-01-30 10:38:39\",\"updated_at\":\"2016-01-31 00:00:22\"}]}]");
        Execution<ModelList<Group>> executionG = new Execution<>();
        LoadResource<ModelList<Group>> groups = executionE.getModel().getGroupsManaged();
        groups.load(executionG.getCallback());
        lock();
        assertOK();
        List<Group> groupsList = groups.getInstance().getModels();
        Group group = groupsList.get(0);
        setResponse("[{\"id\":\"134\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"}]");
        Execution<ModelList<Meeting>> execution2 = new Execution<>();
        LoadResource<ModelList<Meeting>> meetings = group.getMeetingsManaged();
        meetings.load(execution2.getCallback());
        lock();
        assertOK();
        List<Meeting> meetingsList = meetings.getInstance().getModels();
        Meeting meeting = meetingsList.get(0);
        setResponse("[{\"id\":\"398\",\"time_start\":\"2016-01-15 06:00:00\",\"time_end\":\"2016-01-15 09:00:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:18\",\"updated_at\":\"2015-12-31 15:17:40\"},{\"id\":\"399\",\"time_start\":\"2016-01-16 08:00:00\",\"time_end\":\"2016-01-16 10:15:00\",\"meeting_id\":\"134\",\"created_at\":\"2015-12-31 15:17:55\",\"updated_at\":\"2015-12-31 15:17:55\"}]");
        Execution<ModelList<MeetingTimeslot>> execution3 = new Execution<>();
        LoadResource<ModelList<MeetingTimeslot>> timeslots = meeting.getMeetingsTimeslotManaged();
        timeslots.load(execution3.getCallback());
        lock();
        assertOK();
        List<MeetingTimeslot> timeslotsList = timeslots.getInstance().getModels();
        MeetingTimeslot timeslot = timeslotsList.get(0);
        setResponse("{}");
        Execution<MeetingTimeslot> execution4 = new Execution<>();
        timeslot.delete(execution4.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/planners/groups/45/meetings/134/timeslots/398/");
        assertMethod("DELETE");
        assertNull(execution4.getModel().getId());
    }
}
