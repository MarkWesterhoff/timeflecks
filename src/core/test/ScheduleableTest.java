package core.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.*;

public class ScheduleableTest
{


	@Test
	/*
	 * Test creation and getting of IDs
	 */
	public void testIDs()
	{
		Scheduleable s1 = new Task("Task 1");
		long first_id = s1.getId();
		assertEquals("The IDs should start at 0.",s1.getId(),0 + first_id);
		Scheduleable s2 = new Task("Task 2");
		assertEquals("Each scheduleable should start with a different ID",s2.getId(),1 + first_id);
		Scheduleable s3 = new Event("Event 1",new Date(System.currentTimeMillis()),60000);
		assertEquals("Events should keep the same ID counting scheue as Tasks",s3.getId(),2 + first_id);
		Task t4 = new Task("Task 3");
		assertEquals("Tasks should keep the same ID counting scheue as Events",((Scheduleable)t4).getId(),3 + first_id);
		t4.setOrdering(0);
		assertEquals("Set ordering should not disrupt the ID of a task",((Scheduleable)t4).getId(),3 + first_id);
		Scheduleable s5 = new Task("Task 4");
		assertEquals("Set ordering should not disrupt ID creation",((Scheduleable)s5).getId(),4 + first_id);

		
	}

}
