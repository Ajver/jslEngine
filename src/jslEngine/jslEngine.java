package jslEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

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

    // Events on some jslObject (when mouse do something) (to override)
    protected void onMove(jslObject o) {}
    protected void onDrag(jslObject o) {}
    protected void onEnter(jslObject o) {}
    protected void onLeave(jslObject o) {}
    protected void onClick(jslObject o) {}
    protected void onUnclick(jslObject o) {}

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Another classes You can use
    public class jslSettings {
        public boolean isVisible = true;
        public boolean isRendering = true;
        public boolean fontChanged = true;
        public Color bgColor = new Color(100, 100,100);
        public Color txtColor = new Color(255, 255, 255);
        public float txtX = 0, txtY = 0;
        private String fontName = "arial";
        private int fontType = 0;
        private int fontSize = 16;
        public Font font = new Font(fontName, fontType, fontSize);
        public void setFontName(String name) { setFont(name, fontType, fontSize); }
        public void setFontType(int type) { setFont(fontName, type, fontSize); }
        public void setFontSize(int size) { setFont(fontName, fontType, size); }
        public void setFont(String name, int type, int size) { setFont(new Font(name, type, size)); }
        public void setFont(Font font) { this.font = font; fontChanged = false; }
        public Font getFont() { return font; }
        public String getFontName() { return fontName; }
        public int getFontType() { return fontType; }
        public int getFontSize() { return fontSize; }
    }
    public abstract class jslObject {
        protected boolean isTranslating = true;
        protected boolean isMaxX = false, isMaxY = false, isMaxW = false, isMaxH = false,
                isMinX = false, isMinY = false, isMinW = false, isMinH = false;
        protected float x, y, w, h, maxX, maxY, maxW, maxH, minX, minY, minW, minH;
        protected float velX, velY, velR;
        protected float rotate, rotateX, rotateY;
        protected float translateX, translateY;
        public jslSettings settings = new jslSettings();
        protected jslSettings defaultSettings = new jslSettings();
        protected jslSettings onHoverSettings = new jslSettings();
        public boolean hover = false;
        public void setPosition(float x, float y) {
            setX(x);
            setY(y);
        }
        public void setX(float x) {
            if(isMinX) { x = Math.max(x, minX); }
            if(isMaxX) { x = Math.min(x, maxX); }
            this.x = x;
        }
        public void setY(float y) {
            if(isMinY) { y = Math.max(y, minY); }
            if(isMaxY) { y = Math.min(y, maxY); }
            this.y = y;
        }
        public void setSize(float w, float h) {
            setW(w);
            setH(h);
        }
        public void setW(float w) {
            if(isMinW) { w = Math.max(w, minW); }
            if(isMaxW) { w = Math.min(w, maxW); }
            this.w = w;
        }
        public void setH(float h) {
            if(isMinH) { h = Math.max(h, minH); }
            if(isMaxH) { h = Math.min(h, maxH); }
            this.h = h;
        }
        public void setMaxX(boolean flag) { this.isMaxX = flag; }
        public void setMaxY(boolean flag) { this.isMaxY = flag; }
        public void setMaxW(boolean flag) { this.isMaxW = flag; }
        public void setMaxH(boolean flag) { this.isMaxH = flag; }
        public void setMaxX(float maxX) {
            this.maxX = maxX;
            isMaxX = true;
        }
        public void setMaxY(float maxY) {
            this.maxY = maxY;
            isMaxY = true;
        }
        public void setMaxW(float maxW) {
            this.maxW = maxW;
            isMaxW = true;
        }
        public void setMaxH(float maxH) {
            this.maxH = maxH;
            isMaxH = true;
        }
        public void setMinX(boolean flag) { this.isMinX = flag; }
        public void setMinY(boolean flag) { this.isMinY = flag; }
        public void setMinW(boolean flag) { this.isMinW = flag; }
        public void setMinH(boolean flag) { this.isMinH = flag; }
        public void setMinX(float minX) {
            this.minX = minX;
            isMinX = true;
        }
        public void setMinY(float minY) {
            this.minY = minY;
            isMinY = true;
        }
        public void setMinW(float minW) {
            this.minW = minW;
            isMinW = true;
        }
        public void setMinH(float minH) {
            this.minH = minH;
            isMinH = true;
        }
        public void setVelX(float velX) { this.velX = velX; }
        public void setVelY(float velY) { this.velY = velY; }
        public void setVelR(float velR) { this.velR = velR; }
        public void setRotateToCenter() { setRotatePosition(getX() + getW()/2.0f, getY() + getH()/2.0f);}
        public void setRotate(float rotate) { this.rotate = rotate; }
        public void setRotatePosition(float rx, float ry) {
            setRotateX(rx);
            setRotateY(ry);
        }
        public void setRotateX(float rx) { this.rotateX = rx; }
        public void setRotateY(float ry) { this.rotateY = ry; }
        public void translate(float tx, float ty) {
            translateX(tx);
            translateY(ty);
        }
        public void translateX(float tx) { this.translateX += tx; }
        public void translateY(float ty) { this.translateY += ty; }
        public void setTranslate(float tx, float ty) {
            setTranslateX(tx);
            setTranslateY(ty);
        }
        public void setTranslateX(float tx) { this.translateX = tx; }
        public void setTranslateY(float ty) { this.translateY = ty; }
        public void setIsTranslating(boolean flag) { this.isTranslating = flag; }
        public float getX() { return x + (isTranslating ? translateX : 0); }
        public float getY() { return y + (isTranslating ? translateY : 0); }
        public float getW() { return w; }
        public float getH() { return h; }
        public float getVelX() { return velX; }
        public float getVelY() { return velY; }
        public float getVelR() { return velR; }
        public float getRotate() { return rotate; }
        public float getRotateX() { return rotateX; }
        public float getRotateY() { return rotateY; }
        public float getTranslateX() { return (isTranslating ? translateX : 0); }
        public float getTranslateY() { return (isTranslating ? translateY : 0); }
        protected void update(float et) {
            x += velX * et;
            y += velY * et;
            rotate += velR * et;
        }
        protected void render(Graphics g) {}
        public boolean isPointIn(float px, float py) {
            if(rotate != 0.0f) {
                float diffX = px - rotateX;
                float diffY = rotateY - py;
                px = (int)(Math.cos(rotate)*diffX - Math.sin(rotate)*diffY + rotateX);
                py = (int)(Math.sin(rotate)*diffX + Math.cos(rotate)*diffY + rotateY);
            }
            if(px >= getX()) if(px <= getX()+getW()) if(py >= getY()) return py <= getY()+getH();
            return false;
        }
        public void onEnter() { this.settings = onHoverSettings; }
        public void onLeave() { this.settings = defaultSettings; }
        public void onClick() {}
        public void onUnclick() {}
        public void onMove() {}
        public void onDrag() {}
        public void setDefaultSettings(jslSettings s) { this.defaultSettings = this.settings = s; }
        public void setOnHoverSettings(jslSettings s) { this.onHoverSettings = s; }
        public jslSettings getDefaultSettings() { return defaultSettings; }
        public jslSettings getOnHoverSettings() { return onHoverSettings; }
    }
    public class jslButton extends jslObject {
        private String title;
        public jslButton(String title) {
            this(title, 0, 0);
        }
        public jslButton(String title, float x, float y) {
            this(title, x, y, 100, 25);
        }
        public jslButton(String title, float x, float y, float w, float h) {
            this.title = title;
            setPosition(x, y);
            setSize(w, h);
            setRotateToCenter();
            onHoverSettings = defaultSettings = settings = new jslSettings();
        }
        public void setTitle(String title) { this.title = title; }
        public String getTitle() { return title; }
        public void render(Graphics g) {
            g.translate((int)translateX, (int)translateY);
            if(rotate != 0.0f) {
                ((Graphics2D) g).rotate(rotate, rotateX, rotateY);
            }
            g.setColor(settings.bgColor);
            g.fillRect((int)getX(), (int)getY(), (int)getW(), (int)getH());
            g.setColor(settings.txtColor);
            g.setFont(settings.font);
            if(settings.fontChanged) {
                FontMetrics f = g.getFontMetrics();
                settings.txtX = getX() + (getW() - f.stringWidth(title)) / 2.0f;
                settings.txtY = getY() + (f.getAscent() + (getH() - (f.getAscent() + f.getDescent())) / 2.0f);
                settings.fontChanged = false;
            }
            g.drawString(title, (int)settings.txtX, (int)settings.txtY);
            if(rotate != 0.0f) {
                ((Graphics2D) g).rotate(-rotate, rotateX, rotateY);
            }
            g.translate(-(int)translateX, -(int)translateY);
        }
    }
    // Object of this class is created (and called "jsl")
    public class jslManager {
        private boolean autorender = true, autoupdate = true;
        private float translateX = 0, translateY = 0;
        private jslObject clickedOb = null;
        public jslSettings defaulButtonSettings = new jslSettings();
        public jslSettings onHoverButtonSettings = defaulButtonSettings;
        private LinkedList<jslObject> objects = new LinkedList<>();
        public jslManager() {}
        public void setAutorender(boolean flag) { this.autorender = flag;}
        public void setAutoupdate(boolean flag) { this.autoupdate = flag;}
        public void translate(float tx, float ty) {
            translateX(tx);
            translateY(ty);
        }
        public void translateX(float tx) {
            this.translateX += tx;
            for(jslObject o : objects) {
                o.translateX(tx);
            }
        }
        public void translateY(float ty) { this.translateY += ty; }
        public void setTranslate(float tx, float ty) {
            setTranslateX(tx);
            setTranslateY(ty);
        }
        public void setTranslateX(float tx) { this.translateX = tx; }
        public void setTranslateY(float ty) { this.translateY = ty; }
        public jslButton newButton() {
            return newButton("Button");
        }
        public jslButton newButton(String title) {
            return newButton(title, 0, 0);
        }
        public jslButton newButton(String title, float x, float y) {
            return newButton(title, x, y, 100, 25);
        }
        public jslButton newButton(String title, float x, float y, float w, float h) {
            jslButton button = new jslButton(title, x, y, w, h);
            button.setDefaultSettings(defaulButtonSettings);
            button.setOnHoverSettings(onHoverButtonSettings);
            add(button);
            return button;
        }
        public void add(jslObject o) { objects.add(o); }
        public void update(float et) { if(autoupdate) { for(jslObject o : objects) { o.update(et); } } }
        public void render(Graphics g) {
            if(autorender) {
                g.translate((int) translateX, (int) translateY);
                for (jslObject o : objects) {
                    if (o.settings.isVisible) {
                        if (o.settings.isRendering) {
                            o.render(g);
                        }
                    }
                }
                g.translate(-(int) translateX, -(int) translateY);
            }
        }
        public void mouseMoved(MouseEvent e) {
            for(int i=objects.size()-1; i>=0; i--) {
                jslObject o = objects.get(i);
                if (o.settings.isVisible) {
                    if (o.isPointIn(e.getX(), e.getY())) {
                        o.onMove();
                        onMove(o);
                        if (!o.hover) {
                            o.hover = true;
                            o.onEnter();
                            onEnter(o);
                        }
                        for(i=i-1; i>=0; i--) {
                            o = objects.get(i);
                            if(o.hover) {
                                o.hover = false;
                                o.onLeave();
                                onLeave(o);
                            }
                        }
                        return;
                    } else if (o.hover) {
                        o.hover = false;
                        o.onLeave();
                        onLeave(o);
                    }
                }
            }
        }
        public void mouseDragged(MouseEvent e) {
            if(clickedOb != null) {
                if (clickedOb.settings.isVisible) {
                    clickedOb.onDrag();
                    onDrag(clickedOb);
                }
            }
//            else {
//                for (int i = objects.size() - 1; i >= 0; i--) {
//                    jslObject o = objects.get(i);
//                    if (o.settings.isVisible) {
//                        if (o.isPointIn(e.getX(), e.getY())) {
//                            clickedOb = o;
//                            o.onDrag();
//                            onDrag(o);
//                            return;
//                        }
//                    }
//                }
//            }
        }
        public void mousePressed(MouseEvent e) {
            for(int i=objects.size()-1; i>=0; i--) {
                jslObject o = objects.get(i);
                if(o.isPointIn(e.getX(), e.getY())) {
                    clickedOb = o;
                    o.onClick();
                    onClick(o);
                    return;
                }
            }
        }
        public void mouseReleased(MouseEvent e) {
            if(clickedOb != null) {
                if (clickedOb.isPointIn(e.getX(), e.getY())) {
                    clickedOb.onUnclick();
                    onUnclick(clickedOb);
                }
                clickedOb = null;
            }
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
        if(windowType == WindowType.jslStatic) {
            WW = w + 6;
            WH = h + 29;
            frame.setResizable(false);
        }else if(windowType == WindowType.jslNormal) {
            WW = w + 16;
            WH = h + 39;
        }else {
            WW = sw;
            WH = sh;
            frame.setResizable(false);
        }
        if(windowType != WindowType.jslFullscreen) {
            frame.setLocation((sw - WW) / 2, (sh - WH) / 2);
        }
        setSize(WW, WH);
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
        jsl = new jslManager();

        this.thread = new Thread(this);
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
            Graphics2D g2 = (Graphics2D)g;
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
        }
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, WW(), WH());
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

    // DO NOT override those functions!
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

    public int WW() { return getWidth(); }
    public int WH() { return getHeight(); }
    public int getFpsCount() { return fps; }
}
