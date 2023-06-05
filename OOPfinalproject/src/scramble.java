import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class scramble extends gamebackground implements game {
    private JFrame scrm;
    private int attempts = 2;
    private JTextField answer;
    private JLabel word;
    private JLabel attempt;
    private JLabel scorelabel;
    private JPanel choice;
    private JPanel finish1;
    private JPanel finish2;
    private JPanel words;
    private JPanel scor;

    private char[] wordChars;
    private int gamescore;

    private List<String> wordList;

    public scramble() {
        scrm = new JFrame("スクランブル");
        scrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scrm.setSize(600, 400);
        scrm.getContentPane().setBackground(new Color(245, 245, 245));
        scrm.setLayout(new GridLayout(3, 1));
        scrm.setLocationRelativeTo(null);

        answer = new JTextField();
        answer.setPreferredSize(new Dimension(150, 50));
        attempt = new JLabel("remaining attempt : " + attempts);
        word = new JLabel();
        Font font = word.getFont();
        word.setFont(font.deriveFont(24f));
        scorelabel = new JLabel();
        choice = new JPanel(new BorderLayout());
        finish1 = new JPanel();
        finish2 = new JPanel();
        scor = new JPanel();

        finish1.add(answer);
        finish1.add(guess);
        finish2.add(back);
        finish2.add(restart);
        finish2.add(reset);
        choice.add(finish1, BorderLayout.NORTH);
        choice.add(finish2, BorderLayout.CENTER);
        restart.setVisible(false);
        words = new JPanel();
        scor.add(attempt);
        scor.add(scorelabel);
        words.setOpaque(false);
        choice.setOpaque(false);
        finish1.setOpaque(false);
        finish2.setOpaque(false);
        scor.setOpaque(false);

        getword();
        getscore();

        words.add(word);

        img.add(words, constraints);
        constraints.gridy = 1;
        img.add(scor, constraints);
        constraints.gridy = 2;
        img.add(choice, constraints);

        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guesscheck();
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartgame();
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetscore();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrm.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new option();
                    }
                });
            }
        });
        scrm.setContentPane(img);
        scrm.setVisible(true);
    }

    private void getword() {
        try (BufferedReader br = new BufferedReader(new FileReader("wordgame3.txt"))) {
            wordList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                wordList.add(line);
            }
            if (!wordList.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(wordList.size());
                String randomWord = wordList.get(randomIndex);
                word.setText(randomWord);
                wordChars = randomWord.toCharArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getscore() {
        try (BufferedReader br = new BufferedReader(new FileReader("scorescr.txt"))) {
            String line = br.readLine();
            if (line != null) {
                gamescore = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scorelabel.setText("\nYour current score is: " + gamescore);
    }

    private void guesscheck() {
        String input = answer.getText();
        char[] inputChars = input.toCharArray();
        int score = 0;

        if (inputChars.length <= wordChars.length) {
            boolean validWord = true;
            for (char c : inputChars) {
                if (!charcheck(wordChars, c)) {
                    validWord = false;
                    break;
                }
            }
            if (validWord && wordcheck(input, "wordgame2.txt")) {
                score++;
            }
        }

        attempts -= 1;
        attempt.setText("remaining attempt : " + attempts);
        gamescore += score;
        scorelabel.setText("Your current score is: " + gamescore);
        scoresave(gamescore, "scorescr.txt");
        gamecheck();
        answer.setText("");
        answer.requestFocus();
    }


    private void gamecheck() {
        if (attempts == 0) {
            answer.setEnabled(false);
            guess.setEnabled(false);
            restart.setVisible(true);
        }
    }

    private boolean charcheck(char[] arr, char c) {
        for (char element : arr) {
            if (element == c) {
                return true;
            }
        }
        return false;
    }

    private boolean wordcheck(String input, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(input)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void scoresave(int score, String filename) {
        try {
            Files.write(Paths.get(filename), String.valueOf(score).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restartgame() {
        attempts = 2;
        attempt.setText("remaining attempt : " + attempts);
        guess.setEnabled(true);
        answer.setEnabled(true);
        restart.setVisible(false);
        getword();
    }

    public void resetscore() {
        try {
            Files.write(Paths.get("scorescr.txt"), "0".getBytes(), StandardOpenOption.CREATE);
            gamescore = 0;
            scorelabel.setText("Your current score is: " + gamescore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new scramble();
            }
        });
    }

 **/
}
