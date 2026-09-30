package smarttutoring;

public class ProgressTracker {
    private double[] quizScores;
    private int scoreCount;

    public ProgressTracker() {
        quizScores = new double[20];
        scoreCount = 0;
    }

    public void recordScore(double score) {
        if (scoreCount < quizScores.length) {
            quizScores[scoreCount] = score;
            scoreCount++;
        } else {
            System.out.println("Score storage is full. The new score was not saved.");
        }
    }

    public double calculateProgress() {
        if (scoreCount == 0) {
            return 0;
        }

        double total = 0;
        for (int i = 0; i < scoreCount; i++) {
            total = total + quizScores[i];
        }
        return total / scoreCount;
    }

    public String generateFeedback() {
        if (scoreCount == 0) {
            return "No quiz scores yet. Take a quiz first.";
        }

        double average = calculateProgress();
        if (average >= 85) {
            return "Great work! You are ready for a more difficult lesson.";
        } else if (average >= 60) {
            return "Good progress. Practice a little more.";
        }
        return "Review the basic lesson and try the quiz again.";
    }

    public void displayProgress() {
        System.out.println("\n--- Progress ---");
        if (scoreCount == 0) {
            System.out.println("No recorded score yet.");
        } else {
            for (int i = 0; i < scoreCount; i++) {
                System.out.println("Quiz " + (i + 1) + ": " + String.format("%.2f", quizScores[i]) + "%");
            }
            System.out.println("Average: " + String.format("%.2f", calculateProgress()) + "%");
        }
        System.out.println("Feedback: " + generateFeedback());
    }

    public int getScoreCount() {
        return scoreCount;
    }
}

