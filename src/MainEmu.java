import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by gonccalo on 08-07-2016.
 */
public class MainEmu {
    Interface ui;
    public static void main(String[] args){
        new MainEmu().emuStart();
    }
    private void emuStart(){
        ui = new Interface();
        ui.start();
        int op;
        short key;
        int ops = 400;
        int fps = 60;
        int opsPorFrame = ops/fps;
        int intervaloFPS = 1000/fps;
        long time;
        long currentTime;
        Cpu chip8 = new Cpu();
        chip8.CpuReset();
        chip8.loadRom("C:\\Users\\gonccalo\\Univ\\Chip-8Emu\\c8games\\PONG");
        time = System.currentTimeMillis();
        while(true){
            HandleKeys(chip8);
            currentTime = System.currentTimeMillis();
            if(time+intervaloFPS < currentTime) {
                chip8.decreaseTimers();
                for(int i=0;i<opsPorFrame;i++) {
                    op = chip8.getNextOP();
                    chip8.decodeAndExe(op);
                    //System.out.println(Integer.toHexString(op));
                }
                desenha(chip8);
                time = currentTime;
            }
        }
    }
    private void desenha(Cpu chip8) {
        short[][] screen = chip8.getScreen();
        BufferedImage img = new BufferedImage(64, 32, BufferedImage.TYPE_INT_RGB );

        for ( int rc = 0; rc < 32; rc++ ) {
            for ( int cc = 0; cc < 64; cc++ ) {
                // Set the pixel colour of the image n.b. x = cc, y = rc
                if(screen[cc][rc]==1)
                    img.setRGB(cc, rc, Color.WHITE.getRGB() );
                else
                    img.setRGB(cc, rc, Color.BLACK.getRGB());
            }
        }
        ui.setGameImage(img);
    }

    private static void HandleKeys(Cpu chip8) {
    }
}
