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
//De plus, elle contient les informations sur l'avion    
    private int _NumVol;
    private String _NomVol;
    private int _X;
    private int _Y;
    private int _Altitude;
    private int _Angle;
    private int _Vitesse;
  
    /**
     *
     * @param NumVol
     * @param NomVol
     * @param X
     * @param Y
     * @param Altitude
     * @param Angle
     * @param Vitesse
     */
    public CoorDeplAvion (int NumVol, String NomVol, int X, int Y, int Altitude, int Angle, int Vitesse)
    {
        _NumVol = NumVol;
        _NomVol = NomVol;
        _X = X;
        _Y = Y;
        _Altitude = Altitude;
        _Angle = Angle;
        _Vitesse = Vitesse;  
    }
  
    public CoorDeplAvion ()
    {
    }
    
    /**
     * @return la coordonnée _NumVol de l'avion
     */
    public int GetNumVol ()
    {
        return _NumVol;
    }
    
    /**
     * @return la coordonnée _NomVol de l'avion
     */
    public String GetNomVol ()
    {
        return _NomVol;
    }
    
    /**
     * @return la coordonnée _X de l'avion
     */
    public int GetX ()
    {
        return _X;
    }
    
    /**
     * @return la coordonnée _Y de l'avion
     */
    public int GetY ()
    {
        return _Y;
    }
    
    /**
     * @return la coordonnée _Altitude de l'avion
     */
    public int GetAltitude ()
    {
        return _Altitude;
    }
    
    /**
     * @return la coordonnée _Angle de l'avion
     */
    public int GetAngle ()
    {
        return _Angle;
    }
    
    /**
     * @return la coordonnée _Vitesse de l'avion
     */
    public int GetVitesse ()
    {
        return _Vitesse;
    }
   
     /**
    * @param NumVol la coordonnée NumVol à ajouter à l'avion
    */
    public void SetNumVol (int NumVol)
    {
        this._NumVol = NumVol;
    }
   
    /**
    * @param NomVol la coordonnée NomVol à ajouter à l'avion
    */
    public void SetNomVol (String NomVol)
    {
        this._NomVol = NomVol;
    }
    
    /**
    * @param X la coordonnée X à ajouter à l'avion 
    */
    public void SetX (int X)
    {
        this._X = X;
    }
    
    /**
    * @param Y la coordonnée Y à ajouter à l'avion 
    */
    public void SetY (int Y)
    {
        this._Y = Y;
    }
    
    /**
    * @param Altitude la coordonnée Altitude à ajouter à l'avion 
    */
    public void SetAltitude (int Altitude)
    {
        this._Altitude = Altitude;
    }
    
    /**
    * @param Angle la coordonnée Angle à ajouter à l'avion 
    */
    public void SetAngle (int Angle)
    {
        this._Angle = Angle;
    }
    
    /**
    * @param Vitesse la coordonnée Vitesse à ajouter à l'avion 
    */
    public void SetVitesse (int Vitesse)
    {
        this._Vitesse = Vitesse;
    }
}
