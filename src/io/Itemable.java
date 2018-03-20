package io;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

public interface Itemable {
    //todo: return Itemable so commands can be chained

    Itemable stroke(Color color);
    Itemable fill(Color color);
    Itemable onOver(ItemEvent handler);
    Itemable onOut(ItemEvent handler);
    Itemable onClick(ItemEvent handler);
    Itemable onEdge(EventHandler<Event> handler);
    Itemable direction(double angle, double pps);
    Itemable direction(double angle);
    Itemable ranDir();
    Itemable ranDir(double min_angle, double max_angle);
    Itemable speed(double pps);
    Itemable ranSpeed(double min_pps, double max_pps);
    Itemable left(double pps);
    Itemable left();
    Itemable right(double pps);
    Itemable right();
    Itemable up(double pps);
    Itemable up();
    Itemable down(double pps);
    Itemable down();
    Itemable hide();
    Itemable show();
    Itemable stop();
    Itemable text(String text);
    Itemable text(char ch);
    Itemable text(int num);
    Itemable text(double num);
    Itemable hBounce();
    Itemable vBounce();
    Itemable rotate(double degrees);
    Itemable angle(double degrees);
    Itemable opacity(double factor);
    Itemable fadeIn();
    Itemable fadeOut();
    Itemable fadeIn(double seconds);
    Itemable fadeOut(double seconds);
    Itemable sleep(double seconds);
    Itemable hFlip();
    Itemable vFlip();
    Itemable id(String name);

    void remove();

    Itemable edgeBounce();
    Itemable edgeBounceFlip();
    Itemable edgeStop();
    Itemable edgeRemove();
    Itemable edgeIgnore();



}
