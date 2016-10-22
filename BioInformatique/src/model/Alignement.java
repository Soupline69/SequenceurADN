package model;

public class Alignement {
	private final int GAP = -2;
	private final int MATCH = 1;
	private final int MISMATCH = -1;
	private String f1;
	private String f2;
	private int score = 0;
		
	public Alignement(String f1, String f2) {
		this.f1 = f1;
		this.f2 = f2;
		
		check();

		alignementSemiGlobal();
	}
	
	/** 
	 * Aligne sur base de l'alignement semi-global les fragments f1 et f2
	 */
	public void alignementSemiGlobal() {
		Matrice m = new Matrice(f1, f2);
		Position maxPosition = m.calcul(f1, f2, GAP);

		if(maxPosition.getX() == m.getCountLigne() - 1) { // Si le maximum est sur la ligne
			//System.out.println("Le maximum est en ["+(m.getCountLigne() - 1)+"]["+maxPosition.getY()+"] (ligne)");
			alignementLigne(m.getCountLigne() - 1, maxPosition.getY(), m.getMatrice());
		} else { // Sinon le maximum est sur la colonne
			//System.out.println("Le maximum est en ["+maxPosition.getX()+"]["+(m.getCountColonne() - 1)+"] (colonne)");
			alignementColonne(maxPosition.getX(), m.getCountColonne() - 1, m.getMatrice());
		}
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la derni�re colonne de la matrice
	 */
	public void alignementColonne(int ligne, int colonne, int[][] m) {
		for(int i = 0; i < f1.substring(ligne).length(); i++) {
			f2 = f2 + "_";
		} 
			
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.charAt(ligne - 1), f2.charAt(colonne - 1))) { // Si c'est un match ou un mismatch 
				score += match(f1.charAt(ligne - 1), f2.charAt(colonne - 1));
				ligne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + GAP) { // Si c'est un gap de gauche
				String localBegin = f1.substring(0, ligne);
				String localEnd = f1.substring(ligne);
				f1 = localBegin + "-" + localEnd;
				score += GAP;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + GAP) { // Si c'est un grap du haut
				String localBegin = f2.substring(0, colonne);
				String localEnd = f2.substring(colonne);
				f2 = localBegin + "-" + localEnd;
				score += GAP;
				ligne--;
				colonne++;
			}
			
			colonne--;
		}
	
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				f1 = "_" + f1;
			}
		}
		
		if(colonne == 0 && ligne != 0) {	
			for(int i = 0; i < ligne; i++) {
				f2 = "_" + f2;
			}
		}	
	}
	
	/**
	 * Aligne les fragments f1 et f2 si le maximum de la matrice (construite) est sur la derni�re ligne de la matrice
	 */
	public void alignementLigne(int ligne, int colonne, int[][] m) {
		for(int i = 0; i < f2.substring(colonne).length(); i++) {
			f1 = f1 + "_";
		} 
				
		while(colonne > 0 && ligne > 0) {
			if(m[ligne][colonne] == m[ligne - 1][colonne - 1] + match(f1.charAt(ligne - 1), f2.charAt(colonne - 1))) { // Si c'est un match ou un mismatch 
				score += match(f1.charAt(ligne - 1), f2.charAt(colonne - 1));
				colonne--;
			} else if(m[ligne][colonne] == m[ligne][colonne - 1] + GAP) { // Si c'est un gap de gauche
				String localBegin = f1.substring(0, ligne);
				String localEnd = f1.substring(ligne);
				f1 = localBegin + "-" + localEnd;
				score += GAP;
				colonne--;
				ligne++;
			} else if(m[ligne][colonne] == m[ligne - 1][colonne] + GAP) { // Si c'est un gap du haut
				String localBegin = f2.substring(0, colonne);
				String localEnd = f2.substring(colonne);
				f2 = localBegin + "-" + localEnd;
				score += GAP;
			} 
			
			ligne--;
		}
			
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				f1 = "_" + f1;
			}
		}
		
		if(colonne == 0 && ligne != 0) {	
			for(int i = 0; i < ligne; i++) {
				f2 = "_" + f2;
			}
		}	
	}
	
	/**
	 * Effectue le pr�-traitement sur les deux fragments en fonction d'o� se trouve le maximum dans la matrice
	 */
	public String preTraitement(String f1, String f2, int limite) {
		String temp = f1.substring(limite);
		
		for(int i = 0; i < temp.length(); i++) {
			f2 = f2.concat("_");
		} 
		
		return temp + "&" + f2;
	}
	
	/**
	 * Effectue le traitement sur les deux fragments en fonction d'o� se trouve le maximum dans la matrice
	 */
	public String traitement(String temp, String f1, String f2, int ligne, int colonne) {
		temp = f1.charAt(ligne) + temp;
		String localBegin = f2.substring(0, colonne);
		String localEnd = f2.substring(colonne);
		f2 = localBegin + "-" + localEnd;
		
		return temp + "&" + f2;
	}
	
	/**
	 * Effectue le post-traitement sur les deux fragments en fonction d'o� se trouve le maximum dans la matrice
	 */
	public String postTraitement(String temp, String f1, String f2, int ligne, int colonne) {
		if(ligne == 0 && colonne != 0) {
			for(int i = 0; i < colonne; i++) {
				temp = "_" + temp;
			}
		}
		
		if(colonne == 0 && ligne != 0) {
			temp = f1.substring(0, ligne) + temp;
	
			for(int i = 0; i < ligne; i++) {
				f2 = "_" + f2;
			}
		}
		
		return temp + "&" + f2;
	}
	
	/**
	 * Permet de toujours avoir le fragment le plus long sur les lignes (f1 sera toujours plus long que f2)
	 */
	public void check() {
		if(f1.length() < f2.length()) { 
			String tmp = f1;
			f1 = f2;
			f2 = tmp;
		}
	}
	
	/**
	 * Retourne 1 si le caract�re a est �gal au caract�re b (match)
	 * Retourn - 1 sinon (mismatch)
	 */
	public int match(char a, char b) {
		return a == b ? MATCH : MISMATCH;
	}
	
	public String getF1() {
		return f1;
	}
	
	public String getF2() {
		return f2;
	}
	
	public int getScore() {
		return score;
	}

}
