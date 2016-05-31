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
    //private ArrayList<Integer> allProcess = new ArrayList<>();
    //private ArrayList<Integer> allResourses = new ArrayList<>();
    private ProcesorDeskriptor procDesc = new ProcesorDeskriptor();
    private ArrList pps = new ArrList();
    private ArrayList<Integer> aptarnautiProcesai = new ArrayList<>();
    private int aptarnautuProcesuSkaicius = 0;
    private boolean aborted = false;
    
    public void planuotojas()
    {
        OS.plan = true;
        int processNumber = OS.kernel.procDesc.getProcessName();
        int index = OS.kernel.findProc(processNumber, OS.processDesc);
        //TODO gali reikt tikrint ar ne null
        if (!OS.processDesc.get(index).getState().equals("BLOCKED"))
        {
            OS.processDesc.get(index).setCPU();
            OS.kernel.pps.addPps(OS.processDesc.get(index).getId(), OS.processDesc.get(index).getPriority());
            int next_process = OS.kernel.pps.removeFirst();
            next_process = OS.kernel.findProc(next_process, OS.processDesc);
            OS.processDesc.get(next_process).getCpu();
            OS.kernel.procDesc.setProcessName(OS.processDesc.get(next_process).getId());
        }else
        {
            OS.processDesc.get(index).setCPU();
            //OS.kernel.pps.addPps(processNumber, OS.processDesc.get(processNumber).getPriority());
            int next_process = OS.kernel.pps.removeFirst();
            int next_process1 = OS.kernel.findProc(next_process, OS.processDesc);
            OS.processDesc.get(next_process1).getCpu();
            OS.processDesc.get(next_process1).setState("RUN");
            OS.kernel.procDesc.setProcessName(OS.processDesc.get(next_process1).getId());
        }
        
    }
    public void pertraukimuApdorotojas()
    {
        int index;
        index = OS.kernel.findResName("VM_INTERRUPTED", OS.resourseDesc);
        OS.kernel.deaktyvuotiR(index);
        index = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", OS.resourseDesc);
        OS.kernel.deaktyvuotiR(index);
        if (OS.realMachine.getRegisterTI() == 0)
        {
            OS.realMachine.setRegisterTI(50);
            OS.kernel.planuotojas();
        }
        if(OS.realMachine.getRegisterAI() == 1)
        {
            int res = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", OS.resourseDesc);
            OS.kernel.aktyvuotiR(res, 1, "IVESK_PROGRAMA");
//programos ivedimas
            OS.realMachine.setRegisterAI(0);
        }
        if(OS.realMachine.getRegisterAI() == 2)
        {
            int res = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", OS.resourseDesc);
            OS.kernel.aktyvuotiR(res, 1, "DARBO_PABAIGA");
//OS darbo pabaiga
            OS.realMachine.setRegisterAI(0);
        }
        int id = OS.kernel.getProcDesc().getProcessName();
        index = OS.kernel.findProc(id, OS.processDesc);
        String name = OS.processDesc.get(index).getName();
        if(name.equals("VIRTUAL_MACHINE"))
        {
            int govervId = OS.processDesc.get(index).getFather_processor();
            OS.interruptedGovernor = govervId;
            //int governIndex = OS.kernel.findProc(govervId, OS.processDesc);
            OS.kernel.stopProc(id);
            if(OS.realMachine.getRegisterSI() == 4)
            {
               int res = OS.kernel.findResName("VM_INTERRUPTED", OS.resourseDesc);
               OS.kernel.aktyvuotiR(res, 1, "BAIGIAMAS_DARBAS");
                
               //OS.rmMemory[1].cell = "4";
               
               //Virtualios daro pabaiga
               OS.realMachine.setRegisterSI(0);
            }
            if(OS.realMachine.getRegisterSI() == 3)
            {
               //atmintiesPrasymas
               int res = OS.kernel.findResName("VM_INTERRUPTED", OS.resourseDesc);
               OS.kernel.aktyvuotiR(res, 1, "PAPILDOMA_ATMINTIS");
               OS.realMachine.setRegisterSI(0);
            }
            if(OS.realMachine.getRegisterSI() == 2)
            {
               int res = OS.kernel.findResName("VM_INTERRUPTED", OS.resourseDesc);
               OS.kernel.aktyvuotiR(res, 1, "ISVEDIMAS");
                //isvedimo prasymas
               OS.realMachine.setRegisterSI(0);
            }
            if(OS.realMachine.getRegisterSI() == 1)
            {
                int res = OS.kernel.findResName("VM_INTERRUPTED", OS.resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "IVEDIMAS");
                //ivedimo prasymas
                OS.realMachine.setRegisterSI(0);
            }
            OS.realMachine.setRegisterSI(0);
            OS.realMachine.setRegisterPI(0);
        }
        /*index = OS.kernel.findProc(OS.kernel.procDesc.getProcessName(), OS.processDesc);
        if(!OS.processDesc.get(index).getName().equals("INTERFACE"))
        {
            //reikia nustatyti job governor
        }
        else
        {
                
        }*/
       
        
    }
    //procesu primityvai
    
   public void paskirstytojas(int r){
       
        this.aptarnautuProcesuSkaicius = 0;
        this.aptarnautiProcesai = new ArrayList<>();
        int index = OS.kernel.findRes(r, OS.resourseDesc);
        if(OS.resourseDesc.get(index).getName().equals("VM_INTERRUPTED"))
        {
            if(OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().size() > 0)
            {
                if( OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().getList().size() > 0)
                {
                    int proc_index = OS.kernel.findProc(OS.interruptedGovernor, OS.processDesc);
                    ArrList old = OS.processDesc.get(proc_index).getResource();
                    int resource = OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().getList().get(0).part_of_resourse;
                    old.addR(r,resource);
                    OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().remove(0);
                    OS.processDesc.get(proc_index).setResource(old);
                    this.aptarnautiProcesai.add(proc_index);
                    this.aptarnautuProcesuSkaicius++;
                }
            }
        }else
        {
       
        
        System.out.println("resurso vardas: " + OS.resourseDesc.get(index).getName());
        System.out.println("laukianciu procesu: " + OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().size());
        System.out.println("prieinamu resursu: " + OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().getSize());
        for( int i = 0; i < OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().size(); i++ ){
           
            if( OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().getList().size() >=
                OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().get(i).part_of_resourse ){
               
                int proc_index = OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().get(i).processId;
                //System.out.println("resursas procesas :" + OS.processDesc.get(OS.kernel.findProc(proc_index, OS.processDesc)).getName());
                if( !OS.resourseDesc.get(index).getName().equals("OPERATYVIOJI_ATMINTIS")){
                    for( int j = 0; j<OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().get(i).part_of_resourse; j++){
                        ArrList old = OS.processDesc.get(proc_index).getResource();
                        int resource = OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().getList().get(0).part_of_resourse;
                        old.addR(r,resource);
                        OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().remove(0);
                        OS.processDesc.get(proc_index).setResource(old);
                        
                    }
                    //System.out.println("pirmas laukiantis procesas: " + OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().get(proc_index).processId);
                    OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().remove(proc_index);
                } else {
                    for( int j = 0; j<OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().get(i).part_of_resourse; j++){
                        ArrList old = OS.processDesc.get(proc_index).getOperating_memory();
                        int resource = OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().getList().get(0).part_of_resourse;
                        old.addOa(r,resource);
                        OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas().remove(0);
                        OS.processDesc.get(proc_index).setOperating_memory(old);
                        OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().remove(proc_index);
                    }
                    OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().remove(proc_index);
                }
               
                this.aptarnautiProcesai.add(proc_index);
                this.aptarnautuProcesuSkaicius++;    
            }
        }
        System.out.println("UPDATE laukianciu procesu: " + OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas().getList().size());
        }
    }
    
    public void createProcess( ArrList memory, ArrList resourse, int priority, CPU cpu, String name){
        
        ProcessDescriptor process = new ProcessDescriptor();
        //process.setCPU();
        process.setName(name);
        process.cpu = cpu;
        process.setOperating_memory(memory);
        process.setResource(resourse);
        process.setPriority(priority);
        process.setState("READY");
        process.setList_where_process_is(-1);
        if(OS.processDesc.size() != 0)
        {
            int father = OS.kernel.procDesc.getProcessName();
            father = OS.kernel.findProc(father, OS.processDesc);
            process.setFather_processor(OS.processDesc.get(father).getId());
            OS.processDesc.get(father).addSon(process.getId());
        }else
        {
            process.setFather_processor(process.getId());
        }
        OS.kernel.pps.addPps(process.getId(), process.getPriority());
        OS.processDesc.add(process);
    
    }
    
    public void abortProcess( int index ){
        this.aborted = false;
        abort(index);
        if(this.aborted){
            planuotojas();
        }
       
    }
    
    public void abort(int index){
       
        index = OS.kernel.findProc(index, OS.processDesc);
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
               OS.resourseDesc.get(r.processId).getPrieinamu_resursu_sarasas().addPa(r.part_of_resourse, "");
               
           }
        }
        if( OS.processDesc.get(index).getResource().getList().size() > 0 ){
           for( Struct r : OS.processDesc.get(index).getResource().getList() ){
               if(OS.resourseDesc.get(r.processId).isRepeated_use()){
                   OS.processDesc.get(index).getResource().getList().remove(r.processId);
                   OS.resourseDesc.get(r.processId).getPrieinamu_resursu_sarasas().addPa(r.part_of_resourse, "");
               }
           }
        }
        Iterator<Integer> it = OS.processDesc.get(index).getCreated_resourses().iterator();
        while(it.hasNext()){
            OS.resourseDesc.remove(it.next());
        }
        OS.processDesc.remove(index);
    }
    public void stopProc( int index ){
        int ind = OS.kernel.findProc(index, OS.processDesc);
        if( OS.processDesc.get(ind).getState().equals("RUN")){
            OS.processDesc.get(ind).setCPU();
            OS.processDesc.get(ind).setState("BLOCKED");
            planuotojas();
        } else if ( OS.processDesc.get(ind).getState().equals("BLOCKED") ||
                    OS.processDesc.get(ind).getState().equals("BLOCKEDS")){
            OS.processDesc.get(ind).setState("BLOCKEDS");
        } else {
            OS.processDesc.get(ind).setState("READYS");
        }
    }
    
    public void acivateProc( int index ){
        int ind = OS.kernel.findProc(index, OS.processDesc);
        if( OS.processDesc.get(ind).getState().equals("READYS")){
            OS.processDesc.get(ind).setState("READYS");
        } else {
            OS.processDesc.get(ind).getState().equals("READYS");
        }
        if( OS.processDesc.get(ind).equals("READY")){
            planuotojas();
        }
    }
    //resursu primityvai
    public void kurtiResursa(boolean pakartotinio, ArrList prienamumo_aprasymas, int adr, String name)
    {
        //System.out.println("kuriamo resurso prienamumo argumento dydis: " + prienamumo_aprasymas.getSize());
        ResourseDescriptor resDesc = new ResourseDescriptor();
        resDesc.setName(name);
        resDesc.setRes_dist_addr(adr);
        resDesc.setRepeated_use(pakartotinio);
        resDesc.setInfo("");
        resDesc.setFather_resource(OS.kernel.procDesc.getProcessName());
        resDesc.setPrieinamu_resursu_sarasas(prienamumo_aprasymas);
        //System.out.println("kuriamo resurso uzpildymo dydis: " + resDesc.getPrieinamu_resursu_sarasas().getSize());
        int index = OS.kernel.procDesc.getProcessName();
        index = OS.kernel.findProc(index, OS.processDesc);
        ArrayList old = OS.processDesc.get(index).getCreated_resourses();
        old.add(resDesc.getRs());
        OS.processDesc.get(index).setCreated_resourses(old);
        OS.resourseDesc.add(resDesc);
        //System.out.println("-----------------");
        for(int i = 0; i < OS.resourseDesc.size(); i++)
        {
            
            //System.out.println("vardas: " + OS.resourseDesc.get(i).getName());
            //System.out.println("sisteminiai procesai prieinamu procesu saraso dydis: " + OS.resourseDesc.get(i).getPrieinamu_resursu_sarasas().getSize());
        }
        //System.out.println("paskutinis elementas : " + OS.resourseDesc.get(OS.resourseDesc.size()-1).getPrieinamu_resursu_sarasas().getSize());
    }
    public void prasytiResurso(int resourse, int part)
    {
        int index = OS.kernel.findRes(resourse, OS.resourseDesc);
        ArrList old = OS.resourseDesc.get(index).getLaukianciu_procesu_sarasas();
        int id = OS.kernel.procDesc.getProcessName();
        id = OS.kernel.findProc(id, OS.processDesc);
        int prior = OS.processDesc.get(id).getPriority();
        String info = "";
        old.addLps(OS.processDesc.get(id).getId(), part, info, prior);
        OS.resourseDesc.get(index).setLaukianciu_procesu_sarasas(old);
        aptarnautiProcesai = new ArrayList<>();
        aptarnautuProcesuSkaicius = 0;
        paskirstytojas(resourse);
        boolean einamas = true;
        for(int i = 0; i < aptarnautuProcesuSkaicius; i++)
        {
            if(aptarnautiProcesai.get(i) != OS.kernel.procDesc.getProcessName())
            {
                int processName = aptarnautiProcesai.get(i);
                int index1 = OS.kernel.findProc(processName, OS.processDesc);
                OS.kernel.pps.addPps(processName, OS.processDesc.get(index1).getPriority());
                OS.processDesc.get(index1).setList_where_process_is(-1);
                String state = OS.processDesc.get(index1).getState();
                if (state.equals("BLOCKED"))
                {
                    OS.processDesc.get(index1).setState("READY");
                }
                else
                {
                    OS.processDesc.get(index1).setState("READYS");
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
            //OS.kernel.procDesc.setProcessName(-1);
            //OS.kernel.pps.remove(id);
        }
        OS.kernel.planuotojas();
    }
    public void atlaisvintiResursa(int resource, int part, int proc, String info)
    {
        int index = OS.kernel.findRes(resource, OS.resourseDesc);
        int index1 = OS.kernel.findProc(proc, OS.processDesc);
        
        //System.out.println("->>>resurso vardas: " + OS.resourseDesc.get(index).getName());
        
        if(OS.resourseDesc.get(index).isRepeated_use())
        {
            ArrList old = OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas();
            //System.out.println("senas prienaimu dydis: " + old.getSize());
            old.addPa(part, info);
            //System.out.println("papildytas prieinamu: " + old.getSize());
            OS.resourseDesc.get(index).setPrieinamu_resursu_sarasas(old);
            old = OS.processDesc.get(index1).getResource();
            old.removeR(resource, part);
            OS.processDesc.get(index1).setResource(old);
            aptarnautiProcesai = new ArrayList<>();
            aptarnautuProcesuSkaicius = 0;
            paskirstytojas(resource);
            for(int i = 0; i < aptarnautuProcesuSkaicius; i++)
            {
                if(aptarnautiProcesai.get(i) != OS.kernel.procDesc.getProcessName())
                {
                    int processName = aptarnautiProcesai.get(i);
                    int index2 = OS.kernel.findProc(processName, OS.processDesc);
                    OS.kernel.pps.addPps(processName, OS.processDesc.get(index2).getPriority());
                    OS.processDesc.get(index2).setList_where_process_is(-1);
                    String state = OS.processDesc.get(index2).getState();
                    if (state.equals("BLOCKED"))
                    {
                        OS.processDesc.get(index2).setState("READY");
                    }
                    else
                    {
                        OS.processDesc.get(index2).setState("READYS");
                    }
                }
            }

        }
        else
        {
            info = OS.processDesc.get(index1).getResource().getList().get(resource).info;
            ArrList old = OS.resourseDesc.get(index).getUsed_resourse();
            old.addSu(proc, part, info);
            OS.resourseDesc.get(index).setUsed_resourse(old);
            OS.processDesc.get(index1).getResource().removeR(resource, part);
        }
    }
    public void aktyvuotiR(int resource, int part, String info)
    {
        int index = OS.kernel.findRes(resource, OS.resourseDesc);
        ArrList old = OS.resourseDesc.get(index).getPrieinamu_resursu_sarasas();
        old.addPa(part, info);
        OS.resourseDesc.get(index).setPrieinamu_resursu_sarasas(old);
    }
    public void deaktyvuotiR(int resource)
    {
        int index = OS.kernel.findRes(resource, OS.resourseDesc);
        ArrList newL = OS.resourseDesc.get(index).getUsed_resourse();
        newL.getList().clear();
        OS.resourseDesc.get(index).setUsed_resourse(newL);
    }
    public int findProc(int id,ArrayList<ProcessDescriptor> list)
    {
        int index = -1;
        for(ProcessDescriptor obj : list)
        {
            index++;
            if(obj.getId() == id)
            {
                return index;
            }
        }
        return -1;
    }
    public int findRes(int id,ArrayList<ResourseDescriptor> list)
    {
        int index = -1;
        for(ResourseDescriptor obj : list)
        {
            index++;
            if(obj.getRs() == id)
            {
                return index;
            }
        }
        return -1;
    }
    public int findResName(String name,ArrayList<ResourseDescriptor> list)
    {
        for(ResourseDescriptor obj : list)
        {
            if(obj.getName().equals(name))
            {
                return obj.getRs();
            }
        }
        return -1;
    }
    public int findProcName(String name,ArrayList<ProcessDescriptor> list)
    {
        for(ProcessDescriptor obj : list)
        {
            if(obj.getName().equals(name))
            {
                return obj.getId();
            }
        }
        return -1;
    }

    public ProcesorDeskriptor getProcDesc()
    {
        return procDesc;
    }

    public void setProcDesc(ProcesorDeskriptor procDesc)
    {
        this.procDesc = procDesc;
    }

    public ArrList getPps()
    {
        return pps;
    }

    public void setPps(ArrList pps)
    {
        this.pps = pps;
    }
    
}
