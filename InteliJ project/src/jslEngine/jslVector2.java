package jslEngine;

public class jslVector2 {

    public float x, y;

    public jslVector2() {
        this(0, 0);
    }

    public jslVector2(float x, float y) {
        this.set(x, y);
    }

    public jslVector2(jslVector2 v) {
        this.set(v);
    }

    public void normalize() {
        float d = length();
        if(d != 0.0f) {
            divide(d);
        }
    }

    public float length() {
        return (float)Math.sqrt(getX() * getX() + getY() * getY());
    }

    public void multiply(float a) {
        x *= a;
        y *= a;
    }

    public void multiply(jslVector2 v) {
        x *= v.x;
        y *= v.y;
    }

    public void divide(float a) {
        x /= a;
        y /= a;
    }

    public void divide(jslVector2 v) {
        x /= v.x;
        y /= v.y;
    }

    public void add(jslVector2 v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void add(float a) {
        this.x += a;
        this.y += a;
    }

    public void substract(jslVector2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void substract(float a) {
        this.x -= a;
        this.y -= a;
    }

    public void set(jslVector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public jslVector2 copy() {
        return new jslVector2(this);
    }
}
