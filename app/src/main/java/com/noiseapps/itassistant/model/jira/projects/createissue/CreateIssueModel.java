package com.noiseapps.itassistant.model.jira.projects.createissue;


@SuppressWarnings("unused, FieldCanBeLocal")
public class CreateIssueModel {

    private final Fields fields;

    public CreateIssueModel(Fields fields) {
        this.fields = fields;
    }

    public static class Fields {
        private IdField project;
        private IdField issuetype;
        private IdField priority;
        private NameField assignee;
        private NameField reporter;
        private String summary;
        private String description;
        private String environment;
        private String duedate;
        private Timetracking timetracking;

        public void setTimetracking(Timetracking timetracking) {
            this.timetracking = timetracking;
        }

        public void setProject(IdField project) {
            this.project = project;
        }

        public void setIssuetype(IdField issuetype) {
            this.issuetype = issuetype;
        }

        public void setPriority(IdField priority) {
            this.priority = priority;
        }

        public void setAssignee(NameField assignee) {
            this.assignee = assignee;
        }

        public void setReporter(NameField reporter) {
            this.reporter = reporter;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }

        public void setDuedate(String duedate) {
            this.duedate = duedate;
        }

        public static class IdField {
            private final String id;

            public IdField(String id) {
                this.id = id;
            }
        }

        public static class NameField {
            private final String name;

            public NameField(String name) {
                this.name = name;
            }
        }
    }

    public static class Timetracking {
        private final String originalEstimate;
        private final String remainingEstimate;

        public Timetracking(String originalEstimate, String remainingEstimate) {
            this.originalEstimate = originalEstimate;
            this.remainingEstimate = remainingEstimate;
        }
    }
}
