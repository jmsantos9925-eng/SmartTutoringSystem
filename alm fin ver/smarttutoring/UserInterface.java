import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInterface {
    private User currentUser;
    private UserDatabase userDatabase;
    private Lesson[] lessons;
    private Quiz[] quizzes;
    private int lessonCount;
    private int quizCount;
    private Scanner scanner;

    public UserInterface() {
        currentUser = null;
        userDatabase = new UserDatabase();
        lessons = new Lesson[20];
        quizzes = new Quiz[20];
        lessonCount = 0;
        quizCount = 0;
        scanner = new Scanner(System.in);

        setupSampleData();
    }

    public void start() {
        boolean running = true;

        printWelcomeScreen();

        try {
            while (running) {
                printHeader("MAIN MENU");
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                printLine();
                int choice = readInt("Choose: ");

                switch (choice) {
                    case 1:
                        signUp();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        running = false;
                        printHeader("GOODBYE");
                        System.out.println("Thank you for using the Smart Tutoring System.");
                        break;
                    default:
                        System.out.println("Please choose 1 to 3 only.");
                }
            }
        } catch (NoSuchElementException e) {
            // Kapag sarado na yung input, lalabas nang maayos ang program.
            System.out.println("\nInput closed. Exiting the program.");
        } finally {
            scanner.close();
        }
    }

    public void signUp() {
        printHeader("SIGN UP");
        String fullName = readRequired("Full name: ");
        String email = readEmail("Email: ");
        String password = readRequired("Password: ");

        System.out.println("\nChoose your role");
        printLine();
        System.out.println("1. Student");
        System.out.println("2. Tutor");
        System.out.println("3. Admin");
        int role = readIntInRange("Role: ", 1, 3);

        User newUser;
        if (role == 1) {
            String learningStyle = selectLearningStyle();
            newUser = new Student(fullName, email, password, learningStyle);
        } else if (role == 2) {
            newUser = new Tutor(fullName, email, password);
        } else if (role == 3) {
            newUser = new Admin(fullName, email, password);
        } else {
            return;
        }

        try {
            userDatabase.addUser(newUser);
            printSuccess("Account created for " + newUser.getRole() + ".");
        } catch (DuplicateEmailException e) {
            // Custom exception to kapag may kaparehong email.
            System.out.println("Sign-up error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Storage error: " + e.getMessage());
        }
    }

    public void login() {
        printHeader("LOGIN");
        String email = readRequired("Email: ");
        String password = readRequired("Password: ");

        try {
            currentUser = userDatabase.authenticate(email, password);
            printSuccess("Welcome, " + currentUser.getFullName() + "!");
            showDashboard();
        } catch (InvalidLoginException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    public void logout() {
        currentUser = null;
        printSuccess("Logged out successfully.");
    }

    public void showDashboard() {
        if (currentUser instanceof Student) {
            showStudentMenu((Student) currentUser);
        } else if (currentUser instanceof Tutor) {
            showTutorMenu((Tutor) currentUser);
        } else if (currentUser instanceof Admin) {
            showAdminMenu((Admin) currentUser);
        }
    }

    private void showStudentMenu(Student student) {
        boolean insideMenu = true;

        while (insideMenu) {
            printHeader("STUDENT MENU");
            System.out.println("Logged in as: " + student.getFullName());
            printLine();
            System.out.println("1. View lessons");
            System.out.println("2. Recommended lesson");
            System.out.println("3. Take quiz");
            System.out.println("4. View progress");
            System.out.println("5. Change learning style");
            System.out.println("6. Update profile");
            System.out.println("7. Logout");
            printLine();
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1:
                    openLesson();
                    break;
                case 2:
                    recommendLesson(student);
                    break;
                case 3:
                    openQuiz(student);
                    break;
                case 4:
                    student.viewProgress();
                    pause();
                    break;
                case 5:
                    student.setLearningStyle(selectLearningStyle());
                    printSuccess("Learning style updated.");
                    break;
                case 6:
                    updateCurrentProfile();
                    break;
                case 7:
                    logout();
                    insideMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showTutorMenu(Tutor tutor) {
        boolean insideMenu = true;

        while (insideMenu) {
            printHeader("TUTOR MENU");
            System.out.println("Logged in as: " + tutor.getFullName());
            printLine();
            System.out.println("1. View lessons");
            System.out.println("2. Create lesson");
            System.out.println("3. View quizzes");
            System.out.println("4. Create quiz");
            System.out.println("5. Update profile");
            System.out.println("6. Logout");
            printLine();
            int choice = readInt("Choose: ");

            if (choice == 1) {
                listLessons();
                pause();
            } else if (choice == 2) {
                createLessonFromInput(tutor);
            } else if (choice == 3) {
                listQuizzes();
                pause();
            } else if (choice == 4) {
                createQuizFromInput(tutor);
            } else if (choice == 5) {
                updateCurrentProfile();
            } else if (choice == 6) {
                logout();
                insideMenu = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void showAdminMenu(Admin admin) {
        boolean insideMenu = true;

        while (insideMenu) {
            printHeader("ADMIN MENU");
            System.out.println("Logged in as: " + admin.getFullName());
            printLine();
            System.out.println("1. View user accounts");
            System.out.println("2. Remove a user account");
            System.out.println("3. Update profile");
            System.out.println("4. Logout");
            printLine();
            int choice = readInt("Choose: ");

            if (choice == 1) {
                admin.manageUser(userDatabase);
                pause();
            } else if (choice == 2) {
                String email = readRequired("Email to remove: ");
                if (email.equalsIgnoreCase(admin.getEmail())) {
                    System.out.println("You cannot remove your account while logged in.");
                } else if (!userDatabase.emailExists(email)) {
                    System.out.println("User account not found.");
                } else if (readYesNo("Remove the account " + email + "?")) {
                    admin.removeUser(userDatabase, email);
                } else {
                    System.out.println("Removal cancelled.");
                }
            } else if (choice == 3) {
                updateCurrentProfile();
            } else if (choice == 4) {
                logout();
                insideMenu = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void openLesson() {
        listLessons();
        if (lessonCount == 0) {
            return;
        }

        int number = readInt("Lesson number: ");
        if (number >= 1 && number <= lessonCount) {
            lessons[number - 1].displayLesson();
            pause();
        } else {
            System.out.println("Invalid lesson number.");
        }
    }

    private void openQuiz(Student student) {
        listQuizzes();
        if (quizCount == 0) {
            return;
        }

        int number = readInt("Quiz number: ");
        if (number >= 1 && number <= quizCount) {
            student.takeQuiz(quizzes[number - 1], scanner);
            pause();
        } else {
            System.out.println("Invalid quiz number.");
        }
    }

    private void listLessons() {
        printHeader("AVAILABLE LESSONS");
        if (lessonCount == 0) {
            System.out.println("No lessons yet.");
            return;
        }
        for (int i = 0; i < lessonCount; i++) {
            System.out.println((i + 1) + ". " + lessons[i].getLessonTitle()
                    + " | " + lessons[i].getLessonFormat()
                    + " | Level " + lessons[i].getDifficultyLevel());
        }
    }

    private void listQuizzes() {
        printHeader("AVAILABLE QUIZZES");
        if (quizCount == 0) {
            System.out.println("No quizzes yet.");
            return;
        }
        for (int i = 0; i < quizCount; i++) {
            System.out.println((i + 1) + ". " + quizzes[i].getQuizTitle()
                    + " | " + quizzes[i].getQuestionCount() + " questions"
                    + " | Passing: " + String.format("%.2f", quizzes[i].getPassingScore()) + "%");
        }
    }

    private void createLessonFromInput(Tutor tutor) {
        if (lessonCount >= lessons.length) {
            System.out.println("Lesson storage is full.");
            return;
        }

        printHeader("CREATE LESSON");
        String title = readRequired("Lesson title: ");
        String subject = readRequired("Subject: ");
        int difficulty = readIntInRange("Difficulty (1 to 3): ", 1, 3);
        String format = selectLearningStyle();
        String content = readRequired("Short lesson content: ");

        Lesson lesson = tutor.createLesson(title, subject, difficulty, content, format);
        lessons[lessonCount] = lesson;
        lessonCount++;
        printSuccess("Lesson created.");
    }

    private void createQuizFromInput(Tutor tutor) {
        if (quizCount >= quizzes.length) {
            System.out.println("Quiz storage is full.");
            return;
        }

        printHeader("CREATE QUIZ");
        String title = readRequired("Quiz title: ");
        double passing = readDoubleInRange("Passing score (0 to 100): ", 0, 100);
        Quiz quiz = tutor.createQuiz(title, passing);

        int totalQuestions = readIntInRange("Number of questions (1 to 10): ", 1, 10);
        for (int questionNumber = 1; questionNumber <= totalQuestions; questionNumber++) {
            System.out.println("\nQuestion " + questionNumber + " type");
            printLine();
            System.out.println("1. Multiple Choice");
            System.out.println("2. True or False");
            int type = readIntInRange("Choose: ", 1, 2);

            if (type == 1) {
                String questionText = readRequired("\nQuestion: ");
                String[] choices = new String[4];
                for (int i = 0; i < choices.length; i++) {
                    choices[i] = readRequired("Choice " + (char) ('A' + i) + ": ");
                }
                int answerNumber = readIntInRange("Correct choice (1 to 4): ", 1, 4);
                String correctLetter = String.valueOf((char) ('A' + answerNumber - 1));
                quiz.addQuestion(new MultipleChoiceQuestion(
                        questionText, correctLetter, choices));
            } else {
                String questionText = readRequired("Question: ");
                System.out.println("1. True");
                System.out.println("2. False");
                int answer = readIntInRange("Correct answer: ", 1, 2);
                String correctAnswer = answer == 1 ? "True" : "False";
                quiz.addQuestion(new TrueFalseQuestion(questionText, correctAnswer));
            }
        }

        quizzes[quizCount] = quiz;
        quizCount++;
        quiz.generateQuiz();
    }

    private void updateCurrentProfile() {
        printHeader("UPDATE PROFILE");
        String newName = readRequired("New full name: ");
        String newEmail = readEmail("New email: ");

        try {
            userDatabase.updateUserProfile(currentUser, newName, newEmail);
            printSuccess("Profile updated.");
        } catch (DuplicateEmailException e) {
            System.out.println("Update error: " + e.getMessage());
        }
    }

    private String selectLearningStyle() {
        System.out.println("\nSelect learning style or lesson format");
        printLine();
        System.out.println("1. Text");
        System.out.println("2. Video");
        System.out.println("3. Practice");
        int choice = readIntInRange("Learning style or format: ", 1, 3);

        if (choice == 1) {
            return "Text";
        } else if (choice == 2) {
            return "Video";
        }
        return "Practice";
    }

    private Lesson[] getActiveLessons() {
        Lesson[] activeLessons = new Lesson[lessonCount];
        for (int i = 0; i < lessonCount; i++) {
            activeLessons[i] = lessons[i];
        }
        return activeLessons;
    }

    // Available subjects lang ang pipiliin para hindi na kailangang i-type.
    private void recommendLesson(Student student) {
        String[] subjects = getSubjects();
        if (subjects.length == 0) {
            System.out.println("No lesson available.");
            return;
        }
        printHeader("CHOOSE A SUBJECT");
        for (int i = 0; i < subjects.length; i++) {
            System.out.println((i + 1) + ". " + subjects[i]);
        }
        int choice = readIntInRange("Subject: ", 1, subjects.length);
        Lesson suggested = student.recommendLessons(getActiveLessons(), subjects[choice - 1]);
        if (suggested == null) {
            System.out.println("No matching lesson available for this subject.");
        } else {
            printHeader("RECOMMENDED LESSON");
            System.out.println("Learning style: " + student.getLearningStyle()
                    + " | Suggested level: " + student.getSuggestedLevel());
            System.out.println("Recommended: " + suggested.getLessonTitle());
            suggested.displayLesson();
        }
        pause();
    }

    private String[] getSubjects() {
        String[] found = new String[lessonCount];
        int count = 0;
        for (int i = 0; i < lessonCount; i++) {
            String subject = lessons[i].getSubject();
            boolean exists = false;
            for (int j = 0; j < count; j++) {
                if (found[j].equalsIgnoreCase(subject)) {
                    exists = true;
                }
            }
            if (!exists) {
                found[count] = subject;
                count++;
            }
        }
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = found[i];
        }
        return result;
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String value = readRequired(prompt + " (y/n): ");
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes")) {
                return true;
            }
            if (value.equalsIgnoreCase("n") || value.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }

    // Para mabasa muna yung output bago bumalik sa menu.
    private void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private int readIntInRange(String prompt, int minimum, int maximum) {
        while (true) {
            int value = readInt(prompt);
            if (value >= minimum && value <= maximum) {
                return value;
            }
            System.out.println("Please enter a number from " + minimum + " to " + maximum + ".");
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readDoubleInRange(String prompt, double minimum, double maximum) {
        while (true) {
            double value = readDouble(prompt);
            if (value >= minimum && value <= maximum) {
                return value;
            }
            System.out.println("Please enter a value from " + minimum + " to " + maximum + ".");
        }
    }

    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private String readEmail(String prompt) {
        while (true) {
            String email = readRequired(prompt);
            int atPosition = email.indexOf('@');
            int dotPosition = email.lastIndexOf('.');
            if (atPosition > 0 && dotPosition > atPosition + 1
                    && dotPosition < email.length() - 1) {
                return email;
            }
            System.out.println("Please enter a valid email address.");
        }
    }

    private void setupSampleData() {
        Lesson lesson1 = new Lesson(
                "OOP Basics", "Object-Oriented Programming", 1,
                "Classes are blueprints while objects are actual instances of a class.", "Text");
        Lesson lesson2 = new Lesson(
                "Encapsulation Practice", "Object-Oriented Programming", 1,
                "Practice using private variables and public methods.", "Practice");
        Lesson lesson3 = new Lesson(
                "Inheritance Review", "Object-Oriented Programming", 1,
                "A short video-style guide about parent and child classes.", "Video");
        Lesson lesson4 = new Lesson(
                "Polymorphism Practice", "Object-Oriented Programming", 2,
                "Practice calling overridden methods through a parent type.", "Practice");
        Lesson lesson5 = new Lesson(
                "Abstract Classes", "Object-Oriented Programming", 2,
                "Abstract classes can contain shared code and abstract methods.", "Text");
        Lesson lesson6 = new Lesson(
                "OOP Design Challenge", "Object-Oriented Programming", 3,
                "Combine encapsulation, inheritance, and polymorphism in one design.", "Practice");

        lessons[lessonCount++] = lesson1;
        lessons[lessonCount++] = lesson2;
        lessons[lessonCount++] = lesson3;
        lessons[lessonCount++] = lesson4;
        lessons[lessonCount++] = lesson5;
        lessons[lessonCount++] = lesson6;

        Quiz sampleQuiz = new Quiz("OOP Starter Quiz", 60);
        String[] choices = {"Class", "Loop", "Variable", "Scanner"};
        sampleQuiz.addQuestion(new MultipleChoiceQuestion(
                "What is used as a blueprint for objects?", "A", choices));
        sampleQuiz.addQuestion(new TrueFalseQuestion(
                "Inheritance allows a child class to receive features from a parent class.", "True"));
        String[] accessChoices = {"public", "private", "static", "final"};
        sampleQuiz.addQuestion(new MultipleChoiceQuestion(
                "Which access modifier hides a variable inside its class?", "B", accessChoices));
        sampleQuiz.addQuestion(new TrueFalseQuestion(
                "Polymorphism allows one parent type to refer to different child objects.", "True"));
        quizzes[quizCount++] = sampleQuiz;

        try {
            // Demo accounts para madaling ma-test yung tatlong role sa presentation.
            userDatabase.addUser(new Student(
                    "Demo Student", "student@hau.edu.ph", "student123", "Practice"));
            userDatabase.addUser(new Tutor("Demo Tutor", "tutor@hau.edu.ph", "tutor123"));
            userDatabase.addUser(new Admin("Demo Admin", "admin@hau.edu.ph", "admin123"));
        } catch (DuplicateEmailException e) {
            System.out.println("Sample account error: " + e.getMessage());
        }
    }

    // Simple helpers para pare-pareho ang spacing yung headers sa console.
    private void printWelcomeScreen() {
        System.out.println();
        System.out.println("=======================================================");
        System.out.println("                 SMART TUTORING SYSTEM");
        System.out.println("=======================================================");
        System.out.println("        Learn, practice, and track your progress");
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println("=======================================================");
        System.out.println(" " + title);
        System.out.println("=======================================================");
    }

    private void printLine() {
        System.out.println("-------------------------------------------------------");
    }

    private void printSuccess(String message) {
        System.out.println("\n[Success] " + message);
    }
}

