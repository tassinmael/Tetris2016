package source;


import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

import javax.swing.JFrame;

/**
 * La classe principale de jeux
 *
 */
public class Principale extends JFrame {
	
	/**
	 * Définition des différentes constantes du jeux
	 * **/
	
	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = -4722429764792514382L;

	private static final long HORLOGE = 1000L / 50L;
	
	/**
	 * Définition de nombre de pièces éxistantes dans le jeux.
	 */
	private static final int NOMBRE_TYPES_PIECES = Piece.values().length;
	
	/**
	 * Déclaration de nom de fichier des scores
	 */
	
	private static final String NOM_FICHIER="scores.txt";
	
	/**
	 * Définition de la taille de tableau des scores
	 *
	 */
	
	private final int TAILLE_TABLEAU=5;
	private static boolean ecrire=true;
	
	/**
	 * Déclaration de panneau principal du jeux.
	 */
	private Grille surface;
	
	/**
	 * Déclaration de panneau d'affichage
	 */
	private PanneauAffichage panneau;
	
	
	private boolean suspendu;
	
	private boolean nouveauJeu;
	
	private boolean perdu;
	
	
	private int score;
	
	/**
	 * Génération aléatoire des pièces 
	 */
	private Random random;
	
	/**
	 * Vitesse de diffilement des pièces.
	 */
	private Horloge horloge;
				
	/**
	 * Le type de la pièce courante .
	 */
	private Piece typePieceCourante;
	
	private Piece typePieceSuivante;
		
	/**
	 * La colone courante de la pièce en diffilement.
	 */
	private int colonneCourante;
	
	/**
	 * La ligne courante de la pièce en diffilement.
	 */
	private int ligneCourante;
	
	/**
	 * La rotation courante de la pièce en jeu.
	 */
	private int rotationCourante;
		
	
	private int deplacerEnBas;
	
	/**
	 * Déclaration de la vitesse du jeux (diffilement des pièces).
	 */
	private float vitesseJeu;
	
	/**
	 * Déclaration de tableau des scores
	 */
	
	private String [] tableauScores  ;
	
	private boolean stop=false;
	private int indiceTableau;
	private String valeurIntermidiaire;
	private int indiceIntermidiaire;
	
