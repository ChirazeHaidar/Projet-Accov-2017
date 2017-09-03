/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Commun;
//Ce paquet contient toutes les classes communes utilisées dans mon Projet

import java.util.List;
import java.net.Socket;
import java.util.Random;
import java.util.Optional;
import java.io.IOException;
import Package.Sockets.Vol;
import java.util.ArrayList;
import Package.Sockets.SACA;
import Package.Sockets.Radar;
import Package.Threads.VolThread;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Package.Sockets.Controleur;
import javax.swing.DefaultListModel;
import Package.Interface.VolInterface;
import Package.Interface.SACAInterface;
import Package.Interface.RadarInterface;
import Package.Interface.ControleurInterface;

/**
 *
 * @author Chiraze Haidar
 */

public class Fonction extends Thread
{
/*
 *Cette classe contient toutes les fonctions utilisées
 *dans la connexion entre Client et Serveur
 */
    
    public int PORT = 6633;
    public String HOST = "localhost";
    
    public int ALTMAX = 20000;
    public int ALTMIN = 0;
    
    public int VITMAX = 1000;
    public int VITMIN = 200;
    
    public int PAUSE = 2000;
    //2 secondes
    
    private Avion _Avion;
    public Avion _VolActualise;
    private CoorDeplAvion _CDA;
    
    private VolInterface _VolInt;
    private RadarInterface _RadInt;
    private SACAInterface _SACAInt;
    private ControleurInterface _ContInt;

    private ObjectInputStream _In;
    private ObjectOutputStream _Out;
    
    private Socket _Socket = null;
    public boolean _TravailContinuel = true;

    String _NomRadar;
    String _NomControleur;
    Optional <Avion> _VolOptionnel;
    public static List <Avion> _VolDetecte;

    public VolThread _VolThread;
    public VolThread _ControlleurThread;
    public DefaultListModel _ListeVol;
    
//<editor-fold defaultstate="collapsed" desc="Méthodes pour le Socket 'Vol'">

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
    /**
     * ******************************
     *** Fonctions gérant le déplacement de l'avion : ne pas modifier
     * ******************************
     */
    
    public void Initialiser_Avion() 
    {
    // intialisation des paramétres de l'avion d'une façon aléatoire    
        
        _CDA = new CoorDeplAvion();
        Random GenerateurNumero = new Random();

        _CDA.SetX ((int) (1000 + GenerateurNumero.nextInt(100) % 1000));
        _CDA.SetY ((int) (1000 + GenerateurNumero.nextInt(100) % 1000));
        _CDA.SetAltitude((int) (900 + GenerateurNumero.nextInt(100) % 100));
        _CDA.SetAngle ((int) (GenerateurNumero.nextInt(100) % 360));
        _CDA.SetVitesse((int) (600 + GenerateurNumero.nextInt(100) % 200));
        _CDA.SetNomVol((String) RandomNomVol());
        
        _Avion = new Avion(_CDA);
        
        _VolInt.setTitle(_CDA.GetNomVol());
    }

    private void ChangerVitesse(int Vitesse) 
    {
    //Modification de la valeur de l'avion avec la valeur passée en paramètre
        
        if (Vitesse < 0) 
        {
            _CDA.SetVitesse(0);
        } 
        else if (Vitesse > VITMAX) 
        {
            _CDA.SetVitesse(VITMAX);
        } 
        else 
        {
            _CDA.SetVitesse(Vitesse);
        }
    }

    private void ChangerAngle (int Angle) 
    {
    //Modification de l'Angle "CAP" de l'avion avec la valeur passée en paramètre

        if ((Angle >= 0) && (Angle < 360)) 
        {
            _CDA.SetAngle(Angle);
        }
    }

    private void ChangerAltitude (int Alt) 
    {
    //Modification de l'altitude de l'avion avec la valeur passée en paramètre

        if (Alt < 0) 
        {
            _CDA.SetAltitude (0);
        } 
        else if (Alt > ALTMAX) 
        {
            _CDA.SetAltitude(ALTMAX);
        } 
        else 
        {
            _CDA.SetAltitude(Alt);
        }
    }

