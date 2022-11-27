import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class Entity {

    protected double largeur, hauteur;
    protected double x, y;

    protected double vx, vy;
    protected double ax, ay;

    protected Color color;


    /**
     * Met à jour la position et la vitesse de la méduse
     * inspiré du cours
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {
        vx += dt * ax;
        vy += dt * ay;
        x += dt * vx;
        y += dt * vy;

        // Force à ne pas dépasser les côtés de l'écran
        if (x + largeur > HighSeaTower.largeur || x < 0) {
            vx *= -1;
        }

        x = Math.min(x, HighSeaTower.largeur - largeur);
        x = Math.max(x, 0);
        y = Math.min(y, HighSeaTower.hauteur - hauteur);
    }


    /**
     * dessiner dans l'interface les différentes entités (définie dans les sous-classes)
     * @param context
     * @param fenetreY
     */
    public abstract void draw(GraphicsContext context, double fenetreY);
}
