package biblio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Composant logiciel assurant la gestion des livres et des exemplaires
 * de livre.
 */
public class ComposantBDLivre {

  /**
   * Récupération de la liste complète des livres.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un livre.<br/>
   * Il doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : isbn10</li>
   *   <li>2 : isbn13</li>
   *   <li>3 : titre</li>
   *   <li>4 : auteur</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeTousLesLivres() throws SQLException {

    ArrayList<String[]> livres = new ArrayList<String[]>();
    Connection c = Connexion.getConnection();
    Statement s = c.createStatement();// etablissement de la connection
   
    ResultSet r = s.executeQuery("SELECT * FROM LIVRE");// On récupère toutes les lignes de la table " Livre"

    while (r.next()) {
      String[] livre = new String[5];
      livre[0] = r.getString("id");// on ajoute les attributs correspondant à chaque livre dans un tableau 
      livre[1] = r.getString("isbn10");
      livre[2] = r.getString("isbn13");
      livre[3] = r.getString("titre");
      livre[4] = r.getString("auteur");

      livres.add(livre);// On ajoute ce tableau à la liste les livres que l'on va renvoyer 
    }
    r.close();
    s.close();

    return livres;
  }

  /**
   * Retourne le nombre de livres référencés dans la base.
   * 
   * @return le nombre de livres.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int nbLivres() throws SQLException {
	  
     Connection c = Connexion.getConnection();
     Statement s = c.createStatement();// etablissement de la connection
    
     ResultSet r =s.executeQuery("SELECT COUNT(id) AS NB  FROM LIVRE ");// on récupère le nombre de lignes de la table livre
     r.next();
     int nb_livres = r.getInt("NB");
     r.close();
     s.close();
     
     

     
    return nb_livres;
     
  }

  /**
   * Récupération des informations sur un livre connu à partir de son identifiant.
   * 
   * @param idLivre : id du livre à rechercher
   * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
   * tableau doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : isbn10</li>
   *   <li>2 : isbn13</li>
   *   <li>3 : titre</li>
   *   <li>4 : auteur</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static String[] getLivre(int idLivre) throws SQLException {
     String[] livre = new String[5];
     Connection c = Connexion.getConnection();// etablissement de la connection
     Statement s = c.createStatement();
     ResultSet r = s.executeQuery("SELECT * FROM LIVRE WHERE id="+ Integer.toString(idLivre));// on récupère la ligne correspondant au livre dont il est question 
    
     r.next();
     livre[0] = r.getString("id");// on récupère les attributs de ce livre et on les met dans un tableau 
     livre[1] = r.getString("isbn10");
     livre[2] = r.getString("isbn13");
     livre[3] = r.getString("titre");
     livre[4] = r.getString("auteur");
          
     return livre;
   }
  
 /**
  * Récupération des informations sur un livre connu à partir de l'identifiant
  * de l'un de ses exemplaires.
  * 
  * @param idExemplaire : id de l'exemplaire
  * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
  * tableau doit contenir 6 éléments (dans cet ordre) :
  * <ul>
  *   <li>0 : id de l'exemplaire</li>
  *   <li>1 : id du livre</li>
  *   <li>2 : isbn10</li>
  *   <li>3 : isbn13</li>
  *   <li>4 : titre</li>
  *   <li>5 : auteur</li>
  * </ul>
  * @throws SQLException en cas d'erreur de connexion à la base.
  */
  public static String[] getLivreParIdExemplaire(int idExemplaire) throws SQLException {
    String[] livre = new String[6];
    Connection c = Connexion.getConnection();
    Statement s = c.createStatement(); // etablissement de la connection
    ResultSet r = s.executeQuery("SELECT * FROM EXEMPLAIRES EX JOIN LIVRE L ON EX.id_Livre=L.id WHERE EX.id_ex="+idExemplaire);// on récupère le résultat de la requête dans un ResultSet
    r.next();// on récupère les attributs que l'on souhaite obtenir
    livre[0]= r.getString("id_ex");
    livre[1]=r.getString("id");
    livre[2]=r.getString("isbn10");
    livre[3]=r.getString("isbn13");
    livre[4]=r.getString("titre");	
    livre[5]=r.getString("auteur");
    
    return livre;
  }

