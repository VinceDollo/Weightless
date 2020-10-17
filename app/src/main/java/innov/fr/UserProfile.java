package innov.fr;

public class UserProfile {
    public String userEmail;
    public String userPhone;
    public String userName;

    public UserProfile(){

    }
    public UserProfile(String userName, String userEmail,String userPhone){
        this.userName=userName;
        this.userEmail=userEmail;
        this.userPhone=userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public String getUserName() {
        return userName;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
