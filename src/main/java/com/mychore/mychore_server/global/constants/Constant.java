package com.mychore.mychore_server.global.constants;

public final class Constant {
    public static final String ACTIVE_STATUS = "active";
    public static final String INACTIVE_STATUS = "inactive";
    public static final String COMMA = ",";
    public static final String TITLE = "MyChore";
    public static class User{
        // 알림 종류
        public static final String CHORE = "chore";
        public static final String DONE_CHORE = "done";
        public static final String TODAY = "today";
        public static final String NEW_USER = "new-user";
        public static final String DELETE = "delete";
    }

    public static class Notification {
        public static final String[] SCOPES = {"https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform"};
        public static final String PROJECT_ID = "mychore-ced74";
        public static final String BASE_URL = "https://fcm.googleapis.com";
        public static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    }

}
