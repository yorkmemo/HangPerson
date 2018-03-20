package io;

import javafx.application.Application;
import javafx.stage.Stage;

public abstract class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        start();
    }


    public void start() {
        Output.error("App is missing a start() method");
    }
}
