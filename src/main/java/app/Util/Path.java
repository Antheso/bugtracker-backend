package app.Util;

public class Path {
    public static class Web {
        public static final String ISSUE = "/api/issues";
        public static final String ONE_ISSUE = "/api/issues/:id";
        public static final String PROJECTS = "/api/projects";
        public static final String PROJECTSID = "/api/projects/:id";
        public static final String PRIORITY = "/api/priorities";
        public static final String STATUS = "/api/statuses";
        public static final String TYPES = "/api/types";
        public static final String USERS = "/api/users";
        public static final String LOGIN = "/api/login";
        public static final String LOGOUT = "/api/logout";
        public static final String REGISTRATION = "/api/registration";
        public static final String REGISTRATIONTOKEN = "/api/registration/:token";
        public static final String COMMENT_BY_ISSUE = "/api/comments/:issueId";
        public static final String COMMENT = "/api/comments";
        public static final String CUSER = "/api/cuser";
        public static final String PROJECTROLES = "/api/project/roles";
        public static final String PROJECTUSER = "/api/project/users";
        public static final String USERRESEND = "/api/resend-confirmation";
//        public static final String MYPROJECTS = "/api/my-projects";
    }
}