    private Boolean CalculDeplacement() 
    {
    //Recalcule la localisation de l'avion en fonction de sa vitesse et de son Angle
    
        double cosinus;
        double sinus;
        //cos et sin ont un paramétre en radian, dep.cap en degré nos habitudes francophone

        double DepX;
        double DepY;

        if (_CDA.GetVitesse() < VITMIN) 
        {
            _VolInt.TextArea.append("%nVitesse trop faible: Avion va s'écraser%n");
             Message _Mess = new Message("Alert: Accident Avion", 
                                         "Vol", _CDA.GetNomVol(), _Avion, "Radar");
            Envoyer(_Mess);
            CloseConnexion ();
            return false;

        }
        else if (_CDA.GetAltitude() == 0) 
        {
            _VolInt.TextArea.append("%nL'avion s'est ecrase au sol\n");
            Message _Mess = new Message("Alert: L'avion s'est ecrase au sol", 
                                        "Vol", _CDA.GetNomVol(), _Avion, "Radar");
            
            Envoyer(_Mess);
            CloseConnexion ();
            return false;

        }
        
        /**
         *Angle en radian = pi * (angle en degré) / 180 
         *Angle en radian = pi * (angle en grade) / 200 
         *Angle en grade = 200 * (angle en degré) / 180 
         *Angle en degré = 180 * (angle en grade) / 200 
         *Angle en grade = 200 * (angle en radian) / pi 
         *Angle en degré = 180 * (angle en radian) / pi 
        **/

        cosinus = cos(_CDA.GetAngle() * 2 * Math.PI / 360);
        sinus = sin(_CDA.GetAngle() * 2 * Math.PI / 360);

        //newPOS = oldPOS + Vt
        DepX = cosinus * _CDA.GetVitesse() * 10 / VITMIN;
        DepY = sinus * _CDA.GetVitesse() * 10 / VITMIN;

        //On se déplace d'au moins une case quels que soient l'Angle et la Vitesse
        //sauf si l'Angle est un des angles droits
        if ((DepX > 0) && (DepX < 1)) 
        {
            DepX = 1;
        }
        else if ((DepX < 0) && (DepX > -1)) 
        {
            DepX = -1;
        }

        if ((DepY > 0) && (DepY < 1)) 
        {
            DepY = 1;
        }
        else if ((DepY < 0) && (DepY > -1)) 
        {
            DepY = -1;
        }

        _CDA.SetX(_CDA.GetX() + (int) DepX);
        _CDA.SetY(_CDA.GetY() + (int) DepY);
        _Avion.SetInfo(_CDA);
        
        AfficherMessage ();
        return true;
    }

    public void SeDeplacer() 
    {
    //Fonction principale: gère l'exécution de l'avion au fil du temps

        if (CalculDeplacement ()) 
        {
            Envoyer_Caracteristique ();
        }
    }

    private void Envoyer_Caracteristique() 
    {
    /*Cette fonction envoye les différentes caractéristiques 
     *de l'avion courante au gestionnnaire de vol
    */
         
        Message Mess = new Message("Information", "Vol", _CDA.GetNomVol(),
                                   _Avion, "Radar");
        Envoyer (Mess);
    }

    public void AfficherMessage () 
    {
    //Affichage des caractéristiques courantes de l'avion
        _VolInt.TextArea.append("%n" + _Avion.GetInfo());
    }
    
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes partagées entre les différents Sockets">
    
