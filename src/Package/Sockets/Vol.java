/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Sockets;
//Ce paquet contient tous les objets utilisées dans mon Projet

import java.net.Socket;
import javax.swing.Timer;
import java.io.IOException;
import Package.Commun.Avion;
import Package.Commun.Message;
import Package.Commun.Fonction;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import Package.Commun.CoorDeplAvion;
import java.awt.event.ActionListener;
import Package.Interface.VolInterface;

/**
 *
 * @author Chiraze Haidar
 */

public class Vol extends Thread
{
//Cette classe contient les sockets de Vol

    private Socket _Socket;

    private Avion _Avion;
    private CoorDeplAvion _CoorDeplAvion;
    
    private VolInterface _VolInt;
    private Fonction _Fonction;
    
    private ObjectInputStream _In;
    private ObjectOutputStream _Out;
    
    public Vol (VolInterface volInterface) 
    {
        _VolInt = volInterface;
        try 
        {
            if (!_Fonction.OpenConnexion()) 
            {
                _VolInt.TextArea.append("Erreur, "
                                      + "Problème de connexion!!!");
            }
            
            _Out = new ObjectOutputStream (_Socket.getOutputStream());
            _Out.flush();
            
            _In = new ObjectInputStream (_Socket.getInputStream());
            _Fonction.Initialiser_Avion();
            _CoorDeplAvion.SetNumVol(_Socket.getLocalPort());

            Message _Mess = new Message("Information", "Vol", _CoorDeplAvion.GetNomVol(), _Avion, "SACA");
            
            _Fonction.AfficherMessage();
            _Fonction.Envoyer(_Mess);
            this.start();

            Timer MinutrieDeVol = new Timer(_Fonction.PAUSE, new ActionListenerImpl());
            MinutrieDeVol.start();

        } 
        catch (IOException e) 
        {
            System.out.println (e);
            System.out.println("Erreur de connexion: " + Vol.class.getName());
        }
    
    }

    private class ActionListenerImpl implements ActionListener 
    {

        public ActionListenerImpl() 
        {
        }

        @Override
        
        public void actionPerformed(ActionEvent e) 
        {
            
            _Fonction.SeDeplacer();
        
        }
    }
    @Override
    public void run() 
    {
        while (true) 
        {
            _Fonction.Recevoir ();
        }
    }
}
    
    

