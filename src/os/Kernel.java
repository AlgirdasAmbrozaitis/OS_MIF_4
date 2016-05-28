/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Algirdas
 */
public class Kernel
{
    private ArrayList<Integer> allProcess = new ArrayList<>();
    private ArrayList<Integer> allResourses = new ArrayList<>();
    private ProcesorDeskriptor procDesc = new ProcesorDeskriptor();
    private ArrList pps = new ArrList();
    private ArrayList<Integer> aptarnautiProcesai = new ArrayList<>();
    private int aptarnautuProcesuSkaicius = 0;
    private boolean aborted = false;
    
    public void planuotojas()
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
    public void pertraukimuApdorotojas()
    {
        
    }
    //procesu primityvai
    
    public void createProcess( ArrList memory, ArrList resourse, int priority ){
        
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
    
    public void abortProcess( int index ){
        this.aborted = false;
        abort(index);
        if(this.aborted){
            planuotojas();
        }
       
    }
    
    void abort(int index){
       
        if( OS.processDesc.get(index).getState().equals("RUN")){
            this.aborted = true;
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
               OS.resourseDesc.get(r.processId).getPrieinamu_resursu_sarasas().addPa(r.part_of_resourse);
               
           }
        }
        if( OS.processDesc.get(index).getResource().getList().size() > 0 ){
           for( Struct r : OS.processDesc.get(index).getResource().getList() ){
               if(OS.resourseDesc.get(r.processId).isRepeated_use()){
                   OS.processDesc.get(index).getResource().getList().remove(r.processId);
                   OS.resourseDesc.get(r.processId).getPrieinamu_resursu_sarasas().addPa(r.part_of_resourse);
               }
           }
        }
        Iterator<Integer> it = OS.processDesc.get(index).getCreated_resourses().iterator();
        while(it.hasNext()){
            OS.resourseDesc.remove(it.next());
        }
        OS.processDesc.remove(index);
    }
    //resursu primityvai
    public void kurtiResursa(String name, boolean pakartotinio, ArrList prienamumo_aprasymas, int adr)
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
    public void prasytiResurso(int resourse, int part)
    {
        ArrList old = OS.resourseDesc.get(resourse).getLaukianciu_procesu_sarasas();
        int id = OS.kernel.procDesc.getProcessName();
        int prior = OS.processDesc.get(id).getPriority();
        String info = "";
        old.addLps(id, part, info, prior);
        OS.resourseDesc.get(resourse).setLaukianciu_procesu_sarasas(old);
        aptarnautiProcesai = new ArrayList<>();
        aptarnautuProcesuSkaicius = 0;
        paskirstytojas(resourse);
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
    public void atlaisvintiResursa(int resource, int part, int proc)
    {
        if(OS.resourseDesc.get(resource).isRepeated_use())
        {
            ArrList old = OS.resourseDesc.get(resource).getPrieinamu_resursu_sarasas();
            old.addPa(part);
            OS.resourseDesc.get(resource).setPrieinamu_resursu_sarasas(old);
            old = OS.processDesc.get(proc).getResource();
            old.removeR(resource, part);
            OS.processDesc.get(proc).setResource(old);
            aptarnautiProcesai = new ArrayList<>();
            aptarnautuProcesuSkaicius = 0;
            paskirstytojas(resource);
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
            String info = OS.processDesc.get(proc).getResource().get(resource).info;
            ArrList old = OS.resourseDesc.get(resource).getUsed_resourse();
            old.addSu(proc, part, info);
            OS.resourseDesc.get(resource).setUsed_resourse(old);
            OS.processDesc.get(proc).getResource().removeR(resource, part);
        }
    }
    public void aktyvuotiR(int resource, int part, String info)
    {
        ArrList old = OS.resourseDesc.get(resource).getPrieinamu_resursu_sarasas();
        old.addPa(part);
        OS.resourseDesc.get(resource).setPrieinamu_resursu_sarasas(old);
    }
    public void deaktyvuotiR(int resource)
    {
        ArrList newL = new ArrList();
        OS.resourseDesc.get(resource).setUsed_resourse(newL);
    }
    public void paskirstytojas(int resource)
    {
        
    }
    
}
