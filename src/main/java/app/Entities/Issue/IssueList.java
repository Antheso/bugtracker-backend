package app.Entities.Issue;

import java.util.ArrayList;

public class IssueList {
    public ArrayList<Issue> issues = new ArrayList<>();

    public void addToIssues (Issue issue) {
        issues.add(issue);
    }

    public void deleteIssue (Issue issue) {
        issues.remove(issue);
    }

    @Override
    public String toString() {
        return "{ \"Issues\": [ " + issues + " ] }";
    }
}
