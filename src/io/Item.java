package io;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.Queue;

public class Item implements Itemable {
    private static final double DEFAULT_FADE = 1;

    private static final int EDGE_IGNORE = 0;
    private static final int EDGE_BOUNCE = 1;
    private static final int EDGE_BOUNCE_FLIP = 2;
    private static final int EDGE_STOP = 3;
    private static final int EDGE_REMOVE = 4;

    private static final double DEFAULT_PPS = 30;

    private Node node;
    private DojoPane dojo;
    private double dx, dy, dr, da; //deltas x, y, rotate, alpha
    private double dir, speed;
    private EventHandler<Event> edgeHandler;
    private int edge = EDGE_IGNORE;
    private Queue<Runnable> queue = new LinkedList<>();
    private int waiting = 0;

    public Item(Node node) {
        this.node = node;
        node.setUserData(this);
        dojo = ((DojoPane)(node.getParent().getUserData()));
        node.setMouseTransparent(true);

     //   queue ;
    }

    public String id() {
        return node.getId();
    }

    public String getType() {
        return node.getClass().getName();
    }

    void tick() {
        waiting--;

        boolean circle = node instanceof Circle;
        boolean rect = node instanceof Rectangle;
        boolean text = node instanceof Text;

        while (waiting <= 0 && queue.size() > 0) {
            Platform.runLater(queue.remove());
        }

        if (dx != 0) {
            if (circle) ((Circle)node).setCenterX(((Circle)node).getCenterX() + dx);
            else node.setLayoutX(node.getLayoutX() + dx);
        }

        if (dy != 0) {
            if (circle) ((Circle)node).setCenterY(((Circle)node).getCenterY() + dy);
            else node.setLayoutY(node.getLayoutY() + dy);
        }

       // System.out.println(node.getClass().getName() + "|" + edge);

        if (edge != EDGE_IGNORE && (dx != 0 || dy != 0)) {

           // Bounds screen = Win.edges();
            //double w = Win.width();

            double w = node.getParent().getBoundsInParent().getWidth();
            double h = node.getParent().getBoundsInParent().getHeight();

          //  Bounds bounds = node.getBoundsInParent();


          //  double x = node.getBoundsInParent().getMaxX();

            if (node instanceof Circle) {
               // System.out.println(node.getClass().getName() + "|" + bounds.getMinY() + "|" + bounds.getMaxY() + "|" + node.getLayoutY()+ "|" + ((Circle)node).getCenterY());
            }

            if (node.getBoundsInParent().getMinX() < 0) {
                if (edge == EDGE_BOUNCE || edge == EDGE_BOUNCE_FLIP) {
                    direction(180 - dir);
                    if (circle) {
                        ((Circle)node).setCenterX(((Circle)node).getRadius());
                    } else {
                        if (edge == EDGE_BOUNCE_FLIP) {
                            forceFlipHorizontally();
                        }
                        node.setLayoutX(0);

                        while (node.getBoundsInParent().getMinX() < 0) {
                            node.setLayoutX(node.getLayoutX()+1);
                        }
                    }
                } else if (edge == EDGE_STOP) {

                    if (circle) {
                        ((Circle)node).setCenterX(((Circle)node).getRadius());
                    } else {
                        node.setLayoutX(0);
                    }

                    speed(0);
                } else if (edge == EDGE_REMOVE) {
                    this.remove();
                }
            } else if (node.getBoundsInParent().getMaxX() > w) {
                if (edge == EDGE_BOUNCE || edge == EDGE_BOUNCE_FLIP) {
                    direction(180 - dir);
                    if (circle) {
                        ((Circle)node).setCenterX(w - ((Circle)node).getRadius());
                    } else {
                        if (edge == EDGE_BOUNCE_FLIP) {
                            forceFlipHorizontally();
                        }
                        node.setLayoutX(w - node.getLayoutBounds().getWidth());

                        while (node.getBoundsInParent().getMaxX() >= w) {
                            node.setLayoutX(node.getLayoutX()-1);
                        }
                    }
                } else if (edge == EDGE_STOP) {
                    if (circle) {
                        ((Circle)node).setCenterX(w - ((Circle)node).getRadius());
                    } else {
                        node.setLayoutX(w - node.getLayoutBounds().getWidth());
                    }
                    speed(0);
                } else if (edge == EDGE_REMOVE) {
                    this.remove();
                }
            }

            if (text) {
               // System.out.println(bounds.getMinY() + "|" + ((Text)node).getBaselineOffset());
            }


            if (node.getBoundsInParent().getMinY() < 0) {
                if (edge == EDGE_BOUNCE || edge == EDGE_BOUNCE_FLIP) {
                    direction(360 - dir);
                    if (circle) {
                        ((Circle)node).setCenterY(((Circle)node).getRadius());
                    } else {
                        node.setLayoutY(0);
                        if (edge == EDGE_BOUNCE_FLIP) {
                            forceFlipVertically();
                        }
                    }
                } else if (edge == EDGE_STOP) {
                    if (circle) {
                        ((Circle)node).setCenterY(((Circle)node).getRadius());
                    } else {
                        node.setLayoutY(0);
                    }
                    speed(0);
                } else if (edge == EDGE_REMOVE) {
                    this.remove();
                }
            }

            if (node.getBoundsInParent().getMaxY() > h) {
                if (edge == EDGE_BOUNCE || edge == EDGE_BOUNCE_FLIP) {
                    direction(360 - dir);

                    if (circle) {
                       // System.out.println(h - node.getLayoutBounds().getHeight());
                        ((Circle)node).setCenterY(h - ((Circle)node).getRadius());
                    } else if (text) {
                        int pixels = 1;

                        while (node.getBoundsInParent().getMaxY() > h + pixels) pixels++;

                        node.setLayoutY(h - pixels);

                        if (edge == EDGE_BOUNCE_FLIP) {
                            forceFlipVertically();
                        }
                    } else {
                        node.setLayoutY(h - node.getLayoutBounds().getHeight());

                        if (edge == EDGE_BOUNCE_FLIP) {
                            forceFlipVertically();
                        }
                    }
                } else if (edge == EDGE_STOP) {
                    if (circle) {
                        // System.out.println(h - node.getLayoutBounds().getHeight());
                        ((Circle)node).setCenterY(h - ((Circle)node).getRadius());
                    } else {
                        node.setLayoutY(h - node.getLayoutBounds().getHeight());
                    }
                    speed(0);
                } else if (edge == EDGE_REMOVE) {
                    this.remove();
                }
            }
        }

        if (dr != 0) {
            double r = (node.getRotate() + dr) % 360;
            node.setRotate(r);
        }

        if (node.getOpacity() + da > 1) {
            node.setOpacity(1);
        } else if (node.getOpacity() + da < 0) {
            node.setOpacity(0);
        } else {
            node.setOpacity(node.getOpacity() + da);
        }
    }

