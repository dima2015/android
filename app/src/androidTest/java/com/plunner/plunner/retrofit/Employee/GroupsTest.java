package com.plunner.plunner.retrofit.Employee;

import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.Group;
import com.plunner.plunner.models.models.employee.Meeting;
import com.plunner.plunner.models.models.employee.utility.LoadResource;
import com.plunner.plunner.retrofit.RetrofitTest;

import java.util.List;

/**
 * Created by claudio on 22/02/16.
 */
public class GroupsTest extends RetrofitTest {
    public void testGet() throws InterruptedException {
        setResponse("{\"id\":\"34\",\"name\":\"testEmp\",\"email\":\"testEmp@test.com\",\"company_id\":\"11\",\"created_at\":\"2015-12-30 21:31:43\",\"updated_at\":\"2015-12-30 21:31:43\",\"is_planner\":true}");
        Execution<Employee> executionE = new Execution<>();
        (new Employee()).getFactory(executionE.getCallback());
        lock();
        assertOK();
        setResponse("[{\"id\":\"43\",\"created_at\":\"2015-12-30 21:31:44\",\"updated_at\":\"2015-12-30 21:31:44\",\"name\":\"Aurore Kirlin MD\",\"description\":\"Omnis perspiciatis dolores omnis odit officiis impedit molestias.Repellendus atque qui enim exercitationem consequuntur sunt est.\",\"company_id\":\"11\",\"planner_id\":\"31\",\"planner_name\":\"Elinore King\",\"meetings\":[]},{\"id\":\"45\",\"created_at\":\"2015-12-31 14:59:14\",\"updated_at\":\"2015-12-31 14:59:14\",\"name\":\"Gruppo\",\"description\":\"ssdsadasd\",\"company_id\":\"11\",\"planner_id\":\"34\",\"planner_name\":\"testEmp\",\"meetings\":[{\"id\":\"345\",\"title\":\"NewMeeting\",\"description\":\"dsdasdas\",\"group_id\":\"45\",\"start_time\":null,\"duration\":\"900\",\"created_at\":\"2016-01-11 16:30:45\",\"updated_at\":\"2016-01-11 16:30:45\"}]}]");
        Execution<ModelList<Group>> executionG = new Execution<>();
        LoadResource<ModelList<Group>> groups = executionE.getModel().getGroups();
        groups.load(executionG.getCallback());
        lock();
        assertOK();
        assertUrl("/employees/groups/");
        List<Group> groupsList = groups.getInstance().getModels();
        assertEquals(2, groupsList.size()); //number of groups
        assertEquals(0, groupsList.get(0).getMeetings().size()); //number of meetings for group 1
        assertEquals(1, groupsList.get(1).getMeetings().size()); //number of meetings for group 2
        Group group = groupsList.get(1);
        List<Meeting> meetings = group.getMeetings();
        Meeting meeting = meetings.get(0);
        assertEquals("45", group.getId()); //just to verify if group is loaded
        assertEquals("345", meeting.getId()); //just to verify if meeting is loaded
    }
}
