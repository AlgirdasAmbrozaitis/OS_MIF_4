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
    public static ArrayList<ProcessDescriptor> processDesc = new ArrayList<ProcessDescriptor>();
    public static ArrayList<ResourseDescriptor> resourseDesc = new ArrayList<ResourseDescriptor>();
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
            OS.realMachine.setRegisterPI(2);
            return "COMMAND NOT FOUND";
        }
    }
    public static void executeCommand()
    {        
        String command = getCommand();
        int commandNumber = findCommand(command);
        String value = getValue(commandNumber, command);
        
        realMachine.setRegisterTI(realMachine.getRegisterTI() - 1);
        
        if( !value.equals("COMMAND NOT FOUND") )
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        checkInterupts();      
    }
    public static void main(String[] args) {
        // TODO code application logic here
        memoryInit();
        String komandos[] = {"CHNGR", "10", "START"};
        for(int i = 0; i < komandos.length; i++)
        {
            rmMemory[i].setCell(komandos[i]);
            
            //rmMemory[paging.getRMadress(i)].setCell(komandos[i]);
        }
        cpu();
        cpu();
        //cpu();
        String virtualiosKomandos[] = {"CHNGR",  "0", "AD3" , "1"};
        memoryMonitoring();
        for(int i = 0; i < virtualiosKomandos.length; i++)
        {
            int adr = OS.paging.getRMadress(i);
            System.out.println("adresas kuri ziuresiu: " + adr);
            rmMemory[adr].setCell(virtualiosKomandos[i]);
            
            //rmMemory[paging.getRMadress(i)].setCell(komandos[i]);
        }
        cpu();
        cpu();
        //cpu();
        //cpu();
        
        memoryMonitoring();
        
        
        
        
        
        
        //OSgui.refreshRegisterFields();
        
        //rmMemory[0].setCell("CHNGR");
        //rmMemory[0].setCell("LOSP");
        //rmMemory[1].setCell("10");
        //rmMemory[2].setCell("START");

        //OSgui.refreshRegisterFields();*/
        //String komandos[] = {"CHNgR", "10", "START"};
        //String komandos[] = {"BS000", "DB007", "ST001", "DT002", "SZ009", "XCHGN"};
        //for(int i = 0; i < komandos.length; i++)
        //{
        /*    OS.rmMemory[i].setCell(komandos[i]);
            

        }
        OSgui.refreshRegisterFields();
        cpu();
        OSgui.refreshRegisterFields();
        cpu();
        
        
        
        String komandos1[] = {"CHNGR", "99999", "LR900", "SR050", "LOSP"};
        //String komandos[] = {"JP000", "AAAAA"};
        for(int i = 0; i < komandos1.length; i++)
        {
            virtualMachine.memory[i].setCell(komandos1[i]);
            
            rmMemory[paging.getRMadress(i)].setCell(komandos1[i]);
        }
        cpu();
        cpu();
        cpu();
        cpu();
        cpu();
        cpu();
        OSgui.refreshRegisterFields();
        
        /*String a = "2";
        String b = "1";
        String c = String.valueOf(Integer.valueOf(a) - Integer.valueOf(b));
        boolean d = false;
        String f = "false";
        System.out.println(String.valueOf(d));
        //memoryInit();
        //virtualMachine.memoryInit();
        //printMemory();
        rmMemory[0].setCell("AD050");
        String command = getCommand();
        int commandNumber = findCommand(command);
        String value = getValue(commandNumber, command);
        System.out.println(Boolean.valueOf(f));
        */
        /*while(true)
        {
            if(rm.mod == 0)
            command = memory[rm.ic]
            else 
            {
            command = memory[vm.ic]
            }
            atpazystam komnda 
        }*/
    }
    
}
