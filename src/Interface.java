import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by gonccalo on 08-07-2016.
 */
public class Interface {
    ImageIcon game;
    JLabel imglabel = new JLabel();
    private JPanel panel1;
    JFrame frame;
    Cpu context;
    public Interface(Cpu context){
        this.context = context;
    }
    public void start(){
        frame = new JFrame("CHIP8 Emulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(660, 360);
        frame.setVisible(true);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                short key = -1;
                switch (e.getKeyCode()){
                    case KeyEvent.VK_1:
                        key = 1;
                        break;
                    case KeyEvent.VK_2:
                        key = 2;
                        break;
                    case KeyEvent.VK_3:
                        key = 3;
                        break;
                    case KeyEvent.VK_4:
                        key = 12;
                        break;
                    case KeyEvent.VK_Q:
                        key = 4;
                        break;
                    case KeyEvent.VK_W:
                        key = 5;
                        break;
                    case KeyEvent.VK_E:
                        key = 6;
                        break;
                    case KeyEvent.VK_R:
                        key = 13;
                        break;
                    case KeyEvent.VK_A:
                        key = 7;
                        break;
                    case KeyEvent.VK_S:
                        key = 8;
                        break;
                    case KeyEvent.VK_D:
                        key = 9;
                        break;
                    case KeyEvent.VK_F:
                        key = 14;
                        break;
                    case KeyEvent.VK_Z:
                        key = 10;
                        break;
                    case KeyEvent.VK_X:
                        key = 0;
                        break;
                    case KeyEvent.VK_C:
                        key = 11;
                        break;
                    case KeyEvent.VK_V:
                        key = 15;
                        break;
                }
                if(key!=-1)
                    context.pressKey(key);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                short key = -1;
                switch (e.getKeyCode()){
                    case KeyEvent.VK_1:
                        key = 1;
                        break;
                    case KeyEvent.VK_2:
                        key = 2;
                        break;
                    case KeyEvent.VK_3:
                        key = 3;
                        break;
                    case KeyEvent.VK_4:
                        key = 12;
                        break;
                    case KeyEvent.VK_Q:
                        key = 4;
                        break;
                    case KeyEvent.VK_W:
                        key = 5;
                        break;
                    case KeyEvent.VK_E:
                        key = 6;
                        break;
                    case KeyEvent.VK_R:
                        key = 13;
                        break;
                    case KeyEvent.VK_A:
                        key = 7;
                        break;
                    case KeyEvent.VK_S:
                        key = 8;
                        break;
                    case KeyEvent.VK_D:
                        key = 9;
                        break;
                    case KeyEvent.VK_F:
                        key = 14;
                        break;
                    case KeyEvent.VK_Z:
                        key = 10;
                        break;
                    case KeyEvent.VK_X:
                        key = 0;
                        break;
                    case KeyEvent.VK_C:
                        key = 11;
                        break;
                    case KeyEvent.VK_V:
                        key = 15;
                        break;
                }
                if(key!=-1)
                    context.releaseKey(key);
            }
        });
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
