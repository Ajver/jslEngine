package MainFiles;

import jslEngine.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MainClass extends jslEngine {

    private jslSettings defaultSettings = new jslSettings();
    private jslSettings onHoverSettings = new jslSettings();

    private jslButton btn;

    public MainClass() {
        start("jsl Tests", 600, 400, WindowType.jslNormal);

        defaultSettings.bgColor = new Color(141, 12, 206);
        onHoverSettings.bgColor = new Color(6, 119, 132);
        onHoverSettings.txtColor = new Color(0, 0, 0);

        jsl.defaulButtonSettings = defaultSettings;
        jsl.onHoverButtonSettings = onHoverSettings;

        btn = jsl.newButton("HI", 100, 100, 200, 50);
        btn.setVelR(1.0f);
        btn.translateX(200);

        printWS();
    }

    protected void update(float et) {
//        for(int i=0; i<100000; i++) {
//            for(int j=0; j<1; j++) {
//                new Random().nextInt(10000);
//            }
//        }
    }

    protected void onClick(jslObject o) {
        if(o == btn) {
            resizeWindow(800, 600);
            printWS();
        }
    }

    private void printWS() {
        System.out.print("WW: "+WW());
        System.out.println(" | WH: "+WH());
    }

    protected void onKeyPressed() {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(-1);
        }
    }

    protected void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("FPS: "+getFpsCount(), 50, 50);
        g.drawString("WW: "+WW(), 50, 100);
        g.drawString("WH: "+WH(), 50, 125);
    }

    public static void main(String[] args) {
        new MainClass();
    }
}
