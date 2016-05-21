/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.ArrayList;

/**
 *
 * @author Algirdas
 */
public class Kernel
{
    private ArrayList allProcess = new ArrayList();
    private ArrayList allResourses = new ArrayList();
    private ProcesorDeskriptor procDesc = new ProcesorDeskriptor();
    private ArrList pps = new ArrList();
    
    
    void planuotojas()
    {
        int processNumber = OS.kernel.procDesc.getProcessName();
        //ProcessDescriptor processDesc = OS.processDesc.get(processNumber);
        if (OS.processDesc.get(processNumber).getState().equals("READY"))
        {
            OS.processDesc.get(processNumber).setCPU();
            OS.kernel.pps.addPps(processNumber, OS.processDesc.get(processNumber).getPriority());
            int next_process = OS.kernel.pps.removeFirst();
            OS.processDesc.get(next_process).getCpu();
            OS.kernel.procDesc.setProcessName(next_process);
        }else
        {
            OS.processDesc.get(processNumber).setCPU();
            //OS.kernel.pps.addPps(processNumber, OS.processDesc.get(processNumber).getPriority());
            int next_process = OS.kernel.pps.removeFirst();
            OS.processDesc.get(next_process).getCpu();
            OS.kernel.procDesc.setProcessName(next_process);
        }
        
    }
    void pertraukimuApdorotojas()
    {
        
    }
    //procesu primityvai
    
    
    //resursu primityvai
}
