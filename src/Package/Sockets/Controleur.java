/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Sockets;

import java.net.Socket;
import java.io.IOException;
import Package.Commun.Message;
import Package.Commun.Fonction;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Package.Interface.ControleurInterface;

/**
 *
 * @author Cynthia Abou Maroun
 */

public class Controleur extends Thread
{


    private Socket _Socket;
    private Fonction _Fonction;
    
    private String _NomControleur;
    
    private ObjectInputStream _In;
    private ObjectOutputStream _Out;
    
    private ControleurInterface _ContInt;

    public Controleur(ControleurInterface _ContInterface) 
    {
        _ContInt = _ContInterface;
        
        try 
        {
            if (!_Fonction.OpenConnexion()) 
            {
                _ContInt.TextArea.append("%nErreur de connexion au SACA");
                return;
            }
            
            _ContInt.TextArea.append("%nLa Connexion du Controleur est établie");
            
            _Out = new ObjectOutputStream(_Socket.getOutputStream());
            _Out.flush();
            
            _In = new ObjectInputStream(_Socket.getInputStream());
            
            _NomControleur = "Controleur: " + _Socket.getLocalPort();
            _ContInt.setTitle(_NomControleur);
            
            Message _Mess = new Message("Information", "Controleur", _NomControleur, null, "SACA");
            _Fonction.Envoyer(_Mess);
            
            this.start();

        } 
        catch (IOException e) 
        {
            System.out.println (e);
            System.out.println("Erreur de connexion:" 
                               + ControleurInterface.class.getName());        
        }
    }

    @Override
    public void run() {
        while (_Fonction._TravailContinuel) 
        {
            _Fonction.Recevoir();
        }
    }
}
