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

public class CoorDeplAvion implements Serializable 
{
    //Cette classe contient les coordonnées et le déplacement de l'avion
    //De plus, elle contient les informations sur l'avion    
    
    public static int _NumVol;
    public static String _NomVol;
  
    public static int _X;
    public static int _Y;
    public static int _Angle;
    public static int _Vitesse;
    public static int _Altitude;
  
    /**
     * @param NumVol
     * @param NomVol
     * @param X
     * @param Y
     * @param Angle
     * @param Vitesse
     * @param Altitude
    **/
    
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
  
    /**
     * @return la coordonnée _NumVol de l'avion
     */
    public static int GetNumVol ()
    {
        return _NumVol;
    }
    
    /**
     * @return la coordonnée _NomVol de l'avion
     */
    public static String GetNomVol ()
    {
        return _NomVol;
    }
    
    /**
     * @return la coordonnée _X de l'avion
     */
    public static int GetX ()
    {
        return _X;
    }
    
    /**
     * @return la coordonnée _Y de l'avion
     */
    public static int GetY ()
    {
        return _Y;
    }
    
    /**
     * @return la coordonnée _Altitude de l'avion
     */
    public static int GetAltitude ()
    {
        return _Altitude;
    }
    
    /**
     * @return la coordonnée _Angle de l'avion
     */
    public static int GetAngle ()
    {
        return _Angle;
    }
    
    /**
     * @return la coordonnée _Vitesse de l'avion
     */
    public static int GetVitesse ()
    {
        return _Vitesse;
    }
   
     /**
    * @param NumVol la coordonnée NumVol à ajouter à l'avion
    */
    public static void SetNumVol (int NumVol)
    {
        _NumVol = NumVol;
    }
   
    /**
    * @param NomVol la coordonnée NomVol à ajouter à l'avion
    */
    public static void SetNomVol (String NomVol)
    {
        _NomVol = NomVol;
    }
    
    /**
    * @param X la coordonnée X à ajouter à l'avion 
    */
    public static void SetX (int X)
    {
        _X = X;
    }
    
    /**
    * @param Y la coordonnée Y à ajouter à l'avion 
    */
    public static void SetY (int Y)
    {
        _Y = Y;
    }
    
    /**
    * @param Altitude la coordonnée Altitude à ajouter à l'avion 
    */
    public static void SetAltitude (int Altitude)
    {
        _Altitude = Altitude;
    }
    
    /**
    * @param Angle la coordonnée Angle à ajouter à l'avion 
    */
    public static void SetAngle (int Angle)
    {
        _Angle = Angle;
    }
    
    /**
    * @param Vitesse la coordonnée Vitesse à ajouter à l'avion 
    */
    public static void SetVitesse (int Vitesse)
    {
        _Vitesse = Vitesse;
    }

    public CoorDeplAvion() 
    {
    }
}
