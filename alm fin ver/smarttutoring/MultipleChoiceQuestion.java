package smarttutoring;

public class MultipleChoiceQuestion extends Question {
    private String[] choices;

    public MultipleChoiceQuestion(String questionText, String correctAnswer, String[] choices) {
        super(questionText, correctAnswer);
        this.choices = new String[choices.length];
        for (int i = 0; i < choices.length; i++) {
            this.choices[i] = choices[i].trim();
        }
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

        // Puwedeng letter o buong sagot ang ilagay ng student.
        for (int i = 0; i < choices.length; i++) {
            String letter = String.valueOf((char) ('A' + i));
            boolean isCorrectChoice = letter.equalsIgnoreCase(getCorrectAnswer())
                    || choices[i].equalsIgnoreCase(getCorrectAnswer());

            if (isCorrectChoice) {
                return cleanedAnswer.equalsIgnoreCase(letter)
                        || cleanedAnswer.equalsIgnoreCase(choices[i]);
            }
        }
        return false;
    }
}

