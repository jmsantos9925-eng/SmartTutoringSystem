package smarttutoring;

public abstract class User {
    private String fullName;
    private String email;
    private String password;

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    // simple comparison lang muna nilgay ko rito kasi console project palang yunng system
    public boolean passwordMatches(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    public void updateProfile(String newName, String newEmail) {
        fullName = newName;
        email = newEmail;
    }

    public abstract String getRole();
}

