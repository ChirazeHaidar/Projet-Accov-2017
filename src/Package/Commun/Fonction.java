/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Commun;
//Ce paquet contient toutes les classes communes utilisées dans mon Projet

import java.util.Random;
import java.io.Serializable;

/**
 *
 * @author Chiraze Haidar
 */

public class Fonction implements Serializable 
{
/*
 *Cette classe contient toutes les fonctions utilisées
 *dans la connexion entre Client et Serveur
 */
    

    private String RandomNomVol ()
    {
    //Cette fonction donne un nom au Hazard au Vol
        
        String Lettres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Chiffres = "0123456789";
        
        Random Nom = new Random();
        //Le nom de Vol est formée de deux lettres et de trois chiffres
        
        StringBuilder Tampon = new StringBuilder();
        /*
         *StringBuilder est utilisée car il est plus rapide et consomme moins de mémoire
         *On l'utilise avec 'append' et ensuite 'toString'
         */
        for (int i = 0; i < 2; i++) 
        {
            Tampon.append(Lettres.charAt(Nom.nextInt(Lettres.length())));
            //nextInt(int): Retourne la prochaine valeur pseudorandom
            //X.length(): Renvoie la longueur de la chaîne X
            //X.chartAt(int):Renvoie la valeur de char à l'index spécifié.
        }
        
        for (int j = 0; j < 3; j++) 
        {
            Tampon.append(Chiffres.charAt(Nom.nextInt(Chiffres.length())));
        }
        
        return Tampon.toString();
    }
    
}