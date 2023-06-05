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

public class unscramble extends gamebackground implements game {
    private JFrame unscrm;
    private int attempts = 7;
    private int gamescore;
    private JTextField answer;
    private JLabel word;
    private JLabel attempt;
    private JLabel scorelabel;
    private JPanel choice;
    private JPanel finish1;
    private JPanel finish2;
    private JPanel words;
    private JPanel scor;
    private Random random;
    private String unscrambledword;
    private List<String> letterhints;
    public unscramble() {
        unscrm = new JFrame("スクランブルを解除する");
        unscrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        unscrm.setSize(600, 400);
        unscrm.getContentPane().setBackground(new Color(245, 245, 245));
        unscrm.setLayout(new GridLayout(3, 1));
        unscrm.setLocationRelativeTo(null);

        choice = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background1.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        answer = new JTextField();
        answer.setPreferredSize(new Dimension(150, 50));
        attempt = new JLabel("remaining attempt: " + attempts);
        word = new JLabel();
        Font font = word.getFont();
        word.setFont(font.deriveFont(24f));
        choice = new JPanel(new BorderLayout());
        finish1 = new JPanel();
        finish2 = new JPanel();
        scor = new JPanel();
        words = new JPanel();
        scorelabel = new JLabel();

        finish1.add(answer);
        finish1.add(guess);
        finish2.add(back);
        finish2.add(restart);
        finish2.add(reset);
        choice.add(finish1, BorderLayout.NORTH);
        choice.add(finish2, BorderLayout.CENTER);
        restart.setVisible(false);
        words.add(word, BorderLayout.NORTH);
        scor.add(attempt);
        scor.add(scorelabel);
        words.setOpaque(false);
        choice.setOpaque(false);
        finish2.setOpaque(false);
        finish1.setOpaque(false);
        scor.setOpaque(false);

        img.add(words, constraints);
        constraints.gridy = 1;
        img.add(scor, constraints);
        constraints.gridy = 2;
        img.add(choice, constraints);

        random = new Random();
        letterhints = new ArrayList<>();
        getscore();
        initializeword();

        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int score = 0;
                String guessedWord = answer.getText();
                if (guessedWord.equals(unscrambledword)) {
                    score++;
                    JOptionPane.showMessageDialog(null, "Correct guess! You win!");
                    gameend();
                } else {
                    attempts--;
                    attempt.setText("remaining attempt: " + attempts);
                    if (attempts <= 0) {
                        JOptionPane.showMessageDialog(null, "You lose! The word was: " + unscrambledword);
                        guess.setEnabled(false);
                        answer.setEnabled(false);
                        gameend();
                    } else if (attempts > 0) {
                        showhint();
                    }
                }
                gamescore += score;
                scorelabel.setText("Your current score is: " + gamescore);
                scoresave(gamescore, "scoreunscr.txt");
                answer.setText("");
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unscrm.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new option();
                    }
                });
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
        unscrm.setContentPane(img);
        unscrm.setVisible(true);
    }

    private void initializeword() {
        String fileName = "wordgame3.txt";
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!words.isEmpty()) {
            int randomIndex = random.nextInt(words.size());
            unscrambledword = words.get(randomIndex);
            String scrambledWord = scrambleWord(unscrambledword);
            word.setText(scrambledWord);
            hint();
        } else {
            word.setText("No words found");
        }
    }

    private void hint() {
        letterhints.clear();
        String originalword = unscrambledword.toUpperCase();
        for (int i = 0; i < originalword.length(); i++) {
            char letter = originalword.charAt(i);
            letterhints.add("The letter for " + (i + 1) + " number is " + letter);
        }
    }

    private String scrambleWord(String word) {
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }
        StringBuilder scrambled = new StringBuilder();
        while (!characters.isEmpty()) {
            int randomIndex = random.nextInt(characters.size());
            char c = characters.remove(randomIndex);
            scrambled.append(c);
        }
        return scrambled.toString();
    }

    public void restartgame() {
        attempts = 7;
        attempt.setText("remaining attempt: " + attempts);
        guess.setEnabled(true);
        answer.setEnabled(true);
        initializeword();
        restart.setVisible(false);
    }

    private void gameend() {
        guess.setEnabled(false);
        answer.setEnabled(false);
        restart.setVisible(true);
    }

    private void showhint() {
        if (!letterhints.isEmpty()) {
            int randomIndex = random.nextInt(letterhints.size());
            String hint = letterhints.remove(randomIndex);
            JOptionPane.showMessageDialog(null, hint);
        }
    }
    public void scoresave(int score, String filename) {
        try {
            Files.write(Paths.get(filename), String.valueOf(score).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getscore() {
        try (BufferedReader br = new BufferedReader(new FileReader("scoreunscr.txt"))) {
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
            Files.write(Paths.get("scoreunscr.txt"), "0".getBytes(), StandardOpenOption.CREATE);
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
                new unscramble();
            }
        });
    }
 **/
}
