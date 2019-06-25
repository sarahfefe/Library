package biblio;

import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Composant logiciel assurant la gestion des emprunts d'exemplaires de livre
 * par les abonnés.
 */
public class ComposantBDEmprunt {

	/**
	 * Retourne le nombre total d'emprunts en cours référencés dans la base.
	 * 
	 * @return le nombre d'emprunts.
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static int nbEmpruntsEnCours() throws SQLException {
		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		int nb = 0;
		ResultSet r = s.executeQuery("SELECT date_retour FROM EMPRUNTS ");
		while (r.next()) {
			if (r.getString("date_retour") == null){
				nb += 1;
			}
		}
		r.close();
		s.close();

		return nb;
	}

	/**
	 * Retourne le nombre d'emprunts en cours pour un abonné donné connu par son
	 * identifiant.
	 * 
	 * @return le nombre d'emprunts.
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static int nbEmpruntsEnCours(int idAbonne) throws SQLException {
		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		int nb = 0;
		ResultSet r = s
				.executeQuery("SELECT date_retour  FROM EMPRUNTS WHERE id_abo= "
						+ idAbonne);
		while (r.next()) {
			if (r.getString("date_retour") == null){
				nb += 1;
			}
		}
		r.close();
		s.close();

		return nb;
	}

	/**
	 * Récupération de la liste complète des emprunts en cours.
	 * 
	 * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes de
	 *         caractères contenu correspond à un emprunt en cours.<br/>
	 *         Il doit contenir 8 éléments (dans cet ordre) :
	 *         <ul>
	 *         <li>0 : id de l'exemplaire</li>
	 *         <li>1 : id du livre correspondant</li>
	 *         <li>2 : titre du livre</li>
	 *         <li>3 : son auteur</li>
	 *         <li>4 : id de l'abonné</li>
	 *         <li>5 : nom de l'abonné</li>
	 *         <li>6 : son prénom</li>
	 *         <li>7 : la date de l'emprunt</li>
	 *         </ul>
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static ArrayList<String[]> listeEmpruntsEnCours()
			throws SQLException {
		ArrayList<String[]> emprunts = new ArrayList<String[]>();
		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("SELECT DISTINCT * FROM EMPRUNTS EM JOIN ABONNES A ON id_abo=id");
		while (r.next()) {
			String[] emprunt = new String[8];
			emprunt[0] = r.getString("id_ex");
			int id_ex = Integer.parseInt(emprunt[0]);
			String[] livre = ComposantBDLivre.getLivreParIdExemplaire(id_ex);
			emprunt[1] = livre[1];
			emprunt[2] = livre[4];
			emprunt[3] = livre[5];
			emprunt[4] = r.getString("id_abo");
			emprunt[5] = r.getString("nom");
			emprunt[6] = r.getString("prenom");
			emprunt[7] = r.getString("date_emprunt");
			if (r.getString("date_retour") == null){
			//if (estEmprunte(id_ex)){
				emprunts.add(emprunt);
			}
		}
		return emprunts;
	}

	/**
	 * Récupération de la liste des emprunts en cours pour un abonné donné.
	 * 
	 * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes de
	 *         caractères contenu correspond à un emprunt en cours pour
	 *         l'abonné.<br/>
	 *         Il doit contenir 5 éléments (dans cet ordre) :
	 *         <ul>
	 *         <li>0 : id de l'exemplaire</li>
	 *         <li>1 : id du livre correspondant</li>
	 *         <li>2 : titre du livre</li>
	 *         <li>3 : son auteur</li>
	 *         <li>4 : la date de l'emprunt</li>
	 *         </ul>
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static ArrayList<String[]> listeEmpruntsEnCours(int idAbonne)
			throws SQLException {
		ArrayList<String[]> emprunts = new ArrayList<String[]>();

		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		ResultSet r = s
				.executeQuery("SELECT DISTINCT * FROM EMPRUNTS EM JOIN ABONNES A ON id_abo=id WHERE id_abo="
						+ idAbonne);
		while (r.next()) {
			String[] emprunt = new String[5];
			emprunt[0] = r.getString("id_ex");
			int id_ex = Integer.parseInt(emprunt[0]);
			String[] livre = ComposantBDLivre.getLivreParIdExemplaire(id_ex);
			emprunt[1] = livre[1];
			emprunt[2] = livre[4];
			emprunt[3] = livre[5];
			emprunt[4] = r.getString("date_emprunt");
			if (r.getString("date_retour") == null){
				emprunts.add(emprunt);
			}
		}
		return emprunts;
	}

	/**
	 * Récupération de la liste complète des emprunts passés.
	 * 
	 * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes de
	 *         caractères contenu correspond à un emprunt passé.<br/>
	 *         Il doit contenir 9 éléments (dans cet ordre) :
	 *         <ul>
	 *         <li>0 : id de l'exemplaire</li>
	 *         <li>1 : id du livre correspondant</li>
	 *         <li>2 : titre du livre</li>
	 *         <li>3 : son auteur</li>
	 *         <li>4 : id de l'abonné</li>
	 *         <li>5 : nom de l'abonné</li>
	 *         <li>6 : son prénom</li>
	 *         <li>7 : la date de l'emprunt</li>
	 *         <li>8 : la date de retour</li>
	 *         </ul>
	 * @return un <code>ArrayList</code> contenant autant de tableaux de String
	 *         (5 chaînes de caractères) que d'emprunts dans la base.
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static ArrayList<String[]> listeEmpruntsHistorique()
			throws SQLException {
		ArrayList<String[]> emprunts = new ArrayList<String[]>();
		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();

		ResultSet r = s
				.executeQuery("SELECT * FROM EMPRUNTS EM JOIN ABONNES A ON id_abo=id;");

		while (r.next()) {

			String[] emprunt = new String[9];
			emprunt[0] = r.getString("id_ex");
			int id_ex = Integer.parseInt(emprunt[0]);
			if (r.getString("date_retour")!=null) {
			
			String[] livre = ComposantBDLivre
						.getLivreParIdExemplaire(id_ex);
				emprunt[1] = livre[1];
				emprunt[2] = livre[4];
				emprunt[3] = livre[5];
				emprunt[4] = r.getString("id_abo");
				emprunt[5] = r.getString("nom");
				emprunt[6] = r.getString("prenom");
				emprunt[7] = r.getString("date_emprunt");
				emprunt[8] = r.getString("date_retour");///
				emprunts.add(emprunt);
			}
		}
		return emprunts; 
	}

	/**
	 * Emprunter un exemplaire à partir de l'identifiant de l'abonné et de
	 * l'identifiant de l'exemplaire.
	 * 
	 * @param idAbonne
	 *            : id de l'abonné emprunteur.
	 * @param idExemplaire
	 *            id de l'exemplaire emprunté.
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static void emprunter(int idAbonne, int idExemplaire)
			throws SQLException {
		if (estEmprunte(idExemplaire) == false) {
			Connection c = Connexion.getConnection();
			Statement s = c.createStatement();
			Date date_emprunt = new java.sql.Date(
					new java.util.Date().getTime());

			s.executeUpdate("INSERT INTO EMPRUNTS VALUES('" + idAbonne + "','"
					+ idExemplaire + "','" + date_emprunt + "',null,'" + false
					+ "')");
			s.close();
		}
		else{
			throw new SQLException ("l'exemplaire "+idExemplaire+" est déjà emprunté" );
		}

	}

	/**
	 * Retourner un exemplaire à partir de son identifiant.
	 * 
	 * @param idExemplaire
	 *            id de l'exemplaire à rendre.
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static void rendre(int idExemplaire) throws SQLException {
		if (estEmprunte(idExemplaire) == true) {
			Connection c = Connexion.getConnection();
			Statement s = c.createStatement();
			Date date_retour = new java.sql.Date(new java.util.Date().getTime());
			s.executeUpdate("UPDATE EMPRUNTS SET date_retour ='" + date_retour
					+ "' WHERE id_ex=" + idExemplaire);
			s.close();
		}
		else throw new SQLException("L'exemplaire "+idExemplaire+" n'est pas emprunté !");
	}

	//
	/**
	 * Détermine si un exemplaire sonné connu par son identifiant est
	 * actuellement emprunté.
	 * 
	 * @param idExemplaire
	 * @return <code>true</code> si l'exemplaire est emprunté,
	 *         <code>false</code> sinon
	 * @throws SQLException
	 *             en cas d'erreur de connexion à la base.
	 */
	public static boolean estEmprunte(int idExemplaire) throws SQLException {
		boolean estEmprunte = false;

		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		ResultSet r = s
				.executeQuery("SELECT date_retour FROM EMPRUNTS WHERE id_ex="
						+ idExemplaire);
		
		while(r.next()){
			if (r.getString("date_retour") == null){
				estEmprunte = true;
			}
		}
		
		r.close();
		s.close();
		return estEmprunte;
	}

