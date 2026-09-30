package smarttutoring;

public abstract class Question {
    private String questionText;
    private String correctAnswer;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    protected String getCorrectAnswer() {
        return correctAnswer;
    }

    // Magkaiba ang pag-display at pag-check depende sa question type.
    public abstract void displayQuestion();

    public abstract boolean checkAnswer(String answer);
}

