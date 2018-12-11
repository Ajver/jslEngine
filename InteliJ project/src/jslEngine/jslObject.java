package jslEngine;

import java.awt.*;
import java.awt.event.MouseEvent;

public class jslObject {
    protected boolean isTranslating = true;
    protected float x, y, w, h;
    protected float velX, velY, velR;
    protected float rotate, rotateX, rotateY;
    protected float translateX, translateY;
    protected jslLabel label = jslLabel.DEFAULT;
    public boolean hover = false;
    public jslObject() { this(0, 0); }
    public jslObject(float x, float y) { this(x, y, 32, 32); }
    public jslObject(float x, float y, float w, float h) {
        this.setPosition(x, y);
        this.setSize(w, h);
        this.onCreate();
    }
    protected void onCreate() {}
    protected void onCollision(jslObject other) {}
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }
    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void setSize(float w, float h) {
        setW(w);
        setH(h);
    }
    public void setW(float w) {
        this.w = w;
    }
    public void setH(float h) {
        this.h = h;
    }
    public void rotate() {
        rotate(getVelR());
    }
    public void rotate(float theta) {
        this.rotate += theta;
    }
    public void move(float et) {
        move(getVelX() * et, getVelY() * et);
    }
    public void move(float x, float y) {
        this.setX(this.x + x);
        this.setY(this.y + y);
    }
    public void setVel(float x, float y) {
        this.setVelX(x);
        this.setVelY(y);
    }
    public void setVelX(float velX) { this.velX = velX; }
    public void setVelY(float velY) { this.velY = velY; }
    public void setVelR(float velR) { this.velR = velR; }
    public void setRotateToCenter() { setRotatePosition(getW() * 0.5f, getH() * 0.5f); }
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
    public boolean getIsTranslating() { return this.isTranslating; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getW() { return w; }
    public float getH() { return h; }
    public float getCenterX() { return getX() + getW() * 0.5f; }
    public float getCenterY() { return getY() + getH() * 0.5f; }
    public float getVelX() { return velX; }
    public float getVelY() { return velY; }
    public float getVelR() { return velR; }
    public float getRotate() { return rotate; }
    public float getRotateX() { return rotateX; }
    public float getRotateY() { return rotateY; }
    public float getTranslateX() { return (isTranslating ? translateX : 0); }
    public float getTranslateY() { return (isTranslating ? translateY : 0); }
    protected void update(float et) {}
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
    public void setLabel(jslLabel l) { this.label = l; }
    public jslLabel getLabel() { return this.label; }
    public boolean is(jslLabel l) { return getLabel() == l; }
    public void onEnter(MouseEvent e) {}
    public void onLeave(MouseEvent e) {}
    public void onClick(MouseEvent e) {}
    public void onPress(MouseEvent e) {}
    public void onRelease(MouseEvent e) {}
    public void onMove(MouseEvent e) {}
    public void onDrag(MouseEvent e) {}
}
