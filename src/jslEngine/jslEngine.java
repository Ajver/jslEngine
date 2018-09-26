package jslEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public abstract class jslEngine extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    // Those variables are for Your using
    public KeyEvent key = null;
    public MouseEvent mouse = null;

    // Those functions are to override
    protected abstract void update(float et);
    protected abstract void render(Graphics g);

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

    // Events on some jslObject (when mouse do something)
    protected void onEnter() {}
    protected void onLeave() {}
    protected void onClick() {}
    protected void onUnclick() {}

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Another classes You can use

    public class jslButton {
        public jslButton(String title) {
            System.out.print(title);
        }
    }

    // Object of this class is created
    public class jslManager {
        public jslManager() {
            
        }
    }

    public enum WindowType {
        jslNormal, // Resizable
        jslStatic, // No resizable
        jslFullscreen
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Private variables used to run
    private static final long serialVersionUID = 8619356773422621193L;
    private boolean isRunning = false;
    private boolean isWindow = false;
    private JFrame frame;
    private Thread thread;

    // Private help variables
    private int fps = 0;

    // Functions for window controlling
    protected void createWindow(String title, int w, int h, WindowType type) {
        if(isWindow) return;
        isWindow = true;
        frame = new JFrame(title);
        if(type == WindowType.jslFullscreen) {
            frame.setUndecorated(true);
        }
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Toolkit tk = Toolkit.getDefaultToolkit();
        int WW, WH, sw, sh;
        sw = (int)tk.getScreenSize().getWidth();
        sh = (int)tk.getScreenSize().getHeight();
        if(type == WindowType.jslStatic) {
            WW = w + 6;
            WH = h + 29;
            frame.setResizable(false);
        }else if(type == WindowType.jslNormal) {
            WW = w + 16;
            WH = h + 39;
        }else {
            WW = sw;
            WH = sh;
            frame.setResizable(false);
        }
        if(type != WindowType.jslFullscreen) {
            frame.setLocation((sw-w)/2, (sh-h)/2);
        }
        frame.setSize(WW, WH);
    }
    protected void start() {
        start("jsl Application", 400, 300);
    }
    protected void start(String title, int w, int h) {
        start(title, w, h, WindowType.jslStatic);
    }
    protected void start(String title, int w, int h, WindowType type) {
        createWindow(title, w, h, type);
        if(isRunning) return;
        isRunning = true;
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.thread = new Thread(this);
        this.thread.start();
    }
    private void jslRender() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, WW(), WH());
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
            this.update(elapsedTime);
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

    // DO NOT override those functions!
    public void keyPressed(KeyEvent e) { this.key = e; onKeyPressed(); }
    public void keyReleased(KeyEvent e) { this.key = e; onKeyReleased(); }
    public void keyTyped(KeyEvent e) { this.key = e; onKeyTyped(); }
    public void mouseClicked(MouseEvent e) { this.mouse = e; onMouseClicked(); }
    public void mouseEntered(MouseEvent e) { this.mouse = e; onMouseEntered(); }
    public void mouseExited(MouseEvent e) { this.mouse = e; onMouseExited(); }
    public void mousePressed(MouseEvent e) { this.mouse = e; onMousePressed(); }
    public void mouseReleased(MouseEvent e) { this.mouse = e; onMouseReleased(); }
    public void mouseDragged(MouseEvent e) { this.mouse = e; onMouseDragged(); }
    public void mouseMoved(MouseEvent e) { this.mouse = e; onMouseMoved(); }

    public int WW() { return getWidth(); }
    public int WH() { return getHeight(); }
    public int getFpsCount() { return fps; }
}
