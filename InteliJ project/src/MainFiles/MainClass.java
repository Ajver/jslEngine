package MainFiles;

import jslEngine.*;

import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Color;

public class MainClass extends jslEngine {

    private MainClass() {
        start("jsl Tests", 600, 400);

        jsl.translate(60, 60);

        jsl.add(new jslObject(100, 100, 100, 50) {
            private Color[] col;
            private int index = 0;
            public void onCreate() {
                this.index = 1;
                col = new Color[2];
                col[0] = new Color(255, 255, 255);
                col[1] = new Color(128, 0, 255);
                this.translateX(200);
            }
            public void onEnter() {
                index = 1;
            }
            public void onLeave() {
                index = 0;
            }
            public void render(Graphics g) {
                g.setColor(col[index]);
                g.fillRect((int)getX(), (int)getY(), (int)w, (int)h);
            }
        });

        printWS();
    }

    protected void update(float et) {
//        for(int i=0; i<100000; i++) {
//            for(int j=0; j<1; j++) {
//                new Random().nextInt(10000);
//            }
//        }
    }

    protected void onPress(jslObject o) {

    }

    private void printWS() {
        System.out.print("WW: "+WW());
        System.out.println(" | WH: "+WH());
    }

    protected void onKeyPressed() {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(144);
        }
    }

    protected void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("FPS: "+getFpsCount(), 50, 50);
        g.drawString("WW: "+WW(), 50, 100);
        g.drawString("WH: "+WH(), 50, 125);
        if(mouse != null) {
            g.drawString("mx: " + mouse.getX(), 50, 160);
            g.drawString("my: " + mouse.getY(), 50, 185);
        }
    }

    public static void main(String[] args) {
        new MainClass();
    }
}
