package smarttutoring;

public class MultipleChoiceQuestion extends Question {
    private String[] choices;

    public MultipleChoiceQuestion(String questionText, String correctAnswer, String[] choices) {
        super(questionText, correctAnswer);
        this.choices = choices;
    }

    @Override
    public void displayQuestion() {
        System.out.println(getQuestionText());
        for (int i = 0; i < choices.length; i++) {
            char letter = (char) ('A' + i);
            System.out.println(letter + ". " + choices[i]);
        }
    }

    @Override
    public boolean checkAnswer(String answer) {
        String cleanedAnswer = answer.trim();

        // pwede magtype ng letter or buong sagot para hindi hassle sa user
        for (int i = 0; i < choices.length; i++) {
            String letter = String.valueOf((char) ('A' + i));
            if (choices[i].equalsIgnoreCase(getCorrectAnswer())) {
                return cleanedAnswer.equalsIgnoreCase(letter)
                        || cleanedAnswer.equalsIgnoreCase(getCorrectAnswer());
            }
        }
        return false;
    }
}

