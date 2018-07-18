package com.example.plugins.tutorial.jira.workflow;

import org.junit.Before;
import com.atlassian.jira.util.collect.MapBuilder;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ParentIssueBlockingConditionTest  extends AbstractWorkflowTest {
    private ParentIssueBlockingCondition condition;


    @Before

    public void setup() {
        condition = new ParentIssueBlockingCondition(mockIssueManager);
        transientVars = MapBuilder.build("originalissueobject", mockSubTaskIssue);
        args = MapBuilder.build("statuses", "1,2,3");
    }

    @Test
    public void testPassesCondition() throws Exception {
        when(mockStatus.getId()).thenReturn("3");
        assertTrue("condition should pass",condition.passesCondition(transientVars, args, null));
    }

    @Test
    public void testFailsCondition() throws Exception {
        when(mockStatus.getId()).thenReturn("4");
        assertFalse("condition should fail",condition.passesCondition(transientVars, args, null));
    }
}
