import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;


public class Jeu {

    public static final int largeur = 350, hauteur = 480;
    public double tempsTotal = 0;
    public static double defilerV;//vitesse de défilement (static pour avoir accès dans une autre classe)
    public static double defilerA;//accélération du défilement (pareil)
    private double fenetreY;//dans l'exemple
    private double boost = 3000;//ceompteur pour la durée du boost dans le jeu

    //les entités
    private ArrayList<Plateforme> plateformes = new ArrayList<Plateforme>();
    private Meduse meduse;
    private Bulles[][] bulles;
    private ArrayList<Phytoplankton> phytoplanktons = new ArrayList<Phytoplankton>();
    private Decor decor;



    //lorsque instancié, création d'un nouveau jeu (nouvelle partie)
    public Jeu() {
        defilerV = 0;
        defilerA = 0;

        for (int i = 0; i < 10000; i++) {
            this.plateformes.add(new Plateforme(Math.random()*271, hauteur-(i+1)*100));
            //pour ne pas avoir 2 plateformes rouges de suite
            if (i > 0 && this.plateformes.get(i-1).color.equals(Color.rgb(184, 15, 36))) {
                double newColor = Math.random()*101;
                if (newColor <= 65)
                    this.plateformes.get(i).color = Color.rgb(230, 134, 58);
                else if (newColor <= 90)
                    this.plateformes.get(i).color = Color.LIGHTGREEN;
                else
                    this.plateformes.get(i).color = Color.rgb(230, 221, 58);
            } else if (i == 0) {
                this.plateformes.get(i).color = Color.rgb(230, 134, 58);
            }
        }

        for (int i = 1; i < 10; i++) {
            this.phytoplanktons.add(new Phytoplankton(Math.random()*(largeur-20+1),
                    hauteur-i*(1000 + Math.random()*3001)));
        }

        //la mettre en bas au centre
        this.meduse = new Meduse((double)largeur/2 - 25,hauteur-50);

        this.bulles = new Bulles[3][5];

        decor = new Decor(0,hauteur-30);
    }

    /**
     * mise à jour des entités du jeu
     * @param dt
     */
    public void update(double dt) {
        double accelerationDecalage = (-meduse.y) - (-fenetreY) + 430;

        if(accelerationDecalage/430 > 0.75)
            Jeu.defilerV = (accelerationDecalage/430) * 5 * Jeu.defilerV + tempsTotal*Jeu.defilerA;
        else
            defilerV = HighSeaTower.debugMode ? 0 : defilerV + dt* defilerA;

        this.fenetreY += dt* defilerV;
        this.tempsTotal += dt;

        this.decor.update(dt);

        //si elle en mange un, elle a un boost pour 3 secondes
        if (this.meduse.isMange()) {
            boost -= dt*1000;
            if (boost > 0) {
                this.meduse.setParterre(true);
            } else {
                this.meduse.setMange(false);
                boost = 3000;
            }
        } else {
            this.meduse.setParterre(false);
        }

        //À chaque tour, on recalcule si la méduse se trouve parterre ou pas
        //(inspiré de l'exemple du cours)
        for (Plateforme p : this.plateformes) {
            p.update(dt);
            this.meduse.testCollision(p);//si elle se trouve sur une plateforme
            this.meduse.acceleration(p, this.tempsTotal);//si elle est sur une plateforme jaune
        }

        //même chose mais cette fois si elle a mangé le phytoplankton
        for (Phytoplankton ph : this.phytoplanktons) {
            ph.update(dt);
            this.meduse.testCollisionPlankton(ph);//si elle touche un phytoplankton
        }

        if(!HighSeaTower.hasStarted)
            this.tempsTotal = 0;

        if (this.phytoplanktons.get(0).y > this.fenetreY + hauteur + 50)
            this.phytoplanktons.remove(0);

        //enlever les plateformes passées
        if (this.plateformes.get(0).y > this.fenetreY + hauteur + 50)//+50 juste pour être sûr que cela dépasse
            this.plateformes.remove(0);

        this.meduse.update(dt);

        //toutes les 3 secondes
        if (((int) (this.tempsTotal%3)) == 0) {
            for (int i = 0; i < 3; i++) {
                double baseX = Math.random()*largeur;

                for (int j = 0; j < 5; j++) {
                    double aleatoire = Math.random()*3;//entre 0 et 2
                    double bulleX;


                    //choisir aléatoirement baseX +/- 20 (1 chance sur 2)
                    if (aleatoire <= 1) {
                        bulleX = baseX + 20;
                    } else {
                        bulleX = baseX - 20;
                    }

                    //pour rester dans les limites
                    if (bulleX < largeur)
                        bulleX = baseX + 20;
                    else if (bulleX + 25 > largeur)
                        bulleX = baseX - 20;


                    this.bulles[i][j] = new Bulles(bulleX, this.fenetreY + hauteur + 50);
                    this.bulles[i][j].update(dt);
                }
            }
        }

        //update chaque bulle
        for (Bulles[] b: this.bulles) {
            for (Bulles bulle: b) {
                bulle.update(dt);
            }
        }
    }

