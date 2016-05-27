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
    
    void createProcess( ArrList memory, ArrList resourse, int priority ){
        
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
    
    void abortProcess( int index ){
        boolean aborted = false;
        aborted = abort(index);
        if(aborted){
            planuotojas();
        }
        
    }
    
    boolean abort(int index){
               if( OS.processDesc.get(index).getState().equals("RUN")){
            return true;
        }
        if( OS.processDesc.get(index).getList_where_process_is() == -1 ){
            pps.remove(index);
        } else {
            
            OS.resourseDesc.get(OS.processDesc.get(index).getList_where_process_is())
                                                            .getList().remove(index);// do magic
        }
        if( OS.processDesc.get(index).getSons_processes().size() > 0 ){
            for( int i : OS.processDesc.get(index).getSons_processes()){
                abort(i);
            }
        }
        if( OS.processDesc.get(index).getOperating_memory().getList().size() > 0 ){
           for( Struct r : OS.processDesc.get(index).getOperating_memory().getList() ){
               int block_no = 10*r.processId;
               for( int cn = 0; cn < 10 ; cn++){
                OS.rmMemory[block_no + cn].freeCell();
               }
               OS.processDesc.get(index).getOperating_memory().getList().remove(r);
           }
        }
        if( OS.processDesc.get(index).getResource().getList().size() > 0 ){
           for( Struct r : OS.processDesc.get(index).getResource().getList() ){
               if(OS.resourseDesc.get(r.processId).isRepeated_use()){
                   OS.processDesc.get(index).getResource().getList().remove(r.processId);
                   //OS.processDesc.get(index).getResource().get
               }
           }
        }
        return true;
        
    }
    
    void foo(){
        
    }
    //resursu primityvai
    void kurtiResursa(String name, boolean pakartotinio, ArrList prienamumo_aprasymas, int adr)
    {
        ResourseDescriptor resDesc = new ResourseDescriptor();
        //TODO: prideti varda
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
    void prasytiResurso(int resourse, int part)
    {
        ArrList old = OS.resourseDesc.get(resourse).getLaukianciu_procesu_sarasas();
        int id = OS.kernel.procDesc.getProcessName();
        int prior = OS.processDesc.get(id).getPriority();
        String info = "";
        old.addLps(id, part, info, prior);
        OS.resourseDesc.get(resourse).setLaukianciu_procesu_sarasas(old);
        ArrayList<Integer> aptarnautiProcesai = new ArrayList<>();
        int aptarnautuProcesuSkaicius = 0;
        //TODO paskirstytojas
        boolean einamas = true;
        for(int i = 0; i < aptarnautuProcesuSkaicius; i++)
        {
            if(aptarnautiProcesai.get(i) != OS.kernel.procDesc.getProcessName())
            {
                int processName = aptarnautiProcesai.get(i);
                OS.kernel.pps.addPps(processName, OS.processDesc.get(i).getPriority());
                OS.processDesc.get(i).setList_where_process_is(-1);
                String state = OS.processDesc.get(i).getState();
                if (state.equals("BLOCKED"))
                {
                    OS.processDesc.get(i).setState("READY");
                }
                else
                {
                    OS.processDesc.get(i).setState("READYS");
                }
            }
            else
            {
                einamas = false;
            }
        }
        if(einamas)
        {
            OS.processDesc.get(id).setState("BLOCKED");
            OS.processDesc.get(id).setList_where_process_is(resourse);
            OS.kernel.procDesc.setProcessName(-1);
            OS.kernel.pps.remove(id);
        }
        OS.kernel.planuotojas();
    }
    void atlaisvintiResursa(int resource, int part, int proc)
    {
        if(OS.resourseDesc.get(resource).isRepeated_use())
        {
            ArrList old = OS.resourseDesc.get(resource).getPrieinamu_resursu_sarasas();
            old.addPa(part);
            OS.resourseDesc.get(resource).setPrieinamu_resursu_sarasas(old);
            old = OS.processDesc.get(proc).getResource();
            old.removeR(resource, part);
            OS.processDesc.get(proc).setResource(old);
            ArrayList<Integer> aptarnautiProcesai = new ArrayList<>();
            int aptarnautuProcesuSkaicius = 0;
            //TODO paskirstytojas
            for(int i = 0; i < aptarnautuProcesuSkaicius; i++)
            {
                if(aptarnautiProcesai.get(i) != OS.kernel.procDesc.getProcessName())
                {
                    int processName = aptarnautiProcesai.get(i);
                    OS.kernel.pps.addPps(processName, OS.processDesc.get(i).getPriority());
                    OS.processDesc.get(i).setList_where_process_is(-1);
                    String state = OS.processDesc.get(i).getState();
                    if (state.equals("BLOCKED"))
                    {
                        OS.processDesc.get(i).setState("READY");
                    }
                    else
                    {
                        OS.processDesc.get(i).setState("READYS");
                    }
                }
            }

        }
        else
        {
            ArrList old = OS.resourseDesc.get(resource).getUsed_resourse();
            //old.addSu(resource, proc, info);
        }
    }
}
