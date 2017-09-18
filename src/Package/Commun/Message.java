/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Commun;

//Ce paquet contient toutes les classes communes utilisées dans mon Projet

import java.io.Serializable;

/**
 *
 * @author Chiraze Haidar
 */

public class Message implements Serializable 
{
//Cette classe est utilisée pour prédéfinir les messages de communication
//Je l'ai mis à part, pour pouvoir l'utiliser dans toutes les autres classes
    
    public String _Command;
    //Les commandes qui se passent entre:
    //Avion, Radar, Controlleur et SACA
    public String _Type;
    //Avion, Radar, Controlleur ou SACA
    public String _Envoyeur;
    //Celui qui envoye le message
    public String _Receveur;
    //Celui qui reçoie le message
    public Avion _Avion;
    //Les informations de l'avion en question

    public Message (String Comm, String Typ, String Env, Avion Av, 
                         String Rec) 
    {
        this._Command = Comm;
        this._Type = Typ;
        this._Envoyeur = Env;
        this._Avion = Av;
        this._Receveur = Rec;
    }
    
       
    public String EnvoyeMessage ()    
    {
        return "{Type de Commande: '" + _Command + "', Type: '" + _Type + "%s%n"
             + "Envoyeur: '" + _Envoyeur + "', Receveur: '" + _Receveur + "%s%n"
             + "Avion: '" + _Avion.ReturnInfo() + "}";
    }    
}
