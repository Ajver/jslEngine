package jslEngine;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class jslManager {
    private jslEngine engine;
    private boolean autoRender = true, autoUpdate = true, autoClearScreen = true;
    protected boolean isTranslating = true;
    private float translateX = 0, translateY = 0;
    private jslObject clickedOb = null;
    private LinkedList<jslObject> objects = new LinkedList<>();
    public jslManager(jslEngine engine) { this.engine = engine; }
    public void setAutoRender(boolean flag) { this.autoRender = flag; }
    public void setAutoUpdate(boolean flag) { this.autoUpdate = flag; }
    public void setAutoClearScreen(boolean flag) { this.autoClearScreen = flag; }
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
    public float getTranslateX() { return (isTranslating ? translateX : 0); }
    public float getTranslateY() { return (isTranslating ? translateY : 0); }
    public void setIsTranslating(boolean flag) { this.isTranslating = flag; }
    public boolean getIsTranslating(boolean flag) { return this.isTranslating; }
    public void add(jslObject o) { objects.add(o); }
    public void update(float et) { if(autoUpdate) { for(jslObject o : objects) { o.update(et); } } }
    public void render(Graphics g) {
        if(autoRender) {
            if(autoClearScreen) {
                g.setColor(new Color(30, 30, 30));
                g.fillRect(0, 0, engine.WW(), engine.WH());
            }
            g.translate((int) translateX, (int) translateY);
            for (jslObject o : objects) {
                o.render(g);
            }
            g.translate(-(int) translateX, -(int) translateY);
        }
    }
    public void mouseMoved(MouseEvent e) {
        for(int i=objects.size()-1; i>=0; i--) {
            jslObject o = objects.get(i);
            if (o.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                o.onMove();
                engine.onMove(o);
                if (!o.hover) {
                    o.hover = true;
                    o.onEnter();
                    engine.onEnter(o);
                }
                for(i=i-1; i>=0; i--) {
                    o = objects.get(i);
                    if(o.hover) {
                        o.hover = false;
                        o.onLeave();
                        engine.onLeave(o);
                    }
                }
                return;
            } else if (o.hover) {
                o.hover = false;
                o.onLeave();
                engine.onLeave(o);
            }
        }
    }
    public void mouseDragged(MouseEvent e) {
        if(clickedOb != null) {
            clickedOb.onDrag();
            engine.onDrag(clickedOb);
        }
    }
    public void mousePressed(MouseEvent e) {
        for(int i=objects.size()-1; i>=0; i--) {
            jslObject o = objects.get(i);
            if(o.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                clickedOb = o;
                o.onPress();
                engine.onPress(o);
                return;
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
        if(clickedOb != null) {
            if (clickedOb.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                clickedOb.onRelease();
                engine.onRelease(clickedOb);
            }
            clickedOb = null;
        }
    }
}