import javafx.scene.canvas.GraphicsContext;


public class Controleur {

    Jeu jeu;

    public Controleur() {
        jeu = new Jeu();
    }

    void draw(GraphicsContext context) {
        jeu.draw(context);
    }

    void update(double deltaTime) {
        jeu.update(deltaTime);
    }

    void jump() {
        jeu.jump();
    }

    void deplacerGauche() {
        jeu.deplacerGauche();
    }

    void deplacerDroite() {
        jeu.deplacerDroite();
    }

    void stop() {
        jeu.stop();
    }

    void debut() {
        jeu.debut();
    }

    void fin() {
        if (jeu.fin()) {
            jeu = new Jeu();
            HighSeaTower.hasStarted = false;
        }
    }
}