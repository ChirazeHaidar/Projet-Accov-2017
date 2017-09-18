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
import java.net.ServerSocket;
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

    public static int PORT = 9988;
    public static String HOST = "127.0.0.1";

    public static int ALTMIN = 0;
    public static int ALTMAX = 20000;

    public static int VITMIN = 200;
    public static int VITMAX = 1000;

    public static int PAUSE = 2000;
    //2 secondes

    public static Avion _Avion;
    public static CoorDeplAvion _CDA;
    public static Avion _VolActualise;

    public static VolInterface _VolInt;
    public static RadarInterface _RadInt;
    public static SACAInterface _SACAInt;
    public static ControleurInterface _ContInt;

    public static ObjectInputStream _In;
    public static ObjectOutputStream _Out;

    public static Socket _Socket = null;
    public static boolean _TravailContinuel = true;

    public static String _NomRadar;
    public static String _NomControleur;
    
    public static List<Avion> _VolDetecte;
    public static Optional<Avion> _VolOptionnel;

    public static VolThread _VolThread;
    public static DefaultListModel _ListeVol;
    public static VolThread _ControlleurThread;
    
    public static int _Num;
    public static SACA _SACA;
    public static Socket _VolSocket;
    public static String TypeMessage;
    
    public static ServerSocket _SACASocket;
    public static List<VolThread> _VolThreads;
    public static List<VolThread> _RadarThreads;

    //<editor-fold defaultstate="collapsed" desc="Méthodes partagées entre les différents Sockets">
    public static Socket OpenConnexion() 
    {
        //Cette fonction ouvre la connexion entre le socket et le SACA
        //SACA est le gestionnaire de vols

        try 
        {
            _Socket = new Socket(HOST, PORT);
            return _Socket;
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur de connexion:"
                    + VolInterface.class.getName());
        }
        return null;
    }

    public static void CloseConnexion() 
    {
        //Cette fonction ferme la connexion déjà établie entre le Socket et le SACA

        try 
        {
            _ContInt.TextArea.append("/nLe Controleur: " + Controleur.class.getName()
                    + "est bien déconnecté");
            _VolInt.TextArea.append("/nLe Vol: " + Vol.class.getName()
                    + "est bien déconnecté");
            _RadInt.TextArea.append("/nLe Radar: " + Radar.class.getName()
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

    public static void MessageEnvoyer(Message Mess) 
    {
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
            case "SACA":
            {
                System.out.println("Erreur du message envoyé du SACA: "
                        + SACA.class.getName());
                break;
            }
            default:
                break;
        }

    }

    public static void Envoyer(Message Mess) 
    {
        try 
        {
            if (Mess._Type.equals("Controlleur"))
            {
                _Avion.Verrouiller();
            }
            _Out.reset();
            _Out.writeObject(Mess);
            _Out.flush();
            
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            MessageEnvoyer (Mess);
        }
    }

    public static void Recevoir() 
    {
        try 
        {
            Object Objet = _In.readObject();

            if (_Socket == null || _Socket.isClosed() || !_TravailContinuel || (Objet == null)) 
            {
                System.out.println("/nLe message reçu est invalide");
                return;
            }
            
            _VolInt.TextArea.append("/n [SACA]: Nouveau message reçu.");
            /*
             * La méthode 'append' concatène la représentation de chaîne
             * de tout autre type de données à la fin de l'objet appelant
             */
            GererMessage(Objet);

        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println("/nErreur!!!");
        }

    }

    public static void Fermer() 
    {
        try 
        {
            _ContInt.TextArea.append("/nLa Connexion du Controleur: " + Controleur.class.getName()
                    + "est bien fermée");
            _VolInt.TextArea.append("/nLa Connexion du Vol: " + Vol.class.getName()
                    + "est bien Fermée");
            _RadInt.TextArea.append("/nLa Connexion du Radar: " + Radar.class.getName()
                    + "est bien Fermée");

            _Out.reset();
            Message Mess = new Message("Connexion Fermée", "Vol",
                    _CDA.GetNomVol(), _Avion, "SACA");
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

    public static void GererMessage (Object Obj) 
    {
        if (Obj == null) 
        {
            _RadInt.TextArea.append("/nMessage Invalide!!");
            _VolInt.TextArea.append("/n Message Invalide!!");
            _ContInt.TextArea.append("/nMessage Invalide!!");
            return;
            //J'ai utilisé return pour sortie de la fonction
        }
        
        Message _Mess = Message.class.cast(Obj);
        
        if (_Mess._Type.equals("Controleur")) 
        {
            switch (_Mess._Command) 
            {
                case "Changer Angle": 
                {
                    _VolInt.TextArea.append("/n'" + _Mess._Envoyeur
                                          + "': Changer Angle.");
                    ChangerAngle(_Mess._Avion._Info.GetAngle());
                    Message NouveauMessage = new Message("AngleModifie", "Vol",
                              _CDA.GetNomVol(), _Mess._Avion, _Mess._Envoyeur);
                    Envoyer(NouveauMessage);
                    _VolInt.TextArea.append("/n'" + _Mess._Envoyeur
                                          + "': L'Angle est bien modifiée.");
                    AfficherMessage();
                    break;
                }
                case "Changer Vitesse": 
                {
                    _VolInt.TextArea.append("/n'" + _Mess._Envoyeur
                                          + "': Changer Vitesse.");
                    ChangerVitesse(_Mess._Avion._Info.GetVitesse());

                    Message NouveauMessage = new Message("VitesseModifie", "Vol",
                              _CDA.GetNomVol(), _Mess._Avion, _Mess._Envoyeur);

                    Envoyer(NouveauMessage);
                    _VolInt.TextArea.append("/n'" + _Mess._Envoyeur
                                          + "': La vitesse est bien modifiée.");
                    AfficherMessage();
                    break;
                }
                case "changer Altitude": 
                {
                    _VolInt.TextArea.append("/n'" + _Mess._Envoyeur
                                          + "': Changer Altitude.");
                    ChangerAltitude(_Mess._Avion._Info.GetAltitude());

                    Message NouveauMessage = new Message("AltitudeModifie", "Vol",
                              _CDA.GetNomVol(), _Mess._Avion, _Mess._Envoyeur);

                    Envoyer(NouveauMessage);
                    _VolInt.TextArea.append("/n'" + _Mess._Envoyeur
                                          + "': L'Altitude est bien modifiée.");
                    AfficherMessage();
                    break;
                }
                default:
                    break;
            }
        }
        else
        {
            switch (_Mess._Command)
            {
                case "Information": 
                {
                    _ContInt.TextArea.append("/n'" + _Mess._Envoyeur
                                           + "':Les Caractéristiques de l'avion sont bien modifiées");
                    ReactualiserVol(_Mess);
                    break;
                }
                case "MiseAJour": 
                {
                    _ContInt.TextArea.append("/n'" + _Mess._Envoyeur + "': " + _Mess._Command);
                    ReactualiserVol(_Mess);

                    _VolActualise = ReactualiserVol(_Mess);
                    
                    if (_VolActualise != null) 
                    {
                        _VolActualise.Deverrouiller();
                    } 
                    else 
                    {
                        //Avion est accidenté ou débarqué
                        SupprimerVol(_Mess);
                        _ContInt.TextArea.append("/n'" + _Mess._Envoyeur + "': " + _Mess._Command);
                    }
                    break;
                }
                default: break;
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes pour le Socket 'Vol'">
    public static String RandomNomVol() 
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

    public static Avion Initialiser_Avion(VolInterface volInterface,ObjectOutputStream out,ObjectInputStream in) 
    {
        // intialisation des paramétres de l'avion d'une façon aléatoire    
        
        _VolInt = volInterface;
        _CDA = new CoorDeplAvion();
        
        Random GenerateurNumero = new Random();
        _In = in;
        _Out = out;
        _CDA.SetX((int) (1000 + GenerateurNumero.nextInt(100) % 1000));
        _CDA.SetY((int) (1000 + GenerateurNumero.nextInt(100) % 1000));
        _CDA.SetAltitude((int) (900 + GenerateurNumero.nextInt(100) % 100));
        _CDA.SetAngle((int) (GenerateurNumero.nextInt(100) % 360));
        _CDA.SetVitesse((int) (600 + GenerateurNumero.nextInt(100) % 200));
        _CDA.SetNomVol((String) RandomNomVol());

        _Avion = new Avion(_CDA);

        return _Avion;

    }

    public static void ChangerVitesse(int Vitesse) 
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

    public static void ChangerAngle(int Angle) 
    {
        //Modification de l'Angle "CAP" de l'avion avec la valeur passée en paramètre

        if ((Angle >= 0) && (Angle < 360)) 
        {
            _CDA.SetAngle(Angle);
        }
    }

    public static void ChangerAltitude(int Alt) 
    {
        //Modification de l'altitude de l'avion avec la valeur passée en paramètre

        if (Alt < 0) 
        {
            _CDA.SetAltitude(0);
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

    public static Boolean CalculDeplacement() 
    {
        //Recalcule la localisation de l'avion en fonction de sa vitesse et de son Angle

        double cosinus;
        double sinus;
        //cos et sin ont un paramétre en radian, dep.cap en degré nos habitudes francophone

        double DepX;
        double DepY;

        if (_CDA.GetVitesse() < VITMIN) 
        {
            _VolInt.TextArea.append("/nVitesse trop faible: Avion va s'écraser/n");
            
            Message _Mess = new Message("Alert: Accident Avion",
                    "Vol", _CDA.GetNomVol(), _Avion, "Radar");
            
            Envoyer(_Mess);
            
            CloseConnexion();
            
            return false;
        } 
        else if (_CDA.GetAltitude() == 0) 
        {
            _VolInt.TextArea.append("/nL'avion s'est ecrase au sol\n");
            Message _Mess = new Message("Alert: L'avion s'est ecrase au sol",
                                        "Vol", _CDA.GetNomVol(), _Avion, "Radar");

            Envoyer(_Mess);
            
            CloseConnexion();
            
            return false;
        }

        /**
         * Angle en radian = pi * (angle en degré) / 180 Angle en radian = pi *
         * (angle en grade) / 200 Angle en grade = 200 * (angle en degré) / 180
         * Angle en degré = 180 * (angle en grade) / 200 Angle en grade = 200 *
         * (angle en radian) / pi Angle en degré = 180 * (angle en radian) / pi 
        *
         */
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

        AfficherMessage();
        
        return true;
    }

    public void SeDeplacer() 
    {
        //Fonction principale: gère l'exécution de l'avion au fil du temps

        if (CalculDeplacement()) 
        {
            Envoyer_Caracteristique();
        }
    }

    private void Envoyer_Caracteristique() 
    {
        /*Cette fonction envoye les différentes caractéristiques 
         *de l'avion courante au gestionnnaire de vol
         */

        Message Mess = new Message("Information", "Vol", _CDA.GetNomVol(),
                                                         _Avion, "Radar");
        Envoyer(Mess);
    }

    public static void AfficherMessage() 
    {
        //Affichage des caractéristiques courantes de l'avion
        _VolInt.TextArea.append("/n" + _Avion.GetInfo());
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Méthodes Pour le Socket Controleur'">
    public void ChangementAngle(int NumVol, int Angle) throws InterruptedException 
    {
        if (_VolDetecte == null) 
        {
            return;
        }

        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == NumVol).findFirst();

        if (_VolOptionnel == null) {
            return;
        }

        _Avion = _VolOptionnel.get();

        _Avion.Verrouiller();

        _ContInt.TextArea.append("/n'Avion: " + _Avion._Info.GetNomVol() + "': Changement de l'Angle");

        _Avion._Info.SetAngle(Angle);

        Message _Mess = new Message("Changer Angle", "Controleur", _NomControleur,
                _Avion, _Avion._Info.GetNomVol());
        Envoyer(_Mess);
    }

    public void ChangementAltitude(int _NumVol, int Altitude) throws InterruptedException {
        if (_VolDetecte == null) {
            return;
        }

        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _NumVol).findFirst();

        if (_VolOptionnel == null) {
            return;
        }

        _Avion = _VolOptionnel.get();

        _Avion.Verrouiller();

        _ContInt.TextArea.append("/n'Avion: " + _Avion._Info.GetNomVol() + "': Changement de l'Altitude");

        _Avion._Info.SetAltitude(Altitude);

        Message _Mess = new Message("changer Altitude", "Controleur", _NomControleur,
                _Avion, _Avion._Info.GetNomVol());
        Envoyer(_Mess);
    }

    public void ChangementVitesse(int _NumVol, int Vitesse) throws InterruptedException {
        if (_VolDetecte == null) {
            return;
        }

        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _NumVol).findFirst();

        if (_VolOptionnel == null) {
            return;
        }

        _Avion = _VolOptionnel.get();

        _Avion.Verrouiller();

        _ContInt.TextArea.append("/n'Avion: " + _Avion._Info.GetNomVol() + "': Changement de la Vitesse");

        _Avion._Info.SetVitesse(Vitesse);

        Message _Mess = new Message("changer Vitesse", "Controleur", _NomControleur,
                _Avion, _Avion._Info.GetNomVol());
        Envoyer(_Mess);
    }

    public static void SupprimerVol(Message _Mess) 
    {
        if (_VolDetecte == null) 
        {
            _VolDetecte = new ArrayList();
        }

        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _Mess._Avion._Info.GetNumVol()).findFirst();

        if (_VolOptionnel != null && _VolOptionnel.isPresent()) {
            _VolDetecte.remove(_VolOptionnel.get());

        }
    }

    public static Avion ReactualiserVol(Message _Mess) 
    {

        if (_VolDetecte == null) {
            _VolDetecte = new ArrayList();
        }

        _VolActualise = null;

        _VolOptionnel = _VolDetecte.stream().filter(Avion -> Avion._Info.GetNumVol() == _Mess._Avion._Info.GetNumVol()).findFirst();

        if (_VolOptionnel != null && _VolOptionnel.isPresent()) {
            _VolActualise = _VolOptionnel.get();
            _VolActualise.SetInfo(_Mess._Avion.GetInfo());

        } else {
            _VolDetecte.add(_Mess._Avion);
            _VolActualise = _Mess._Avion;
        }

        _ListeVol = new DefaultListModel();

        for (int i = 0; i < _VolDetecte.size(); i++) {
            _ListeVol.addElement(_VolDetecte.get(i).GetInfo());
        }

        _ContInt.ListeVol.setModel(_ListeVol);

        return _VolActualise;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes Pour le Socket Radar'">
    public void RemplirListeVol(Object ListeVol) {
        if (ListeVol != null) {
            _VolDetecte = (ArrayList) ListeVol;

            _ListeVol = new DefaultListModel();

            for (int i = 0; i < _VolDetecte.size(); i++) {
                _ListeVol.addElement(_VolDetecte.get(i).GetInfo());

            }
            _RadInt.ListeVol.setModel(_ListeVol);
        }
    }

    public static Object RecevoirListeVol() 
    {
        try 
        {
            return _In.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println("/nLe Radar: '" + Radar.class.getName()
                    + "' n'a pas reçu la liste de vol!!");
        }
        return null;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Méthodes Pour le Socket SACA'">
    
    public static void AjouterVol(SACA saca) 
    {
        _VolThread = new VolThread(saca);
    }

    public List<Avion> GetVol() 
    {
        if (_VolThreads == null) 
        {
            return null;
        }

        List<Avion> CollectionAvion = new ArrayList();

        VolThread _VolTh;

        for (int i = 0; i < _VolThreads.size(); i++) 
        {
            _VolTh = _VolThreads.get(i);

            CollectionAvion.add(_VolTh._Avion);
        }
        return CollectionAvion;
    }

    public static VolThread ChercherRadarThread (int Num) 
    {
        for (int i = 0; i < _RadarThreads.size(); i++) 
        {
            if (Num == _RadarThreads.get(i).GetNum()) 
            {
                return _RadarThreads.get(i);
            }
        }
        return null;
    }

    public static VolThread ChercherThread (int Num) 
    {
        for (int i = 0; i < _VolThreads.size(); i++) 
        {
            if (_VolThreads.get(i)._Avion._Info.GetNumVol() == Num) 
            {
                return _VolThreads.get(i);
            }
        }
        return null;
    }
    
    public static void RecevoirSACA() 
    {
        try 
        {
            Object Objet = _In.readObject();

            if (_Socket == null || _Socket.isClosed() || !_TravailContinuel || (Objet == null)) 
            {
                System.out.println("/nLe message reçu est invalide");
                return;
            }

            Message MESS = Message.class.cast(Objet);
            _SACAInt.TextArea.append("/n [SACA]: Nouveau message reçu.");
            /*
             * La méthode 'append' concatène la représentation de chaîne
             * de tout autre type de données à la fin de l'objet appelant
             */
            
            if (MESS._Command.contains("MiseAJour")) 
            {
                _Avion = MESS._Avion;
                _Avion.Deverrouiller();
            }
            GererMessageSACA(_Num, MESS);

        } 
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println("/nErreur!!!");
        }
    }
    
    public static void GererMessageSACA (int NUM, Message MESS) 
    {

        VolThread _VolThreadEnv;
        VolThread _ControleurThread;
        switch (MESS._Type) 
        {
            case "Vol": 
            {
                _VolThreadEnv = ChercherThread(MESS._Avion._Info.GetNumVol());
                //Chercher le thread du Vol

                if (_VolThreadEnv == null) 
                {
                    _SACAInt.TextArea.append("Le Vol: '%s', est introuvable." + MESS._Receveur);
                    return;
                }

                switch (MESS._Command) 
                {
                    case "Connexion": 
                    {
                        _VolThreads.remove(_VolThreadEnv);
                        
                        if (_RadarThreads != null && _RadarThreads.size() > 0) 
                        {
                            for (int i = 0; i < _RadarThreads.size(); i++) 
                            {
                                Message NouveauMessage;
                                NouveauMessage = new Message("Attention: La Connexion est fermée",
                                        "Radar", MESS._Envoyeur, MESS._Avion,
                                        "Radar/Controlleur" + _RadarThreads.get(i).GetNum());

                                _RadarThreads.get(i)._Fonction.Envoyer(NouveauMessage);
                            }
                        }
                        break;
                    }
                    case "MiseAJour": 
                    {
                        _VolThreadEnv._Avion = MESS._Avion;
                        
                        _SACAInt.TextArea.append("/n Envoyeur du message: '" + MESS._Envoyeur
                                + "', Recipient du message: '" + MESS._Receveur
                                + "', Commande du message: '" + MESS._Command
                                + "'");
                        
                        if (_RadarThreads != null && _RadarThreads.size() > 0) 
                        {
                            for (int i = 0; i < _RadarThreads.size(); i++) 
                            {
                                Message NouveauMessage = new Message(MESS._Command, "Radar", MESS._Envoyeur, MESS._Avion,
                                        "Radar/Controlleur" + _RadarThreads.get(i).GetNum());

                                _RadarThreads.get(i)._Fonction.Envoyer(NouveauMessage);
                            }
                        }
                        break;
                    }
                    case "Information"://Information sur le Vol
                    {
                        _VolThreadEnv._Avion = MESS._Avion;
                        
                        _SACAInt.TextArea.append("/n L'envoyeur du message: '" + MESS._Envoyeur
                                + "' --> Tous: La position du Vol '" + MESS._Avion._Info.GetNomVol()
                                + "'avait été mise à jour");

                        if (_RadarThreads != null && _RadarThreads.size() > 0) 
                        {
                            for (int i = 0; i < _RadarThreads.size(); i++) 
                            {
                                Message NouveauMessage = new Message("Information", "Radar", MESS._Envoyeur, MESS._Avion,
                                        "Radar/Controlleur" + _RadarThreads.get(i).GetNum());

                                _RadarThreads.get(i)._Fonction.Envoyer(NouveauMessage);
                            }
                        }
                        break;
                    }
                    case "Alerte"://Avion est accidenté ou débarqué
                    {
                        _VolThreadEnv._Avion = MESS._Avion;
                        
                        _SACAInt.TextArea.append("/nEnvoyeur '" + MESS._Envoyeur
                                + "' --> Recipient '" + MESS._Receveur
                                + "': " + MESS._Command);
                        
                        _VolThreads.remove(_VolThreadEnv);
                        _VolThreadEnv.interrupt();

                        if (_RadarThreads != null && _RadarThreads.size() > 0) 
                        {
                            for (int i = 0; i < _RadarThreads.size(); i++) 
                            {
                                _RadarThreads.get(i)._Fonction.Envoyer(MESS);
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case "Controleur": 
            {
                if (MESS._Command.contains("Connexion")) 
                {
                    _SACAInt.TextArea.append("/n'" + MESS._Envoyeur + "' --> 'SACA': La connexion est fermée!!!");
                                    
                    _ControleurThread = ChercherRadarThread (Integer.parseInt(MESS._Envoyeur));
                    
                    if (_ControleurThread != null) 
                    {
                        _RadarThreads.remove(_ControleurThread);
                    }
                    return;
                }
                GererMessageControleur (MESS);
            }
            break;
        }
    }

    public static void GererMessageControleur(Message MESS) 
    {
        VolThread _VolThreadDes;
        _VolThreadDes = ChercherThread(MESS._Avion._Info.GetNumVol());
        //Chercher le Vol pour pouvoir envoyer les commandes

        if (_VolThreadDes == null) 
        {
            _SACAInt.TextArea.append("Le Vol: '%s', est introuvable." + MESS._Receveur);
            return;
        }

        switch (MESS._Command) 
        {
            case "Changer Angle": 
            {
                _VolInt.TextArea.append("/n'" + MESS._Envoyeur
                                              + "': Changer Angle.");
                _VolThreadDes._Fonction.Envoyer(MESS);
                break;
            }
            case "Changer Vitesse": 
            {
                _VolInt.TextArea.append("/n'" + MESS._Envoyeur
                                              + "': Changer Vitesse.");
                _VolThreadDes._Fonction.Envoyer(MESS);
                break;
            }
            case "changer Altitude": 
            {
                _VolInt.TextArea.append("/n'" + MESS._Envoyeur
                                              + "': Changer Altitude.");
                _VolThreadDes._Fonction.Envoyer(MESS);
                break;
            }
        }
    }
}


//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Classe Pour les Threads'">

class VolThread extends Thread 
{
    Avion _Avion;
    Fonction _Fonction;
    
    public static int GetNum() 
    {
        return Fonction._Num;
    }

    public static String GetType() 
    {
        return Fonction.TypeMessage;
    }

    public  VolThread (SACA Socket) 
    {
        _Fonction = new Fonction ();
        _Avion = Fonction._Avion;
        try 
        {
            Fonction._SACA = Socket;
            Fonction._VolSocket = Socket._SACASocket.accept();
            Fonction._Num = Fonction._VolSocket.getLocalPort();

            Fonction._Out = new ObjectOutputStream(Fonction._VolSocket.getOutputStream());
            Fonction._Out.flush();
               
            Fonction._In = new ObjectInputStream(Fonction._VolSocket.getInputStream());
              
            Object Obj = Fonction._In.readObject();
                
            if (Obj != null) 
            {
                Message Mess = Message.class.cast(Obj);
                Fonction.TypeMessage = Mess._Type;

                Fonction._SACAInt.TextArea.append("/n'" + Mess._Envoyeur + 
                                                  "' -->  'SACA': Nouveau message de type '%s' dÃ©tectÃ©." + Mess._Type);
                    
                if (Mess._Type.equals("Vol")) 
                {
                    Socket._VolThreads.add(this);
                    Fonction._Avion = Mess._Avion;
                    Fonction._SACAInt.TextArea.append("/n'SACA' --> Information du vol: " + Mess._Avion.GetInfo());
                }    
                else 
                {
                    Socket._RadarThreads.add(this);
                }
            }
            this.start();
        }    
        catch (IOException | ClassNotFoundException e) 
        {
            System.out.println(e);
            System.out.println(VolThread.class.getName());            
        } 
    }
    @Override
    public void run() 
    {
        while (true) 
        {
            Fonction.Recevoir();

        }
    }
}
//</editor-fold>
