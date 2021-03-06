package com.plunner.plunner.retrofit.Employee.planner;

import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.models.models.employee.planner.Group;
import com.plunner.plunner.models.models.employee.planner.Planner;
import com.plunner.plunner.models.models.employee.utils.LoadResource;
import com.plunner.plunner.retrofit.RetrofitTest;

import java.util.List;

/**
 * Created by claudio on 25/02/16.
 */
public class Groups extends RetrofitTest {
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
        assertUrl("/employees/planners/groups/");
        List<Group> groupsList = groups.getInstance().getModels();
        assertEquals(1, groupsList.size()); //number of groups
        assertEquals(6, groupsList.get(0).getMeetings().size()); //number of meetings for group 1
        Group group = groupsList.get(0);
        List<Meeting> meetings = group.getMeetings();
        Meeting meeting = meetings.get(0);
        assertEquals(executionE.getModel().getId(), group.getPlannerId()); //just to verify if the planner id is correct
        assertEquals("45", group.getId()); //just to verify if group is loaded
        assertEquals("134", meeting.getId()); //just to verify if meeting is loaded
    }
}
