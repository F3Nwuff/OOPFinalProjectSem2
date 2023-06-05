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

public class wordle extends gamebackground implements game{
    private JFrame wrdle;
    private int gamescore;
    private int attempts = 8;
    private int currentPanelIndex = 0;
    private JTextField answer;
    private JLabel word;
    private JLabel attempt;
    private JLabel scorelabel;
    private JPanel answerpanel;
    private List<String> wordList;
    private char[] wordChars;
    private JButton[][] buttons;

    public wordle() {
        wrdle = new JFrame("言葉");
        wrdle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wrdle.setSize(600, 700);
        wrdle.getContentPane().setBackground(new Color(245, 245, 245));
        wrdle.setLayout(new BorderLayout());
        wrdle.setLocationRelativeTo(null);

        answer = new JTextField();
        answer.setPreferredSize(new Dimension(50, 30));
        attempt = new JLabel("Remaining attempts: " + attempts);
        scorelabel = new JLabel();
        word = new JLabel();

        buttons = new JButton[8][5];

        getWord();
        getscore();

        answerpanel = new JPanel(new GridLayout(8, 5));
        answerpanel.setOpaque(false);

        for (int index = 0; index < 8; index++) {
            JPanel answeringPanel = new JPanel(new FlowLayout());
            answeringPanel.setName("answering" + index);
            answeringPanel.setOpaque(false);

            for (int i = 0; i < 5; i++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                answeringPanel.add(button);
                buttons[index][i] = button;
            }

            answerpanel.add(answeringPanel);
        }

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(answerpanel, BorderLayout.CENTER);
        inputPanel.add(answer, BorderLayout.SOUTH);
        inputPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guess);
        buttonPanel.add(back);
        buttonPanel.add(restart);
        buttonPanel.add(reset);
        restart.setVisible(false);
        buttonPanel.setOpaque(false);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(attempt, BorderLayout.SOUTH);
        infoPanel.add(scorelabel, BorderLayout.NORTH);
        infoPanel.setOpaque(false);

        resetscore();

        img.add(inputPanel, constraints);
        constraints.gridy = 1;
        img.add(buttonPanel, constraints);
        constraints.gridy = 2;
        img.add(infoPanel, constraints);


        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkInput();
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
                wrdle.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new option();
                    }
                });
            }
        });
        wrdle.setContentPane(img);
        wrdle.setVisible(true);
    }
    private void checkInput() {
        String input = answer.getText();
        if (input.length() == 5) {
            char[] inputChars = input.toCharArray();
            setButtonPanelText(inputChars, currentPanelIndex);


            for (int i = 0; i < inputChars.length; i++) {
                char inputChar = inputChars[i];
                for (int j = 0; j < wordChars.length; j++) {
                    char wordChar = wordChars[j];
                    if (inputChar == wordChar && i == j) {
                        same(i, currentPanelIndex);
                    } else if (inputChar == wordChar){
                        similar(i, currentPanelIndex);
                    }
                }
            }

            gameEnd();

            currentPanelIndex++;
            attempts--;
            attempt.setText("Remaining attempts: " + attempts);
            answer.setText("");
            answer.requestFocus();
        }
    }
    private void same(int i, int tried){
        JPanel trypanel = (JPanel) answerpanel.getComponent(tried);
        JButton trybutton = (JButton) trypanel.getComponent(i);
        trybutton.setBackground(Color.GREEN);
    }
    private void similar(int i, int tried){
        JPanel trypanel = (JPanel) answerpanel.getComponent(tried);
        JButton trybutton = (JButton) trypanel.getComponent(i);
        Color currentColor = trybutton.getBackground();
        if (!currentColor.equals(Color.GREEN)) {
            trybutton.setBackground(Color.YELLOW);
        }
    }

    private void setButtonPanelText(char[] buttonTexts, int panelIndex) {
        Component[] components = answerpanel.getComponents();
        JPanel answeringPanel = (JPanel) components[panelIndex];
        Component[] buttonComponents = answeringPanel.getComponents();
        for (int j = 0; j < buttonComponents.length; j++) {
            JButton button = (JButton) buttonComponents[j];
            button.setText(String.valueOf(buttonTexts[j]));
        }
    }
    private void gameEnd() {
        String answers = "The answer is " + word.getText();
        String answerwin = "Congratulations! You have won!";
        String answerlose = "Better luck next time!";
        String input = answer.getText();
        scoresave(gamescore, "scorewordle.txt");

        if (input.equals(word.getText())) {
            JOptionPane.showMessageDialog(null, answers + "\n" + answerwin);
            guess.setEnabled(false);
            answer.setEnabled(false);
            restart.setVisible(true);
            gamescore++;
            scorelabel.setText("Your current score is: " + gamescore);
        } if (attempts <= 1) {
            JOptionPane.showMessageDialog(null, answers + "\n" + answerlose);
            guess.setEnabled(false);
            answer.setEnabled(false);
            restart.setVisible(true);
        }
        scoresave(gamescore, "scorewordle.txt");
    }
    public void resetscore() {
        try {
            Files.write(Paths.get("scorewordle.txt"), "0".getBytes(), StandardOpenOption.CREATE);
            gamescore = 0;
            scorelabel.setText("Your current score is: " + gamescore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void restartgame() {
        attempts = 8; // Reset attempts to 6
        currentPanelIndex = 0;
        attempt.setText("Remaining attempts: " + attempts);
        guess.setEnabled(true);
        answer.setEnabled(true);
        answer.setText("");
        clearButtonPanelText();
        getWord();
        restart.setVisible(false);
    }
    public void scoresave(int score, String filename) {
        try {
            Files.write(Paths.get(filename), String.valueOf(score).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getscore() {
        try (BufferedReader br = new BufferedReader(new FileReader("scorewordle.txt"))) {
            String line = br.readLine();
            if (line != null) {
                gamescore = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scorelabel.setText("\nYour current score is: " + gamescore);
    }
    private void clearButtonPanelText() {
        Component[] components = answerpanel.getComponents();
        for (Component component : components) {
            JPanel answeringPanel = (JPanel) component;
            Component[] buttonComponents = answeringPanel.getComponents();
            for (Component buttonComponent : buttonComponents) {
                JButton button = (JButton) buttonComponent;
                button.setText("");
                button.setBackground(new JButton().getBackground());
            }
        }
    }

    private void getWord() {
        try (BufferedReader br = new BufferedReader(new FileReader("wordle.txt"))) {
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
/***
    public static void main(String[] args) {
        SwingUtilities.invokeLater(wordle::new);
    }
 ***/

}
