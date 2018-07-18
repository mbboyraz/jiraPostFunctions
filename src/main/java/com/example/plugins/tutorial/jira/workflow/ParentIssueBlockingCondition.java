package com.example.plugins.tutorial.jira.workflow;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.workflow.WorkflowFunctionUtils;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.opensymphony.module.propertyset.PropertySet;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Scanned
public class ParentIssueBlockingCondition extends AbstractJiraCondition {
    private static final Logger log = LoggerFactory.getLogger(ParentIssueBlockingCondition.class);

    public static final String FIELD_WORD = "word";

    @JiraImport
    private IssueManager issueManager;

    public ParentIssueBlockingCondition(IssueManager issueManager){
        this.issueManager = issueManager;
    }

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        Issue subTask = (Issue) transientVars.get(WorkflowFunctionUtils.ORIGINAL_ISSUE_KEY);

        Issue parentIssue = issueManager.getIssueObject(subTask.getParentId());
        if (parentIssue == null) {
            return false;
        }

        String statuses = (String) args.get("statuses");
        StringTokenizer st = new StringTokenizer(statuses, ",");

        while (st.hasMoreTokens()) {
            String statusId = st.nextToken();
            if (parentIssue.getStatus().getId().equals(statusId)) {
                return true;
            }
        }
        return false;
    }
}
