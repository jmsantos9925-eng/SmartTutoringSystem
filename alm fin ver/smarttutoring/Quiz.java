import java.util.Scanner;

public class Quiz {
    private String quizTitle;
    private Question[] questions;
    private double passingScore;
    private int questionCount;

    public Quiz(String quizTitle, double passingScore) {
        this.quizTitle = quizTitle;
        this.passingScore = passingScore;
        questions = new Question[10];
        questionCount = 0;
    }

    public void addQuestion(Question question) {
        if (question == null) {
            return;
        }

        if (questionCount < questions.length) {
            questions[questionCount] = question;
            questionCount++;
        } else {
            System.out.println("A quiz can contain up to 10 questions.");
        }
    }

    public void generateQuiz() {
        System.out.println("\n[Success] Quiz ready: " + quizTitle
                + " with " + questionCount + " questions.");
    }

    public double startQuiz(Scanner scanner, String studentName) {
        if (questionCount == 0) {
            System.out.println("This quiz has no questions yet.");
            return 0;
        }

        int correctAnswers = 0;
        System.out.println("\n=======================================================");
        System.out.println(" QUIZ: " + quizTitle);
        System.out.println("=======================================================");

        for (int i = 0; i < questionCount; i++) {
            System.out.println("\nQuestion " + (i + 1));
            System.out.println("-------------------------------------------------------");
            questions[i].displayQuestion();
            if (questions[i] instanceof MultipleChoiceQuestion) {
                System.out.println("Enter A, B, C, or D.");
            } else if (questions[i] instanceof TrueFalseQuestion) {
                System.out.println("Enter A/B or True/False.");
            }

            String answer = "";
            boolean valid = false;

            // repeat yung question kapag hindi valid ang input
            while (!valid) {
                System.out.print("Your answer: ");
                answer = scanner.nextLine().trim();

                // letters lang pwedeng sagot sa multiple choice.
                if (questions[i] instanceof MultipleChoiceQuestion) {
                    if (answer.equalsIgnoreCase("A") || answer.equalsIgnoreCase("B") ||
                        answer.equalsIgnoreCase("C") || answer.equalsIgnoreCase("D")) {
                        valid = true;
                    } else {
                        System.out.println("Please enter A, B, C, or D.");
                    }
                // Dapat both ang allowed sa input at answer checking.
                } else if (questions[i] instanceof TrueFalseQuestion) {
                    if (answer.equalsIgnoreCase("True") || answer.equalsIgnoreCase("False") ||
                        answer.equalsIgnoreCase("A") || answer.equalsIgnoreCase("B")) {
                        valid = true;
                    } else {
                        System.out.println("Please enter A/B or True/False.");
                    }
                } else {
                    valid = true;
                }
            }

            if (questions[i].checkAnswer(answer)) {
                System.out.println("Correct!");
                correctAnswers++;
            } else {
                System.out.println("Incorrect.");
            }
        }

        double score = calculateScore(correctAnswers);
        System.out.println("\n-------------------------------------------------------");
        // sshow yung pangalan ng students and result
        System.out.println("Here are the results from the quiz, " + studentName);
        System.out.println("Score: " + String.format("%.2f", score) + "%");
        System.out.println(isPassed(score) ? "Result: Passed" : "Result: Try again");
        System.out.println("=======================================================");
        return score;
    }

    public double calculateScore(int correctAnswers) {
        if (questionCount == 0) {
            return 0;
        }
        return ((double) correctAnswers / questionCount) * 100;
    }

    public boolean isPassed(double score) {
        return score >= passingScore;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public double getPassingScore() {
        return passingScore;
    }
}
