package smarttutoring;

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

        double average = progressTracker.calculateProgress();
        int suggestedLevel = 1;
        if (average >= 85) {
            suggestedLevel = 3;
        } else if (average >= 60) {
            suggestedLevel = 2;
        }

        // Fixed rules lang muna: subject, format at quiz average ang basehan.
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
            if (lesson != null) {
                return lesson;
            }
        }
        return null;
    }

    public void takeQuiz(Quiz quiz, Scanner scanner) {
        double score = quiz.startQuiz(scanner);
        progressTracker.recordScore(score);
    }

    public void viewProgress() {
        progressTracker.displayProgress();
    }

    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }
}

