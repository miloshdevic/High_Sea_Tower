import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Phytoplankton extends Entity {

    private Image[] images;
    private Image image;
    private boolean eaten = false;

    public Phytoplankton(double x, double y) {
        this.x = x;
        this.y = y;

        this.largeur = 25;
        this.hauteur = 20;

        images = new Image[] {
                new Image("phytoplankton1.png"),
                new Image("phytoplankton2.png"),
                new Image("phytoplankton3.png")
        };
        double choisirImage = Math.random()*3;//entre 0 et 2
        image = images[(int)choisirImage];
    }

    /**
     * dessiner les phytoplanktons
     * @param context
     * @param fenetreY
     */
    @Override
    public void draw(GraphicsContext context, double fenetreY) {
        double yAffiche = this.y - fenetreY;

        if (!this.eaten) {
            context.drawImage(image, x, yAffiche, this.largeur, this.hauteur);
        }
    }

    /**
     * getter pour "eaten"
     * @return
     */
    public boolean isEaten() {
        return eaten;
    }

    /**
     * setter pour "eaten"
     * @param eaten
     */
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }
}
