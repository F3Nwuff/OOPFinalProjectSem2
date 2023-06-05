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

public class hangman extends gamebackground implements game{
    private JFrame hman;
    private int attempts = 13;
    private int gamescore;
    private JLabel word;
    private JLabel attempt;
    private JLabel truth;
    private JLabel scorelabel;
    private JTextField answer;
    private JPanel fail;
    private JPanel finish;
    private JPanel finish1;
    private JPanel finish2;
    private JPanel scor;
    private char[] answerhangman;
    private List<Character> prev;

    public hangman() {
        hman = new JFrame("絞首刑執行人");
        hman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hman.setSize(600, 400);
        hman.getContentPane().setBackground(new Color(245, 245, 245));
        hman.setLayout(new GridLayout(3, 1));
        hman.setLocationRelativeTo(null);

        word = new JLabel();
        Font font = word.getFont();
        word.setFont(font.deriveFont(24f));
        attempt = new JLabel("attempts left = " + attempts);
        answer = new JTextField();
        answer.setPreferredSize(new Dimension(150, 50));
        finish = new JPanel(new BorderLayout());
        finish1 = new JPanel();
        finish2 = new JPanel();
        scor = new JPanel();
        scorelabel = new JLabel();

        fail = new JPanel(new BorderLayout());

        getscore();
        analyze();

        attempt.setText("attempts left = " + attempts);

        finish1.add(answer);
        finish1.add(guess);
        finish2.add(back);
        finish2.add(restart);
        finish2.add(reset);
        finish.add(finish1, BorderLayout.NORTH);
        finish.add(finish2, BorderLayout.CENTER);
        restart.setVisible(false);
        fail.add(word, BorderLayout.CENTER);
        scor.add(attempt,constraints);
        scor.add(scorelabel,constraints);
        fail.setOpaque(false);
        finish1.setOpaque(false);
        finish2.setOpaque(false);
        finish.setOpaque(false);
        scor.setOpaque(false);

        img.add(fail, constraints);
        constraints.gridy = 1;
        img.add(scor, constraints);
        constraints.gridy = 2;
        img.add(finish, constraints);

        prev = new ArrayList<>();

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetscore();
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartgame();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hman.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new option();
                    }
                });
            }
        });

        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int score = 0;
                String guessedLetter = answer.getText();
                if (guessedLetter.length() == 1) {
                    if (attempts >= 1) {
                        guessedLetter = guessedLetter.toLowerCase();
                        char letter = guessedLetter.charAt(0);

                        if (!prev.contains(letter)) {
                            prev.add(letter);
                            changeHangman(letter);
                        }

                        attempt.setText("attempts left = " + attempts);
                        answer.setText("");
                        answer.requestFocus();
                    }
                } else if (guessedLetter.length() > 1) {
                    gamewinword();
                    if (attempts >= 1) {
                        if (!gamewinword()) {
                            guessedLetter = guessedLetter.toLowerCase();
                            char letter = guessedLetter.charAt(0);

                            if (!prev.contains(letter)) {
                                prev.add(letter);
                                changeHangman(letter);
                            }

                            attempt.setText("attempts left = " + attempts);
                            answer.setText("");
                            answer.requestFocus();
                        }
                    }
                    scorelabel.setText("Your current score is: " + gamescore);
                    scoresave(gamescore, "scorehangman.txt");
                    gamewin();
                }
            }
        });
        hman.setContentPane(img);
        hman.setVisible(true);
    }

    public void analyze() {
        String file1 = "wordgame1.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(file1))) {
            List<String> words = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                for (String word : lineWords) {
                    words.add(word);
                }
            }

            Random random = new Random();
            int index = random.nextInt(words.size());
            String getword = words.get(index);

            int length = getword.length();
            answerhangman = getword.toCharArray();

            StringBuilder current = new StringBuilder();
            for (int i = 0; i < length; i++) {
                current.append("*");
            }

            word.setText(current.toString());
        } catch (IOException ex) {
            word.setText("An error occurred while reading the file.");
        }
    }

    public void changeHangman(char a) {
        if (answerhangman != null) {
            List<Integer> change = new ArrayList<>();
            for (int i = 0; i < answerhangman.length; i++) {
                if (answerhangman[i] == a) {
                    change.add(i);
                }
            }

            if (!change.isEmpty()) {
                StringBuilder outputBuilder = new StringBuilder(word.getText());
                for (int i : change) {
                    outputBuilder.setCharAt(i, a);
                }
                word.setText(outputBuilder.toString());
            }

            attempts -= 1;
            attempt.setText("   attempts left = " + attempts);

            if (attempts == 0) {
                if (!gamewin()) {
                    gameend();
                }
            }
            else if (!word.getText().contains("*")) {
                gamewin();
            }
        }
    }

    public Boolean gamewinword() {
        String guessedWord = answer.getText();
        if (String.valueOf(answerhangman).equals(guessedWord)) {
            word.setText(guessedWord);
            answer.setText("");
            answer.requestFocus();
            truth = new JLabel("You are correct, congratulations!");
            gamescore += 1;
            scorelabel.setText("Your current score is: " + gamescore);
            scoresave(gamescore, "scorehangman.txt");
            fail.add(truth, BorderLayout.SOUTH);
            guess.setEnabled(false);
            answer.setEnabled(false);
            back.setVisible(true);
            restart.setVisible(true);
            return true;
        }
        return false;
    }

    public Boolean gamewin() {
        String currentWord = word.getText();
        if (!currentWord.contains("*")) {
            scoresave(gamescore, "scorehangman.txt");
            truth = new JLabel("You are correct, congratulations!");
            if (!gamewinword()) {
                gamescore+=1;
            }
            scorelabel.setText("\nYour current score is: " + gamescore);
            fail.add(truth, BorderLayout.SOUTH);
            guess.setEnabled(false);
            answer.setEnabled(false);
            restart.setVisible(true);
            return true;
        }
        return false;
    }

    public void gameend() {
        restart.setVisible(true);
        guess.setEnabled(false);
        answer.setEnabled(false);
        back.setVisible(true);
        truth = new JLabel("     the answer was = " + String.valueOf(answerhangman));
        fail.add(truth, BorderLayout.SOUTH);
        fail.revalidate();
        fail.repaint();
    }
    public void restartgame() {
        attempts = 13;
        attempt.setText("   attempts left = " + attempts);
        guess.setEnabled(true);
        answer.setEnabled(true);
        restart.setVisible(false);
        scoresave(gamescore, "scorehangman.txt");
        fail.remove(truth);
        analyze();
        prev.clear();
    }
    public void scoresave(int score, String filename) {
        try {
            Files.write(Paths.get(filename), String.valueOf(score).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getscore() {
        try (BufferedReader br = new BufferedReader(new FileReader("scorehangman.txt"))) {
            String line = br.readLine();
            if (line != null) {
                gamescore = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scorelabel.setText("\nYour current score is: " + gamescore);
    }
    public void resetscore() {
        try {
            Files.write(Paths.get("scorehangman.txt"), "0".getBytes(), StandardOpenOption.CREATE);
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
                new hangman();
            }
        });
    }
 **/
}
