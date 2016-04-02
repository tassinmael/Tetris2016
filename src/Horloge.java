package source;


/**
 * Classe permettant de controler l'horloge
 * qui régit le jeu, elle permet entre autres de:
 * -définir la vitesse de diffilement des pièces
 * -déifnir le cycle de passage d'une pièce à une autre
 */
public class Horloge {
	
	/**
	 * Définition de la durée d'un cycle.
	 */
	private float cycle;
	
	/**
	 * Le dernier moment ou l'horloge a été mise à jour.
	 */
	private long derniereMAJ;
	
	/**
	 * Le nombre de cycles écoulés.
	 */
	private int cyclesEcoules;
	
	private float excessCycles;
	
	/**
	 * vérifie si l'horloge a été mise en pause
	 */
	private boolean suspendu;
	
	/**
	 * Création de l'horloge et définition de nombre de cycle par seconde.
	 */
	public Horloge(float cyclesParSeconde) {
		setcyclesParSeconde(cyclesParSeconde);
		RemiseZero();
	}
	
	
	public void setcyclesParSeconde(float cyclesParSeconde) {
		this.cycle = (1.0f / cyclesParSeconde) * 1000;
	}
	
	/**
	 * Remise à zéro des paramètres de l'horloge
	 */
	public void RemiseZero() {
		this.cyclesEcoules = 0;
		this.excessCycles = 0.0f;
		this.derniereMAJ = getTempsActuel();
		this.suspendu = false;
	}
	
	/**
	 * Mise à jour des paramètres de l'horloge
	 */
	public void mAJHorloge() {
		
		long tempsActuel = getTempsActuel();
		float delta = (float)(tempsActuel - derniereMAJ) + excessCycles;
		
		if(!suspendu) {
			this.cyclesEcoules += (int)Math.floor(delta / cycle);
			this.excessCycles = delta % cycle;
		}
		
		this.derniereMAJ = tempsActuel;
	}
	
	
	public void setPaused(boolean paused) {
		this.suspendu = paused;
	}
	

	public boolean suspendu() {
		return suspendu;
	}
	
	/**
	 * Vérifie si le cycle de l'horloge est écoulé ou pas encore
	 */
	public boolean cycleEstEcoule() {
		if(cyclesEcoules > 0) {
			this.cyclesEcoules--;
			return true;
		}
		return false;
	}
	
	
	public boolean cycleEstEcoule1() {
		return (cyclesEcoules > 0);
	}
	
	
	private static final long getTempsActuel() {
		return (System.nanoTime() / 1000000L);
	}
	
	public int getCycleEcoules()
	{
		return this.cyclesEcoules;
	}
	
	public float getExcessCycles()
	{
		return this.excessCycles;
	}
	
	public boolean isSuspendu()
	{
		return this.suspendu;
	}
	

}
