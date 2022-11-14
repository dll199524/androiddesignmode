package com.example.designmode.retrofit;

public class UserLogResult extends BaseResult {
    public Object data;
    public class UserInfo {
        public String userName;
        public String userSex;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "userName='" + userName + '\'' +
                    ", userSex='" + userSex + '\'' +
                    '}';
        }
    }
}
