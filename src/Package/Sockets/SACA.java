/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Sockets;

import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import Package.Commun.Fonction;
import Package.Interface.SACAInterface;

/**
 *
 * @author Cynthia Abou Maroun
 */
public class SACA extends Fonction 
{
    public SACA(SACAInterface SACAInt) 
    {
        try 
        {
            _SACAInt = SACAInt;
            _VolThreads = new ArrayList();
            _RadarThreads = new ArrayList();
            _SACASocket = new ServerSocket(PORT);

            this.start();
        } 
        catch (IOException e) 
        {
            System.out.println(e);
            System.out.println("Erreur dans le gestionnaire de vol: '" + SACA.class.getName());
        }
    }

    @Override
    public void run() 
    {
        while (true) 
        {
            _SACAInt.TextArea.append("%nLe gestionnaire de Vol est en état de surveillance");
            AjouterVol(this); //Cette fonction est predéfinie dans la classe 'Fonction'
        }
    }
}
