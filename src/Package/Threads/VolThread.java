/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Package.Threads;

import java.net.Socket;
import java.io.IOException;
import Package.Sockets.SACA;
import Package.Commun.Avion;
import Package.Commun.Message;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Package.Interface.SACAInterface;

/**
 *
 * @author Chiraze Haidar
 */


public class VolThread extends Thread 
{

    public int _Num;
    public Avion _Avion;
    public String _Type;
    public SACA _Socket;
    private Socket _VolSocket;
    private ObjectInputStream _In;
    private SACAInterface _SACAInt;
    private ObjectOutputStream _Out;

    public VolThread() 
    {

    }

    public int GetNum() 
    {
        return _Num;
    }

    public String GetType() 
    {
        return _Type;
    }

    public VolThread (SACA _Socket) 
    {
        try 
        {
            this._Socket = _Socket;
            _SACAInt = _Socket._SACAInt;
            _VolSocket = _Socket._SACASocket.accept();
            _Num = _VolSocket.getLocalPort();

            _Out = new ObjectOutputStream(_VolSocket.getOutputStream());
            _Out.flush();
               
            _In = new ObjectInputStream(_VolSocket.getInputStream());
              
            Object Obj = _In.readObject();
                
            if (Obj != null) 
            {
                Message _Mess = Message.class.cast(Obj);
                _Type = _Mess._Type;

                _SACAInt.TextArea.append("%n'" + _Mess._Envoyeur + 
                                         "' -->  'SACA': Nouveau message de type '%s' détecté." + _Mess._Type);

                if (_Mess._Type.equals("Vol")) 
                {
                    _Socket._VolThreads.add(this);
                    _Avion = _Mess._Avion;
                    _SACAInt.TextArea.append("%n'SACA' --> Information du vol: " + _Mess._Avion.GetInfo());
                } 
                else 
                {
                    _Socket._RadarThreads.add(this);
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

    public void Envoyer(Message NouveauMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}