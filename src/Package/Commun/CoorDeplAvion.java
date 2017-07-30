/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Commun;
//Ce paquet contient toutes les classes partagées utilisées dans mon Projet

import java.io.Serializable;

/**
 * @author Chiraze Haidar
 */

public class CoorDeplAvion implements Serializable {
//Cette classe contient les coordonnées et le déplacement de l'avion
    
    private String _Id;
    private int _X;
    private int _Y;
    private int _Altitude;
    private int _Cap;
    private int _Vitesse;
  
    public CoorDeplAvion (String Id, int X, int Y, int Altitude, int Cap, int Vitesse)
    {
        _Id = Id;    
        _X = X;
        _Y = Y;
        _Altitude = Altitude;
        _Cap = Cap;
        _Vitesse = Vitesse;
    }

    public CoorDeplAvion ()
    {
    }
    
    /**
     * getter Id
     * @return la coordonnée Id de l'avion
     */
    public String GetId ()
    {
        return _Id;
    }
    
    /**
     * getter X
     * @return la coordonnée X de l'avion
     */
    public int GetX ()
    {
        return _X;
    }
    
    /**
     * getter Y
     * @return la coordonnée Y de l'avion
     */
    public int GetY ()
    {
        return _Y;
    }
    
    /**
     * getter Altitude
     * @return la coordonnée Altitude de l'avion
     */
    public int GetAltitude ()
    {
        return _Altitude;
    }
    
    /**
     * getter Cap
     * @return la coordonnée Cap de l'avion
     */
    public int GetCap ()
    {
        return _Cap;
    }
    
    /**
     * getter Vitesse
     * @return la coordonnée Vitesse de l'avion
     */
    public int GetVitesse ()
    {
        return _Vitesse;
    }
    
    /**
    * setter Id
    * @param Id la coordonnée ID à ajouter à l'avion
    */
    public void SetId (String Id)
    {
        this._Id = Id;
    }
    
    /**
    * setter X
    * @param X la coordonnée X à ajouter à l'avion 
    */
    public void SetX (int X)
    {
        this._X = X;
    }
    
    /**
    * setter Y
    * @param Y la coordonnée Y à ajouter à l'avion 
    */
    public void SetY (int Y)
    {
        this._Y = Y;
    }
    
    /**
    * setter Altitude
    * @param Altitude la coordonnée Altitude à ajouter à l'avion 
    */
    public void SetAltitude (int Altitude)
    {
        this._Altitude = Altitude;
    }
    
    /**
    * setter Cap
    * @param Cap la coordonnée Cap à ajouter à l'avion 
    */
    public void SetCap (int Cap)
    {
        this._Cap = Cap;
    }
    
    /**
    * setter Vitesse
    * @param Vitesse la coordonnée Vitesse à ajouter à l'avion 
    */
    public void SetVitesse (int Vitesse)
    {
        this._Vitesse = Vitesse;
    }
}