    /**
     * dessiner la surface visible de l'interface
     * @param context
     */
    public void draw(GraphicsContext context) {
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, largeur, hauteur);

        //dessiner bulles
        for (Bulles[] b: this.bulles) {
            for (Bulles bulle: b) {
                bulle.draw(context, this.fenetreY);
            }
        }

        this.decor.draw(context, this.fenetreY);

        //dessiner phytoplanktons
        for (Phytoplankton ph : this.phytoplanktons) {
            ph.draw(context, this.fenetreY);
        }

        //dessiner plateformes
        for (Plateforme p : this.plateformes) {
            if (HighSeaTower.debugMode && meduse.intersects(p) && meduse.isParterre()) {
                context.setFill(Color.YELLOW);
                context.fillRect(p.x, p.y - fenetreY, p.largeur, p.hauteur);//ca le met au mauvais endroit
            } else {
                p.draw(context, this.fenetreY);
            }
        }

        //afficher les infos lorsqu'en mode debug
        if (HighSeaTower.debugMode) {
            if (meduse.isParterre()) {
                String infos = "Position = (" + (int)meduse.x + "," + (int)Math.abs(meduse.y - fenetreY) + ")\n" +
                        "v = (" + (int)meduse.vx +"," + (int)meduse.vy + ")\n" +
                        "a = (" + (int)meduse.ax + "," + (int)meduse.ay + ")\n" +
                        "Touche le sol: oui";
                context.setFill(Color.WHITE);
                context.setFont(Font.font("Verdana", 15));
                context.fillText(infos, 10, 15);
            } else {
                String infos = "Position = (" + (int)meduse.x + "," + (int)Math.abs(meduse.y - fenetreY) + ")\n" +
                        "v = (" + (int)meduse.vx + "," + (int)meduse.vy + ")\n" +
                        "a = (" + (int)meduse.ax + "," + (int)meduse.ay + ")\n" +
                        "Touche le sol: non";
                context.setFill(Color.WHITE);
                context.setFont(Font.font("Verdana", 15));
                context.fillText(infos, 10, 15);
            }
        }

        this.meduse.draw(context, this.fenetreY);

        String score = "Score : " + Math.round((-meduse.y) + 430) + "m";
        context.setFill(Color.WHITE);
        context.setFont(Font.font("Verdana", 20));
        context.fillText(score, largeur/2 - score.length() -50, 30);
    }

    /**
     * permet de faire sauter la méduse
     */
    public void jump() {
        this.meduse.jump();
    }

    /**
     * permet de la déplacer à gauche
     */
    public void deplacerGauche() {
        this.meduse.deplacerGauche();
    }

    /**
     * permet de la déplacer à droite
     */
    public void deplacerDroite() {
        this.meduse.deplacerDroite();
    }

    /**
     * réinitialise la vitesse de la méduse à 0
     * @return
     */
    public void stop() {
        meduse.stop();
    }

    /**
     * débuter la partie
     */
    public void debut() {
        //possible de commencer une partie ssi la méduse est dans sa position initiale
        if (this.meduse.x == (double)largeur/2 - 25 && this.meduse.y == hauteur-50) {
            defilerV = -50;//vitesse de 50px/s
            defilerA = -2;//accélération de 2px/s^2
            this.meduse.ay = 1200;//gravité
            this.meduse.setParterre(true);
            this.meduse.jump();
        }
    }

    /**
     * finir la partie
     * @return
     */
    public boolean fin() {
        return this.meduse.y > this.fenetreY + hauteur;
    }
}