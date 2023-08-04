package com.mychore.mychore_server.global.constants;

public final class Constant {
    public static final String ACTIVE_STATUS = "active";
    public static final String INACTIVE_STATUS = "inactive";
    public static final String JWT_EXPIRED = "JWT_EXPIRED";
    public static final String COMMA = ",";

    public static class User{
        // 알림 종류
        public static final String CHORE = "chore";
        public static final String DONE_CHORE = "done";
        public static final String TODAY = "today";
        public static final String NEW_USER = "new-user";
        public static final String DELETE = "delete";
    }
}
