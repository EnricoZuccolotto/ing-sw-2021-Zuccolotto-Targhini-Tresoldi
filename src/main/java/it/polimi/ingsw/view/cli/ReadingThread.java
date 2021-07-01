package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * This class is used to read the input stream and making the input kind of interruptible.
 */
public class ReadingThread implements Callable<String> {
    private final BufferedReader bufferedReader;

    public ReadingThread() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads the line from input
     * @return The input text
     * @throws IOException If there is an error loading from stdin
     * @throws InterruptedException If the thread interrupts abnormally.
     */
    @Override
    public String call() throws IOException, InterruptedException {
        String input;
        // wait until there is data to complete a readLine()
        while (!bufferedReader.ready()) {
            Thread.sleep(200);
        }
        input = bufferedReader.readLine();
        return input;
    }
}