package com.example.plugins.tutorial.jira.workflow;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.status.Status;

import java.util.Map;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Base class for setting up mocks
 *
 * @since v5.0
 */
public abstract class AbstractWorkflowTest {
    protected MutableIssue mockParentIssue, mockSubTaskIssue;
    protected IssueManager mockIssueManager;
    protected Map transientVars, args;
    protected Status mockStatus;

    @Before
    public void setupMocks() {
        createMocks();
        stubMockMethods();
    }

    private void createMocks() {
        mockIssueManager = mock(IssueManager.class);
        mockSubTaskIssue = mock(MutableIssue.class);
        mockParentIssue = mock(MutableIssue.class);
        mockStatus = mock(Status.class);
    }

    private void stubMockMethods() {
        when(mockSubTaskIssue.getParentId()).thenReturn(1L);
        when(mockParentIssue.getId()).thenReturn(1L);
        when(mockParentIssue.getStatus()).thenReturn(mockStatus);
        when(mockIssueManager.getIssueObject(1L)).thenReturn(mockParentIssue);
    }
}
