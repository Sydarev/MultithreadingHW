package ru.netology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        List<Future> threads = new ArrayList<>();
        final ExecutorService threadPool = Executors.newFixedThreadPool(7);

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        for (String text : texts) {
            Callable<String> myCall = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    int maxSize = 0;
                    for (int i = 0; i < text.length(); i++) {
                        for (int j = 0; j < text.length(); j++) {
                            if (i >= j) {
                                continue;
                            }
                            boolean bFound = false;
                            for (int k = i; k < j; k++) {
                                if (text.charAt(k) == 'b') {
                                    bFound = true;
                                    break;
                                }
                            }
                            if (!bFound && maxSize < j - i) {
                                maxSize = j - i;
                            }
                        }
                    }
                    return (text.substring(0, 100) + " -> " + maxSize);
                }
            };
            Future<String> task = threadPool.submit(myCall);
            threads.add(task);
        }
        for (Future f : threads) {
            try {
                System.out.println(f.get());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            threadPool.shutdown();
        }
//        long startTs = System.currentTimeMillis(); // start time
//        for (String text : texts) {
//            int maxSize = 0;
//            for (int i = 0; i < text.length(); i++) {
//                for (int j = 0; j < text.length(); j++) {
//                    if (i >= j) {
//                        continue;
//                    }
//                    boolean bFound = false;
//                    for (int k = i; k < j; k++) {
//                        if (text.charAt(k) == 'b') {
//                            bFound = true;
//                            break;
//                        }
//                    }
//                    if (!bFound && maxSize < j - i) {
//                        maxSize = j - i;
//                    }
//                }
//            }
//            System.out.println(text.substring(0, 100) + " -> " + maxSize);
//        }
//        long endTs = System.currentTimeMillis(); // end time
//
//        System.out.println("Time: " + (endTs - startTs) + "ms");
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