/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Sockets;
//Ce paquet contient tous les objets utilisées dans mon Projet

import javax.swing.Timer;
import java.io.IOException;
import Package.Commun.Message;
import Package.Commun.Fonction;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Package.Interface.VolInterface;

/**
 *
 * @author Chiraze Haidar
 */

public class Vol extends Fonction
{
//Cette classe contient les sockets de Vol


    public Vol (VolInterface volInterface) 
    {
        _VolInt = volInterface;
        try 
        {
            _Socket = OpenConnexion();
            if (_Socket == null) 
            {
                _VolInt.TextArea.append("Erreur, "
                                      + "Problème de connexion!!!");
            }
            
            _Out = new ObjectOutputStream (_Socket.getOutputStream());
            _Out.flush();
            
            _In = new ObjectInputStream (_Socket.getInputStream());
            _Avion = Initialiser_Avion(_VolInt,_Out,_In);
            
            _VolInt.setTitle(_Avion.GetInfo().GetNomVol());
            _CDA =_Avion.GetInfo();
            
            _CDA.SetNumVol(_Socket.getLocalPort());

            Message _Mess = new Message("Information", "Vol", _CDA.GetNomVol(), _Avion, "SACA");
            
            AfficherMessage();
            
            Envoyer(_Mess);
            
            this.start();

            Timer MinutrieDeVol = new Timer(PAUSE, new ActionListenerImpl());
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
            
            SeDeplacer();
        
        }
    }
    
    @Override
    public void run() 
    {
        while (true) 
        {
            Recevoir ();
        }
    }
}
    
    

