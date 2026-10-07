public class Lesson {
    private String lessonTitle;
    private String subject;
    private int difficultyLevel;
    private String lessonContent;
    private String lessonFormat;

    public Lesson(String lessonTitle, String subject, int difficultyLevel,
                  String lessonContent, String lessonFormat) {
        this.lessonTitle = lessonTitle.trim();
        this.subject = subject.trim();
        this.difficultyLevel = difficultyLevel;
        this.lessonContent = lessonContent.trim();
        this.lessonFormat = lessonFormat.trim();
    }

    public void displayLesson() {
        System.out.println("\n=======================================================");
        System.out.println(" LESSON: " + lessonTitle);
        System.out.println("=======================================================");
        System.out.println("Subject: " + subject);
        System.out.println("Difficulty: " + difficultyLevel);
        System.out.println("Format: " + lessonFormat);
        System.out.println("-------------------------------------------------------");
        System.out.println(lessonContent);
        System.out.println("-------------------------------------------------------");
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

