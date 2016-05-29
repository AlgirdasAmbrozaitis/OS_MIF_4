/*
TODO:
    1 apsaugoti, kad atpazinus komanda butu validus argumentas
    tiesiog eilute bandymo
    2 labas
    bandymas
    2 Labas
    3 EIMANTAS
jkj
lkhj
xxxxxxxxxxxxxxxxx
;abasasdbasdgkjasldkgjm
lkhjb
 */
package os;


import static java.lang.String.*;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Algirdas
 */
public class OS { 

    /**
     * @param args the command line arguments
     */  
    public static final int RM_MEMORY_SIZE = 1000;
    public static final int VM_MEMORY_SIZE = 100;
    public static final int EXTERNAL_MEMORY_SIZE = 4000;
    
    //agregatai
    public static RealMachine realMachine = new RealMachine();
    //public static VirtualMachine virtualMachine = new VirtualMachine();
    //public static final ArrayList myarr = new ArrayList();
    public static ArrayList<ProcessDescriptor> processDesc = new ArrayList<>();
    public static ArrayList<ResourseDescriptor> resourseDesc = new ArrayList<>();
    public static Kernel kernel = new Kernel();
    
    
    
    public static Paging paging = new Paging();
    //public static GUI OSgui = new GUI();
    public static ChannelDevice cd = new ChannelDevice();
    
    // REALIOS MAŠINOS ATMINTS
    public static Memory[] rmMemory = new Memory[RM_MEMORY_SIZE];
    // IŠORINĖ ATMINTIS
    public static Memory[] externalMemory = new Memory[EXTERNAL_MEMORY_SIZE];
    
    //galimos komandos
    public static String[] COMMANDS = {
        // Bendros komandos
        "CHNGR", "LR", "SR", "LO", "AD", "SB", "MP", "DI", "CR", "RL", "RG", "CZ",
        "JC", "JP", "CA", "PU", "PO", "RETRN", "SY", "LP", 
        // Tik realios masinos komandos
        "CHNGM", "PI", "TI", "PTR", "SP", "IN", "START", "CALLI", "IRETN",
        "BS", "DB", "ST", "DT", "SZ", "XCHGN"
    };
    
    
    public static void memoryInit()
    {
        for( int i = 0 ; i < RM_MEMORY_SIZE ; i++ )
        {
            rmMemory[i] = new Memory();
        }
        for( int i = 0 ; i < EXTERNAL_MEMORY_SIZE ; i++ )
        {
            externalMemory[i] = new Memory();
        }
    }
    
    // ATMINTIES OUTPUT
    public static void memoryMonitoring(){
        for( int i = 0 ; i < RM_MEMORY_SIZE ; i++ ){   
            System.out.println(i + " " + rmMemory[i]);
        }
    }
    