    public Boolean OpenConnexion () 
    {
        System.out.println("ici");
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
    //Cette fonction ferme la connexion déjà établie entre le Socket et le SACA
        
        try 
        {
            _ContInt.TextArea.append("%nLe Controleur: "  + Controleur.class.getName() 
                                   + "est bien déconnecté");
            _VolInt.TextArea.append("%nLe Vol: "  + Vol.class.getName() 
                                   + "est bien déconnecté");
            _RadInt.TextArea.append("%nLe Radar: "  + Radar.class.getName() 
                                   + "est bien déconnecté");
            
            _TravailContinuel = false;
            
            Fermer();
            
            if (_Socket != null) 
            {
                _Socket.close();
            }
            if (_In != null) 
            {
                _In.close();
            }
            if (_Out != null) 
            {
                _Out.close();
            }
            this.interrupt();
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur de déconnexion" 
                               + VolInterface.class.getName());
            System.out.println("Erreur de déconnexion" 
                               + ControleurInterface.class.getName());
            System.out.println("Erreur de déconnexion" 
                               + RadarInterface.class.getName());
        }
    }
    
    public synchronized void Envoyer (Message Mess) 
    {
        try 
        {
            if (Mess._Type.equals("Controleur")) 
            {
                this._Avion.Verrouiller();

            }
            
            _Out.reset();
            _Out.writeObject(Mess);
            _Out.flush();
            //La commande 'flush' rince le flux de sortie 'Buffer'.
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            switch (Mess._Type) 
            {
                case "Vol":
                {
                    System.out.println("Erreur du message envoyé du Vol: "
                                      + Vol.class.getName());
                break;
                } 
                case "Controleur":
                {
                    System.out.println("Erreur du message envoyé du Controleur: "
                                      + Controleur.class.getName());
                    break;
                }
                case "Radar":
                {
                    System.out.println("Erreur du message envoyé du Radar: "
                                      + Radar.class.getName());
                    break;
                }
                default: break;
            } 
            
        }
    }

    public void Recevoir ()
    {
        try 
        {
            Object Objet = _In.readObject();

            if (_Socket != null && !_Socket.isClosed() && _TravailContinuel && Objet != null) 
            {
            } 
            else 
            {
                System.out.println ("%nLe message reçu est invalide");
                return;
            }
            
            _VolInt.TextArea.append("%n [SACA]: Nouveau message reçu.");
            /*
             * La méthode 'append' concatène la représentation de chaîne
             * de tout autre type de données à la fin de l'objet appelant
             */
            GererMessage(Objet);

        } 
        catch (IOException  | ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println("%nErreur!!!");
        } 
        
    }

    private void Fermer () 
    {  
        try 
        {
            _ContInt.TextArea.append ("%nLa Connexion du Controleur: "  + Controleur.class.getName() 
                                    + "est bien fermée");
            _VolInt.TextArea.append ("%nLa Connexion du Vol: "  + Vol.class.getName() 
                                   + "est bien Fermée");
            _RadInt.TextArea.append ("%nLa Connexion du Radar: "  + Radar.class.getName() 
                                   + "est bien Fermée");
                
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
     
    private void GererMessage(Object Obj) 
    {
        if (Obj == null) 
        {
            _VolInt.TextArea.append ("%n Message Invalide!!");
            _ContInt.TextArea.append ("%nMessage Invalide!!");
            _RadInt.TextArea.append ("%nMessage Invalide!!");
            return;
            //J'ai utilisé return pour sortie de la fonction
        }

        Message _Mess = Message.class.cast (Obj);
        
        switch (_Mess._Type) 
        {
            case "Vol":
            {
                _VolThread = ChercherThread (_Mess._Avion._Info.GetNumVol());
                //Chercher le thread du Vol
                
                if (_VolThread == null)
                {
                    _SACAInt.TextArea.append ("Le Vol: '%s', est introuvable." + _Mess._Receveur);
                    return;
                }
                
                switch (_Mess._Command) 
                {
                    case "Connection":
                    {
                        SACA._VolThreads.remove (_VolThread);
                        if (SACA._RadarThreads != null && SACA._RadarThreads.size() > 0)
                        {
                            for (int i = 0; i < SACA._RadarThreads.size(); i++)
                            {
                                Message NouveauMessage = new Message ("Attention: La Connexion est fermée",
                                                                      "Radar", _Mess._Envoyeur, _Mess._Avion, 
                                                                      "Radar/Controlleur" + SACA._RadarThreads.get(i).GetNum());
                                
                                SACA._RadarThreads.get(i);
                                Envoyer (NouveauMessage);
                            }
                        }
                        break;
                    }
                    case "MiseAJour":
                    {
                        _VolThread._Avion = _Mess._Avion;
                        _SACAInt.TextArea.append ("%n Envoyeur du message: '" + _Mess._Envoyeur
                                                + "', Recipient du message: '" + _Mess._Receveur
                                                + "', Commande du message: '" + _Mess._Command
                                                + "'");
                        if (SACA._RadarThreads != null && SACA._RadarThreads.size () > 0)
                        {
                            for (int i = 0; i < SACA._RadarThreads.size(); i++)
                            {
                                Message NouveauMessage = new Message (_Mess._Command, "Radar", _Mess._Envoyeur, _Mess._Avion, 
                                                                      "Radar/Controlleur" + SACA._RadarThreads.get(i).GetNum());
                                
                                SACA._RadarThreads.get(i);
                                Envoyer (NouveauMessage);
                            }
                        }
                        break;
                    }
                    case "Information"://Information sur le Vol
                    {
                        _VolThread._Avion = _Mess._Avion;
                        _SACAInt.TextArea.append ("%n L'envoyeur du message: '" + _Mess._Envoyeur +
                                                  "' --> Tous: La position du Vol '" + _Mess._Avion._Info.GetNomVol() +
                                                  "'avait été mise à jour");
                        
                        if (SACA._RadarThreads != null && SACA._RadarThreads.size () > 0)
                        {
                            for (int i = 0; i < SACA._RadarThreads.size(); i++)
                            {
                                Message NouveauMessage = new Message ("Information", "Radar", _Mess._Envoyeur, _Mess._Avion, 
                                                                      "Radar/Controlleur" + SACA._RadarThreads.get(i).GetNum());
                                
                                SACA._RadarThreads.get(i);
                                Envoyer (NouveauMessage);
                            }
                        }
                        break;
                    }
                    case "Alerte"://Avion est accidenté ou débarqué
                    {
                        _VolThread._Avion = _Mess._Avion;
                        _SACAInt.TextArea.append("%nEnvoyeur '" + _Mess._Envoyeur
                                               + "' --> Recipient '" +_Mess._Receveur
                                               + "': " + _Mess._Command);
                        SACA._VolThreads.remove(_VolThread);
                        _VolThread.interrupt();
                        
                        if (SACA._RadarThreads != null && SACA._RadarThreads.size () > 0)
                        {
                            for (int i = 0; i < SACA._RadarThreads.size(); i++)
                            {
                                SACA._RadarThreads.get(i);
                                Envoyer (_Mess);
                            }
                        }
                        break;
                    }
                    case "Changer Angle":
                    {
                        _VolInt.TextArea.append("%n'" + _Mess._Envoyeur +
                                                "': Changer Angle.");
                        ChangerAngle (_Mess._Avion._Info.GetAngle());
                        Message NouveauMessage = new Message("AngleModifie", "Vol",
                                  _CDA.GetNomVol(), _Mess._Avion, _Mess._Envoyeur);
                        Envoyer (NouveauMessage);
                        _VolInt.TextArea.append("%n'" + _Mess._Envoyeur
                                              + "': L'Angle est bien modifiée.");
                        AfficherMessage ();
                        break;
                    }
                    case "Changer Vitesse":
                    {
                        _VolInt.TextArea.append("%n'" + _Mess._Envoyeur +
                                                "': Changer Vitesse.");
                        ChangerVitesse (_Mess._Avion._Info.GetVitesse());
                        
                        Message NouveauMessage = new Message("VitesseModifie", "Vol",
                                    _CDA.GetNomVol(), _Mess._Avion, _Mess._Envoyeur);
                        
                        Envoyer (NouveauMessage);
                        _VolInt.TextArea.append("%n'" + _Mess._Envoyeur
                                              + "': La vitesse est bien modifiée.");
                        AfficherMessage ();
                        break;
                    }
                    case "changer Altitude":
                    {
                        _VolInt.TextArea.append("%n'" + _Mess._Envoyeur +
                                                "': Changer Altitude.");
                        ChangerAltitude (_Mess._Avion._Info.GetAltitude());
                        
                        Message NouveauMessage = new Message("AltitudeModifie", "Vol",
                                     _CDA.GetNomVol(), _Mess._Avion, _Mess._Envoyeur);
                        
                        Envoyer (NouveauMessage);
                        _VolInt.TextArea.append("%n'" + _Mess._Envoyeur
                                              + "': L'Altitude est bien modifiée.");
                        AfficherMessage ();
                        break;
                    }
                    default: break;
                }
                break;
            }
            case "Controleur":
            {
                _VolThread = ChercherThread (_Mess._Avion._Info.GetNumVol());
                //Chercher le Vol pour pouvoir envoyer la commande
                        
                if (_VolThread == null)
                {
                    _SACAInt.TextArea.append("%nLe Vol '" + _Mess._Receveur + "' est introuvable!!!");
                    return;
                }
                        
                switch (_Mess._Command) 
                {
                    case "Connection"://Utiliser juste comme information
                    {
                        _SACAInt.TextArea.append ("%nEnvoyeur: '" + _Mess._Envoyeur + 
                                                  "' --> 'SACA': Attention; La Connexion est fermée");
                        
                        _ControlleurThread = ChercherRadarThread (Integer.parseInt (_Mess._Envoyeur));
                        
                        if (_ControlleurThread != null)
                        {
                            SACA._RadarThreads.remove(_ControlleurThread);
                        }
                        break;
                    }
                    case "Information":
                    {
                        _ContInt.TextArea.append("%n'" + _Mess._Envoyeur + 
                                                 "':Les Caractéristiques de l'avion sont bien modifiées");
                        ReactualiserVol (_Mess);
                        break;
                    }
                    case "MiseAJour":
                    {
                        _ContInt.TextArea.append("%n'" + _Mess._Envoyeur + "': " + _Mess._Command);
                        ReactualiserVol (_Mess);
                     
                        _VolActualise = ReactualiserVol(_Mess);
                      
                        if (_VolActualise != null) 
                        {
                            _VolActualise.Deverrouiller();
                        }
                        else 
                        {
                        //Avion est accidenté ou débarqué
                            SupprimerVol (_Mess);
                            _ContInt.TextArea.append("%n'" + _Mess._Envoyeur + "': " + _Mess._Command);
                        }
                        break;
                    }
                    case "Changer Angle":
                    {
                        _SACAInt.TextArea.append ("%nEnvoyeur '" + _Mess._Envoyeur +
                                                  "' --> Recipient '" + _Mess._Receveur +
                                                  "': Changement de l'Angle du Vol");
                        _VolThread.Envoyer(_Mess);
                        break;
                    }
                    case "Changer Altitude":
                    {
                        _SACAInt.TextArea.append ("%nEnvoyeur '" + _Mess._Envoyeur +
                                                  "' --> Recipient '" + _Mess._Receveur +
                                                  "': Changement de l'Altitude du Vol");
                        _VolThread.Envoyer(_Mess);
                        break;
                    }
                    case "Changer Vitesse":
                    {
                        _SACAInt.TextArea.append ("%nEnvoyeur '" + _Mess._Envoyeur +
                                                  "' --> Recipient '" + _Mess._Receveur +
                                                  "': Changement de la Vitesse du Vol");
                        _VolThread.Envoyer(_Mess);
                        break;
                    }
                    default: break;
                }
                break;
            }
            case "Radar":
            {
                switch (_Mess._Command) 
                {
                    case "Information": 
                        {
                            _RadInt.TextArea.append("%n'" + _Mess._Envoyeur + 
                                                    "':Les Caractéristiques de l'avion sont bien modifiées");
                            ReactualiserVol (_Mess);
                            break;
                        }
                        default:
                        {
                            SupprimerVol(_Mess);
                            _RadInt.TextArea.append("%n'" + _Mess._Envoyeur + "': " + _Mess._Command);
                            break;
                        }
                    }
                }
                default: break;
            }
        }
   
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes Pour le Socket Controleur'">

    public  void ChangementAngle (int NumVol, int Angle) throws InterruptedException 
    {
        if (_VolDetecte == null) 
        {
            return;
        }
        
        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == NumVol).findFirst();

        if (_VolOptionnel == null) 
        {
            return;
        }
        
        _Avion = _VolOptionnel.get();

        _Avion.Verrouiller();
        
        _ContInt.TextArea.append("%n'Avion: " + _Avion._Info.GetNomVol() + "': Changement de l'Angle");
        
        _Avion._Info.SetAngle(Angle);
       
        Message _Mess = new Message ("Changer Angle", "Controleur", _NomControleur, 
                                      _Avion, _Avion._Info.GetNomVol());
        Envoyer (_Mess);
    }

    public void ChangementAltitude (int _NumVol, int Altitude) throws InterruptedException 
    {
        if (_VolDetecte == null) 
        {
            return;
        }
        
        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _NumVol).findFirst();

        if (_VolOptionnel == null) 
        {
            return;
        }

        _Avion = _VolOptionnel.get();

        _Avion.Verrouiller();
        
        _ContInt.TextArea.append("%n'Avion: " + _Avion._Info.GetNomVol() + "': Changement de l'Altitude");
        
        _Avion._Info.SetAltitude(Altitude);
        
        Message _Mess = new Message("changer Altitude", "Controleur", _NomControleur,
                                     _Avion, _Avion._Info.GetNomVol());
        Envoyer(_Mess);
    }

    public void ChangementVitesse (int _NumVol, int Vitesse) throws InterruptedException 
    {
        if (_VolDetecte == null) 
        {
            return;
        }
        
        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _NumVol).findFirst();

        if (_VolOptionnel == null) 
        {
            return;
        }

        _Avion = _VolOptionnel.get();

        _Avion.Verrouiller();
        
        _ContInt.TextArea.append("%n'Avion: " + _Avion._Info.GetNomVol() + "': Changement de la Vitesse");
        
        _Avion._Info.SetVitesse (Vitesse);
        
        Message _Mess = new Message("changer Vitesse", "Controleur", _NomControleur,
                                     _Avion, _Avion._Info.GetNomVol());
        Envoyer(_Mess);
    }

    private void SupprimerVol (Message _Mess) 
    {
        if (_VolDetecte == null) 
        {
            _VolDetecte = new ArrayList();
        }
        
        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _Mess._Avion._Info.GetNumVol()).findFirst();
        
        if (_VolOptionnel != null && _VolOptionnel.isPresent()) 
        {
            _VolDetecte.remove(_VolOptionnel.get());

        }
    }
        
    private Avion ReactualiserVol (Message _Mess) 
    {

        if (_VolDetecte == null) 
        {
            _VolDetecte = new ArrayList();
        }
        
        _VolActualise = null;
        
        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _Mess._Avion._Info.GetNumVol()).findFirst();
        
        if (_VolOptionnel != null && _VolOptionnel.isPresent()) 
        {
            _VolActualise = _VolOptionnel.get();
            _VolActualise.SetInfo(_Mess._Avion.GetInfo());

        } 
        else 
        {
            _VolDetecte.add(_Mess._Avion);
            _VolActualise = _Mess._Avion;
        }
        
        _ListeVol = new DefaultListModel();

        for (int i = 0; i < _VolDetecte.size(); i++) 
        {
            _ListeVol.addElement(_VolDetecte.get(i).GetInfo());
        }
        
        _ContInt.ListeVol.setModel(_ListeVol);
        
        return _VolActualise;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes Pour le Socket Radar'">

    public void RemplirListeVol (Object ListeVol) 
    {
        if (ListeVol != null) 
        {
            _VolDetecte = (ArrayList) ListeVol;
            
            _ListeVol = new DefaultListModel();
            
            for (int i = 0; i < _VolDetecte.size(); i++) 
            {
                _ListeVol.addElement(_VolDetecte.get(i).GetInfo());

            }
            _RadInt.ListeVol.setModel(_ListeVol);
        }
    }

    public Object RecevoirListeVol() 
    {
        try 
        {
            return _In.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println("%nLe Radar: '" + Radar.class.getName() +
                               "' n'a pas reçu la liste de vol!!");
        }
        return null;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes Pour le Socket SACA'">

    public void EnvoyerListeVol(Object _Obj, String Message) 
    {
        try 
        {
            _Out.writeObject(_Obj);
            _Out.flush();
            _SACAInt.TextArea.append(Message);
        } catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("%nLe SACA: '" + SACA.class.getName() +
                               "' n'a pas réussi à envoyer la liste de vol!!");
        }
    }
    
    public void AjouterVol () 
    {
        _VolThread = new VolThread ();
    }

    public List<Avion> GetVol() 
    {
        if (SACA._VolThreads == null) 
        {
            return null;
        }
        
        List <Avion> CollectionAvion = new ArrayList();
        
        VolThread _VolTh;
        
        for (int i = 0; i < SACA._VolThreads.size(); i++) 
        {
            _VolTh = SACA._VolThreads.get(i);
            
            CollectionAvion.add(_VolTh._Avion);
        }
        return CollectionAvion;
    }

    public VolThread ChercherRadarThread (int Num) 
    {
        for (int i = 0; i < SACA._RadarThreads.size(); i++) 
        {
            if (Num == SACA._RadarThreads.get(i).GetNum()) 
            {
                return SACA._RadarThreads.get(i);
            }
        }
        return null;
    }

    private VolThread ChercherThread(int Num) 
    {
        for (int i = 0; i < SACA._VolThreads.size(); i++) 
        {
            if (SACA._VolThreads.get(i)._Avion._Info.GetNumVol() == Num) 
            {
                return SACA._VolThreads.get(i);
            }
        }
        return null;
    }

//</editor-fold>

}
