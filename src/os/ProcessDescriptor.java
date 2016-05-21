package os;

import java.awt.List;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collections;

/**
 *
 * @author eimantas
 */
public class ProcessDescriptor {
    
    private static int process_id_counter = 0; 
    private final int id = process_id_counter++;
    
    private String state; // proceso būsena
    private int priority, // Proceso prioritetas
                f_processor; // tėvinio proceso ID

    private ArrayList   s_processes, // sūnų sąrašas
                        resource, // esami proceso resursai
                        process_list, // nuoroda, kuriame sąraše esti procesas (LPS,PPS)
                        created_resourses; // sukurtų resursų sąrašas
    
    private ArrayList operating_memory; // operatyvioji atmintis
    
    CPU cpu = new CPU();
    
    public ProcessDescriptor()
    {
        
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
        this.priority = priority;
    }

    public int getF_processor() {
        return f_processor;
    }

    public void setF_processor(int f_processor) {
        this.f_processor = f_processor;
    }

    public ArrayList getS_processes() {
        return s_processes;
    }

    public void setS_processes(ArrayList s_processes) {
        this.s_processes = s_processes;
    }

    public ArrayList getResource() {
        return resource;
    }

    public void setResource(ArrayList resource) {
        this.resource = resource;
    }

    public ArrayList getProcess_list() {
        return process_list;
    }

    public void setProcess_list(ArrayList process_list) {
        this.process_list = process_list;
    }

    public ArrayList getCreated_resourses() {
        return created_resourses;
    }

    public void setCreated_resourses(ArrayList created_resourses) {
        this.created_resourses = created_resourses;
    }

    public ArrayList getOperating_memory() {
        return operating_memory;
    }

    public void setOperating_memory(ArrayList operating_memory) {
        this.operating_memory = operating_memory;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    // Nustatomos registrų reikšmės
    public void setCPU(){
        
        cpu.setRegisterR(OS.realMachine.getRegisterR());
        cpu.setRegisterPTR(OS.realMachine.getRegisterPTR());
        cpu.setRegisterIC(OS.realMachine.getRegisterIC());
        cpu.setRegisterSP(OS.realMachine.getRegisterSP());
        cpu.setRegisterINT(OS.realMachine.getRegisterINT());
        cpu.setRegisterCT(OS.realMachine.getRegisterCT());
        cpu.setRegisterPI(OS.realMachine.getRegisterPI());
        cpu.setRegisterSI(OS.realMachine.getRegisterSI());
        cpu.setRegisterC(OS.realMachine.isRegisterC());
        cpu.setRegisterMOD(OS.realMachine.isRegisterMOD());   
    }    
    
}
