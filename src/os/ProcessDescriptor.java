package os;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author eimantas
 */
public class ProcessDescriptor {
    
    private static int process_id_counter = 0; 
    private final int id = process_id_counter++;
    private String name = "";
    
    private String state; // proceso būsena
    private int priority; // Proceso prioritetas
    private int father_processor; // tėvinio proceso ID

    private ArrayList<Integer> sons_processes = new ArrayList(); // sūnų sąrašas
    private ArrList resource = new ArrList(); // esami proceso resursai
    private int list_where_process_is; // nuoroda, kuriame sąraše esti procesas (LPS,PPS)
    private ArrayList<Integer> created_resourses = new ArrayList<>(); // sukurtų resursų sąrašas
    
    private ArrList operating_memory  = new ArrList(); // operatyvioji atmintis
    private String info = "";
    
    CPU cpu;
    
    public ProcessDescriptor()
    {
        
    }
    void addSon( int index ){
        this.sons_processes.add(index);
    }
    
    /*
    Proceso veikimui užstatomos/gaunamos realios mašinos registrų reikšmės
    */
    public void setCPU(){
        
        cpu.setRegisterR(OS.realMachine.getRegisterR());
        cpu.setRegisterPTR(OS.realMachine.getRegisterPTR());
        cpu.setRegisterIC(OS.realMachine.getRegisterIC());
        cpu.setRegisterSP(OS.realMachine.getRegisterSP());
        cpu.setRegisterINT(OS.realMachine.getRegisterINT());
        cpu.setRegisterCT(OS.realMachine.getRegisterCT());
        //cpu.setRegisterPI(OS.realMachine.getRegisterPI());
        //cpu.setRegisterSI(OS.realMachine.getRegisterSI());
        cpu.setRegisterC(OS.realMachine.isRegisterC());
        cpu.setRegisterMOD(OS.realMachine.isRegisterMOD());   
    }
    
    public void getCpu()
    {
        OS.realMachine.setRegisterR(cpu.getRegisterR());
        OS.realMachine.setRegisterPTR(cpu.getRegisterPTR());
        OS.realMachine.setRegisterIC(cpu.getRegisterIC());
        OS.realMachine.setRegisterSP(cpu.getRegisterSP());
        OS.realMachine.setRegisterINT(cpu.getRegisterINT());
        OS.realMachine.setRegisterCT(cpu.getRegisterCT());
        //OS.realMachine.setRegisterPI(cpu.getRegisterPI());
        //OS.realMachine.setRegisterSI(cpu.getRegisterSI());
        OS.realMachine.setRegisterC(cpu.isRegisterC());
        OS.realMachine.setRegisterMOD(cpu.isRegisterMOD());
    }
    public CPU getCpu2()
    {
        return this.cpu;
    }

    public static int getProcess_id_counter() {
        return process_id_counter;
    }

    public static void setProcess_id_counter(int process_id_counter) {
        ProcessDescriptor.process_id_counter = new Integer(process_id_counter);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = new Integer(priority);
    }

    public int getFather_processor() {
        return father_processor;
    }

    public void setFather_processor(int father_processor) {
        this.father_processor = new Integer(father_processor);
    }

    public ArrayList<Integer> getSons_processes() {
        return sons_processes;
    }

    public void setSons_processes(ArrayList<Integer> sons_processes) {
        this.sons_processes = new ArrayList<>(sons_processes);
    }

    public ArrList getResource() {
        return resource;
    }

    public void setResource(ArrList resource) {
        this.resource.myCopy(resource.getList());
    }

    public int getList_where_process_is() {
        return list_where_process_is;
    }

    public void setList_where_process_is(int list_where_process_is) {
        this.list_where_process_is = new Integer(list_where_process_is);
    }

    public ArrayList getCreated_resourses() {
        return created_resourses;
    }

    public void setCreated_resourses(ArrayList<Integer> created_resourses) {
        this.created_resourses = new ArrayList<>(created_resourses);
    }

    public ArrList getOperating_memory() {
        return operating_memory;
    }

    public void setOperating_memory( ArrList operating_memory) { 
        this.operating_memory.myCopy(operating_memory.getList());
    }

    public int getId() {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }
    
}
