package smarttutoring;

public class Tutor extends Staff {
    public Tutor(String fullName, String email, String password) {
        super(fullName, email, password);
    }

    @Override
    public String getRole() {
        return "Tutor";
    }

    public Lesson createLesson(String title, String subject, int difficulty,
                               String content, String format) {
        return new Lesson(title, subject, difficulty, content, format);
    }

    public Quiz createQuiz(String title, double passingScore) {
        return new Quiz(title, passingScore);
    }
}

