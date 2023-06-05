import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class option extends gamebackground{
    JFrame options = new JFrame("言葉遊び");
    JLabel div1 = new JLabel("Word Games");
    JPanel word = new JPanel(new GridBagLayout());
    GridBagConstraints opt = new GridBagConstraints();

    public option() {
        options.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        options.setSize(200, 500);
        options.getContentPane().setBackground(new Color(245, 245, 245));
        options.setLayout(new BorderLayout());
        options.setLocationRelativeTo(null);

        Font font = div1.getFont();
        div1.setFont(font.deriveFont(24f));

        word.setLayout(new GridBagLayout());
        word.setOpaque(false);

        opt.insets = new Insets(5, 10, 5, 10);

        opt.gridx = 3;
        opt.anchor = GridBagConstraints.CENTER;

        opt.gridy = 0;
        word.add(div1, opt);

        for (int i = 0; i < 4; i++) {
            opt.gridy = i + 4;
            word.add(wordgame[i], opt);
        }

        opt.gridy = 10;
        word.add(back, opt);

        img.add(word, constraints);

        options.setContentPane(img);
        options.setVisible(true);

        wordgame[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new hangman();
                    }
                });
            }
        });

        wordgame[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new scramble();
                    }
                });
            }
        });

        wordgame[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new unscramble();
                    }
                });
            }
        });

        wordgame[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new wordle();
                    }
                });
            }
        });


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new Main();
                    }
                });
            }
        });

    }

}