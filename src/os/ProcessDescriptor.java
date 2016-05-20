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
                t; // tėvinio proceso ID
    private ArrayList   r, // esami proceso resursai
                        sd, // nuoroda, kuriame sąraše esti procesas (LPS,PPS)
                        sr, // sukurtų resursų sąrašas
                        st, // 
                        s;

    private ArrayList oa; // operatyvioji atmintis
    
    CPU cpu = new CPU();
    
    ProcessDescriptor( int father_id, int prior, ArrayList oa,
                       ArrayList r, ArrayList sd ){
        
        this.r = new ArrayList();
        this.sd = new ArrayList();
        this.sr = new ArrayList();
        
        this.state = "Ready"; // Sukurto proceso būsena po default yra READY
        this.t = father_id; // Tėvo ID yra šiuo metu veikiantis procesas
        this.priority = prior; 
        
        setOA(oa);
        setR(r);
        setSD(sd); // Sukurtas procesas bus PPS sąraše
    }
    
    // Nustatomas operatyviosios atminties dalis
    public void setOA( ArrayList arr ){
        oa = arr;
    }
    
    // ??
    public void setR( ArrayList arr ){
        this.sd = arr;
    }
    
    public void setSD( ArrayList arr ){
        this.sd = arr;
    }
    
    // TO DO: proceso sukurti resursai primityvų pagalba,
    // bus talpinami į SR ArrayList'ą
    public void setSR(/*value*/){
        //sr.append(/*value*/);
    }
    
    // Nuoroda, pagal duota ID, į kitą deskriptorių
    public void setT(){
        // Nustatyti, kad tėvas yra šiuo metu vykstantis procesas
    }
    
    // Nuoroda į kitą deskriptorių, sukurtą šio proceso
    public void setS(){
        // Appendinti vaikus
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
        cpu.setRegisterTI(OS.realMachine.getRegisterTI());
        cpu.setRegisterC(OS.realMachine.isRegisterC());
        cpu.setRegisterMOD(OS.realMachine.isRegisterMOD());
        
    }
    
    
}
