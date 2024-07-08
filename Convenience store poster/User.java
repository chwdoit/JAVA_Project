package practice01;

import java.util.Date;

public class User {
    String username;
    String password;
    Date loginTime;
    Date logoutTime;
    boolean isLoggedIn; // 로그인 상태를 나타내는 변수 추가


    public boolean login(String inputUsername, String inputPassword) {
        if (this.username.equals(inputUsername) && this.password.equals(inputPassword)) {
            this.loginTime = new Date();
            this.isLoggedIn = true; // 로그인 성공 시 true로 설정
            System.out.println(username + " 로그인 하였습니다 " + loginTime);
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        this.logoutTime = new Date();
        long workedMinutes = (logoutTime.getTime() - loginTime.getTime()) / (1000 * 60);
        this.isLoggedIn = false; // 로그아웃 시 false로 설정
        System.out.println(username + " 로그아웃 되었습니다 ");
        System.out.println("일한 시간: " + workedMinutes + "분");
    }
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false; // 초기 로그인 상태는 false
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