	private int fin;
	/**
	 * Constructeur de la fénétre principale du jeux
	 */
	private Principale() {
		
		super("Jeu du Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		
		this.surface = new Grille(this);
		this.panneau = new PanneauAffichage(this);
		
		/*
		 * Ajout de la grille de jeux et de panneau d'affichage.
		 */
		add(surface, BorderLayout.CENTER);
		add(panneau, BorderLayout.EAST);
		
		this.tableauScores= new String [TAILLE_TABLEAU];
		
		InputStream ips;
		try {
			ips = new FileInputStream(NOM_FICHIER);
		 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			int i=0;
		
			while ((ligne=br.readLine())!=null){
				tableauScores[i]=ligne;
				i++;
			}
				br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		
		/*
		 * Ajout d'écouteurs sur le clavier pour permettre de 
		 * difnir les touches résponsables de contrôle du jeux.
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
								
				switch(e.getKeyCode()) {
				
				/*
				 * Accélerer le mouvement de descente de la pièce en appuyant
				 * Sur la flèche en bas de clavier.
				 */
				case KeyEvent.VK_DOWN:
					if(!suspendu && deplacerEnBas == 0) {
						horloge.setcyclesParSeconde(25.0f);
					}
					break;
					
				/*
				 * Déplacer la pièce vers la gauche en appuyant
				 * sur la flèche gauche du clavier 
				 */
				case KeyEvent.VK_LEFT:
					if(!suspendu && surface.caseVide(typePieceCourante, colonneCourante - 1, ligneCourante, rotationCourante)) {
						colonneCourante--;
					}
					break;
					
					/*
					 * Déplacer la pièce vers la droite en appuyant
					 * sur la flèche droite du clavier 
					 */
				case KeyEvent.VK_RIGHT:
					if(!suspendu && surface.caseVide(typePieceCourante, colonneCourante + 1, ligneCourante, rotationCourante)) {
						colonneCourante++;
					}
					break;
					
				/*
				 * Faire tourner la pièce en appuyant sur la flèche 
				 * vers le haut 
				 */
				case KeyEvent.VK_UP:
					if(!suspendu) {
						tournerPiece((rotationCourante == 0) ? 3 : rotationCourante - 1);
					}
					break;
				
				/*
				 * Suspendre le jeu en appuyant sur la touche espace de clavier
				 */
				case KeyEvent.VK_P:
					if(!perdu && !nouveauJeu) {
						suspendu = !suspendu;
						horloge.setPaused(suspendu);
					}
					break;
				
				/*
				 * Démarrer le jeu en appuyant sur la touche Entrer
				 */
				case KeyEvent.VK_ENTER:
					if(perdu || nouveauJeu) {
						remiseAZero();
					}
					break;
				
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				
				case KeyEvent.VK_DOWN:
					horloge.setcyclesParSeconde(vitesseJeu);
					horloge.RemiseZero();
					break;
				}
				
			}
			
		});
		
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		
	}
	
	
	private void commencerJeu() {
		/*
		 * initialiser le générateur aléatoire des pièces
		 */
		this.random = new Random();
		this.nouveauJeu = true;
		this.vitesseJeu = 1.0f;
		
		
		this.horloge = new Horloge(vitesseJeu);
		horloge.setPaused(true);
		
		while(true) {
			
			long start = System.nanoTime();
			
			horloge.mAJHorloge();
			
			
			if(horloge.cycleEstEcoule()) {
				mettreAJour();
			}
		
			
			if(deplacerEnBas > 0) {
				deplacerEnBas--;
			}
			
			
			redissiner();
			
			
			
			
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < HORLOGE) {
				try {
					Thread.sleep(HORLOGE - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	private void mettreAJour() {
		/*
		 * Vérifier si la pièce peut être défillée vers le bas 
		 * à partir de sa position actuelle
		 */
		if(surface.caseVide(typePieceCourante, colonneCourante, ligneCourante + 1, rotationCourante)) {
			//Si c'est le cas on incrémente sa position sur la grille.
			ligneCourante++;
		} else {
			/*
			 * Sinon on dépose la pièce sur le reste de tetris constuit
			 */
			surface.ajouterPiece(typePieceCourante, colonneCourante, ligneCourante, rotationCourante);
			
			/*
			 * Vérifier si l'ajout de la pièce génére la destruction de lignes, le score 
			 * est calculé à partir de nombre de lignes détruites comme suit:
			 * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]).
			 */
			int cleared = surface.verifierLigne();
			if(cleared > 0) {
				score += 50 << cleared;
			}
			
			deplacerEnBas = 25;
			
			fractionner();
		}
        
	}
	
	/**
	 * Forcer la redissinage de panneau pour le mettre à jour
	 */
	private void redissiner() {
		surface.repaint();
		panneau.repaint();
	}
	
	/**
	 * Réinitialisation des valeurs des différentes variables 
	 * au redémmarage du jeu
	 * 
	 */
	private void remiseAZero(){
		
		this.score = 0;
		this.vitesseJeu = 1.0f;
		this.typePieceSuivante = Piece.values()[random.nextInt(NOMBRE_TYPES_PIECES)];
		this.nouveauJeu = false;
		this.perdu = false;		
		surface.effacer();
		horloge.RemiseZero();;
		horloge.setcyclesParSeconde(vitesseJeu);
		fractionner();
	}

	
	/*private void fractionner() {
	
			//PrintWriter writer = new PrintWriter( new FileWriter(NOM_FICHIER) );			
		this.typePieceCourante = typePieceSuivante;
		this.colonneCourante = typePieceCourante.getcolone1umn();
		this.ligneCourante = typePieceCourante.getligne1();
		this.rotationCourante = 0;
		this.typePieceSuivante = Piece.values()[random.nextInt(NOMBRE_TYPES_PIECES)];
		
	
		if(!surface.caseVide(typePieceCourante, colonneCourante, ligneCourante, rotationCourante)) 
		{
			setFin(4);
			this.perdu = true;
			horloge.setPaused(true);
			
			while (getIndiceTableau()<TAILLE_TABLEAU &&  isStop() == false)
			{
				if(Integer.valueOf(getTableauScores()[getIndiceTableau()])< getScore())
				{
					setValeurIntermidiaire(getTableauScores()[getIndiceTableau()]);
					setIndiceIntermidiaire(getIndiceTableau()+1);
					
					
					getTableauScores()[getIndiceTableau()]=String.valueOf(getScore());
					
					setStop(true);
					
					
					while (getFin()>getIndiceIntermidiaire())
					{
						
						getTableauScores()[getFin()]=getTableauScores()[getFin()-1];
						setFin(getFin()-1);
						
					}
					getTableauScores()[getIndiceIntermidiaire()]=getValeurIntermidiaire();
					if(ecrire)
					{
						for(int j=0;j<5;j++)
						{
							System.out.println(getTableauScores()[j]);
						}
						ecrire=false;
					}
					
				}
				
			}
			
		}	
		
	}*/
	
	private void fractionner() {
		
		this.typePieceCourante = typePieceSuivante;
		this.colonneCourante = typePieceCourante.getcolone1umn();
		this.ligneCourante = typePieceCourante.getligne1();
		this.rotationCourante = 0;
		this.typePieceSuivante = Piece.values()[random.nextInt(NOMBRE_TYPES_PIECES)];
			
		if(!surface.caseVide(typePieceCourante, colonneCourante, ligneCourante, rotationCourante)) {
			this.perdu = true;
			horloge.setPaused(true);
			setFin(4);
			
			while (getIndiceTableau()<TAILLE_TABLEAU &&  isStop() == false)
			{
				
				if(Integer.valueOf(getTableauScores()[getIndiceTableau()])< getScore())
				{
					
					
					setValeurIntermidiaire(getTableauScores()[getIndiceTableau()]);
					setIndiceIntermidiaire(getIndiceTableau()+1);
					
					
					getTableauScores()[getIndiceTableau()]=String.valueOf(getScore());
					
					setStop(true);
					
					/**
					 * insertion de nouveau meilleur score dans le tableau des scores
					 */
					while (getFin()>getIndiceIntermidiaire())
					{
						
						getTableauScores()[getFin()]=getTableauScores()[getFin()-1];
						setFin(getFin()-1);
						
					}
					getTableauScores()[getIndiceIntermidiaire()]=getValeurIntermidiaire();
					
					/**
					 * Ecriture des nouveaux scores dans le fichier des meilleurs scores
					 */
					if(ecrire)
					{
						PrintWriter writer;
						try {
							writer = new PrintWriter( new FileWriter(NOM_FICHIER) );
						
						for(int j=0;j<5;j++)
						{
							writer.write(getTableauScores()[j]+"\n");
						}
						writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ecrire=false;
						
						long tEnd = System.currentTimeMillis() + 1000;
						while (System.currentTimeMillis() < tEnd) {
						}
						dispose();
					}	
				}	
			}
			
		}		
	}

	/**
	 * Méthode permettant de tourner la pièce
	 */
	private void tournerPiece(int newRotation) {
		
		int newColumn = colonneCourante;
		int newRow = ligneCourante;
		
		
		int left = typePieceCourante.getLeftInset(newRotation);
		int right = typePieceCourante.getRightInset(newRotation);
		int top = typePieceCourante.getTopInset(newRotation);
		int bottom = typePieceCourante.getBottomInset(newRotation);
		
		
		if(colonneCourante < -left) {
			newColumn -= colonneCourante - left;
		} else if(colonneCourante + typePieceCourante.getDimension() - right >= Grille.NOMBRE_COLONES) {
			newColumn -= (colonneCourante + typePieceCourante.getDimension() - right) - Grille.NOMBRE_COLONES + 1;
		}
		
		if(ligneCourante < -top) {
			newRow -= ligneCourante - top;
		} else if(ligneCourante + typePieceCourante.getDimension() - bottom >= Grille.NOMBRE_LIGNES) {
			newRow -= (ligneCourante + typePieceCourante.getDimension() - bottom) - Grille.NOMBRE_LIGNES + 1;
		}
		
		/*
		 * Vérifie si la position de la pièce après son retournement
		 * est acceptable ou non
		 */
		if(surface.caseVide(typePieceCourante, newColumn, newRow, newRotation)) {
			rotationCourante = newRotation;
			ligneCourante = newRow;
			colonneCourante = newColumn;
		}
	}
	
	/**
	 * Retourne vrai si le jeux est en suspend
	 */
	public boolean suspendu() {
		return suspendu;
	}
	
	/**
	 * Retourne vrai si le joueur à perdu la partie.
	 */
	public boolean perdu() {
		return perdu;
	}
	
	
	public boolean nouveauJeu() {
		return nouveauJeu;
	}
	
	
	public int getScore() {
		return score;
	}
	
	
	public Piece getPieceType() {
		return typePieceCourante;
	}
	
	
	public Piece getNextPieceType() {
		return typePieceSuivante;
	}
	
	
	public int getPieceCol() {
		return colonneCourante;
	}
	
	
	public int getPieceRow() {
		return ligneCourante;
	}
	
	
	public int getPieceRotation() {
		return rotationCourante;
	}
	
	public String [] getTableauScores()
	{
		return tableauScores;
	}
	
	public void setTableauScores(String [] scores)
	{
		tableauScores=scores;
	}

	public boolean isStop() {
		return stop;
	}


	public void setStop(boolean stop) {
		this.stop = stop;
	}


	public int getIndiceTableau() {
		return indiceTableau;
	}


	public void setIndiceTableau(int indiceTableau) {
		this.indiceTableau = indiceTableau;
	}
	
	
	public static void main(String[] args)  {
		Principale tetris = new Principale();
		tetris.commencerJeu();
		
		
		
	}


	public String getValeurIntermidiaire() {
		return valeurIntermidiaire;
	}


	public void setValeurIntermidiaire(String valeurIntermidiaire) {
		this.valeurIntermidiaire = valeurIntermidiaire;
	}


	public int getIndiceIntermidiaire() {
		return indiceIntermidiaire;
	}


	public void setIndiceIntermidiaire(int indiceIntermidiaire) {
		this.indiceIntermidiaire = indiceIntermidiaire;
	}

    public int getFin()
    {
    	return this.fin;
    }
    
    public void setFin(int fin)
    {
    	this.fin=fin;
    }
	

}
