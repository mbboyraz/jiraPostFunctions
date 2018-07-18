package com.example.plugins.tutorial.jira.workflow;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.comparator.ConstantsComparator;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.atlassian.jira.util.collect.MapBuilder;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *   This is the factory class responsible for dealing with the UI for the post-function.
 *   This is typically where you put default values into the velocity context and where you store user input.
 */
@Scanned
public class ParentIssueBlockingConditionFactory extends AbstractWorkflowPluginFactory
    implements WorkflowPluginConditionFactory {

    @JiraImport
    private final ConstantsManager constantsManager;

    public ParentIssueBlockingConditionFactory(ConstantsManager constantsManager) {
        this.constantsManager = constantsManager;
    }

    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        //all available statuses
        Collection<Status> statuses = constantsManager.getStatuses();
        velocityParams.put("statuses", Collections.unmodifiableCollection(statuses));
    }

    protected void getVelocityParamsForEdit(Map<String, Object>  velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        velocityParams.put("selectedStatuses", getSelectedStatusIds(descriptor));
    }

    protected void getVelocityParamsForView(Map<String, Object>  velocityParams, AbstractDescriptor descriptor) {
        Collection selectedStatusIds = getSelectedStatusIds(descriptor);
        List<Status> selectedStatuses = new ArrayList<>();
        for (Object selectedStatusId : selectedStatusIds) {
            String statusId = (String) selectedStatusId;
            Status selectedStatus = constantsManager.getStatus(statusId);
            if (selectedStatus != null) {
                selectedStatuses.add(selectedStatus);
            }
        }
        selectedStatuses.sort(new ConstantsComparator());

        velocityParams.put("statuses", Collections.unmodifiableCollection(selectedStatuses));
    }

    public Map<String, Object> getDescriptorParams(Map conditionParams) {
        //  process the map which will contain the request parameters
        //  for now simply concatenate into a comma separated string
        //  production code would do something more robust.
        Collection statusIds = conditionParams.keySet();
        StringBuilder statIds = new StringBuilder();

        for (Object statusId : statusIds) {
            statIds.append((String) statusId).append(",");
        }

        return MapBuilder.build("statuses", statIds.substring(0, statIds.length() - 1));
    }

    private Collection getSelectedStatusIds(AbstractDescriptor descriptor) {
        Collection<String> selectedStatusIds = new ArrayList<>();
        if (!(descriptor instanceof ConditionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a ConditionDescriptor.");
        }

        ConditionDescriptor conditionDescriptor = (ConditionDescriptor) descriptor;

        String statuses = (String) conditionDescriptor.getArgs().get("statuses");
        StringTokenizer st = new StringTokenizer(statuses, ",");

        while (st.hasMoreTokens()) {
            selectedStatusIds.add(st.nextToken());
        }
        return selectedStatusIds;
    }
}