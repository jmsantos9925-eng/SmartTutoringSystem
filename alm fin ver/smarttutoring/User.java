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

    // Simple comparison muna dahil console project lang at walang encrypted storage.
    public boolean passwordMatches(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    public void updateProfile(String newName, String newEmail) {
        fullName = newName.trim();
        email = newEmail.trim();
    }

    // Bawat child class ang magbibigay ng sarili nitong role.
    public abstract String getRole();
}

