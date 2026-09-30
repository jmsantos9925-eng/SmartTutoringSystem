package smarttutoring;

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

        System.out.println("=================================");
        System.out.println("     SMART TUTORING SYSTEM");
        System.out.println("=================================");

        while (running) {
            System.out.println("\n1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
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
                    System.out.println("Thank you for using STS.");
                    break;
                default:
                    System.out.println("Please choose 1 to 3 only.");
            }
        }

        scanner.close();
    }

    public void signUp() {
        System.out.println("\n--- Sign Up ---");
        System.out.print("Full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("1. Student");
        System.out.println("2. Tutor");
        System.out.println("3. Admin");
        int role = readInt("Role: ");

        User newUser;
        if (role == 1) {
            System.out.print("Learning style (Text, Video, or Practice): ");
            String learningStyle = scanner.nextLine().trim();
            newUser = new Student(fullName, email, password, learningStyle);
        } else if (role == 2) {
            newUser = new Tutor(fullName, email, password);
        } else if (role == 3) {
            newUser = new Admin(fullName, email, password);
        } else {
            System.out.println("Invalid role. Sign-up cancelled.");
            return;
        }

        try {
            userDatabase.addUser(newUser);
            System.out.println("Account created for " + newUser.getRole() + ".");
        } catch (DuplicateEmailException e) {
            // Ito ang custom exception kapag may kaparehong email.
            System.out.println("Sign-up error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Storage error: " + e.getMessage());
        }
    }

    public void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            currentUser = userDatabase.authenticate(email, password);
            System.out.println("Welcome, " + currentUser.getFullName() + "!");
            showDashboard();
        } catch (InvalidLoginException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
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
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View lessons");
            System.out.println("2. Recommended lesson");
            System.out.println("3. Take quiz");
            System.out.println("4. View progress");
            System.out.println("5. Logout");
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1:
                    openLesson();
                    break;
                case 2:
                    System.out.print("Preferred subject: ");
                    String preferredSubject = scanner.nextLine().trim();
                    Lesson suggested = student.recommendLessons(getActiveLessons(), preferredSubject);
                    if (suggested == null) {
                        System.out.println("No lesson available.");
                    } else {
                        System.out.println("Recommended: " + suggested.getLessonTitle());
                        suggested.displayLesson();
                    }
                    break;
                case 3:
                    openQuiz(student);
                    break;
                case 4:
                    student.viewProgress();
                    break;
                case 5:
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
            System.out.println("\n--- Tutor Menu ---");
            System.out.println("1. View lessons");
            System.out.println("2. Create lesson");
            System.out.println("3. View quizzes");
            System.out.println("4. Create quiz");
            System.out.println("5. Logout");
            int choice = readInt("Choose: ");

            if (choice == 1) {
                listLessons();
            } else if (choice == 2) {
                createLessonFromInput(tutor);
            } else if (choice == 3) {
                listQuizzes();
            } else if (choice == 4) {
                createQuizFromInput(tutor);
            } else if (choice == 5) {
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
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View user accounts");
            System.out.println("2. Remove a user account");
            System.out.println("3. Logout");
            int choice = readInt("Choose: ");

            if (choice == 1) {
                admin.manageUser(userDatabase);
            } else if (choice == 2) {
                System.out.print("Email to remove: ");
                String email = scanner.nextLine().trim();
                if (email.equalsIgnoreCase(admin.getEmail())) {
                    System.out.println("You cannot remove your account while logged in.");
                } else {
                    admin.removeUser(userDatabase, email);
                }
            } else if (choice == 3) {
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
        } else {
            System.out.println("Invalid quiz number.");
        }
    }

    private void listLessons() {
        System.out.println("\n--- Lessons ---");
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
        System.out.println("\n--- Quizzes ---");
        if (quizCount == 0) {
            System.out.println("No quizzes yet.");
            return;
        }
        for (int i = 0; i < quizCount; i++) {
            System.out.println((i + 1) + ". " + quizzes[i].getQuizTitle()
                    + " | " + quizzes[i].getQuestionCount() + " questions");
        }
    }

    private void createLessonFromInput(Tutor tutor) {
        if (lessonCount >= lessons.length) {
            System.out.println("Lesson storage is full.");
            return;
        }

        System.out.print("Lesson title: ");
        String title = scanner.nextLine();
        System.out.print("Subject: ");
        String subject = scanner.nextLine();
        int difficulty = readInt("Difficulty (1 to 3): ");
        System.out.print("Lesson format (Text, Video, or Practice): ");
        String format = scanner.nextLine();
        System.out.print("Short lesson content: ");
        String content = scanner.nextLine();

        Lesson lesson = tutor.createLesson(title, subject, difficulty, content, format);
        lessons[lessonCount] = lesson;
        lessonCount++;
        System.out.println("Lesson created.");
    }

    private void createQuizFromInput(Tutor tutor) {
        if (quizCount >= quizzes.length) {
            System.out.println("Quiz storage is full.");
            return;
        }

        System.out.print("Quiz title: ");
        String title = scanner.nextLine();
        double passing = readDouble("Passing score: ");
        Quiz quiz = tutor.createQuiz(title, passing);

        System.out.print("Multiple-choice question: ");
        String mcqText = scanner.nextLine();
        String[] choices = new String[4];
        for (int i = 0; i < choices.length; i++) {
            System.out.print("Choice " + (char) ('A' + i) + ": ");
            choices[i] = scanner.nextLine();
        }
        System.out.print("Correct answer text: ");
        String mcqAnswer = scanner.nextLine();
        quiz.addQuestion(new MultipleChoiceQuestion(mcqText, mcqAnswer, choices));

        System.out.print("True or false question: ");
        String tfText = scanner.nextLine();
        System.out.print("Correct answer (True/False): ");
        String tfAnswer = scanner.nextLine();
        quiz.addQuestion(new TrueFalseQuestion(tfText, tfAnswer));

        quizzes[quizCount] = quiz;
        quizCount++;
        quiz.generateQuiz();
    }

    private Lesson[] getActiveLessons() {
        Lesson[] activeLessons = new Lesson[lessonCount];
        for (int i = 0; i < lessonCount; i++) {
            activeLessons[i] = lessons[i];
        }
        return activeLessons;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Number lang ang ilagay.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Valid number ang kailangan.");
            }
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

        lessons[lessonCount++] = lesson1;
        lessons[lessonCount++] = lesson2;
        lessons[lessonCount++] = lesson3;

        Quiz sampleQuiz = new Quiz("OOP Starter Quiz", 60);
        String[] choices = {"Class", "Loop", "Variable", "Scanner"};
        sampleQuiz.addQuestion(new MultipleChoiceQuestion(
                "What is used as a blueprint for objects?", "Class", choices));
        sampleQuiz.addQuestion(new TrueFalseQuestion(
                "Inheritance allows a child class to receive features from a parent class.", "True"));
        quizzes[quizCount++] = sampleQuiz;

        try {
            // May demo accounts para mabilis ma-test sa presentation.
            userDatabase.addUser(new Tutor("Demo Tutor", "tutor@sts.com", "tutor123"));
            userDatabase.addUser(new Admin("Demo Admin", "admin@sts.com", "admin123"));
        } catch (DuplicateEmailException e) {
            System.out.println("Sample account error: " + e.getMessage());
        }
    }
}

