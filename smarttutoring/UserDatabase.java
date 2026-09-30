package smarttutoring;

public class UserDatabase {
    private User[] users;
    private int userCount;

    public UserDatabase() {
        users = new User[50];
        userCount = 0;
    }

    public void addUser(User user) throws DuplicateEmailException {
        if (emailExists(user.getEmail())) {
            throw new DuplicateEmailException("Registered na ang email na ito.");
        }

        if (userCount >= users.length) {
            throw new IllegalStateException("Puno na ang user storage.");
        }

        // Dito nilalagay ang object sa next available na part ng array.
        users[userCount] = user;
        userCount++;
    }

    public User findUserByEmail(String email) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getEmail().equalsIgnoreCase(email)) {
                return users[i];
            }
        }
        return null;
    }

    public boolean emailExists(String email) {
        return findUserByEmail(email) != null;
    }

    public User authenticate(String email, String password) throws InvalidLoginException {
        User foundUser = findUserByEmail(email);

        if (foundUser == null || !foundUser.passwordMatches(password)) {
            throw new InvalidLoginException("Mali ang email o password.");
        }
        return foundUser;
    }

    public User[] getUsers() {
        User[] activeUsers = new User[userCount];
        for (int i = 0; i < userCount; i++) {
            activeUsers[i] = users[i];
        }
        return activeUsers;
    }

    public boolean removeUserByEmail(String email) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getEmail().equalsIgnoreCase(email)) {
                // Inaakyat ang remaining users para walang empty space sa gitna.
                for (int j = i; j < userCount - 1; j++) {
                    users[j] = users[j + 1];
                }
                users[userCount - 1] = null;
                userCount--;
                return true;
            }
        }
        return false;
    }

    public int getUserCount() {
        return userCount;
    }
}

