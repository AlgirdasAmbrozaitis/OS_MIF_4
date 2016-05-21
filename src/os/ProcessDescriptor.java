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
    private int priority; // Proceso prioritetas
    private int father_processor; // tėvinio proceso ID

    private ArrayList<Integer> sons_processes; // sūnų sąrašas
    private ArrayList<Struct> resource; // esami proceso resursai
    private int list_where_process_is; // nuoroda, kuriame sąraše esti procesas (LPS,PPS)
    private int created_resourses; // sukurtų resursų sąrašas
    
    private ArrayList<Integer> operating_memory; // operatyvioji atmintis
    
    CPU cpu = new CPU();
    
    public ProcessDescriptor()
    {
        
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
    public void getCpu()
    {
        OS.realMachine.setRegisterR(cpu.getRegisterR());
    }
    
}
