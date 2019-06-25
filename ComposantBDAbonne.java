package biblio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Composant logiciel assurant la gestion des abonnés.
 */
public class ComposantBDAbonne {

  /**
   * Récupération de la liste complète des abonnés.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un abonné.<br/>
   * Il doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : nom</li>
   *   <li>2 : prénom</li>
   *   <li>3 : statut</li>
   *   <li>4 : adresse email</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeTousLesAbonnes() throws SQLException {
    // L'ArrayList qui sera renvoyé : structure de données de type tableau non limitée en taille
    ArrayList<String[]> abonnes = new ArrayList<String[]>();
    Connection c = Connexion.getConnection();
	Statement s = c.createStatement();
	ResultSet r = s.executeQuery("SELECT * FROM ABONNES"); 
	while(r.next()){
		String[] abonne = new String[5];
		abonne[0]=r.getString("id");
		abonne[1]=r.getString("nom");
		abonne[2]=r.getString("prenom");
		abonne[3]=r.getString("statut");
		abonne[4]=r.getString("email");	
		abonnes.add(abonne);
		}
	
	r.close();
	s.close();
    return abonnes;
  }
  

  
  

  /**
   * Retourne le nombre d'abonnés référencés dans la base.
   * 
   * @return le nombre d'abonnés.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int nbAbonnes() throws SQLException {
    Connection c = Connexion.getConnection();
    Statement s = c.createStatement();
	ResultSet r = s.executeQuery("SELECT COUNT(*) AS NB FROM ABONNES  ");
	r.next();
	int nb_abonnes = r.getInt("NB");
    r.close();
    s.close();
    return nb_abonnes;
  }

  /**
   * Récupération des informations sur un abonné à partir de son identifiant.
   * 
   * @param idAbonne : id de l'abonné à rechercher
   * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
   * tableau doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : nom</li>
   *   <li>2 : prénom</li>
   *   <li>3 : statut</li>
   *   <li>4 : adresse email</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static String[] getAbonne(int idAbonne) throws SQLException {
    String[] abonne = new String[5];
    Connection c = Connexion.getConnection();
    Statement s = c.createStatement();
    ResultSet r = s.executeQuery("SELECT  * FROM ABONNES WHERE id="+ Integer.toString(idAbonne));
    while (r.next()){
       	 abonne[0] = r.getString("id");
            abonne[1] = r.getString("nom");
            abonne[2] = r.getString("prenom");
            abonne[3] = r.getString("statut");
            abonne[4] = r.getString("email");
        }
        
        return abonne;
 }
     

  /**
   * Référencement d'un nouvel abonné dans la base de données.
   * 
   * @param nom
   * @param prenom
   * @param statut (deux valeurs possibles <i>Etudiant</i> et <i>Enseignant</i>)
   * @param email
   * @return l'identifiant de l'abonné référencé.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int insererNouvelAbonne(String nom, String prenom, String statut, String email) throws SQLException {
	  Connection c = Connexion.getConnection();
	  Statement s = c.createStatement();
	  s.executeUpdate("insert into abonnes values (nextval('abonnes_id_seq'),'"+nom+"','"+prenom+"','"+statut+"','"+email+"')");
	  ResultSet r = s.executeQuery("select currval('abonnes_id_seq') as id");
	  r.next();
	  int id = r.getInt("id");
	  r.close();
	  s.close();
    return id;
    
  }

  /**
   * Modification des informations d'un abonné donné connu à partir de son
   * identifiant : les nouvelles valeurs (nom, prenom, etc.) écrasent les anciennes.
   * 
   * @param idAbonne : identifiant de l'abonné dont on veut modifier les informations.
   * @param nom
   * @param prenom
   * @param statut (deux valeurs possibles <i>Etudiant</i> et <i>Enseignant</i>)
   * @param email
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void modifierAbonne(int idAbonne, String nom, String prenom, String statut, String email) throws SQLException {
	  Connection c = Connexion.getConnection();
	  Statement s = c.createStatement();
	  s.executeUpdate("UPDATE ABONNES SET nom = '"+nom+"', prenom= '"+prenom+"', statut ='"+statut+"', email ='"+email+"' WHERE id="+idAbonne);
	  s.close();
	  
  }

  /**
   * Suppression d'un abonné connu à partir de son identifiant.
   * 
   * @param idAbonne : identifiant de l'utilisateur
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void supprimerAbonne(int idAbonne) throws SQLException {
		Connection c = Connexion.getConnection();
		Statement s = c.createStatement();
		s.executeUpdate("DELETE FROM ABONNES WHERE id="+idAbonne);
  }
  
}
