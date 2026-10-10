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
        System.out.println("Enter A, B, C, or D.");
    }

    @Override
    public boolean checkAnswer(String answer) {
        String cleanedAnswer = answer.trim();

        // Letters lang ang allowed at dito kino compare sa tamang choice.
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

    @Override
    public boolean isValidAnswer(String answer) {
        String cleaned = answer.trim();
        return cleaned.length() == 1 && Character.toUpperCase(cleaned.charAt(0)) >= 'A'
                && Character.toUpperCase(cleaned.charAt(0)) < 'A' + choices.length;
    }

    @Override
    public String getCorrectAnswerDisplay() {
        for (int i = 0; i < choices.length; i++) {
            String letter = String.valueOf((char) ('A' + i));
            if (letter.equalsIgnoreCase(getCorrectAnswer())
                    || choices[i].equalsIgnoreCase(getCorrectAnswer())) {
                return letter + ". " + choices[i];
            }
        }
        return getCorrectAnswer();
    }
}

