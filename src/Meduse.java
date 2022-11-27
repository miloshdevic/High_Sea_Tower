import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Meduse extends Entity {

    private Image[] framesDroite;
    private Image[] framesGauche;
    private Image image;
    private double frameRate = 8; // 8 frames par seconde
    private double tempsTotal = 0;

    private boolean parterre;
    private boolean mange;


    public Meduse(double x, double y) {
        this.x = x;
        this.y = y;
        this.largeur = 50;
        this.hauteur = 50;
        this.ay = 1200;//gravité
        this.vx = 0;//vitesse initiale

        //lorsque la méduse se déplace vers la droite
        framesDroite = new Image[] {
                new Image("jellyfish1.png"),
                new Image("jellyfish2.png"),
                new Image("jellyfish3.png"),
                new Image("jellyfish4.png"),
                new Image("jellyfish5.png"),
                new Image("jellyfish6.png")
        };

        //lorsque la méduse se déplace vers la gauche
        framesGauche = new Image[] {
                new Image("jellyfish1g.png"),
                new Image("jellyfish2g.png"),
                new Image("jellyfish3g.png"),
                new Image("jellyfish4g.png"),
                new Image("jellyfish5g.png"),
                new Image("jellyfish6g.png")
        };

        image = new Image("file:jellyfish1.png");//image au point de départ
    }


    /**
     * fais une mise à jour pour la méduse et ses attributs
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        // Physique du personnage
        super.update(dt);

        // Mise à jour de l'image affichée
        this.tempsTotal += dt;

        this.vx += dt*ax;//change la vitesse

        int frame = (int) (this.tempsTotal * this.frameRate);

        if (vx >= 0)
            this.image = this.framesDroite[frame % this.framesDroite.length];
        else
            this.image = this.framesGauche[frame % this.framesGauche.length];
    }

    /**
     * dessiner la méduse
     * @param context
     * @param fenetreY
     */
    @Override
    public void draw(GraphicsContext context, double fenetreY) {
        double yAffiche = this.y - fenetreY;

        if(HighSeaTower.debugMode){
            context.setFill(Color.rgb(255,0,0, 0.5));
            context.fillRect(x, yAffiche, this.largeur, this.hauteur);
        } else if (this.mange) {
            context.setFill(Color.YELLOW);
            context.fillRect(x, yAffiche, this.largeur, this.hauteur);
        }

        context.drawImage(image, x, yAffiche, this.largeur, this.hauteur);
    }


    /**
     * test pour voir s'il y a une collision avec une plateforme
     * @param other
     */
    public void testCollision(Plateforme other) {
        /**
         * La collision avec une plateforme a lieu seulement si :
         *
         * - Il y a une intersection entre la plateforme et le personnage
         *
         * - La collision a lieu entre la plateforme et le *bas du personnage*
         * seulement
         *
         * - La vitesse va vers le bas (le personnage est en train de tomber,
         * pas en train de sauter)
         */

        Color green = Color.LIGHTGREEN;
        Color red = Color.rgb(184, 15, 36);
        Color orange = Color.rgb(230, 134, 58);
        Color yellow = Color.rgb(230, 221, 58);


        if (intersects(other) && Math.abs(this.y + this.hauteur - other.y) < 10
                && this.vy > 0 && other.color.equals(orange)) { //plateforme orange
            pushOut(other);
            this.vy = 0;
            this.parterre = true;

        } else if (intersects(other) && other.color.equals(green) &&
                Math.abs(this.y + this.hauteur - other.y) < 10  && this.vy > 0) { //plateforme verte

            //rebond d'au moins de 100px/s vers le haut
            if (this.vy < 100)
                this.vy = 100;

            this.vy *= -1.5;//augmente la vitesse
            this.parterre = true;

        } else if (intersects(other) && other.color.equals(yellow) &&
                Math.abs(this.y + this.hauteur - other.y) < 10  && this.vy > 0) {
            pushOut(other);
            this.vy = 0;
            this.parterre = true;

        } else if ((intersects(other) && other.color.equals(red))) { //plateforme rouge
            //arrive du haut
            if (Math.abs(this.y + this.hauteur - other.y) < 10  && this.vy > 0) {
                pushOut(other);
                this.vy = 0;
                this.parterre = true;
            } else if (Math.abs(other.y - this.y) < 10 && this.vy < 0){ //arrive du bas
                pushDown(other);
                this.vy = 0;
                this.parterre = false;
            }
        }

    }

    /**
     * Repousse la méduse vers le haut (sans déplacer la
     * plateforme)
     */
    public void pushOut(Plateforme other) {
        double deltaY = this.y + this.hauteur - other.y;
        this.y -= deltaY;
    }

    /**
     * pareil que "pushOut" mais vers le bas
     * @param other
     */
    public void pushDown(Plateforme other) {
        double deltaY = other.hauteur + other.y - this.y;
        this.y += deltaY;
    }


    /**
     * vérifie s'il y a une intersection avec les plateformes
     * @param other
     * @return
     */
    public boolean intersects(Plateforme other) {
        return !(this.x + this.largeur < other.x || other.x + other.largeur < this.x
                || this.y + this.hauteur < other.y
                || other.y + other.hauteur < this.y);
    }


    /**
     * si c'est une plateforme jaune alors la vitesse de défilement accélère
     * seulement le temps que la méduse est sur cette plateforme,
     * dès qu'elle la quitte, la vitesse redevient comme avant
     * @return
     */
    public void acceleration(Plateforme plateforme, double tempsTotal) {
        Color yellow = Color.rgb(230, 221, 58);

        if (intersects(plateforme) && plateforme.color.equals(yellow) && this.parterre) { //si c'est sur une jaune
            Jeu.defilerV = 3*Jeu.defilerV + tempsTotal*Jeu.defilerA;

        }
        //si ce n'est pas sur une jaune et pas en position de départ
        else if ((this.y + this.hauteur < plateforme.y) &&
                (this.x != (double)Jeu.largeur /2 - 25) && (this.y != Jeu.hauteur-50)) {
            Jeu.defilerV = -50 + tempsTotal*Jeu.defilerA;
        }
    }


    /**
     * La méduse peut seulement sauter si elle se trouve sur une
     * plateforme (sauf si elle mange un phytoplankton)
     */
    public void jump() {
        if (parterre) {
            this.vy = -600;
        }
    }


    /**
     * déplace la méduse vers la gauche
     */
    public void deplacerGauche() {
        this.vx = 0;
        this.ax = -1200;
    }


    /**
     * déplace la méduse vers la droite
     */
    public void deplacerDroite() {
        this.vx = 0;
        this.ax = 1200;
    }


    /**
     * réinitialise la vitesse de la méduse à 0
     * @return
     */
    public void stop() {
        this.vx = 0;
        this.ax = 0;
    }


    /**
     * test s'il y a une collision avec un phytoplankton
     * @param other
     */
    public void testCollisionPlankton (Phytoplankton other) {
        if (intersects(other)) {
            other.setEaten(true);
            this.mange = true;
        }
    }


    /**
     * vérifie s'il y a une intersection avec les phytoplanktons
     * @param other
     * @return
     */
    public boolean intersects(Phytoplankton other) {
        return !(this.x + this.largeur < other.x || other.x + other.largeur < this.x
                || this.y + this.hauteur < other.y
                || other.y + other.hauteur < this.y);
    }



    //les getters et setters:

    /**
     * setter pour "parterre"
     * @param parterre
     */
    public void setParterre(boolean parterre) {
        this.parterre = parterre;
    }

    /**
     * getter pour "parterre"
     * @return
     */
    public boolean isParterre() {
        return parterre;
    }

    /**
     * getter pour "mange"
     * @return
     */
    public boolean isMange() {
        return mange;
    }

    /**
     * setter pour "mange"
     * @param mange
     */
    public void setMange(boolean mange) {
        this.mange = mange;
    }
}