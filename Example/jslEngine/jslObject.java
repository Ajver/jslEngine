package jslEngine;

import java.awt.*;

public class jslObject {
    protected boolean isTranslating = true;
    protected boolean isMaxX = false, isMaxY = false, isMaxW = false, isMaxH = false,
            isMinX = false, isMinY = false, isMinW = false, isMinH = false;
    protected float x, y, w, h, maxX, maxY, maxW, maxH, minX, minY, minW, minH;
    protected float velX, velY, velR;
    protected float rotate, rotateX, rotateY;
    protected float translateX, translateY;
    public boolean hover = false;
    public jslObject() { this(0, 0); }
    public jslObject(float x, float y) { this(0, 0, 32, 32); }
    public jslObject(float x, float y, float w, float h) {
        this.setPosition(x, y);
        this.setSize(w, h);
        this.onCreate();
    }
    protected void onCreate() {}
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
        px -= getTranslateX();
        py -= getTranslateY();
        if(rotate != 0.0f) {
            float diffX = px - rotateX;
            float diffY = rotateY - py;
            px = (int)(Math.cos(rotate)*diffX - Math.sin(rotate)*diffY + rotateX);
            py = (int)(Math.sin(rotate)*diffX + Math.cos(rotate)*diffY + rotateY);
        }
        if(px >= getX()) if(px <= getX()+getW()) if(py >= getY()) return py <= getY()+getH();
        return false;
    }
    public void onEnter() {}
    public void onLeave() {}
    public void onPress() {}
    public void onRelease() {}
    public void onMove() {}
    public void onDrag() {}
}
