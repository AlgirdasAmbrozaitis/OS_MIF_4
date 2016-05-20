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
    
    private String state;
    private int priority;
    private ArrayList   r,
                        sd,
                        sr,
                        st,
                        t,
                        s;

    private ArrayList oa;
    
    CPU cpu = new CPU();
    
    ProcessDescriptor(){
        this.r = new ArrayList();
    }
    
    public void setOA( ArrayList arr ){
        oa = arr;
    }
    
    public void setR(  ){
        
    }
    
    public void setCPU(){
        
        cpu.setRegisterR(OS.realMachine.getRegisterR());
        cpu.setRegisterPTR(OS.realMachine.getRegisterPTR());
        cpu.setRegisterIC(OS.realMachine.getRegisterIC());
        cpu.setRegisterSP(OS.realMachine.getRegisterSP());
        cpu.setRegisterINT(OS.realMachine.getRegisterINT());
        cpu.setRegisterCT(OS.realMachine.getRegisterCT());
        cpu.setRegisterPI(OS.realMachine.getRegisterPI());
        cpu.setRegisterSI(OS.realMachine.getRegisterSI());
        cpu.setRegisterTI(OS.realMachine.getRegisterTI());
        cpu.setRegisterC(OS.realMachine.isRegisterC());
        cpu.setRegisterMOD(OS.realMachine.isRegisterMOD());
        
    }
    
    
}
