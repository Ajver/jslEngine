package jslEngine;

import java.awt.*;

public class jslCursor {

    public static boolean cursorChanged = false;
    public static Cursor cursor;

    public static final Cursor HAND = new Cursor(Cursor.HAND_CURSOR);
    public static final Cursor DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);
    public static final Cursor MOVE = new Cursor(Cursor.MOVE_CURSOR);

    public static void setCursor(Cursor c) {
        cursor = c;
        cursorChanged = true;
    }

    public static void setCursor(String path) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.getImage(path);
        cursor = tk.createCustomCursor(image,
                new Point(image.getWidth(null)/2, image.getHeight(null)/2), "img");
        cursorChanged = true;
    }

    public static void reset() { cursorChanged = false; }
}