    private double getRealAngle(double degrees) {
        double deg = degrees;

        while (deg < 0) deg += 360;

        deg = deg % 360;

        return deg;
    }

    public Itemable onOver(ItemEvent handler) {
        if (waiting > 0) {
            queue.add(()->onOver(handler));
        } else {
            if (handler == null) {
                if (node.getOnMouseExited() == null && node.getOnMouseClicked() == null) {
                    node.setMouseTransparent(true);
                }
                node.setOnMouseEntered(null);
            } else {
                node.setMouseTransparent(false);
                node.setOnMouseEntered(e->{handler.handle(this);});
            }

        }
        return this;
    }

    public Itemable onOut(ItemEvent handler) {
        if (waiting > 0) {
            queue.add(()->onOut(handler));
        } else {
            if (handler == null) {
                if (node.getOnMouseEntered() == null && node.getOnMouseClicked() == null) {
                    node.setMouseTransparent(true);
                }
                node.setOnMouseExited(null);
            } else {
                node.setMouseTransparent(false);
                node.setOnMouseExited(e->{handler.handle(this);});
            }

        }
        return this;
    }

    public Itemable onClick(ItemEvent handler) {
        if (waiting > 0) {
            queue.add(()->onClick(handler));
        } else {
            if (handler == null) {
                if (node.getOnMouseEntered() == null && node.getOnMouseExited() == null) {
                    node.setMouseTransparent(true);
                }
                node.setOnMousePressed(null);
            } else {
                node.setMouseTransparent(false);
                node.setOnMousePressed(e->{handler.handle(this);});
            }

        }
        return this;
    }

    @Override
    public Itemable onEdge(EventHandler<Event> handler) {
        if (waiting > 0) {
            queue.add(()-> onEdge(handler));
        } else {
            edgeHandler = handler;
        }
        return this;
    }

    public Itemable stroke(Color color) {
        if (waiting > 0) {
            queue.add(()->stroke(color));
        } else {

            if (node instanceof Shape) {
                ((Shape) node).setStroke(color);
            }
        }

        return this;
    }

    public Itemable fill(Color color) {
        // System.out.println(fill);
        if (waiting > 0) {
            queue.add(()->fill(color));
        } else {
            if (node instanceof Shape) {
                ((Shape) node).setFill(color);
            }
        }

        return this;
    }

    private void updateDeltas() {
        dx = Math.cos(Math.toRadians(dir)) * speed;
        dy = Math.sin(Math.toRadians(dir)) * speed;
       // System.out.println("dx="+dx+",dy="+dy);
    }

