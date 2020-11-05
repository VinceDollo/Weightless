package innov.fr;

public class UserProfile {
    public String userEmail;
    public String userPhone;
    public String userName;
    public int compteurPoubelle;

    public UserProfile(){

    }
    public UserProfile(String userName, String userEmail,String userPhone, int compteurPoubelle){
        this.userName=userName;
        this.userEmail=userEmail;
        this.userPhone=userPhone;
        this.compteurPoubelle=compteurPoubelle;
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
    public int getCompteurPoubelle() {
        return compteurPoubelle;
    }

    public void setCompteurPoubelle(int compteurPoubelle) {
        this.compteurPoubelle = compteurPoubelle;
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
