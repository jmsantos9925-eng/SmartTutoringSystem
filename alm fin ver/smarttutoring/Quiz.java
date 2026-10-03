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
        System.out.println("Quiz ready: " + quizTitle + " with " + questionCount + " questions.");
    }

    public double startQuiz(Scanner scanner) {
        if (questionCount == 0) {
            System.out.println("This quiz has no questions yet.");
            return 0;
        }

        int correctAnswers = 0;
        System.out.println("\n=== " + quizTitle + " ===");

        for (int i = 0; i < questionCount; i++) {
            System.out.println("\nQuestion " + (i + 1));
            questions[i].displayQuestion();
            System.out.print("Your answer: ");
            String answer = scanner.nextLine();

            // Question ang array type pero child class method ang tumatakbo.
            if (questions[i].checkAnswer(answer)) {
                System.out.println("Correct!");
                correctAnswers++;
            } else {
                System.out.println("Incorrect.");
            }
        }

        double score = calculateScore(correctAnswers);
        System.out.println("\nScore: " + String.format("%.2f", score) + "%");
        System.out.println(isPassed(score) ? "Result: Passed" : "Result: Try again");
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

