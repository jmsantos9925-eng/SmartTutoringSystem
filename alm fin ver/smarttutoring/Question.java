package smarttutoring;

public abstract class Question {
    private String questionText;
    private String correctAnswer;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText.trim();
        this.correctAnswer = correctAnswer.trim();
    }

    public String getQuestionText() {
        return questionText;
    }

    protected String getCorrectAnswer() {
        return correctAnswer;
    }

    // Magkaiba ang display at checking depende sa actual question type.
    public abstract void displayQuestion();

    public abstract boolean checkAnswer(String answer);
}

