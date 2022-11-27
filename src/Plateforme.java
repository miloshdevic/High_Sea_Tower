import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Plateforme extends Entity {

    //constructeur
    public Plateforme(double x, double y) {
        this.x = x;
        this.y = y;

        if (x >= 0 && x < 175)
            this.largeur = 80 + Math.random()*96;//entre 80px et 175px
        else
            this.largeur = 80 + Math.random()*(271 - x);//entre 80px et 175px de largeur


        this.hauteur = 10;

        int choisirCouleur = (int)(Math.random() * 101);//choisit la couleur aléatoirement

        if (choisirCouleur <= 65) //donc [0, 65]
            this.color = Color.rgb(230, 134, 58);//plateforme simple

        else if (choisirCouleur <= 85) //donc ]65, 85]
            this.color = Color.LIGHTGREEN;//plateforme rebondissante

        else if (choisirCouleur <= 95) //donc ]85, 95]
            this.color = Color.rgb(230, 221, 58);//plateforme accélérante

        else //donc ]95, 100]
            this.color = Color.rgb(184, 15, 36);//plateforme solide

    }


    /**
     * dessiner la plateforme
     * @param context
     */
    @Override
    public void draw(GraphicsContext context, double fenetreY) {
        double yAffiche = this.y - fenetreY;

        context.setFill(color);
        context.fillRect(x, yAffiche, largeur, hauteur);
    }
}