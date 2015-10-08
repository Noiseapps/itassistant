package com.noiseapps.itassistant.model.jira.projects.createissue;

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

        Fields(IdField project, IdField issuetype, IdField priority, NameField assignee, NameField reporter, String summary, String description, String environment, String duedate) {
            this.project = project;
            this.issuetype = issuetype;
            this.priority = priority;
            this.assignee = assignee;
            this.reporter = reporter;
            this.summary = summary;
            this.description = description;
            this.environment = environment;
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
}
