public class Question {

    private String questionText;
    private String optionA, optionB, optionC, optionD;
    private String answer;

    public Question(String questionText,
                    String optionA, String optionB,
                    String optionC, String optionD,
                    String answer) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer  = answer;
    }

    public String getQuestionText() { return questionText; }
    public String getOptionA()      { return optionA; }
    public String getOptionB()      { return optionB; }
    public String getOptionC()      { return optionC; }
    public String getOptionD()      { return optionD; }
    public String getAnswer()       { return answer; }

    public void display(int number) {
        System.out.println("\nQ" + number + ": " + questionText);
        System.out.println("   A) " + optionA);
        System.out.println("   B) " + optionB);
        System.out.println("   C) " + optionC);
        System.out.println("   D) " + optionD);
    }
}