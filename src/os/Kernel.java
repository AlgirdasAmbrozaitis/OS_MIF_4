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
    
    void createProcess( ArrayList memory, ArrList resourse, int priority ){
        
        ProcessDescriptor process = new ProcessDescriptor();
        process.setCPU();
        process.setOperating_memory(memory);
        process.setResource(resourse);
        process.setPriority(priority);
        process.setState("READY");
        process.setList_where_process_is(-1);
        process.setFather_processor(OS.processDesc.get(OS.kernel.procDesc.getProcessName()).getId());
        OS.processDesc.get(OS.kernel.procDesc.getProcessName()).addSon(process.getId());
        OS.processDesc.add(process);
    
    }
    
    void foo(){
        
    }
    //resursu primityvai
    void kurtiResursa(boolean pakartotinio, ArrList prienamumo_aprasymas, int adr)
    {
        ResourseDescriptor resDesc = new ResourseDescriptor();
        resDesc.setRes_dist_addr(adr);
        resDesc.setRepeated_use(pakartotinio);
        resDesc.setInfo("");
        resDesc.setFather_resource(OS.kernel.procDesc.getProcessName());
        resDesc.setPrieinamu_resursu_sarasas(prienamumo_aprasymas);
        ArrayList old = OS.processDesc.get(OS.kernel.procDesc.getProcessName()).getCreated_resourses();
        old.add(resDesc.getRs());
        OS.processDesc.get(OS.kernel.procDesc.getProcessName()).setCreated_resourses(old);
        OS.resourseDesc.add(resDesc);
    }
}
