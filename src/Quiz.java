import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Scanner;

// CONCEPT: Interface (implements) + Multithreading (uses TimerThread)
public class Quiz implements QuizOperations {

    private static final int TIME_LIMIT_SECONDS = 60;
    private final Scanner sc;

    public Quiz(Scanner sc) {
        this.sc = sc;
    }

    private ArrayList<Question> loadQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> col = db.getCollection("questions");

            try (MongoCursor<Document> cursor = col.find().iterator()) {
                while (cursor.hasNext()) {
                    Document d = cursor.next();
                    list.add(new Question(
                            d.getString("questionText"),
                            d.getString("optionA"),
                            d.getString("optionB"),
                            d.getString("optionC"),
                            d.getString("optionD"),
                            d.getString("answer")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("[Error] Could not load questions: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void startQuiz(Participant participant) {

        ArrayList<Question> questions = loadQuestions();

        if (questions.isEmpty()) {
            System.out.println("No questions available. Ask the admin to add some first.");
            return;
        }

        // Start timer in a separate thread
        TimerThread timer = new TimerThread(TIME_LIMIT_SECONDS);
        timer.start();

        int score = 0;

        for (int i = 0; i < questions.size(); i++) {

            if (timer.isTimeUp()) {
                System.out.println("Quiz stopped — time ran out.");
                break;
            }

            Question q = questions.get(i);
            q.display(i + 1);
            System.out.print("Your answer (A/B/C/D) or Q to quit: ");

            // CONCEPT: Exception Handling
            try {
                String userAnswer = sc.next().trim().toUpperCase();

                if (userAnswer.equals("Q")) {
                    System.out.println("Quiz exited early.");
                    break;
                }

                if (!userAnswer.equals("A") && !userAnswer.equals("B")
                        && !userAnswer.equals("C") && !userAnswer.equals("D")) {
                    throw new IllegalArgumentException(
                            "Invalid choice '" + userAnswer + "'. Skipping question.");
                }

                if (userAnswer.equals(q.getAnswer())) {
                    System.out.println("✔  Correct!");
                    score++;
                } else {
                    System.out.println("✘  Wrong! Correct answer: " + q.getAnswer());
                }

            } catch (IllegalArgumentException e) {
                System.out.println("[Input Error] " + e.getMessage());
            }
        }

        if (!timer.isTimeUp()) {
            timer.interrupt();
        }

        participant.setScore(score);
        participant.saveResult(score, questions.size());

        System.out.println("\n========== QUIZ FINISHED ==========");
        System.out.println("Score: " + score + " / " + questions.size());
        System.out.println("====================================\n");
    }

    @Override
    public void showResult(Participant participant) {
        participant.viewMyResults();
    }
}