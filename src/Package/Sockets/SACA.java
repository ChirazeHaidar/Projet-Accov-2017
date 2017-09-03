/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Sockets;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import Package.Commun.Message;
import Package.Commun.Fonction;
import Package.Threads.VolThread;
import Package.Interface.SACAInterface;

/**
 *
 * @author Cynthia Abou Maroun
 */

public class SACA extends Thread 
{
    
    private Fonction _Fonction;
    public SACAInterface _SACAInt;

    public ServerSocket _SACASocket;
    public static List <VolThread> _VolThreads;
    public static List <VolThread> _RadarThreads;

    public SACA (SACAInterface SACAInt) 
    {
        try {
                _SACAInt = SACAInt;
                _SACASocket = new ServerSocket(_Fonction.PORT);
                _VolThreads = new ArrayList();
                _RadarThreads = new ArrayList();

                this.start();
            } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println ("Erreur dans le gestionnaire de vol: '" + SACA.class.getName ());
        }
    }

    @Override
    public void run() 
    {
        while (true) 
        {
            _SACAInt.TextArea.append("%nLe gestionnaire de Vol est en état de surveillance");
            for (int i = 0; i < 10; i++)//Tel que i est le nombre de Vol
            {
                _Fonction.AjouterVol();
                _Fonction.Recevoir ();
            }
        }
    }
}