    private double calculateSpeed(double pps) {
        return pps / dojo.fps();
    }

    public Itemable direction(double angle, double pps) {
        if (waiting > 0) {
            queue.add(()->direction(angle, pps));
        } else {
            speed = pps;
            direction(angle);
        }
        return this;
    }

    public Itemable direction(double angle) {
        if (waiting > 0) {
            queue.add(()->direction(angle));
        } else {
            dir = getRealAngle(angle);

            updateDeltas();
        }

        return this;
    }

    @Override
    public Itemable ranDir() {
        direction(Math.random() * 360);
        return this;
    }

    @Override
    public Itemable ranDir(double min_angle, double max_angle) {
        min_angle = getRealAngle(min_angle);
        max_angle = getRealAngle(max_angle);

        double min = Math.min(min_angle,max_angle);
        double max = Math.max(min_angle,max_angle);

        direction(Math.random() * (max - min + 1) + min);

        return this;
    }

    public Itemable speed(double pps) {
        if (waiting > 0) {
            queue.add(()->speed(pps));
        } else {
            speed = calculateSpeed(pps);
            updateDeltas();
        }
        return this;
    }

    @Override
    public Itemable ranSpeed(double min_pps, double max_pps) {
        double min = Math.min(min_pps,max_pps);
        double max = Math.max(min_pps,max_pps);

        speed(Math.random() * (max - min + 1) + min);
        return this;
    }

    public Itemable left(double pps) {
        if (waiting > 0) {
            queue.add(()->left(pps));
        } else {
            this.speed = calculateSpeed(pps);
            direction(180);
        }
        return this;
    }

    public Itemable left() {
        if (waiting > 0) {
            queue.add(()->left());
        } else {
            if (speed <= 0) {
                speed = calculateSpeed(DEFAULT_PPS);
            }
            direction(180);
        }
        return this;
    }

    public Itemable right(double pps) {
        if (waiting > 0) {
            queue.add(()->right(pps));
        } else {
            this.speed = calculateSpeed(pps);
            direction(0);
        }
        return this;
    }

    public Itemable right() {
        if (waiting > 0) {
            queue.add(this::right);
        } else {
            if (speed <= 0) {
                speed = calculateSpeed(DEFAULT_PPS);
            }
            direction(0);
        }
        return this;
    }

    @Override
    public Itemable up(double pps) {
        if (waiting > 0) {
            queue.add(()->up(pps));
        } else {
            this.speed = calculateSpeed(pps);
            direction(270);
        }
        return this;
    }

    @Override
    public Itemable up() {
        if (waiting > 0) {
            queue.add(()->up());
        } else {
            if (speed <= 0) {
                speed = calculateSpeed(DEFAULT_PPS);
            }
            direction(270);
        }
        return this;
    }

    @Override
    public Itemable down(double pps) {
        if (waiting > 0) {
            queue.add(()->down(pps));
        } else {
            this.speed = calculateSpeed(pps);
            direction(90);
        }
        return this;
    }

    @Override
    public Itemable down() {
        if (waiting > 0) {
            queue.add(()->down());
        } else {
            if (speed <= 0) {
                speed = calculateSpeed(DEFAULT_PPS);
            }
            direction(90);
        }
        return this;
    }

    @Override
    public Itemable hide() {
        if (waiting > 0) {
            queue.add(()->hide());
        } else {
            node.setVisible(false);
        }
        return this;
    }

    @Override
    public Itemable show() {
        if (waiting > 0) {
            queue.add(()->show());
        } else {
            node.setVisible(true);
        }
        return this;
    }

    @Override
    public Itemable stop() {
        if (waiting > 0) {
            queue.add(()->stop());
        } else {
            speed(0);
            rotate(0);
        }
        return this;
    }

    @Override
    public Itemable text(String text) {
        if (waiting > 0) {
            queue.add(()->text(text));
        } else {
            if (node instanceof Text) {
                ((Text)node).setText(text);
            } else {
                Output.error("The text() method can only be used on Text");
            }
        }
        return this;
    }

    @Override
    public Itemable text(char ch) {
        if (waiting > 0) {
            queue.add(()->text(ch));
        } else {
            if (node instanceof Text) {
                ((Text)node).setText(ch+"");
            } else {
                Output.error("The text() method can only be used on Text");
            }
        }
        return this;
    }

    @Override
    public Itemable text(int num) {
        if (waiting > 0) {
            queue.add(()->text(num));
        } else {
            if (node instanceof Text) {
                ((Text)node).setText(Integer.toString(num));
            } else {
                Output.error("The text() method can only be used on Text");
            }
        }
        return this;
    }

