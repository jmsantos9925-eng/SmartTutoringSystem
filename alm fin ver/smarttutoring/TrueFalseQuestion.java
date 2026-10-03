public class TrueFalseQuestion extends Question {
    public TrueFalseQuestion(String questionText, String correctAnswer) {
        super(questionText, correctAnswer);
    }

    @Override
    public void displayQuestion() {
        System.out.println(getQuestionText());
        System.out.println("A. True");
        System.out.println("B. False");
    }

    @Override
    public boolean checkAnswer(String answer) {
        String cleaned = answer.trim();
        if (cleaned.equalsIgnoreCase("A")) {
            cleaned = "True";
        } else if (cleaned.equalsIgnoreCase("B")) {
            cleaned = "False";
        }

        return cleaned.equalsIgnoreCase(getCorrectAnswer());
    }
}

