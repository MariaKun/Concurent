import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static ArrayBlockingQueue<String> arrayBlockingQueueA = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> arrayBlockingQueueB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> arrayBlockingQueueC = new ArrayBlockingQueue<>(100);
    public static final int size = 10_000;

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            String[] texts = new String[size];
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", 100_000);
            }
            try {
                for (int i = 0; i < size; i++) {
                    arrayBlockingQueueA.put(texts[i]);
                    arrayBlockingQueueB.put(texts[i]);
                    arrayBlockingQueueC.put(texts[i]);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread1 = new Thread(() -> {
            maxCount('a', arrayBlockingQueueA);
        });

        Thread thread2 = new Thread(() -> {
            maxCount('b', arrayBlockingQueueB);
        });

        Thread thread3 = new Thread(() -> {
            maxCount('c', arrayBlockingQueueC);
        });

        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();
    }

    public static void maxCount(char letter, ArrayBlockingQueue<String> arrayBlockingQueue) {
        int max = 0;
        try {
            for (int i = 0; i < size; i++) {
                String str = arrayBlockingQueue.take();
                int countA = repeatsCount(str, letter);
                if (countA > max) {
                    max = countA;
                }

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(letter + " : " + max);
    }

    public static int repeatsCount(String text, char letter) {
        return (int) text.chars().filter(ch -> ch == letter).count();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}