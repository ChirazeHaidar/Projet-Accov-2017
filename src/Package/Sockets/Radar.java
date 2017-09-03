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
import Package.Interface.RadarInterface;

/**
 *
 * @author Cynthia Abou Maroun
 */

public class Radar extends Thread
{
    
    private Socket _Socket;
    private String _NomRadar;
    
    private Fonction _Fonction;
    private ObjectInputStream _In;
    private RadarInterface _RadInt;
    private ObjectOutputStream _Out;

    public Radar (RadarInterface _RadarInterface) 
    {
        _RadInt = _RadarInterface;
        try {
            
                if (!_Fonction.OpenConnexion()) 
                {
                    _RadInt.TextArea.append("%nErreur de connexion au SACA");
                    return;
                }
                _RadInt.TextArea.append("%nLa Connexion du Radar est établie");
            
                _Out = new ObjectOutputStream(_Socket.getOutputStream());
                _Out.flush();
               
                _In = new ObjectInputStream(_Socket.getInputStream());
           
                _NomRadar = "Radar: " + _Socket.getLocalPort();
                _RadInt.setTitle(_NomRadar);
            
                Message _Mess = new Message("Information", "radar", _NomRadar, null, "SACA");
                _Fonction.Envoyer(_Mess);
            
                this.start();
            }   
            catch (IOException e) 
            {
                System.out.println (e);
                System.out.println("Erreur de connexion:" 
                                  + RadarInterface.class.getName());        
            }
        }
    
    @Override
    public void run() 
    {
        while (_Fonction._TravailContinuel) 
        {
            _Fonction.Recevoir ();
        }
    }
}
