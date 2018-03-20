import io.*;
import javafx.scene.paint.Color;

public class Main extends App {


    @Override
    public void start() {

        Win.open("Hang Man", 1000,700);

        Win.onLetterKey(ch->{


        });


      //  Sound.tts("Welcome, " + User.name + " you are using " + Sys.name);
    }


    //tobe injected at runtime
    public static void main(String[] args) {
        launch();
    }


}