	/**
	 * Récupération des statistiques sur les emprunts (nombre d'emprunts et de
	 * retours par jour).
	 * 
	 * @return un <code>HashMap<String, int[]></code>. Chaque enregistrement de
	 *         la structure de données est identifiée par la date (la clé)
	 *         exprimée sous la forme d'une chaîne de caractères. La valeur est
	 *         un tableau de 2 entiers qui représentent :
	 *         <ul>
	 *         <li>0 : le nombre d'emprunts</li>
	 *         <li>1 : le nombre de retours</li>
	 *         </ul>
	 *         Exemple :
	 * 
	 *         <pre>
	 * +-------------------------+
	 * | "2017-04-01" --> [3, 1] |
	 * | "2017-04-02" --> [0, 1] |
	 * | "2017-04-07" --> [5, 9] |
	 * +-------------------------+
	 * </pre>
	 * 
	 * @throws SQLException
	 */

	public static HashMap<String, int[]> statsEmprunts() throws SQLException {
		HashMap<String, int[]> stats = new HashMap<String, int[]>();
		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("SELECT  date_emprunt, count(*) as nb  FROM EMPRUNTS GROUP BY date_emprunt");

		while (r.next()) {

			String date_emprunt = r.getString("date_emprunt");
			int[] valeur = new int[2];
			valeur[0] = Integer.parseInt(r.getString("nb"));
			valeur[1] = 0;
			stats.put(date_emprunt, valeur);
		}
		ResultSet r2 = s.executeQuery("SELECT  date_retour, count(*) as nb FROM EMPRUNTS GROUP BY date_retour");
		while (r2.next()) {
			String date_retour = r2.getString("date_retour");
			if (date_retour != null) {
				if (stats.containsKey(date_retour)) {
					int[] valeur = stats.get(date_retour);
					valeur[1] = Integer.parseInt(r2.getString("nb"));
					stats.put(date_retour, valeur);
				} 
				else {
					int[] valeur = new int[2];
					valeur[0] = 0;
					valeur[1] = Integer.parseInt(r2.getString("nb"));
					stats.put(date_retour, valeur);
				}
			}
		}
		r2.close();
		s.close();
		return stats; 
	}
}
