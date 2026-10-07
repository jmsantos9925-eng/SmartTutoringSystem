public abstract class User {
    private String fullName;
    private String email;
    private String password;

    public User(String fullName, String email, String password) {
        this.fullName = fullName.trim();
        this.email = email.trim();
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    // Simple comparison muna kasi console project lang naman and wala pang encrypted storage.
    public boolean passwordMatches(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    public void updateProfile(String newName, String newEmail) {
        fullName = newName.trim();
        email = newEmail.trim();
    }

    // bawat child class yung magbibigay ng sarili nyang role.
    public abstract String getRole();
}

