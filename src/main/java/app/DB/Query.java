package app.DB;

public class Query {
    public static final String SELECT_ALL_INFO_ISSUES
            = "select * from \"BugTracker\".\"Issue\"";
    public static final String SELECT_TABLE_INFO_ISSUES
            = "select issue_id, summary, name from \"BugTracker\".\"Issue\"";

    public static final String SELECT_TABLE_PROJECTS
            = "select name, project_id from \"BugTracker\".\"Project\"";

    public static final String SELECT_TABLE_PRIORITY
            = "select name, priority_id from \"BugTracker\".\"Priority\"";

    public static final String SELECT_TABLE_STATUSES
            = "select name, status_id from \"BugTracker\".\"Status\"";

    public static final String SELECT_TABLE_TYPES
            = "select name, type_id from \"BugTracker\".\"Type\"";

    public static final String SELECT_TABLE_USERS
            = "select name, user_id from \"BugTracker\".\"User\"";

    public static final String SELECT_USER
            = "select user_id, name, login, role_id, password from \"BugTracker\".\"User\"";

    public static final String SELECT_COMMENT
            = "select text, user_id from \"BugTracker\".\"Comment\"";

    public static String SELECT_ISSUE_BY_ID(String id)
    {
        return SELECT_ALL_INFO_ISSUES + "where issue_id = '" + id + "'";
    }

    public static final String SELECT_USER_BY_LOGIN
            = SELECT_USER + "where login = ? ";


    public static String SELECT_COMMENT_BY_ISSUE_ID(String id)
    {
        return SELECT_COMMENT + "where issue_id = '" + id + "'";
    }

    public static String INSERT_ISSUE_PARAMS
        = INSERT("Issue")
                .append("(summary, description, priority_id, status_id, project_id, assignee_id, author_id) VALUES(?, ?, ?, ?, ?, ?, ?)")
                .toString();

    public static String UPDATE_ISSUE_BY_ID
        = UPDATE("Issue")
            .append("summary=?, description=?, priority_id=?, status_id=?, project_id=?, assignee_id=? WHERE issue_id=?")
            .toString();

    public static String DELETE_ISSUE_BY_ID
    (
        String id
    )
    {
        return DELETE("Issue")
                .append("WHERE issue_id= \'")
                .append(id).append("\'")
                .toString();
    }

    public static String INSERT_COMMENT_PARAMS
            = INSERT("Comment")
                .append("(text, issue_id, user_id ) VALUES(?, ?, ?)")
                .toString();


    public static StringBuffer INSERT(String tableName)
    {
        return new StringBuffer("INSERT INTO \"BugTracker\".")
                .append("\"")
                    .append(tableName)
                .append("\" ");
    }

    public static StringBuffer DELETE(String tableName)
    {
        return new StringBuffer("DELETE FROM \"BugTracker\".")
                .append("\"")
                    .append(tableName)
                .append("\" ");
    }

    public static StringBuffer UPDATE(String tableName)
    {
        return new StringBuffer("UPDATE \"BugTracker\".")
                .append("\"")
                    .append(tableName)
                .append("\"SET ");
    }
}
