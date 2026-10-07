import java.util.Scanner;

public class Student extends User {
    private String learningStyle;
    private ProgressTracker progressTracker;

    public Student(String fullName, String email, String password, String learningStyle) {
        super(fullName, email, password);
        this.learningStyle = learningStyle;
        progressTracker = new ProgressTracker();
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public void setLearningStyle(String learningStyle) {
        this.learningStyle = learningStyle;
    }

    public String getLearningStyle() {
        return learningStyle;
    }

    public Lesson recommendLessons(Lesson[] lessons) {
        return recommendLessons(lessons, "");
    }

    public Lesson recommendLessons(Lesson[] lessons, String preferredSubject) {
        if (lessons == null || lessons.length == 0) {
            return null;
        }

        int suggestedLevel = getSuggestedLevel();

        // Fixed rules to - subject, learning style, and yung quiz average yung basis nya
        for (Lesson lesson : lessons) {
            if (lesson != null
                    && lesson.getSubject().equalsIgnoreCase(preferredSubject)
                    && lesson.getLessonFormat().equalsIgnoreCase(learningStyle)
                    && lesson.getDifficultyLevel() == suggestedLevel) {
                return lesson;
            }
        }

        for (Lesson lesson : lessons) {
            if (lesson != null
                    && lesson.getSubject().equalsIgnoreCase(preferredSubject)
                    && lesson.getDifficultyLevel() == suggestedLevel) {
                return lesson;
            }
        }

        for (Lesson lesson : lessons) {
            if (lesson != null
                    && lesson.getSubject().equalsIgnoreCase(preferredSubject)
                    && lesson.getLessonFormat().equalsIgnoreCase(learningStyle)) {
                return lesson;
            }
        }

        for (Lesson lesson : lessons) {
            if (lesson != null
                    && lesson.getSubject().equalsIgnoreCase(preferredSubject)) {
                return lesson;
            }
        }

        // Walang match sa subject, kaya walang unrelated na lesson na ibabalik.
        return null;
    }

    public void takeQuiz(Quiz quiz, Scanner scanner) {
        if (quiz == null || quiz.getQuestionCount() == 0) {
            System.out.println("This quiz is not ready yet.");
            return;
        }

        // Ipadala ang pangalan para kasama sa quiz result.
        double score = quiz.startQuiz(scanner, getFullName());
        progressTracker.recordScore(score);
    }

    public void viewProgress() {
        progressTracker.displayProgress();
    }

    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    public int getSuggestedLevel() {
        double average = progressTracker.calculateProgress();
        if (average >= 85) {
            return 3;
        } else if (average >= 60) {
            return 2;
        }
        return 1;
    }
}
