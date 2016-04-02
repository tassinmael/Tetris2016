package source;


import java.awt.Color;

/**
 * Classe contenant les propriétés des pièces qui peuvent être utilisée dans la jeu
 *
 */
public enum Piece {

	/**
	 * Piece TypeI.
	 */
	TypeI(new Color(Grille.COLOR_MIN, Grille.COLOR_MAX, Grille.COLOR_MAX), 4, 4, 1, new boolean[][] {
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	
	/**
	 * Piece TypeJ.
	 */
	TypeJ(new Color(Grille.COLOR_MIN, Grille.COLOR_MIN, Grille.COLOR_MAX), 3, 3, 2, new boolean[][] {
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeL.
	 */
	TypeL(new Color(Grille.COLOR_MAX, 127, Grille.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeO.
	 */
	TypeO(new Color(Grille.COLOR_MAX, Grille.COLOR_MAX, Grille.COLOR_MIN), 2, 2, 2, new boolean[][] {
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	
	/**
	 * Piece TypeS.
	 */
	TypeS(new Color(Grille.COLOR_MIN, Grille.COLOR_MAX, Grille.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeT.
	 */
	TypeT(new Color(128, Grille.COLOR_MIN, 128), 3, 3, 2, new boolean[][] {
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeZ.
	 */
	TypeZ(new Color(Grille.COLOR_MAX, Grille.COLOR_MIN, Grille.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	});
		
	/**
	 * Définition des propriétés graphiques des différentes pièces
	 */
	private Color couleurPrincipale;
	private Color ombre;
	private Color couleurFoncee;
	private int colone1;
	private int ligne1;
	
	private int dimension;
	private int lignes;
	private int colones;
	
	private boolean[][] pieces;
	
	/**
	 * Création de la nouvelle grille
	 */
	private Piece(Color color, int dimension, int colones, int lignes, boolean[][] pieces) {
		this.couleurPrincipale = color;
		this.ombre = color.brighter();
		this.couleurFoncee = color.darker();
		this.dimension = dimension;
		this.pieces = pieces;
		this.colones = colones;
		this.lignes = lignes;
		
		this.colone1 = 5 - (dimension >> 1);
		this.ligne1 = getTopInset(0);
	}
	
	public Color getcouleurPrincipale() {
		return couleurPrincipale;
	}

	public Color getombre() {
		return ombre;
	}
	
	public Color getcouleurFoncee() {
		return couleurFoncee;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public int getcolone1umn() {
		return colone1;
	}
	
	public int getligne1() {
		return ligne1;
	}
	
	public int getlignes() {
		return lignes;
	}
	
	public int getcolones() {
		return colones;
	}

	public boolean estUnePiece(int x, int y, int rotation) {
		return pieces[rotation][y * dimension + x];
	}

	public int getLeftInset(int rotation) {

		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(estUnePiece(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	
	public int getRightInset(int rotation) {

		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(estUnePiece(x, y, rotation)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}

	public int getTopInset(int rotation) {

		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(estUnePiece(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}

	public int getBottomInset(int rotation) {

		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(estUnePiece(x, y, rotation)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}
	
}
