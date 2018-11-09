package MainFiles;

import jslEngine.jslObject;

import java.awt.*;

public class Rect extends jslObject {

    public Rect(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public void render(Graphics g) {
        g.setColor(new Color(255, 255, 0));
        g.fillRect((int)x, (int)y, (int)w, (int)h);
    }

}
