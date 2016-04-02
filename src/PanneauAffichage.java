package source;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Cette classe permet d'afficher le panneau contenant les différentes informations
 * sur le jeux, tel que le score et les controleurs de mouvement des pièces
 *
 */
public class PanneauAffichage extends JPanel {
	
	/**
	 * Serial Version UID.
	 */
	
	/**
	 * Définition des constantes et des variables de classe 
	 */
	
	private static final long serialVersionUID = 2181495598854992747L;
	private static final int TAILLE_PIECE = Grille.TAILLE_PIECE >> 1;	
	private static final int TAILLE_OMBRE = Grille.TAILLE_OMBRE >> 1;
	private static final int NOMBRE_PIECES = 5;
	private static final int X = 130;
	private static final int Y = 65;
	private static final int TAILLE_CARREE = (TAILLE_PIECE * NOMBRE_PIECES >> 1);
	private static final int DECALAGE_MINIMUM = 20;
	private static final int DECALAGE_MAXIMUM = 40;
	private static final int STATS = 175;
	private static final int CASE_COMMANDES = 300;
	private static final int TAILLE_TEXTE = 25;
	
	private static final Font FONT_MINIMALE = new Font("Tahoma", Font.BOLD, 11);
	private static final Font FONT_MAXIMALE = new Font("Tahoma", Font.BOLD, 13);
	private static final Color PARAMETRES_COULEUR = new Color(128, 192, 128);
	
	private Principale tetris;
	
	/**
	 * Création de panneau d'affichage
	 */
	public PanneauAffichage(Principale tetris) {
		this.tetris = tetris;
		
		setPreferredSize(new Dimension(200, Grille.HAUTEUR_PANNEAU));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		g.setColor(PARAMETRES_COULEUR);

		int offset;
		/**
		 * Affichage des cinq meilleurs scores sur le panneau d'affichage
		 */
		g.setFont(FONT_MAXIMALE);
		g.drawString("Meilleurs scores", DECALAGE_MINIMUM, offset =DECALAGE_MAXIMUM);
		g.setFont(FONT_MINIMALE);
		g.drawString(tetris.getTableauScores()[0], DECALAGE_MAXIMUM, offset +=TAILLE_TEXTE);
		g.drawString(tetris.getTableauScores()[1], DECALAGE_MAXIMUM, offset +=TAILLE_TEXTE);
		g.drawString(tetris.getTableauScores()[2], DECALAGE_MAXIMUM, offset +=TAILLE_TEXTE);
		g.drawString(tetris.getTableauScores()[3], DECALAGE_MAXIMUM, offset +=TAILLE_TEXTE);
		g.drawString(tetris.getTableauScores()[4], DECALAGE_MAXIMUM, offset +=TAILLE_TEXTE);
		
		
		g.drawString("", DECALAGE_MINIMUM, offset = STATS);
		g.setFont(FONT_MINIMALE);
		
		/**
		 * Affichge du score de la partie en cours
		 */
		
		g.drawString("" , DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		g.drawString("Score: " + tetris.getScore(), DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		
		
		/**
		 * Affichage des commandes de jeu
		 */
		
		g.setFont(FONT_MAXIMALE);
		g.drawString("Commandes", DECALAGE_MINIMUM, offset = CASE_COMMANDES);
		g.setFont(FONT_MINIMALE);
		g.drawString(" Flèche gauche: gauche", DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		g.drawString(" Flèche gauche: droite", DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		g.drawString(" Flèche haut  : Tourner", DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		g.drawString(" Flèche bas   : accélerer", DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		g.drawString(" P            :Suspendre", DECALAGE_MAXIMUM, offset += TAILLE_TEXTE);
		
	}
	
}
