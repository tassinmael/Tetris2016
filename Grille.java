package source;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Grille extends JPanel {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 5055679736784226108L;
	public static final int COLOR_MIN = 35;
	public static final int COLOR_MAX = 255 - COLOR_MIN;
	private static final int BORDER_WIDTH = 5;
	public static final int NOMBRE_COLONES = 10;
	private static final int NOMBRE_COLONES_VISIBLES = 20;
	private static final int NOMBRE_LIGNES_INVISIBLES = 2;
	public static final int NOMBRE_LIGNES = NOMBRE_COLONES_VISIBLES + NOMBRE_LIGNES_INVISIBLES;
	public static final int TAILLE_PIECE = 24;
	public static final int TAILLE_OMBRE = 4;
	private static final int X = NOMBRE_COLONES * TAILLE_PIECE / 2;
	private static final int Y = NOMBRE_COLONES_VISIBLES * TAILLE_PIECE / 2;
	public static final int LARGEUR_PANNEAU = NOMBRE_COLONES * TAILLE_PIECE + BORDER_WIDTH * 2;
	public static final int HAUTEUR_PANNEAU = NOMBRE_COLONES_VISIBLES * TAILLE_PIECE + BORDER_WIDTH * 2;
	private static final Font FONT_MAXIMALE = new Font("Tahoma", Font.BOLD, 16);
	private static final Font FONT_MINIMALE = new Font("Tahoma", Font.BOLD, 12);
	
	private Principale tetris;
	private Piece[][] pieces;
		
	/**
	 * Création de la nouvelle surface du 
	 */
	public Grille(Principale tetris) {
		this.tetris = tetris;
		this.pieces = new Piece[NOMBRE_LIGNES][NOMBRE_COLONES];
		
		setPreferredSize(new Dimension(LARGEUR_PANNEAU, HAUTEUR_PANNEAU));
		setBackground(Color.GRAY);
	}
	
	/**
	 * Rémise à zèro des valeurs de la pièce en 
	 */

	public void effacer() {
		
		for(int i = 0; i < NOMBRE_LIGNES; i++) {
			for(int j = 0; j < NOMBRE_COLONES; j++) {
				pieces[i][j] = null;
			}
		}
	}
	
	/**
	 * Vérifie si la pièce peut être placée dans l'emplacement donné en paramètre.
	 */
	public boolean caseVide(Piece type, int x, int y, int rotation) {
				
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= NOMBRE_COLONES) {
			return false;
		}
		
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= NOMBRE_LIGNES) {
			return false;
		}
		
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.estUnePiece(col, row, rotation) && estOccupee(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Mettre une nouvelle pièce en 
	 */
	public void ajouterPiece(Piece type, int x, int y, int rotation) {
		
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.estUnePiece(col, row, rotation)) {
					setPiece(col + x, row + y, type);
				}
			}
		}
	}
	
	/**
	 * Vérifie si un ou plusieurs lignes de tetris ont été détruites
	 */
	public int verifierLigne() {
		int lignesCompletees = 0;
		
		for(int row = 0; row < NOMBRE_LIGNES; row++) {
			if(verifier(row)) {
				lignesCompletees++;
			}
		}
		return lignesCompletees;
	}
			
	/**
	 * Vérifie si une ligne du  est complète ou non
	 */
	private boolean verifier(int line) {
		
		for(int col = 0; col < NOMBRE_COLONES; col++) {
			if(!estOccupee(col, line)) {
				return false;
			}
		}
		
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < NOMBRE_COLONES; col++) {
				setPiece(col, row + 1, getPiece(col, row));
			}
		}
		return true;
	}
	
	
	/**
	 * Vérifie si une case de la grille est déja occupée ou non.
	 */
	private boolean estOccupee(int x, int y) {
		return pieces[y][x] != null;
	}
	
	
	private void setPiece(int  x, int y, Piece type) {
		pieces[y][x] = type;
	}
		
	/**
	 * Récuperer le type de la pièce placée dans la case de la grillle passée en paramètres
	 */
	private Piece getPiece(int x, int y) {
		return pieces[y][x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		if(tetris.suspendu()) {
			g.setFont(FONT_MAXIMALE);
			g.setColor(Color.WHITE);
			String msg = "PAUSE";
			g.drawString(msg, X - g.getFontMetrics().stringWidth(msg) / 2, Y);
		} else if(tetris.nouveauJeu() || tetris.perdu()) {
			g.setFont(FONT_MAXIMALE);
			g.setColor(Color.WHITE);
		
			String msg = tetris.nouveauJeu() ? "TETRIS" : "GAME OVER";
			g.drawString(msg, X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(FONT_MINIMALE);
			msg = "Appuyez sur Entrer pour jouer" + (tetris.nouveauJeu() ? "" : " Again");
			g.drawString(msg, X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else {
			
			for(int x = 0; x < NOMBRE_COLONES; x++) {
				for(int y = NOMBRE_LIGNES_INVISIBLES; y < NOMBRE_LIGNES; y++) {
					Piece tile = getPiece(x, y);
					if(tile != null) {
						dessinerPiece(tile, x * TAILLE_PIECE, (y - NOMBRE_LIGNES_INVISIBLES) * TAILLE_PIECE, g);
					}
				}
			}
			
			
			Piece type = tetris.getPieceType();
			int pieceCol = tetris.getPieceCol();
			int pieceRow = tetris.getPieceRow();
			int rotation = tetris.getPieceRotation();
			
			for(int col = 0; col < type.getDimension(); col++) {
				for(int row = 0; row < type.getDimension(); row++) {
					if(pieceRow + row >= 2 && type.estUnePiece(col, row, rotation)) {
						dessinerPiece(type, (pieceCol + col) * TAILLE_PIECE, (pieceRow + row - NOMBRE_LIGNES_INVISIBLES) * TAILLE_PIECE, g);
					}
				}
			}
			
			g.setColor(Color.DARK_GRAY);
			for(int x = 0; x < NOMBRE_COLONES; x++) {
				for(int y = 0; y < NOMBRE_COLONES_VISIBLES; y++) {
					g.drawLine(0, y * TAILLE_PIECE, NOMBRE_COLONES * TAILLE_PIECE, y * TAILLE_PIECE);
					g.drawLine(x * TAILLE_PIECE, 0, x * TAILLE_PIECE, NOMBRE_COLONES_VISIBLES * TAILLE_PIECE);
				}
			}
		}
		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TAILLE_PIECE * NOMBRE_COLONES, TAILLE_PIECE * NOMBRE_COLONES_VISIBLES);
	}
	
	/**
	 * Déssiner la pièce dans la surface du 
	 */
	private void dessinerPiece(Piece type, int x, int y, Graphics g) {
		dessinerPiece(type.getcouleurPrincipale(), type.getombre(), type.getcouleurFoncee(), x, y, g);
	}
	
	private void dessinerPiece(Color base, Color light, Color dark, int x, int y, Graphics g) {
	
		g.setColor(base);
		g.fillRect(x, y, TAILLE_PIECE, TAILLE_PIECE);
		
		g.setColor(dark);
		g.fillRect(x, y + TAILLE_PIECE - TAILLE_OMBRE, TAILLE_PIECE, TAILLE_OMBRE);
		g.fillRect(x + TAILLE_PIECE - TAILLE_OMBRE, y, TAILLE_OMBRE, TAILLE_PIECE);
		
		g.setColor(light);
		for(int i = 0; i < TAILLE_OMBRE; i++) {
			g.drawLine(x, y + i, x + TAILLE_PIECE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TAILLE_PIECE - i - 1);
		}
	}

}