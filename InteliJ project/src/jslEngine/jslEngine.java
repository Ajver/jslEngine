package jslEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public abstract class jslEngine extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    // Those variables are for Your using
    public boolean[] mouseButton = new boolean[3];
    public int mouseX = 0, mouseY = 0;
    public jslManager jsl;

    private WindowType windowType;
    private boolean antialiasing = false;

    // Those functions are to override
    protected abstract void update(float et);

    // Before jslManager render
    protected void beforeRender(Graphics g) {}

    // After jslManager render
    protected abstract void render(Graphics g);

    // After create all elements (on the end of constructor)
    protected void onCreate() {}

    // Events (to override)

    // Key goes down
    protected void onKeyPressed(KeyEvent e) {}

    // Key goes up
    protected void onKeyReleased(KeyEvent e) {}

    // Key goes down (difference between key press:
    // https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html )
    protected void onKeyTyped(KeyEvent e) {}

    // Mouse goes down
    protected void onMousePressed(MouseEvent e) {}

    // Mouse goes up
    protected void onMouseReleased(MouseEvent e) {}

    // Mouse goes down and up in the same place
    protected void onMouseClicked(MouseEvent e) {}

    // Mouse enter the application window
    protected void onMouseEntered(MouseEvent e) {}

    // Mouse exit the application window
    protected void onMouseExited(MouseEvent e) {}

    // Mouse moved (while all buttons are up)
    protected void onMouseMoved(MouseEvent e) {}

    // Mouse dragged (moved while button is pressed)
    protected void onMouseDragged(MouseEvent e) {}

    // Functions that may be helpful ( --DO NOT-- override)

    // Window Width
    public int WW() { return getWidth(); }

    // Window Height
    public int WH() { return getHeight(); }

    // Frames in previous frame
    public int getFpsCount() { return fps; }

    // Functions for window controlling
    protected void setWindowTitle(String title) {
        frame.setTitle(title);
    }
    protected void setWindowPosition(int x, int y) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int sw = (int)tk.getScreenSize().getWidth();
        int sh = (int)tk.getScreenSize().getHeight();

        // Fit window to screen
        if(x < 0) x = 0;
        else if(x+WW() > sw) x = sw - WW();

        if(y < 0) y = 0;
        else if(y+WH() > sh) y = sh - WH();

        frame.setLocation(x, y);
    }
    protected void setAntialiasing(boolean flag) { antialiasing = flag; }
    protected void createWindow(String title, int w, int h, WindowType type) {
        // Do override multiply windows
        if(isWindow) {
            return;
        }

        isWindow = true;
        windowType = type;
        frame = new JFrame(title);

        if(type == WindowType.FULLSCREEN) {
            // Remove default bar on top (with exit button, title...)
            frame.setUndecorated(true);
        }

        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        resizeWindow(w, h);
    }
    protected void resizeWindow(int w, int h) {
        // If trying to set the same size
        if(w == WW()) {
            if(h == WH()) {
                return;
            }
        }

        Toolkit tk = Toolkit.getDefaultToolkit();
        int WW, WH, sw, sh;
        sw = (int)tk.getScreenSize().getWidth();
        sh = (int)tk.getScreenSize().getHeight();

        if(this.windowType == WindowType.STATIC) {
            WW = w + 6;
            WH = h + 29;
            frame.setResizable(false);
        }else if(this.windowType == WindowType.NORMAL) {
            WW = w + 16;
            WH = h + 39;
        }else {
            WW = sw;
            WH = sh;
            frame.setResizable(false);
        }
        if(this.windowType != WindowType.FULLSCREEN) {
            frame.setLocation((sw - WW) / 2, (sh - WH) / 2);
        }

        setSize(WW, WH);
        frame.setSize(WW, WH);
    }

    // Default creators
    protected void start() {
        start(defaultTitle);
    }
    protected void start(String title) {
        start(title, defaultW, defaultH);
    }
    protected void start(WindowType type) {
        start(defaultW, defaultH, type);
    }
    protected void start(int w, int h) {
        start(defaultTitle, w, h);
    }
    protected void start(int w, int h, WindowType type) {
        start(defaultTitle, w, h, type);
    }
    protected void start(String title, WindowType type) {
        start(title, defaultW, defaultH, type);
    }
    protected void start(String title, int w, int h) {
        start(title, w, h, WindowType.STATIC);
    }
    protected void start(String title, int w, int h, WindowType type) {
        if(isRunning) return;
        createWindow(title, w, h, type);
        isRunning = true;
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        thread = new Thread(this);
        jsl = new jslManager(this);

        thread.start();
    }
    private void jslUpdate(float et) {
        jsl.update(et);
        update(et);
        executeEvents();
    }
    private void jslRender() {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        if(antialiasing) {
            ((Graphics2D)g).setRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
        }

        beforeRender(g);
        jsl.render(g);
        render(g);

        g.dispose();
        bs.show();
    }

    private void changeCursor() {
        if(jslCursor.cursorChanged) {
            setCursor(jslCursor.cursor);
            jslCursor.reset();
        }
    }

    // Main loop
    public void run() {
        requestFocus();
        onCreate();

        // Need for counting elapsed time
        long start, stop;

        // Elapsed time from previous frame
        float elapsedTime = 0.0f;

        // Timer to updating FPS
        jslTimer fpsTimer = new jslTimer(1.0f);
        fpsTimer.start();

        int fpsCounter = 0;

        while(isRunning) {
            start = System.currentTimeMillis();

            this.jslUpdate(elapsedTime);
            this.jslRender();

            changeCursor();
            fpsCounter++;

            stop = System.currentTimeMillis();

            if(fpsTimer.update()) {
                fps = fpsCounter;
                fpsCounter = 0;
            }

            elapsedTime = (stop - start) / 1000.0f;
        }
    }

    private ArrayList<Event> events = new ArrayList<>();

    private enum EventType {
        M_CLICK,
        M_PRESS,
        M_RELEASE,
        M_MOVE,
        M_DRAG,
        M_ENTER,
        M_EXIT,
        K_PRESS,
        K_RELEASE,
        K_TYPE,
    }

    private class Event {
        public EventType type;
        public MouseEvent mouse;
        public KeyEvent key;
        public Event(EventType t, MouseEvent e) { type = t; mouse = e; key = null; }
        public Event(EventType t, KeyEvent e) { type = t; mouse = null; key = e; }
    }

    private void executeEvents() {
        for(int i=0; i<events.size(); i++) {
            try {
                Event e = events.get(i);

                switch (e.type) {
                    case K_PRESS:   onKeyPressed(e.key);       jsl.keyPressed(e.key);       break;
                    case K_RELEASE: onKeyReleased(e.key);      jsl.keyReleased(e.key);      break;
                    case K_TYPE:    onKeyTyped(e.key);         jsl.keyTyped(e.key);         break;
                    case M_CLICK:   onMouseClicked(e.mouse);   jsl.mouseClicked(e.mouse);   break;
                    case M_PRESS:   onMousePressed(e.mouse);   jsl.mousePressed(e.mouse);   break;
                    case M_RELEASE: onMouseReleased(e.mouse);  jsl.mouseReleased(e.mouse);  break;
                    case M_MOVE:    onMouseMoved(e.mouse);     jsl.mouseMoved(e.mouse);     break;
                    case M_DRAG:    onMouseDragged(e.mouse);   jsl.mouseDragged(e.mouse);   break;
                    case M_ENTER:   onMouseEntered(e.mouse);   break;
                    case M_EXIT:    onMouseExited(e.mouse);    break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        events.clear();
    }

    private void addEvent(Event e) {
        events.add(e);
    }

    // ( --DO NOT-- override)
    public void keyPressed(KeyEvent e)      { addEvent(new Event(EventType.K_PRESS, e)); }
    public void keyReleased(KeyEvent e)     { addEvent(new Event(EventType.K_RELEASE, e)); }
    public void keyTyped(KeyEvent e)        { addEvent(new Event(EventType.K_TYPE, e)); }
    public void mouseClicked(MouseEvent e)  { addEvent(new Event(EventType.M_CLICK, e)); }
    public void mouseEntered(MouseEvent e)  { addEvent(new Event(EventType.M_ENTER, e)); }
    public void mouseExited(MouseEvent e)   { addEvent(new Event(EventType.M_EXIT, e)); }
    public void mousePressed(MouseEvent e)  { addEvent(new Event(EventType.M_PRESS, e)); mouseButton[e.getButton()-1] = true; }
    public void mouseReleased(MouseEvent e) { addEvent(new Event(EventType.M_RELEASE, e)); mouseButton[e.getButton()-1] = false; }
    public void mouseDragged(MouseEvent e)  { addEvent(new Event(EventType.M_DRAG, e)); mouseX = e.getX(); mouseY = e.getY(); }
    public void mouseMoved(MouseEvent e)    { addEvent(new Event(EventType.M_MOVE, e)); mouseX = e.getX(); mouseY = e.getY(); }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Which type is created window
    public enum WindowType {
        NORMAL, // Resizable
        STATIC, // No resizable
        FULLSCREEN
    }

    // Private variables used to run
    private static final long serialVersionUID = 8619356773422621193L;

    // Is main loop running
    private boolean isRunning = false;

    // Is window created
    private boolean isWindow = false;

    // Default window size
    private int defaultW = 600, defaultH = 400;

    // Default window title
    private String defaultTitle = "jsl Application";

    // Frame and main thread
    private JFrame frame;
    private Thread thread;

    // fps counter
    private int fps = 0;

}
