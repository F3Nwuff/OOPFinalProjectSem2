import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends gamebackground {
    JFrame game = new JFrame("言葉遊び");
    ImageIcon start = new ImageIcon("image/start.png");

    public Main() {
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setSize(200, 200);
        game.setLayout(new BorderLayout());
        game.setLocationRelativeTo(null);

        game.setLayout(new BorderLayout());

        startbutton.setBorderPainted(false);
        startbutton.setFocusPainted(false);
        startbutton.setContentAreaFilled(false);

        startbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new option();
                    }
                });
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        panel.add(startbutton, constraints);

        img.add(startbutton, constraints);

        game.setContentPane(img);
        game.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new Main();
    }
}