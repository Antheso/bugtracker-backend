package app.DB;

public class Query {
    public static final String SELECT_ALL_INFO_ISSUES
            = "select * from \"BugTracker\".\"Issue\"";

    public static final String SELECT_TABLE_INFO_ISSUES
            = "select issue_number, summary, issue_number as number from \"BugTracker\".\"Issue\"";

    public static final String SELECT_TABLE_LIMIT
            = SELECT_TABLE_INFO_ISSUES + "Limit ? Offset ?";

    public static final String SELECT_TABLE_SEARCH
            = SELECT_TABLE_INFO_ISSUES + " where description LIKE ? or summary LIKE ? Limit ? Offset ?";

    public static final String SELECT_TABLE_PROJECTS
            = "select name, project_id from \"BugTracker\".\"Project\"";

    public static final String SELECT_TABLE_PRIORITY
            = "select name, priority_id from \"BugTracker\".\"Priority\"";

    public static final String SELECT_TABLE_STATUSES
            = "select name, status_id from \"BugTracker\".\"Status\"";

    public static final String SELECT_TABLE_TYPES
            = "select name, type_id from \"BugTracker\".\"Type\"";

    public static final String SELECT_TABLE_USERS
            = "select first_name, user_id from \"BugTracker\".\"User\"";

    public static final String SELECT_TABLE_ROLE
            = "select name, role_id from \"BugTracker\".\"Role\"";

    public static final String SELECT_USER
            = "select user_id, first_name, role_id, password from \"BugTracker\".\"User\"";

    public static final String SELECT_COMMENT
            = "select comment_id, text, user_id, date, extract(epoch from timestamp) as timestamp from \"BugTracker\".\"Comment\"";

    public static String SELECT_ISSUE_BY_ID
            = "select issue.status_id, issue.summary as summary, issue.description as description, issue.priority_id as priority_id, issue.type_id as type_id, issue.number as number, issue.deprecated, " +
                "buser.user_id as assignee_id, buser.first_name as assignee_name, " +
                "b2user.user_id as author_id, b2user.first_name as author_name, " +
                "project.project_id as project_id, project.name as project_name " +
                "from \"BugTracker\".\"Issue\" issue " +
                "inner join \"BugTracker\".\"User\" buser on issue.assignee_id = buser.user_id " +
                "inner join \"BugTracker\".\"User\" b2user on issue.author_id = b2user.user_id " +
                "inner join \"BugTracker\".\"Project\" project on issue.project_id = project.project_id " +
                "where issue.project_id = ? and issue.number = ?";

    public static final String SELECT_USER_BY_LOGIN
            = SELECT_USER + "where email = ? ";

    public static final String SELECT_VALID_USER
            = "select user_id, first_name, role_id from \"BugTracker\".\"User\" where role_id = ? and user_id = ? and first_name = ? ";

    public static final String SELECT_USER_BY_ID
            = SELECT_USER + "where user_id = ? ";

    public static String SELECT_COMMENT_BY_ISSUE_ID
            = "select comment.comment_id, comment.text, comment.user_id, buser.first_name, extract(epoch from timestamp) as timestamp " +
            "from \"BugTracker\".\"Comment\" comment " +
            "inner join \"BugTracker\".\"User\" buser on comment.user_id = buser.user_id " +
            "where issue_number = ?";

    public static String INSERT_ISSUE_PARAMS
        = INSERT("Issue")
                .append("(summary, description, priority_id, status_id, type_id, project_id, assignee_id, author_id, deprecated) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .toString();

    public static String INSERT_USER_PARAMS
            = INSERT("User")
            .append("(first_name, last_name, password, email, role_Id) VALUES(?, ?, ?, ?, ?)")
            .toString();

    public static String UPDATE_ISSUE_BY_ID
        = UPDATE("Issue")
            .append("summary=?, description=?, priority_id=?, type_id=?, status_id=?, assignee_id=? WHERE project_id =? and number =?")
            .toString();

    public static String UPDATE_ISSUE_DEPRECATED_BY_ID
            = UPDATE("Issue")
            .append("deprecated = ? WHERE project_id =? and number =?")
            .toString();

    public static String DELETE_ISSUE_BY_ID
        = DELETE("Issue")
                .append("WHERE project_id = ? AND number= ? ")
                .toString();

    public static String DELETE_COMMENT_ISSUE
            = DELETE("Comment")
                .append("WHERE issue_number = ? ")
            .toString();

    public static String UPDATE_COMMENT_ISSUE
            = UPDATE("Comment")
                .append("issue_number =" +
                        " (select issue_number from \"BugTracker\".\"Issue\" where deprecated = ? limit 1)" +
                        "WHERE issue_number = ?")
            .toString();

    public static String INSERT_COMMENT_PARAMS
            = INSERT("Comment")
                .append("(issue_number, text, user_id, timestamp ) VALUES(?, ?, ?, to_timestamp(?))")
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
