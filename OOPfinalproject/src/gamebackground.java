import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;

public class gamebackground {
    public ImageIcon background1;
    public ImageIcon start;
    public ImageIcon trying;
    public ImageIcon reset1;
    public ImageIcon restart1;
    public ImageIcon backtoopt;
    public ImageIcon hangimg;
    public ImageIcon scrmimg;
    public ImageIcon unscrming;
    public ImageIcon wrlimg;
    public JButton startbutton;
    public JPanel img;
    public JButton guess;
    public JButton back;
    public JButton restart;
    public JButton reset;
    JButton[] wordgame = new JButton[4];
    public GridBagConstraints constraints = new GridBagConstraints();

    public gamebackground() {
        background1 = new ImageIcon("image/bg.jpg");
        start = new ImageIcon("image/start.png");
        restart1 = new ImageIcon("image/restart.png");
        reset1 = new ImageIcon("image/reset.png");
        trying = new ImageIcon("image/try.png");
        backtoopt = new ImageIcon("image/back.png");
        hangimg = new ImageIcon("image/hangman.png");
        scrmimg = new ImageIcon("image/scramble.png");
        unscrming = new ImageIcon("image/unscramble.png");
        wrlimg = new ImageIcon("image/wordle.png");
        startbutton = new JButton();
        guess = new JButton();
        back = new JButton();
        restart = new JButton();
        reset = new JButton();
        wordgame[0] = new JButton();
        wordgame[1] = new JButton();
        wordgame[2] = new JButton();
        wordgame[3] = new JButton();

        reset.setBorderPainted(false);
        reset.setFocusPainted(false);
        reset.setContentAreaFilled(false);
        restart.setBorderPainted(false);
        restart.setFocusPainted(false);
        restart.setContentAreaFilled(false);
        guess.setBorderPainted(false);
        guess.setFocusPainted(false);
        guess.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.setContentAreaFilled(false);
        wordgame[0].setBorderPainted(false);
        wordgame[0].setFocusPainted(false);
        wordgame[0].setContentAreaFilled(false);
        wordgame[1].setBorderPainted(false);
        wordgame[1].setFocusPainted(false);
        wordgame[1].setContentAreaFilled(false);
        wordgame[2].setBorderPainted(false);
        wordgame[2].setFocusPainted(false);
        wordgame[2].setContentAreaFilled(false);
        wordgame[3].setBorderPainted(false);
        wordgame[3].setFocusPainted(false);
        wordgame[3].setContentAreaFilled(false);

        startbutton.setIcon(new ImageIcon(start.getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        reset.setIcon(new ImageIcon(reset1.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
        restart.setIcon(new ImageIcon(restart1.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
        guess.setIcon(new ImageIcon(trying.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
        back.setIcon(new ImageIcon(backtoopt.getImage().getScaledInstance(120, 50, Image.SCALE_SMOOTH)));
        wordgame[0].setIcon(new ImageIcon(hangimg.getImage().getScaledInstance(120, 50, Image.SCALE_SMOOTH)));
        wordgame[1].setIcon(new ImageIcon(scrmimg.getImage().getScaledInstance(120, 50, Image.SCALE_SMOOTH)));
        wordgame[2].setIcon(new ImageIcon(unscrming.getImage().getScaledInstance(120, 50, Image.SCALE_SMOOTH)));
        wordgame[3].setIcon(new ImageIcon(wrlimg.getImage().getScaledInstance(120, 50, Image.SCALE_SMOOTH)));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;

        img = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background1.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
    }
/**
    public static void main(String[] args) {
        gamebackground gameBackground = new gamebackground();

        // Other code or logic here
    }
 **/
}
