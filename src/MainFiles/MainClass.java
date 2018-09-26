package MainFiles;

import jslEngine.*;

import java.awt.*;
import java.util.Random;

public class MainClass extends jslEngine {

    public MainClass() {
        start("jsl Tests", 600, 400);
        setSize(300, 400);

        new jslButton("hehehe");
    }

    protected void update(float et) {
        for(int i=0; i<100000; i++) {
            for(int j=0; j<1; j++) {
                new Random().nextInt(10000);
            }
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
