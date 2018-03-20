package io;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class DojoPane {
    private static final String DEFAULT_IMAGE_FILE = "io/blank.png";

    private static final double DEFAULT_FONT_SIZE = 18;
    private static final String DEFAULT_FONT_NAME = "Arial";
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Color DEFAULT_FILL = Color.BLACK;
    private static final Color DEFAULT_STROKE = Color.BLACK;
    private static final double DEFAULT_STROKE_WIDTH = 0.5;

    private static final int DEFAULT_FPS = 24;

    private AnchorPane anchorPane;
    private StackPane stackPane;
    private BorderPane borderPane;

    private Timeline timeline;
    private Pane parentPane;

    private double width;
    private double height;

    private int fps = DEFAULT_FPS;

    private String fontName = DEFAULT_FONT_NAME;
    private double fontSize = DEFAULT_FONT_SIZE;
    private Color fontColor = DEFAULT_TEXT_COLOR;

    private Color fill = DEFAULT_FILL;
    private Color stroke = DEFAULT_STROKE;
    private double strokeWidth = DEFAULT_STROKE_WIDTH;

    private DefaultKeyEvent leftKeyHandler;
    private DefaultKeyEvent rightKeyHandler;
    private DefaultKeyEvent downKeyHandler;
    private DefaultKeyEvent upKeyHandler;
    private DefaultKeyEvent spaceKeyHandler;
    private CharKeyEvent letterKeyHandler;
    private NumericKeyEvent numericKeyHandler;
    private DefaultKeyEvent escapeKeyHandler;
    private DefaultKeyEvent enterKeyHandler;

    private Map<String, Item> idMap = new HashMap<String, Item>();
    private ItemGroup circles = new ItemGroup("javafx.scene.shape.Circle");
    private ItemGroup rectangles = new ItemGroup("javafx.scene.shape.Rectangle");
    private ItemGroup texts = new ItemGroup("javafx.scene.text.Text");
    private ItemGroup images = new ItemGroup("javafx.scene.image.ImageView");
    private ItemGroup all = new ItemGroup();

    public DojoPane(Pane parentPane, double width, double height) {
        this.parentPane = parentPane;
        this.width = width;
        this.height = height;

        anchorPane = new AnchorPane();
        anchorPane.setUserData(this);

        if (parentPane instanceof BorderPane) {
            ((BorderPane)parentPane).setCenter(anchorPane);

        } else {
            parentPane.getChildren().add(anchorPane);
        }

        anchorPane.setMinSize(width, height);
        anchorPane.setMaxSize(width, height);
        anchorPane.setPrefSize(width, height);

        anchorPane.setClip(new Rectangle(0,0, width, height));

        System.out.println(width);

        double duration = Math.round(1000 / fps);

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(new Duration(duration), e -> tick()));
        timeline.play();

    }



    private Item registerNode(Node node) {
        anchorPane.getChildren().add(node);

        Item item = new Item(node);

        // System.out.println("Registering .... " + node.getClass().getName());

        if (node instanceof Circle) {
            circles.add(item);
        } else if (node instanceof Rectangle) {
            rectangles.add(item);
        } else if (node instanceof Text) {
            texts.add(item);
        } else if (node instanceof ImageView) {
            images.add(item);
        }

        all.add(item);

        return item;
    }

    void assignId(Item item, String id) {
        if (id != null) {
            if (idMap.containsKey(id)) {
                return;
            }
            item.getNode().setId(id);
            idMap.put(id, item);
        }
    }

    private boolean remove(String id) {

        if (idMap.containsKey(id)) {
            anchorPane.getChildren().remove(idMap.get(id).getNode());
            idMap.remove(id);
            return true;
        }

        return false;
    }

    boolean remove(Item item) {
        if (item.id() != null) return remove(item.id());

        return anchorPane.getChildren().remove(item.getNode());
    }


    public double width() {
        return width;
        // return ((Rectangle2D)anchorPane.getUserData()).getWidth();
    }

    public double height() {
        return height;
        //return ((Rectangle2D)anchorPane.getUserData()).getHeight();
    }

    public int fps() {
        return fps;
    }

    public void fps(int fps) {
        this.fps = fps;
    }

    private void tick() {
        for (int i = 0; i < anchorPane.getChildren().size(); i++) {
            Node node = anchorPane.getChildren().get(i);

            if (node.getUserData() != null) {
                Item item = (Item)node.getUserData();
                item.tick();
            }
        }

    }

    public void keyPressed(KeyEvent e) {

        if (leftKeyHandler != null && e.getCode() == KeyCode.LEFT) {
            //  System.out.println(e.getCode());
            leftKeyHandler.handle(null);
        } else if (rightKeyHandler != null && e.getCode() == KeyCode.RIGHT) {
            //    System.out.println(e.getCode());
            rightKeyHandler.handle(null);
        } else if (downKeyHandler != null && e.getCode() == KeyCode.DOWN) {
            //    System.out.println(e.getCode());
            downKeyHandler.handle(null);
        } else if (upKeyHandler != null && e.getCode() == KeyCode.UP) {
            //    System.out.println(e.getCode());
            upKeyHandler.handle(null);
        } else if (upKeyHandler != null && e.getCode() == KeyCode.ESCAPE) {
            //    System.out.println(e.getCode());
            escapeKeyHandler.handle(null);
        } else if (upKeyHandler != null && e.getCode() == KeyCode.ENTER) {
            //    System.out.println(e.getCode());
            enterKeyHandler.handle(null);
        } else if (spaceKeyHandler != null && e.getCode() == KeyCode.SPACE) {
            //    System.out.println(e.getCode());
            spaceKeyHandler.handle(null);
        } else if (letterKeyHandler != null && e.getCode().isLetterKey()) {
            //    System.out.println(e.getCode());
            letterKeyHandler.handle(e.getText().toUpperCase().charAt(0));
        } else if (numericKeyHandler != null && e.getCode().isDigitKey()) {
            //    System.out.println(e.getCode());
            numericKeyHandler.handle(Integer.parseInt(e.getText()));
        }

    }

    public void fill(Color color) {
        fill = color;
    }

    public void stroke(Color color) {
        stroke = color;
    }

    public void stroke(Color color, double width) {
        stroke = color;
        strokeWidth = width;
    }

    public void stroke(double width) {
        strokeWidth = width;
    }


    public void font(String name, double size, Color color) {
        fontName = name;
        fontSize = size;
        fontColor = color;
    }

    public void font(double size, Color color) {
        fontSize = size;
        fontColor = color;
    }

    public void font(String name, Color color) {
        fontName = name;
        fontColor = color;
    }


    public void font(Color color) {
        fontColor = color;
    }

    public void font(String name, double size) {
        fontName = name;
        fontSize = size;
    }

    public void font(double size) {
        fontSize = size;
    }

    public void font(String name) {
        fontName = name;
    }

    public Itemable addText(String text) {
        return addText(text, null, null);
    }

    public Itemable addText(String text, double topY) {
        return addText(text, null, topY);
    }

    private Itemable addText(String text, Double leftX, Double topY) {
        Text displayText = new Text(text);
        displayText.setFont(Font.font(fontName, fontSize));

        double x, y;

        if (leftX == null) {
            x = width() / 2.0 - displayText.getLayoutBounds().getWidth() / 2.0;
        } else {
            x = leftX;
        }

        if (topY == null) {
            y = height() / 2.0 - displayText.getLayoutBounds().getHeight() / 2.0;
        } else {
            y = topY;
        }

        displayText.setLayoutX(x);
        displayText.setLayoutY(y);

        displayText.setFont(Font.font(fontName, fontSize));
        displayText.setFill(fontColor);

        return registerNode(displayText);

    }

    public Itemable addText(String text, double leftX, double topY) {
        return addText(text,Double.valueOf(leftX),Double.valueOf(topY));
    }


    public Itemable addCircle(double radius) {
        Circle circle = new Circle(width()/2.0, height()/2.0, radius, fill);
        circle.setStroke(stroke);
        circle.setStrokeWidth(strokeWidth);

        return registerNode(circle);
    }


    public Itemable addCircle(double centerX, double centerY, double radius) {
        Circle circle = new Circle(centerX, centerY, radius, fill);
        circle.setStroke(stroke);
        circle.setStrokeWidth(strokeWidth);

        return registerNode(circle);
    }


    public Itemable addRectangle(double leftX, double topY, double width, double height) {
        Rectangle rectangle = new Rectangle(leftX, topY, width, height);
        rectangle.setFill(fill);
        rectangle.setStroke(stroke);
        rectangle.setStrokeWidth(strokeWidth);

        return registerNode(rectangle);
    }

    public Itemable addImage(String filename) {
        return addImage(filename,0,0);
    }

    public Itemable addImage(String filename, double leftX, double topY) {

        ImageView imageView = null;
        try {
            imageView = new ImageView(new Image(filename));
        } catch (IllegalArgumentException e) {
            Output.error("Image file not found: " + filename);
            imageView = new ImageView(DEFAULT_IMAGE_FILE);
        }
        imageView.setLayoutX(leftX);
        imageView.setLayoutY(topY);

        return registerNode(imageView);
    }

    public Itemable addImage(String filename, double leftX, double topY, double width) {
        ImageView imageView = null;
        try {
            imageView = new ImageView(new Image(filename));
        } catch (IllegalArgumentException e) {
            Output.error("Image file not found: " + filename);
            imageView = new ImageView(DEFAULT_IMAGE_FILE);
        }

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setLayoutX(leftX);
        imageView.setLayoutY(topY);

        return registerNode(imageView);
    }

    public Itemable addImage(String filename, double leftX, double topY, double width, double height) {
        ImageView imageView = null;
        try {
            imageView = new ImageView(new Image(filename));
        } catch (IllegalArgumentException e) {
            Output.error("Image file not found: " + filename);
            imageView = new ImageView(DEFAULT_IMAGE_FILE);
        }

        imageView.setPreserveRatio(false);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(leftX);
        imageView.setLayoutY(topY);

        return registerNode(imageView);
    }

    public Itemable id(String id) {
        if (!idMap.containsKey(id)) {
            Output.error("Identifier not found: " + id);
            return new NullItem();
        }

        return idMap.get(id);
    }

    public Itemable all() {
        return all;
    }

    public Itemable circles() {
        return circles;
    }

    public Itemable rectangles() {
        System.out.println(rectangles.size());
        return rectangles;
    }

    public Itemable images() {
        return images;
    }

    public Itemable texts() {
        return texts;
    }

    public void clear() {
        while (anchorPane.getChildren().size() > 0) {
            Node node = anchorPane.getChildren().get(anchorPane.getChildren().size()-1);
            Item item = (Item)node.getUserData();

            remove(item);
        }
    }

    public void onLeftKey(DefaultKeyEvent handler) {
        leftKeyHandler = handler;
    }

    public void onRightKey(DefaultKeyEvent handler) {
        rightKeyHandler = handler;
    }

    public void onDownKey(DefaultKeyEvent handler) {
        downKeyHandler = handler;
    }

    public void onUpKey(DefaultKeyEvent handler) {
        upKeyHandler = handler;
    }

    public void onSpaceKey(DefaultKeyEvent handler) {
        spaceKeyHandler = handler;
    }

    public void onLetterKey(CharKeyEvent handler) {
        letterKeyHandler = handler;
    }

    public void onEscapeKey(DefaultKeyEvent handler) {
        escapeKeyHandler = handler;
    }

    public void onEnterKey(DefaultKeyEvent handler) {
        enterKeyHandler = handler;
    }

    public void onNumericKey(NumericKeyEvent handler) {
        numericKeyHandler = handler;
    }

    public void background(Color color) {
        bg(color);
    }

    public void bg(Color color) {
        anchorPane.setBackground(new Background(new BackgroundFill(color,null,null)));
    }



}
