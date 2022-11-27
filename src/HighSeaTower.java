import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/*
Milosh Devic (20158232) et Théodore Jordan (20147067)
*/



public class HighSeaTower extends Application {

    public static final int largeur = 350, hauteur = 480;
    public static boolean hasStarted = false;
    public static boolean debugMode = false;

    /**
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = new Pane();
        Scene scene = new Scene(root, largeur, hauteur);

        Canvas canvas = new Canvas(largeur, hauteur);
        root.getChildren().add(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        Controleur controleur = new Controleur();

        scene.setOnKeyPressed((value) -> {
            if(!hasStarted)
                controleur.debut();

            hasStarted = true;

            if (value.getCode() == KeyCode.SPACE || value.getCode() == KeyCode.UP) {
                controleur.jump();
            } else if (value.getCode() == KeyCode.LEFT) {
                controleur.deplacerGauche();
            } else if (value.getCode() == KeyCode.RIGHT) {
                controleur.deplacerDroite();
            } else if (value.getCode() == KeyCode.ESCAPE) {
                Platform.exit();//quitter le jeu
            } else if (value.getCode() == KeyCode.T){
                debugMode = !debugMode;
            }
        });

        scene.setOnKeyReleased((value) -> {
            if(value.getCode() == KeyCode.LEFT || value.getCode() == KeyCode.RIGHT){
                controleur.stop();
            }
        });

        //basé sur le code de l'exemple
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;


                controleur.update(deltaTime);
                controleur.fin();//regarde si la méduse est "englouti" par l'océan
                controleur.draw(context);

                lastTime = now;
            }
        };
        timer.start();



        primaryStage.setTitle("High Sea Tower");
        primaryStage.getIcons().add(new Image("file:jellyfish1.png"));
        primaryStage.setResizable(false);//fenêtre pas "resizable"
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}