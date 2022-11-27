import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Decor extends Entity {

    public Decor(double x, double y) {
        this.x = x;
        this.y = y;
        this.largeur = 350;
        this.hauteur = 30;
        this.color = Color.rgb(234, 206, 106);
    }

    /**
     * dessiner le décor
     * @param context
     */
    @Override
    public void draw(GraphicsContext context, double fenetreY) {
        double yAffiche = this.y - fenetreY;

        context.setFill(color);
        context.fillRect(x, yAffiche, largeur, hauteur);

        //pour donner de la forme au sable
        for (int i = 0; i < 10; i++) {
            context.setFill(color);
            context.fillOval(x + i*40, yAffiche-10, 70, hauteur);
        }

        //ajouter des cailloux/coquillages
        context.setFill(Color.GRAY);
        context.fillOval(x + 20, yAffiche-5, 5, 5);

        context.setFill(Color.DARKGRAY);
        context.fillOval(x + 80, yAffiche-5, 15, 10);

        context.setFill(Color.DIMGRAY);
        context.fillOval(x + 300, yAffiche, 30, 20);

        context.setFill(Color.DEEPPINK);
        context.fillOval(x + 100, yAffiche-5, 5, 5);

        context.setFill(Color.DARKRED);
        context.fillOval(x + 30, yAffiche, 10, 5);

        context.setFill(Color.INDIANRED);
        context.fillOval(x + 200, yAffiche, 15, 10);

        context.setFill(Color.GRAY);
        context.fillOval(x + 250, yAffiche, 10, 5);

        context.setFill(Color.DARKRED);
        context.fillOval(x + 280, yAffiche-5, 15, 10);

        context.setFill(Color.DARKSEAGREEN);
        context.fillOval(x + 50, yAffiche-3, 10, 5);

        //ajouter des algues
        for (int i = 0; i < 5; i++) {
            context.setFill(Color.DARKGREEN);
            context.fillRect(i*60 + 60, yAffiche-100, 15, 100);

            context.setFill(Color.DARKBLUE);
            context.fillOval(i*60 + 58, yAffiche-110, 6, 30);
            context.fillOval(i*60 + 58, yAffiche-85, 6, 30);
            context.fillOval(i*60 + 59, yAffiche-60, 5, 30);
            context.fillOval(i*60 + 58, yAffiche-45, 6, 30);
            context.fillOval(i*60 + 58, yAffiche-40, 4, 30);

            context.fillOval(i*60 +71, yAffiche-110, 6, 30);
            context.fillOval(i*60 +70, yAffiche-90, 6, 30);
            context.fillOval(i*60 +71, yAffiche-65, 5, 30);
            context.fillOval(i*60 +71, yAffiche-50, 6, 30);
            context.fillOval(i*60 +73, yAffiche-40, 4, 30);
        }
    }
}
