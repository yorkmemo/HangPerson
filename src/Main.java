import io.*;
import javafx.scene.paint.Color;

public class Main extends App {
    String word = "Mustangs";

    @Override
    public void start() {

        Win.open("Hang Man", 1000,700);

        Win.font(40);

        Win.addText("_", 100, 50).id("M");
        Win.addText("_", 130, 50).id("u");
        Win.addText("_", 160, 50).id("s");

        Win.onLetterKey(ch->{

            if (ch == 'M') {
                Win.id("M").text("M");
            }

        });


      //  Sound.tts("Welcome, " + User.name + " you are using " + Sys.name);
    }


    //tobe injected at runtime
    public static void main(String[] args) {
        launch();
    }


}
