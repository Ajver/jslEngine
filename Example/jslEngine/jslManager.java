package jslEngine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class jslManager {
    private jslEngine engine;
    private boolean autoRender = true, autoUpdate = true, autoClearScreen = true;
    protected boolean isTranslating = true;
    private float translateX = 0, translateY = 0;
    private jslObject clickedOb = null;
    private LinkedList<jslObject> objects = new LinkedList<>();
    private LinkedList<jslKeyInput> keyInputs = new LinkedList<>();
    public jslManager(jslEngine engine) { this.engine = engine; }
    public void setAutoRender(boolean flag) { this.autoRender = flag; }
    public void setAutoUpdate(boolean flag) { this.autoUpdate = flag; }
    public void setAutoClearScreen(boolean flag) { this.autoClearScreen = flag; }
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
    public float getTranslateX() { return (isTranslating ? translateX : 0); }
    public float getTranslateY() { return (isTranslating ? translateY : 0); }
    public void setIsTranslating(boolean flag) { this.isTranslating = flag; }
    public boolean getIsTranslating(boolean flag) { return this.isTranslating; }
    public void update(float et) { if(autoUpdate) { for(jslObject o : objects) { o.update(et); } } }
    public void render(Graphics g) {
        if(autoRender) {
            if(autoClearScreen) {
                g.setColor(new Color(30, 30, 30));
                g.fillRect(0, 0, engine.WW(), engine.WH());
            }
            if(isTranslating) {
                g.translate((int) translateX, (int) translateY);
            }
            for (jslObject o : objects) {
                o.beforeRender(g);
                o.render(g);
                o.afterRender(g);
            }
            if(isTranslating) {
                g.translate(-(int) translateX, -(int) translateY);
            }
        }
    }
    public void mouseMoved(MouseEvent e) {
        for(int i=objects.size()-1; i>=0; i--) {
            jslObject o = objects.get(i);
            if (o.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                o.onMove();
                o.onMove(e);
                engine.onMove(o);
                engine.onMove(o, e);
                if (!o.hover) {
                    o.hover = true;
                    o.onEnter();
                    o.onEnter(e);
                    engine.onEnter(o);
                    engine.onEnter(o, e);
                }
                for(i=i-1; i>=0; i--) {
                    o = objects.get(i);
                    if(o.hover) {
                        o.hover = false;
                        o.onLeave();
                        o.onLeave(e);
                        engine.onLeave(o);
                        engine.onLeave(o, e);
                    }
                }
                return;
            } else if (o.hover) {
                o.hover = false;
                o.onLeave();
                o.onLeave(e);
                engine.onLeave(o);
                engine.onLeave(o, e);
            }
        }
    }
    public void mouseDragged(MouseEvent e) {
        if(clickedOb != null) {
            clickedOb.onDrag();
            clickedOb.onDrag(e);
            engine.onDrag(clickedOb);
            engine.onDrag(clickedOb, e);
        }
    }
    public void mousePressed(MouseEvent e) {
        for(int i=objects.size()-1; i>=0; i--) {
            jslObject o = objects.get(i);
            if(o.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                clickedOb = o;
                o.onPress();
                o.onPress(e);
                engine.onPress(o);
                engine.onPress(o, e);
                return;
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
        if(clickedOb != null) {
            if (clickedOb.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                clickedOb.onRelease();
                clickedOb.onRelease(e);
                engine.onRelease(clickedOb);
                engine.onRelease(clickedOb, e);
            }
            clickedOb = null;
        }
    }
    public void add(jslObject o) { objects.add(o); }
    public LinkedList<jslObject> getObjects() { return objects; }
    public jslObject getObject(int i) { return objects.get(i); }
    public void removeAllObjects() { objects.clear(); }
    public void removeObject(int i) { objects.remove(i); }
    public void removeObject(jslObject o) {
        for(int i=objects.size()-1; i>=0; i--) {
            if(getObject(i) == o) {
                removeObject(i);
            }
        }
    }
    public void keyPressed(KeyEvent e) {
        for(jslKeyInput k : keyInputs) {
            k.onPress(e);
        }
    }
    public void keyReleased(KeyEvent e) {
        for(jslKeyInput k : keyInputs) {
            k.onRelease(e);
        }
    }
    public void keyTyped(KeyEvent e) {
        for(jslKeyInput k : keyInputs) {
            k.onType(e);
        }
    }
    public void add(jslKeyInput k) { keyInputs.add(k); }
    public LinkedList<jslKeyInput> getKeyInputs() { return keyInputs; }
    public jslKeyInput getKeyInput(int i) { return keyInputs.get(i); }
    public void removeAllKeyInputs() { keyInputs.clear(); }
    public void removeKeyInput(int i) { keyInputs.remove(i); }
    public void removeKeyInput(jslKeyInput k) {
        for(int i=keyInputs.size()-1; i>=0; i--) {
            if(getKeyInput(i) == k) {
                removeKeyInput(i);
            }
        }
    }
}