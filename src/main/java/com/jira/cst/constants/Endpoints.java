package com.jira.cst.constants;

public class Endpoints {
    public static final String API_ROOT = "/api";

    private Endpoints() {

    }

    /**
     * Issue configuration endpoints.
     */
    public static final class Issue {

        public static final String ROOT = API_ROOT + "/issue";
        public static final String SUM  = ROOT + "/sum";

        /**
         * Request parameters / query string parameters for User.
         */
        public static final class QParam {
            public static final String QUERY = "query";
            public static final String NAME = "name";
        }

    }
}
