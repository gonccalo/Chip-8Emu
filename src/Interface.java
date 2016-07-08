import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by gonccalo on 08-07-2016.
 */
public class Interface {
    ImageIcon game;
    JLabel imglabel = new JLabel();
    private JPanel panel1;
    JFrame frame;

    public void start(){
         frame = new JFrame("UserInterface");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.pack();
         frame.setSize(660, 360);
         frame.setVisible(true);
         imglabel.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 0));
         frame.add(imglabel);
         game = new ImageIcon();

     }
    public void setGameImage(BufferedImage img){
        BufferedImage newImage = new BufferedImage(640, 320, BufferedImage.TYPE_INT_RGB);

        Graphics g = newImage.createGraphics();
        g.drawImage(img, 0, 0, 640, 320, null);
        g.dispose();
        game.setImage(newImage);
        imglabel.setIcon(game);
        imglabel.repaint();
    }
}
