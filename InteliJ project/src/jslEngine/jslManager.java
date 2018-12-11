package jslEngine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class jslManager {
    private jslEngine engine;
    private boolean autoRender = true, autoUpdate = true, autoClearScreen = true;

    // Translation
    protected boolean isTranslating = true;
    private float translateX = 0, translateY = 0;

    // Object that is clicked (before mouse release)
    private jslObject clickedOb = null;

    // All game objects
    private ArrayList<jslObject> objects = new ArrayList<>();

    // Order to displaying objects (and checking which is clicked)
    private ArrayList<jslLabel> renderOrder = new ArrayList<>();

    // All objects with mouse listener
    private ArrayList<jslObject> mouseInputs = new ArrayList<>();

    // All class with key listener
    private ArrayList<jslKeyInput> keyInputs = new ArrayList<>();

    public jslManager(jslEngine engine) { this.engine = engine; }

    public void setAutoRender(boolean flag) { this.autoRender = flag; }
    public void setAutoUpdate(boolean flag) { this.autoUpdate = flag; }
    public void setAutoClearScreen(boolean flag) { this.autoClearScreen = flag; }

    // Translating
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

    public void update(float et) {
        if(autoUpdate) {
            for(int i=objects.size()-1; i>=0; i--) {
                objects.get(i).update(et);
            }
        }
    }

    public void render(Graphics g) {
        if(autoRender) {
            if(autoClearScreen) {
                g.setColor(new Color(30, 30, 30));
                g.fillRect(0, 0, engine.WW(), engine.WH());
            }
            if(isTranslating) {
                g.translate((int) translateX, (int) translateY);
            }
            for(int i=0; i<objects.size(); i++) {
                jslObject o = objects.get(i);
                o.render(g);
            }
            if(isTranslating) {
                g.translate(-(int) translateX, -(int) translateY);
            }
        }
    }

    // Mouse input
    public void mouseMoved(MouseEvent e) {
        for(int i=mouseInputs.size()-1; i>=0; i--) {
            jslObject o = mouseInputs.get(i);

            if (o.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                o.onMove(e);

                if (!o.hover) {
                    o.hover = true;
                    o.onEnter(e);
                }

                for(i=i-1; i>=0; i--) {
                    // Remove hover from all objects below currently hovered
                    o = mouseInputs.get(i);
                    if(o.hover) {
                        o.hover = false;
                        o.onLeave(e);
                    }
                }

                return;
            } else if (o.hover) {
                o.hover = false;
                o.onLeave(e);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(clickedOb != null) {
            clickedOb.onDrag(e);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(clickedOb != null) {
            clickedOb.onClick(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        for(int i=mouseInputs.size()-1; i>=0; i--) {
            jslObject o = mouseInputs.get(i);

            if(o.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                clickedOb = o;
                o.onPress(e);
                return;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(clickedOb != null) {
            // If released in the same object, that was clicked
            if (clickedOb.isPointIn(e.getX()-getTranslateX(), e.getY()-getTranslateY())) {
                clickedOb.onRelease(e);
            }

            // Clear clicked object
            clickedOb.hover = false;
            clickedOb.onLeave(e);
            clickedOb = null;
        }
    }

    // Object managing
    public void add(jslObject o) {
        objects.add(o);
        sortObjects();
    }

    public ArrayList<jslObject> getObjects() { return objects; }
    public jslObject getObject(int i) { return objects.get(i); }
    public jslObject getObject(jslLabel l) {
        for(jslObject o : objects) {
            if(o.is(l)) {
                return o;
            }
        }

        return null;
    }

    public void removeAllObjects() { objects.clear(); }
    public void removeObject(int i) { objects.remove(i); }

    // Remove one object by reference
    public void removeObject(jslObject o) {
        for(int i=objects.size()-1; i>=0; i--) {
            if(getObject(i) == o) {
                removeObject(i);
                return;
            }
        }
    }

    // Remove all objects by reference
    public void removeAllObjects(jslObject o) {
        for(int i=objects.size()-1; i>=0; i--) {
            if(getObject(i) == o) {
                removeObject(i);
            }
        }
    }

    // Remove one object by jslLabel
    public void removeObject(jslLabel l) {
        for(int i=objects.size()-1; i>=0; i--) {
            if(getObject(i).is(l)) {
                removeObject(i);
                return;
            }
        }
    }

    // Remove all objects by jslLabel
    public void removeAllObjects(jslLabel l) {
        for(int i=objects.size()-1; i>=0; i--) {
            if(getObject(i).is(l)) {
                removeObject(i);
            }
        }
    }

    // Key inputs managing
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
    public ArrayList<jslKeyInput> getKeyInputs() { return keyInputs; }
    public jslKeyInput getKeyInput(int i) { return keyInputs.get(i); }
    public void removeAllKeyInputs() { keyInputs.clear(); }
    public void removeKeyInput(int i) { keyInputs.remove(i); }

    // Remove one key input by reference
    public void removeKeyInput(jslKeyInput k) {
        for(int i=keyInputs.size()-1; i>=0; i--) {
            if(getKeyInput(i) == k) {
                removeKeyInput(i);
                return;
            }
        }
    }

    // Remove all key inputs by reference
    public void removeAllKeyInputs(jslKeyInput k) {
        for(int i=keyInputs.size()-1; i>=0; i--) {
            if(getKeyInput(i) == k) {
                removeKeyInput(i);
            }
        }
    }

    // Mouse inputs managing
    public void addMouseInput(jslObject o) {
        mouseInputs.add(o);
        sortObjects();
    }

    public ArrayList<jslObject> getMouseInputs() { return mouseInputs; }
    public jslObject getMouseInput(int i) { return mouseInputs.get(i); }

    public void removeAllMouseInputs() { mouseInputs.clear(); }
    public void removeMouseInput(int i) { mouseInputs.remove(i); }

    // Remove one mouse input by reference
    public void removeMouseInput(jslObject o) {
        for(int i=mouseInputs.size()-1; i>=0; i--) {
            if(getMouseInput(i) == o) {
                removeKeyInput(i);
                return;
            }
        }
    }

    // Remove all mouse inputs by reference
    public void removeAllMouseInput(jslObject o) {
        for(int i=mouseInputs.size()-1; i>=0; i--) {
            if(getMouseInput(i) == o) {
                removeKeyInput(i);
            }
        }
    }

    // Render order managing
    public void addToRenderOrder(jslLabel l) {
        // Remove this label, if is in list
        for(int i=renderOrder.size()-1; i>=0; i--) {
            if(renderOrder.get(i) == l) {
                renderOrder.remove(i);
            }
        }

        renderOrder.add(l);
        sortObjects();
    }

    public void setRenderOrder(ArrayList<jslLabel> labels) {
        renderOrder = labels;
        sortObjects();
    }

    public void setRenderOrder(jslLabel... labels) {
        renderOrder.clear();

        for(int i=0; i<labels.length; i++) {
            renderOrder.add(labels[i]);
        }

        sortObjects();
    }

    private void sortObjects() {
        // Sorted objects
        ArrayList<jslObject> sortedOb = new ArrayList<>();

        // Sorted mouse inputs
        ArrayList<jslObject> sortedMI = new ArrayList<>();

        for(int j=0; j<renderOrder.size(); j++) {
            jslLabel l = renderOrder.get(j);
            // Sort objects to render
            for(int i=0; i<objects.size(); ) {
                jslObject o = objects.get(i);

                if(o.is(l)) {
                    sortedOb.add(o);
                    objects.remove(i);
                }else {
                    if(!renderOrder.contains(o.getLabel())) {
                        renderOrder.add(o.getLabel());
                    }

                    i++;
                }
            }

            // Sort mouse inputs
            for(int i=0; i<mouseInputs.size(); ) {
                jslObject o = mouseInputs.get(i);

                if(o.is(l)) {
                    sortedMI.add(o);
                    mouseInputs.remove(i);
                }else {
                    if(!renderOrder.contains(o.getLabel())) {
                        renderOrder.add(o.getLabel());
                    }

                    i++;
                }
            }
        }

        objects = sortedOb;
        mouseInputs = sortedMI;
    }

    public ArrayList<jslLabel> getRenderOrder() { return this.renderOrder; }
}