    //darbas su komandomis
    public static String getCommand()
    {
        if (realMachine.isRegisterMOD())
        {
            //turim ziureti pagal virtualia masina
            int adr = OS.paging.getRMadress(realMachine.getRegisterIC());
            return rmMemory[adr].getCell();
        }
        else 
        {
            //turim ziuret pagal realia masina
            return rmMemory[realMachine.getRegisterIC()].getCell();
        }
    }
    public static int findCommand( String command ){
        
        for(char i = 0 ; i < COMMANDS.length; i++ ){
            if ( command.contains(COMMANDS[i]) ){
                return i;
            }
        }
        return -1;
    }
    public static String getValue(int commandNumber, String command)
    {
        String registers[] = {"CT", "IC", "SP", "C", "R", "MOD", "PTR", "TI","SI","PI", "INT"}; 
        if( commandNumber >= 0 )
        {
            String commandBegin = COMMANDS[commandNumber];
            String maybe = command.replace(commandBegin, "");
            for(int i = 0; i < registers.length; i++)
            {
                if(maybe.endsWith(registers[i]))
                {
                    return maybe;
                }
                else if (maybe.matches("\\d+"))
                {
                    return maybe;
                }else if(maybe.equals(""))
                {
                    return maybe;
                }
            }
            OS.realMachine.setRegisterPI(2);
            return "COMMAND NOT FOUND";
        } 
        else 
        { 
            //OS.realMachine.setRegisterPI(2);
            return "PROCESS LINE";
        }
    }
    public static void executeCommand()
    {        
        String command = getCommand();
        int commandNumber = findCommand(command);
        String value = getValue(commandNumber, command);
        
        realMachine.setRegisterTI(realMachine.getRegisterTI() - 1);
        if(value.equals("PROCESS LINE"))
        {
            String line = rmMemory[realMachine.getRegisterIC()].getCell();
            if(line.matches("\\d+"))
            {
                int proc = OS.kernel.getProcDesc().getProcessName();
                proc = OS.kernel.findProc(proc, OS.processDesc);
                String name = OS.processDesc.get(proc).getName();
                switch(name)
                {
                    case "START/STOP":
                    {
                        startStop(Integer.valueOf(line));
                        break;
                    }
                }
            }
            realMachine.setRegisterIC(realMachine.getRegisterIC() + 1);
        }else if( !value.equals("COMMAND NOT FOUND") )
        {
            /*if( realMachine.isRegisterMOD() )
            {
                // MOD = 1, paskiriame komandos vykdyma virtualiai masinai
                    virtualMachine.setRegisterIC(virtualMachine.getRegisterIC() + 1);
                    virtualMachine.doCommand(commandNumber, value );
            } 
            else
            {*/
                // MOD = 0, paskiriame komandos vykdyma realiai masinai
                    realMachine.setRegisterIC(realMachine.getRegisterIC() + 1);
                    realMachine.doCommand(commandNumber, value);
            //}
        }
        else 
        {
            // PI = 2, pertraukimo reikšmė dėl neleistino operacijos kodo
            /*OS.realMachine.setRegisterPI(2);
            if(realMachine.isRegisterMOD())
            {
                virtualMachine.setRegisterIC(virtualMachine.getRegisterIC() + 1);
            }
            else
            {*/

            realMachine.setRegisterIC(realMachine.getRegisterIC() + 1);
            //}
        }
        
    }
    public static void checkInterupts()
    {
        if (realMachine.getRegisterSI() != 0 || 
            realMachine.getRegisterPI() != 0 ||
            realMachine.getRegisterTI() == 0)
        {
            opperateInterupts();
        }
    }
    public static void opperateInterupts()
    {
        
        //OSgui.refreshRegisterFields();
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if (realMachine.getRegisterSI() != 0)
        {
            //OSgui.output("SI was: " + String.valueOf(realMachine.getRegisterSI()) + " ");
            realMachine.setRegisterSI(0);
            
        }
        else if (realMachine.getRegisterPI() != 0)
        {
            //OSgui.output("PI was: " + String.valueOf(realMachine.getRegisterPI()) + " ");
            realMachine.setRegisterPI(0);
        }else if (realMachine.getRegisterTI() == 0)
        {
            //OSgui.output("TI was: " + String.valueOf(realMachine.getRegisterTI()) + " ");
            realMachine.setRegisterTI(10);
        }
    }
    public static void cpu()
    {
        executeCommand();
        //OSgui.refreshRegisterFields();
        out.println(realMachine.toString());
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        checkInterupts();      
    }
    public static void initializeSystem()
    {
        //sukurtas start stop procesas
        CPU cpu = new CPU(false, 0, 0);
        ArrList memory = new ArrList();
        ArrList resource = new ArrList();
        OS.kernel.createProcess(memory, resource, 0, cpu, "START/STOP");

        //kuriami resursai
        int adr = 0; //kolkas neaisku
        ArrList pa = new ArrList();
        //operatyvi atmintis
        
        
        for(int i = 0; i < OS.rmMemory.length / 10; i++)
        {
            pa.addPa(i);
        }
        
        OS.kernel.kurtiResursa(true, pa, adr, "OPERATYVIOJI_ATMINTIS");
          
        // ivedimo irenginys
        pa.getList().clear();
        pa.addPa(1);
        OS.kernel.kurtiResursa(true, pa, adr, "IVEDIMO_IRENGINYS");
        
        // isvedimo irenginys
        pa.getList().clear();
        pa.addPa(1);
        OS.kernel.kurtiResursa(true, pa, adr, "ISVEDIMO_IRENGINYS");
        
        // isorine atmintis
        pa.getList().clear();
        for(int i = 0; i < OS.externalMemory.length / 10; i++)
        {
            pa.addPa(i);
        }
        OS.kernel.kurtiResursa(true, pa, adr, "ISORINE_ATMINTIS");
        
        // laukimo resursas
        pa.getList().clear();
        pa.addPa(1);
        OS.kernel.kurtiResursa(true, pa, adr, "SYSTEM_IDLE");
        
        // darbo pabaiga
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "DARBO_PABAIGA");
        
