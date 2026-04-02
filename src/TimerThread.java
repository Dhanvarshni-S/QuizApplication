// CONCEPT: Multithreading
public class TimerThread extends Thread {

    private int seconds;
    private boolean timeUp = false;

    public TimerThread(int seconds) {
        this.seconds = seconds;
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    @Override
    public void run() {
        try {
            for (int remaining = seconds; remaining > 0; remaining--) {
                if (remaining == seconds) {
                    System.out.println("\n⏱  Timer started: " + seconds + " seconds.");
                }
                if (remaining == 30 || remaining == 10) {
                    System.out.println("\n⚠  " + remaining + " seconds remaining!");
                }
                Thread.sleep(1000);
            }
            timeUp = true;
            System.out.println("\n⏰  TIME'S UP! Quiz ended automatically.");

        } catch (InterruptedException e) {
            // Quiz finished before timer ran out — normal behavior
        }
    }
}