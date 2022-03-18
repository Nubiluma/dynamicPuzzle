package dynamicPuzzle.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Highscore {

    private final String file = "highscores.txt";
    private final List<Integer> highscores = new ArrayList<>();

    public Highscore(int score) {
        readFile();
        replaceEntry(score);
    }

    /**
     * puts values read from txt file to an array list
     */
    private void readFile() {
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            String line;

            for (int i = 0; (line = r.readLine()) != null; i++) {
                highscores.add(i, Integer.parseInt(line));
            }
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.logLine(Color.RED.colorCode, "ERROR: highscores file not found!");
        }
    }

    /**
     * replaces one of the 3 entries of the txt file by comparing given score to already existing score values
     *
     * @param newScore achieved by the player
     */
    private void replaceEntry(int newScore) {

        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(file));

            if (newScore > highscores.get(0)) {
                w.write(String.valueOf(newScore));
                w.write("\n" + highscores.get(1));
                w.write("\n" + highscores.get(2));
                Logger.nextLine();
                Logger.logLine(Color.GREEN.colorCode, "Congratulations! You reached a new best score of " + newScore + " points!");

            } else if (newScore > highscores.get(1)) {
                w.write(String.valueOf(highscores.get(0)));
                w.write("\n" + newScore);
                w.write("\n" + highscores.get(2));
                Logger.nextLine();
                Logger.logLine(Color.GREEN.colorCode, "Congratulations! You reached a new second best score of " + newScore + " points!");

            } else if (newScore > highscores.get(2)) {
                w.write(String.valueOf(highscores.get(0)));
                w.write("\n" + highscores.get(1));
                w.write("\n" + newScore);
                Logger.nextLine();
                Logger.logLine(Color.GREEN.colorCode, "Congratulations! You reached a new third best score of " + newScore + " points!");

            } else {
                w.write(String.valueOf(highscores.get(0)));
                w.write("\n" + highscores.get(1));
                w.write("\n" + highscores.get(2));
                Logger.nextLine();
                Logger.logLine(Color.RED.colorCode, "No new highscore achieved");
            }
            w.close();

        } catch (IOException e) {
            e.printStackTrace();
            Logger.logLine(Color.RED.colorCode, "ERROR: highscores file not found!");
        }
    }
}
