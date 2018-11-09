package jslEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public abstract class jslEngine extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    // Those variables are for Your using
    public KeyEvent key = null;
    public MouseEvent mouse = null;
    public jslManager jsl;
    private WindowType windowType;
    private boolean antialiasing = false;

    // Those functions are to override
    protected abstract void update(float et);
    protected abstract void render(Graphics g);

    // After create all elements (on the end of constructor) ( --DO NOT-- override)
    protected void onCreate() {}

    // Events (to override)
    protected void onKeyPressed() {}
    protected void onKeyReleased() {}
    protected void onKeyTyped() {}
    protected void onMouseClicked() {}
    protected void onMouseEntered() {}
    protected void onMouseExited() {}
    protected void onMousePressed() {}
    protected void onMouseReleased() {}
    protected void onMouseDragged() {}
    protected void onMouseMoved() {}

    // Mouse events on specific jslObject (to override)
    protected void onMove(jslObject o) {}
    protected void onDrag(jslObject o) {}
    protected void onEnter(jslObject o) {}
    protected void onLeave(jslObject o) {}
    protected void onPress(jslObject o) {}
    protected void onRelease(jslObject o) {}

    // Functions that may be helpful ( --DO NOT-- override)
    public int WW() { return getWidth(); }
    public int WH() { return getHeight(); }
    public int getFpsCount() { return fps; }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Which type is created window
    public enum WindowType {
        jslNormal, // Resizable
        jslStatic, // No resizable
        jslFullscreen
    }
    // Private variables used to run
    private static final long serialVersionUID = 8619356773422621193L;
    private boolean isRunning = false;
    private boolean isWindow = false;
    private int defaultW = 600, defaultH = 400;
    private String defaultTitle = "jsl Application";
    private JFrame frame;
    private Thread thread;

    // Private help variables
    private int fps = 0;

    // Functions for window controlling
    protected void setWindowTitle(String title) {
        frame.setTitle(title);
    }
    protected void setWindowPosition(int x, int y) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int sw = (int)tk.getScreenSize().getWidth();
        int sh = (int)tk.getScreenSize().getHeight();
        if(x < 0) x = 0;
        else if(x+WW() > sw) x = sw - WW();
        if(y < 0) y = 0;
        else if(y+WH() > sh) y = sh - WH();
        frame.setLocation(x, y);
    }
    protected void setAntialiasing(boolean flag) { antialiasing = flag; }
    protected void createWindow(String title, int w, int h, WindowType type) {
        if(isWindow) return;
        windowType = type;
        isWindow = true;
        frame = new JFrame(title);
        if(type == WindowType.jslFullscreen) {
            frame.setUndecorated(true);
        }
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        resizeWindow(w, h);
    }
    protected void resizeWindow(int w, int h) {
        if(w == WW()) {
            if(h == WH()) {
                return;
            }
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        int WW, WH, sw, sh;
        sw = (int)tk.getScreenSize().getWidth();
        sh = (int)tk.getScreenSize().getHeight();
        if(this.windowType == WindowType.jslStatic) {
            WW = w + 6;
            WH = h + 29;
            this.frame.setResizable(false);
        }else if(this.windowType == WindowType.jslNormal) {
            WW = w + 16;
            WH = h + 39;
        }else {
            WW = sw;
            WH = sh;
            this.frame.setResizable(false);
        }
        if(this.windowType != WindowType.jslFullscreen) {
            this.frame.setLocation((sw - WW) / 2, (sh - WH) / 2);
        }
        this.setSize(WW, WH);
        this.frame.setSize(WW, WH);
    }
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
        start(title, w, h, WindowType.jslStatic);
    }
    protected void start(String title, int w, int h, WindowType type) {
        if(isRunning) return;
        this.createWindow(title, w, h, type);
        this.isRunning = true;
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.thread = new Thread(this);
        this.jsl = new jslManager(this);

        this.onCreate();

        this.thread.start();
    }
    private void jslUpdate(float et) {
        jsl.update(et);
        update(et);
    }
    private void jslRender() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if(antialiasing) {
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D)g).setRenderingHints(rh);
        }
        jsl.render(g);
        render(g);
        g.dispose();
        bs.show();
    }

    // Main loop
    public void run() {
        this.requestFocus();
        long start = 0;
        long stop = 0;
        float elapsedTime = 0.0f;
        long fpsTimer = System.currentTimeMillis();
        int fpsCounter = 0;
        while(isRunning) {
            start = System.currentTimeMillis();
            this.jslUpdate(elapsedTime);
            this.jslRender();
            fpsCounter++;
            stop = System.currentTimeMillis();
            elapsedTime = (stop - start) / 1000.0f;
            if(System.currentTimeMillis() >= fpsTimer) {
                fpsTimer += 1000;
                fps = fpsCounter;
                fpsCounter = 0;
            }
        }
    }

    // ( --DO NOT-- override)
    public void keyPressed(KeyEvent e) { this.key = e; onKeyPressed(); }
    public void keyReleased(KeyEvent e) { this.key = e; onKeyReleased(); }
    public void keyTyped(KeyEvent e) { this.key = e; onKeyTyped(); }
    public void mouseClicked(MouseEvent e) { this.mouse = e; onMouseClicked(); }
    public void mouseEntered(MouseEvent e) { this.mouse = e; onMouseEntered(); }
    public void mouseExited(MouseEvent e) { this.mouse = e; onMouseExited(); }
    public void mousePressed(MouseEvent e) { this.mouse = e; jsl.mousePressed(e); onMousePressed(); }
    public void mouseReleased(MouseEvent e) { this.mouse = e; jsl.mouseReleased(e); onMouseReleased(); }
    public void mouseDragged(MouseEvent e) { this.mouse = e; jsl.mouseDragged(e); onMouseDragged(); }
    public void mouseMoved(MouseEvent e) { this.mouse = e; jsl.mouseMoved(e); onMouseMoved(); }
}