  /**
   * Référencement d'un nouveau livre dans la base de données.
   * 
   * @param isbn10
   * @param isbn13
   * @param titre
   * @param auteur
   * @return l'identifiant (id) du livre créé.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */

  
  public static int insererNouveauLivre(String isbn10, String isbn13, String titre, String auteur) throws SQLException {
	  Connection c = Connexion.getConnection();
	  Statement s = c.createStatement();// etablissement de la connection
	  s.executeUpdate("insert into livre values(nextval('livre_id_seq'),'"+isbn10+"','"+isbn13+"','"+titre+"','"+auteur+"');");// insertion d'une nouvelle ligne dans la table Livre
	  ResultSet r = s.executeQuery("select currval('livre_id_seq') as id");// on récupère l'identifiant pour le renvoyer
	  r.next();
	  int id = r.getInt("id");
	  r.close();
	  s.close();
    return id;
  }
  
/**
   * Modification des informations d'un livre donné connu à partir de son
   * identifiant : les nouvelles valeurs (isbn10, isbn13, etc.) écrasent les
   * anciennes.
   * 
   * @param idLivre : id du livre à modifier.
   * @param isbn10 : nouvelle valeur d'isbn10.
   * @param isbn13 : nouvelle valeur d'isbn13.
   * @param titre : nouvelle valeur du titre.
   * @param auteur : nouvel auteur.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void modifierLivre(int idLivre, String isbn10, String isbn13, String titre, String auteur) throws SQLException {
	Connection c = Connexion.getConnection();
	Statement s = c.createStatement();// etablissement de la connection
	s.executeUpdate("UPDATE LIVRE SET isbn10 = '"+isbn10+"', isbn13= '"+isbn13+"', titre ='"+titre+"', auteur ='"+auteur+"' WHERE id="+idLivre);// actualisation des attributs du livre considéré
	s.close();
  }

  /**
   * Suppression d'un abonné connu à partir de son identifiant.
   * 
   * @param idLivre : id du livre à supprimer.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static void supprimerLivre(int idLivre) throws SQLException {
	Connection c = Connexion.getConnection();
	Statement s = c.createStatement();// etablissement de la connection
	s.executeUpdate("DELETE FROM LIVRE WHERE id="+idLivre);// suppression du livre en question
   }

   /**
    * Retourne le nombre d'exemplaire d'un livre donné connu à partir
    * de son identifiant.
    * 
    * @param idLivre : id du livre dont on veut connaître le nombre d'exemplaires.
    * @return le nombre d'exemplaires
    * @throws SQLException en cas d'erreur de connexion à la base.
    */
   public static int nbExemplaires(int idLivre) throws SQLException {
    Connection c = Connexion.getConnection();
    Statement s = c.createStatement();// etablissement de la connection
    
    ResultSet r =s.executeQuery("SELECT COUNT(id_ex) AS NB  FROM EXEMPLAIRES WHERE id_Livre="+idLivre); //On récupère le nombre d'exemplaires du livre dont l'identifiant est idLivre
    r.next();
    int nb_Exemplaires = r.getInt("NB");
    r.close();
    s.close();
     return nb_Exemplaires;
   }

  /**
   * Récupération de la liste des identifiants d'exemplaires d'un livre donné
   * connu à partir de son identifiant.
   * 
   * @param idLivre : identifiant du livre dont on veut la liste des exemplaires.
   * @return un <code>ArrayList<Integer></code> contenant les identifiants des exemplaires
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<Integer> listeExemplaires(int idLivre) throws SQLException {
    ArrayList<Integer> exemplaires = new ArrayList<Integer>();
    Connection c = Connexion.getConnection();
	Statement s = c.createStatement();// etablissement de la connection
	ResultSet r = s.executeQuery("SELECT id_ex AS id FROM EXEMPLAIRES WHERE id_Livre="+Integer.toString(idLivre));//on récupère tous les exemplaires associés au livre ayant pour identifiant idLivre

    while (r.next()) {
    int id_exemplaire = Integer.parseInt(r.getString("id"));// Transformation de l'identifiant ( récupéré en "String") en "int"
      exemplaires.add(id_exemplaire); // on ajoute l'identifiant de l'exemplaire à la liste d'identifiants d'exemplaires 
      }
    r.close();
    s.close();

    
    return exemplaires;
  }

  /**
   * Ajout d'un exemplaire à un livre donné connu par son identifiant.
   * 
   * @param id identifiant du livre dont on veut ajouter un exemplaire.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static void ajouterExemplaire(int idLivre) throws SQLException {
	   Connection c = Connexion.getConnection();
	   Statement s = c.createStatement();// etablissement de la connection
		  s.executeUpdate("insert into EXEMPLAIRES values(nextval('exemplaires_id_ex_seq'),'"+idLivre+"','"+0+"');");// On ajoute une ligne à la table exemplaires
		  s.close();
   }

    /**
     * Suppression d'un exemplaire donné connu par son identifiant.
     * 
     * @param idExemplaire : identifiant du livre dont on veut supprimer un exemplaire.
     * @throws SQLException en cas d'erreur de connexion à la base.
     */
   public static void supprimerExemplaire(int idExemplaire) throws SQLException {
     Connection c = Connexion.getConnection();
	   Statement s = c.createStatement();// etablissement de la connection
	s.executeUpdate("DELETE FROM EXEMPLAIRES WHERE id_ex="+idExemplaire);// On supprime une liste de la table exemplaires
  
   }

}
