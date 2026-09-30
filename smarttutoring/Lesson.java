package smarttutoring;

public class Lesson {
    private String lessonTitle;
    private String subject;
    private int difficultyLevel;
    private String lessonContent;
    private String lessonFormat;

    public Lesson(String lessonTitle, String subject, int difficultyLevel,
                  String lessonContent, String lessonFormat) {
        this.lessonTitle = lessonTitle;
        this.subject = subject;
        this.difficultyLevel = difficultyLevel;
        this.lessonContent = lessonContent;
        this.lessonFormat = lessonFormat;
    }

    public void displayLesson() {
        System.out.println("\n--- " + lessonTitle + " ---");
        System.out.println("Subject: " + subject);
        System.out.println("Difficulty: " + difficultyLevel);
        System.out.println("Format: " + lessonFormat);
        System.out.println(lessonContent);
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public String getSubject() {
        return subject;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getLessonFormat() {
        return lessonFormat;
    }
}

