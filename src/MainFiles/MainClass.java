package MainFiles;

import jslEngine.*;

import java.awt.*;
import java.util.Random;

public class MainClass extends jslEngine {

    private jslSettings defaultSettings = new jslSettings();
    private jslSettings onHoverSettings = new jslSettings();

    public MainClass() {
        start("jsl Tests", 600, 400);
        setSize(300, 400);

        defaultSettings.bgColor = new Color(255, 100,100);
        onHoverSettings.bgColor = new Color(200, 0,255);

        jsl.defaulButtonSettings = defaultSettings;
        jsl.onHoverButtonSettings = onHoverSettings;

        jslButton btn = jsl.newButton("HI", 100, 100, 200, 50);
        btn.setVelR(1.7f);
        btn.setRotateToCenter();
    }

    protected void update(float et) {
//        for(int i=0; i<100000; i++) {
//            for(int j=0; j<1; j++) {
//                new Random().nextInt(10000);
//            }
//        }
    }

    protected void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("FPS: "+getFpsCount(), 50, 50);
//        g.drawString("WW: "+WW(), 50, 100);
//        g.drawString("WH: "+WH(), 50, 125);
    }

    public static void main(String[] args) {
        new MainClass();
    }
}
