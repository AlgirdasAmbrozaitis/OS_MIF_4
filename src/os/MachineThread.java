/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Algirdas
 */
public class MachineThread extends Thread
{
    public void run()
    {
        while(!OS.osEnd)
        {
            if(OS.startInput)
            {
                InputThread inputThread = new InputThread();
                inputThread.start();
            }
            if(OS.inputStreamOk)
            {
                OS.kernel.acivateProc(OS.blockedProcessId);
            }
            //zingsnis++;
            //try
            //{
                //System.out.println("zingsnis: " + zingsnis);
                OS.cpu();
                //rmMemory[0].setState(false);
                //rmMemory[80].setState(false);
                /*int idd = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(idd, processDesc);
                System.out.println("einamas procesas: " + OS.processDesc.get(index).getName());
                for(int i = 0; i < OS.kernel.getPps().getSize(); i++)
                {
                    int id = OS.kernel.getPps().getList().get(i).processId;
                    id = OS.kernel.findProc(id, processDesc);
                    String name = OS.processDesc.get(id).getName();
                    System.out.println(name + " prioritetas: " + OS.processDesc.get(id).getPriority());
                }
                for(int i = 0; i < OS.processDesc.size(); i++)
                {
                    System.out.println("proceso vardas: " + OS.processDesc.get(i).getName() + "proceso busena: " + OS.processDesc.get(i).getState());
                }*/
                /*Thread.sleep(1000);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
}
