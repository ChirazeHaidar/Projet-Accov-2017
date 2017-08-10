/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Commun;
//Ce paquet contient toutes les classes communes utilisées dans mon Projet

import java.net.Socket;
import java.util.Random;
import java.io.IOException;
import java.io.Serializable;
import Package.Objets.Vol;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Package.Interface.VolInterface;

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
    
    int PORT;
    String HOST;
    private Avion _Avion;
    private CoorDeplAvion _CDA;
    private VolInterface _VolInt;
    private ObjectInputStream _In;
    private ObjectOutputStream _Out;
    private Socket _Socket = null;

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
 private Boolean OpenConnexion () 
    {
    //Cette fonction ouvre la connexion entre le socket et le SACA
    //SACA est le gestionnaire de vols
    
        try {
                _Socket = new Socket(HOST, PORT);
                return true;
            } 
        catch (IOException e) 
        {
            System.out.println (e);
            System.out.println("Erreur de connexion:" 
                               + VolInterface.class.getName());
        }
        return false;
    }
    
    public void CloseConnexion () 
    {
    //Cette fonction ferme la connexion déjà établie entre l' Avion et le SACA
        
        try 
        {
            Fermer();
            _Socket.close();
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur de déconnexion" 
                               + VolInterface.class.getName());
        }
    }
    
    public void Envoyer (Message Mess) 
    {
        
        try 
        {
            _Out.reset();
            _Out.writeObject(Mess);
            _Out.flush();
            //La commande 'flush' rince le flux de sortie 'Buffer'.
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur du message envoyé de: " 
                               + VolInterface.class.getName());
        }
    }

    private void Recevoir () 
    {
        try 
        {
            Object Obj = _In.readObject();
            _VolInt.TextArea.append("%n [SACA]: Nouveau message reçu.");
            /*
             * La méthode 'append' concatène la représentation de chaîne
             * de tout autre type de données à la fin de l'objet appelant
             */
            GererMessage(Obj);//fonction a definir apres

        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur Entrée/Sortie: " + Vol.class.getName());
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println("Erreur de Classe: " + Vol.class.getName());
        }
    }

    private void Fermer () 
    {  
        try 
        {
            _Out.reset();
            Message Mess = new Message ("Connexion Fermée","Vol",
                                        _CDA.GetNomVol() ,_Avion,"SACA");
            _Out.writeObject(Mess);
            _Out.flush();

        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur de déconnexion: " 
                              + VolInterface.class.getName());
        }
    }
    
}