    @Override
    public Itemable text(double num) {
        if (waiting > 0) {
            queue.add(()->text(num));
        } else {
            if (node instanceof Text) {
                ((Text)node).setText(Double.toString(num));
            } else {
                Output.error("The text() method can only be used on Text");
            }
        }
        return this;
    }

    @Override
    public Itemable hBounce() {
        if (waiting > 0) {
            queue.add(()-> hBounce());
        } else {
            direction(360 - dir);
        }
        return this;
    }

    @Override
    public Itemable vBounce() {
        if (waiting > 0) {
            queue.add(()-> vBounce());
        } else {
            direction(180 - dir);
        }
        return this;
    }


    public Itemable rotate(double degrees) {
        if (waiting > 0) {
            queue.add(()->rotate(degrees));
        } else {
            dr = degrees;
        }
        return this;
    }

    @Override
    public Itemable fadeIn() {
        if (waiting > 0) {
            queue.add(()->fadeIn());
        } else {
            node.setVisible(true);
            node.setOpacity(0);
            da = 1 / (dojo.fps() * DEFAULT_FADE);
            sleep(DEFAULT_FADE);
        }
        return this;
    }

    @Override
    public Itemable fadeOut() {
        if (waiting > 0) {
            queue.add(()->fadeOut());
        } else {
            da = -1 / (dojo.fps() * DEFAULT_FADE);
            sleep(DEFAULT_FADE);
        }

        return this;
    }


    @Override
    public Itemable fadeIn(double seconds) {
        if (waiting > 0) {
            queue.add(()->fadeIn(seconds));
        } else {
            node.setOpacity(0);
            da = 1 / (dojo.fps() * seconds);
            sleep(seconds);
        }
        return this;
    }

    @Override
    public Itemable fadeOut(double seconds) {
        if (waiting > 0) {
            queue.add(()->fadeOut(seconds));
        } else {
            node.setOpacity(1);
            da = -1 / (dojo.fps() * seconds);
            sleep(seconds);
        }

        return this;
    }

    @Override
    public Itemable sleep(double seconds) {
        if (waiting > 0) {
            queue.add(()->sleep(seconds));
        } else {
            waiting = (int) Math.round(seconds * dojo.fps());
        }

        return this;
    }

    private void forceFlipHorizontally() {

        if (node.getScaleX() < 0) {
            node.setScaleX(1);
        } else {
            node.setScaleX(-1);
        }

    }


    @Override
    public Itemable hFlip() {
        if (waiting > 0) {
            queue.add(()-> hFlip());
        } else {
            if (node.getScaleX() < 0) {
                node.setScaleX(1);
            } else {
                node.setScaleX(-1);
            }
        }

        return this;
    }

    private void forceFlipVertically() {

            if (node.getScaleY() < 0) {
                node.setScaleY(1);
            } else {
                node.setScaleY(-1);
            }

    }


    @Override
    public Itemable vFlip() {
        if (waiting > 0) {
            queue.add(()-> vFlip());
        } else {
            forceFlipVertically();
        }

        return this;
    }

    @Override
    public Itemable id(String id) {
        dojo.assignId(this, id);

        return this;
    }

    @Override
    public Itemable angle(double degrees) {
        if (waiting > 0) {
            queue.add(()-> angle(degrees));
        } else {
            node.setRotate(degrees);
        }

        return this;
    }

    @Override
    public Itemable opacity(double factor) {
        if (waiting > 0) {
            queue.add(()-> opacity(factor));
        } else {
            node.setOpacity(factor > 1 ? 1 : factor < 0 ? 0 : factor);
        }

        return this;
    }

    @Override
    public void remove() {
        if (waiting > 0) {
            queue.add(this::remove);
        } else {
            dojo.remove(this);
        }

    }

    Node getNode() {
        return node;
    }


    @Override
    public Itemable edgeBounce() {
        if (waiting > 0) {
            queue.add(()-> edgeBounce());
        } else {
            edge = EDGE_BOUNCE;
        }
        return this;
    }

    @Override
    public Itemable edgeBounceFlip() {
        if (waiting > 0) {
            queue.add(()-> edgeBounceFlip());
        } else {
            edge = EDGE_BOUNCE_FLIP;
        }
        return this;
    }

    @Override
    public Itemable edgeStop() {
        if (waiting > 0) {
            queue.add(()-> edgeStop());
        } else {
            edge = EDGE_STOP;
        }
        return this;
    }

    @Override
    public Itemable edgeRemove() {
        if (waiting > 0) {
            queue.add(()-> edgeRemove());
        } else {
            edge = EDGE_REMOVE;
        }
        return this;
    }

    @Override
    public Itemable edgeIgnore() {
        if (waiting > 0) {
            queue.add(()-> edgeRemove());
        } else {
            edge = EDGE_IGNORE;
        }
        return this;
    }



}