        // virtualios masinos pertraukimas
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "VM_INTERRUPTED");
        
        // pranesimas vartotojui
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "PRANESIMAS_VARTOTOJUI");
        
        // programos ivedimo
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "IVESK_PROGRAMA");
        
        // programos ivedimo pabaiga
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "IVESK_PROGRAMA_END");
        
        // duomenu ivedimas
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "INPUT_DATA");
        
        // duomenu ivedimo pabaiga
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "INPUT_DATA_END");
        
        // duomenu isvedimas
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "OUTPUT_DATA");
        
        // duomenu isvedimo pabaiga
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "OUTPUT_DATA_END");
        
        // atminties prasymas
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "ASK_MEMORY");
        
        // atminties davimas
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "MEMORY_GIVEN");
        
        // uzduotis paimta
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "UZDUOTIS_PAIMTA");
        
        // uzduotis isorineje atmintyje
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "UZDUOTIS_ISORINEJE_ATMINTYJE");
        
        // pakrovimo paketas
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "PAKROVIMO_PAKETAS");
        
        // uzduotis poakrauta
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "LOADER_END");
        
        // isvedimo vartotojui pabaiga
        pa.getList().clear();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "OUTPUT_TO_USER_END");
    }
    
    public static void startStop(int line)
    {
        ArrList memory = new ArrList();
        ArrList resource = new ArrList();
        int priority = 0;
        CPU cpu = new CPU(false, 0 , 0);
        switch(line)
        {
            case 0: 
            {
                int ic = 10;
                cpu = new CPU(false, 0, ic);
                priority = 1;
                OS.kernel.createProcess(memory, resource, priority, cpu, "OUTPUT_TO_USER");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 1: 
            {
                int ic = 20;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "INPUT_PROGRAM");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 2: 
            {
                int ic = 30;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "INPUT_DATA");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 3: 
            {
                int ic = 40;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "OUTPUT_DATA");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 4: 
            {
                int ic = 50;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "ADDITIONAL_MEMORY");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 5: 
            {
                int ic = 60;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "MAIN_PROC");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 6: 
            {
                int ic = 70;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "LOADER");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 7: 
            {
                int ic = 80;
                cpu = new CPU(false, 0, ic);
                priority = 5;
                OS.kernel.createProcess(memory, resource, priority, cpu, "SYSTEM_IDLE");
                rmMemory[ic].cell = "0";
                rmMemory[ic + 1].setCell("LR0");
                rmMemory[ic + 2].setCell("AD5");
                rmMemory[ic + 3].setCell("SR0");
                rmMemory[ic + 4].setCell("JP0");
                rmMemory[ic + 5].setCell("1");
                break;
            }
            case 8: 
            {
                for(int i = 0; i < OS.kernel.getPps().getSize(); i++)
                {
                    int id = OS.kernel.getPps().getList().get(i).processId;
                    id = OS.kernel.findProc(id, processDesc);
                    String name = OS.processDesc.get(id).getName();
                    System.out.println(name + " prioritetas: " + OS.processDesc.get(id).getPriority());
                }
                int res = OS.kernel.findResName("DARBO_PABAIGA", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, 1);
                break;
            }
            case 9: 
            {
                System.exit(0);
                break;
            }
        }
    }
    
    public static void outputToUser(int line)
    {
        switch(line)
        {
            case 0:
            {
                String res = "PRANESIMAS_VARTOTOJUI";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                break;
            }
            
        }
    }
    public static void main(String[] args) {
        memoryInit();
        rmMemory[0].cell = "0";
        rmMemory[1].setCell("LR0");
        rmMemory[2].setCell("AD5");
        rmMemory[3].setCell("SR0");
        rmMemory[4].setCell("JP0");
        rmMemory[5].setCell("1");
        initializeSystem();
        OS.kernel.planuotojas();
        for(int i = 0; i < 40; i++)
        {
            System.out.println(i);
            cpu();
            
            rmMemory[0].setState(false);
        }
        for(int i = 0; i < 2; i++)
        {
            System.out.println(i);
            cpu();
            
            rmMemory[0].setState(false);
        }
        
        for(int i = 0; i < OS.processDesc.size(); i++)
        {
            System.out.println("proceso vardas: " + OS.processDesc.get(i).getName());
        }

        
    }
    
}
