public class Admin extends Staff {
    public Admin(String fullName, String email, String password) {
        super(fullName, email, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public void manageUser(UserDatabase database) {
        User[] users = database.getUsers();
        System.out.println("\n--- Registered Users ---");

        if (users.length == 0) {
            System.out.println("No registered users.");
            return;
        }

        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getFullName()
                    + " | " + users[i].getRole()
                    + " | " + users[i].getEmail());
        }
    }

    public void removeUser(UserDatabase database, String email) {
        if (database.removeUserByEmail(email)) {
            System.out.println("User account removed.");
        } else {
            System.out.println("User account not found.");
        }
    }
}

