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
import java.util.Scanner;
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
    
    public static boolean osEnd = false;
    
    public static int interruptedGovernor;
    
    public static int governorPtr;
    public static int governorId;
    
    public static int governorIc = 10;
    public static int vmIc = 20;
    
    public static ArrayList<ArrayList<Integer>> loaderOpreatingBlocks = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> loaderExternalBlocks = new ArrayList<>();
    
    public static ArrayList<Integer> askMemoryVmId = new ArrayList<>();
    public static int askMemorySize = 0;
    public static ArrayList<String> askMemoryR = new ArrayList<>();
    public static ArrayList<Integer> askMemoryPtr = new ArrayList<>();
    
    public static ArrayList<String> outputDataStrings = new ArrayList<>();
    public static ArrayList<Integer> outputDataPtr = new ArrayList<>();
    
    public static ArrayList<String> inputDataStrings = new ArrayList<>();
    public static ArrayList<Integer> inputDataPtr = new ArrayList<>();
    
    public static ArrayList<ArrayList<Integer>> uzduotisIsorinejeAtmintyje = new ArrayList<>();
    
    public static int blockedProcessId = -1; 
    public static boolean inputStreamOk = false;
    public static boolean startInput = false;
    public static boolean inputStarted = false;
    public static ArrayList<String> inputStream = new ArrayList<>();
    public static boolean outputStreamInUse = false;
    public static ArrayList<String> outputStream = new ArrayList<>();
    
    public static boolean plan = false;
    public static final int RM_MEMORY_SIZE = 1000;
    public static final int VM_MEMORY_SIZE = 100;
    public static final int EXTERNAL_MEMORY_SIZE = 4000;
    
    public static GUI gui = new GUI();
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
                    case "OUTPUT_TO_USER":
                    {
                        outputToUser(Integer.valueOf(line));
                        break;
                    }
                    case "INPUT_PROGRAM":
                    {
                        inputProgram(Integer.valueOf(line));
                        break;
                    }
                    case "INPUT_DATA":
                    {
                        inputData(Integer.valueOf(line));
                        break;
                    }
                    case "OUTPUT_DATA":
                    {
                        outpuData(Integer.valueOf(line));
                        break;
                    }
                    case "ADDITIONAL_MEMORY":
                    {
                        additionalMemory(Integer.valueOf(line));
                        break;
                    }
                    case "MAIN_PROC":
                    {
                        mainProc(Integer.valueOf(line));
                        break;
                    }
                    case "LOADER":
                    {
                        loader(Integer.valueOf(line));
                        break;
                    }
                    case "SYSTEM_IDLE":
                    {
                        idle(Integer.valueOf(line));
                        break;
                    }
                    default:
                    {
                        
                    }
                }
            }
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
            realMachine.getRegisterAI() != 0 ||
            realMachine.getRegisterTI() == 0)
        {
            opperateInterupts();
        }
    }
    public static void opperateInterupts()
    {
        OS.kernel.pertraukimuApdorotojas();
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
        OS.kernel.getPps().removeFirst();
        OS.processDesc.get(0).setState("RUN");
        String info= "";

        //kuriami resursai
        int adr = 0; //kolkas neaisku
        ArrList pa = new ArrList();
        //operatyvi atmintis
        
        
        for(int i = 0; i < OS.rmMemory.length / 10; i++)
        {
            pa.addPa(i, info);
        }
        
        OS.kernel.kurtiResursa(true, pa, adr, "OPERATYVIOJI_ATMINTIS");
          
        // ivedimo irenginys
        pa = new ArrList();
        pa.addPa(1, info);
        OS.kernel.kurtiResursa(true, pa, adr, "IVEDIMO_IRENGINYS");
        
        // isvedimo irenginys
        pa = new ArrList();
        pa.addPa(1, info);
        OS.kernel.kurtiResursa(true, pa, adr, "ISVEDIMO_IRENGINYS");
        
        // isorine atmintis
        pa = new ArrList();;
        for(int i = 0; i < OS.externalMemory.length / 10; i++)
        {
            pa.addPa(i, info);
        }
        OS.kernel.kurtiResursa(true, pa, adr, "ISORINE_ATMINTIS");
        
        // isorinis atminties irenginys
        pa = new ArrList();
        pa.addPa(1, info);
        OS.kernel.kurtiResursa(true, pa, adr, "ISORINIS_ATMINTIES_IRENGINYS");
        
        // laukimo resursas
        pa = new ArrList();
        pa.addPa(1, info);
        OS.kernel.kurtiResursa(true, pa, adr, "LAUKIMAS");
        
        // darbo pabaiga
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "DARBO_PABAIGA");
        
        // virtualios masinos pertraukimas
        pa = new ArrList();;
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "VM_INTERRUPTED");
        
        // pranesimas vartotojui
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "PRANESIMAS_VARTOTOJUI");
        
        // programos ivedimo
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "IVESK_PROGRAMA");
        
        // programos ivedimo pabaiga
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "IVESK_PROGRAMA_END");
        
        // duomenu ivedimas
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "INPUT_DATA");
        
        // duomenu ivedimo pabaiga
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "INPUT_DATA_END");
        
        // duomenu isvedimas
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "OUTPUT_DATA");
        
        // duomenu isvedimo pabaiga
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "OUTPUT_DATA_END");
        
        // atminties prasymas
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "ASK_MEMORY");
        
        // atminties davimas
        pa = new ArrList();;
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "MEMORY_GIVEN");
        
        // uzduotis paimta
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "UZDUOTIS_PAIMTA");
        
        // uzduotis isorineje atmintyje
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "UZDUOTIS_ISORINEJE_ATMINTYJE");
        
        // pakrovimo paketas
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "PAKROVIMO_PAKETAS");
        
        // uzduotis poakrauta
        pa = new ArrList();
        //pa.addPa(1);
        OS.kernel.kurtiResursa(false, pa, adr, "LOADER_END");
        
        // isvedimo vartotojui pabaiga
        pa = new ArrList();
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
                int ic = 1;
                cpu = new CPU(false, 0, ic);
                priority = 1;
                OS.kernel.createProcess(memory, resource, priority, cpu, "OUTPUT_TO_USER");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "1";
                break;
            }
            case 1: 
            {
                int ic = 2;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "INPUT_PROGRAM");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "2";
                break;
            }
            case 2: 
            {
                int ic = 3;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "INPUT_DATA");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "3";
                break;
            }
            case 3: 
            {
                int ic = 4;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "OUTPUT_DATA");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "4";
                break;
            }
            case 4: 
            {
                int ic = 5;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "ADDITIONAL_MEMORY");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "5";
                break;
            }
            case 5: 
            {
                int ic = 6;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "MAIN_PROC");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "6";
                break;
            }
            case 6: 
            {
                int ic = 7;
                cpu = new CPU(false, 0, ic);
                priority = 2;
                OS.kernel.createProcess(memory, resource, priority, cpu, "LOADER");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "7";
                break;
            }
            case 7: 
            {
                int ic = 8;
                cpu = new CPU(false, 0, ic);
                priority = 5;
                OS.kernel.createProcess(memory, resource, priority, cpu, "SYSTEM_IDLE");
                rmMemory[ic].cell = "0";
                rmMemory[0].cell = "8";
                break;
            }
            case 8: 
            {
                /*for(int i = 0; i < OS.kernel.getPps().getSize(); i++)
                {
                    int id = OS.kernel.getPps().getList().get(i).processId;
                    id = OS.kernel.findProc(id, processDesc);
                    String name = OS.processDesc.get(id).getName();
                    System.out.println(name + " prioritetas: " + OS.processDesc.get(id).getPriority());
                }*/
                int res = OS.kernel.findResName("DARBO_PABAIGA", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, 1);
                /*int id = OS.kernel.findRes(res, resourseDesc);
                for(int i = 0; i <OS.resourseDesc.get(id).getLaukianciu_procesu_sarasas().getSize(); i++ )
                {
                    System.out.println("darbo pabaigos resursas: " + OS.resourseDesc.get(id).getLaukianciu_procesu_sarasas().getList().get(i).processId);
                }*/
                rmMemory[0].cell = "9";
                break;
            }
            case 9: 
            {
                osEnd = true;
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
                int res = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, 1);
                OS.rmMemory[1].cell = "1";
                break;
            }
            case 1:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                System.out.println("proceso vardas: "  + OS.processDesc.get(index).getName());
                int resId = OS.processDesc.get(index).getResource().getList().get(0).processId;
                int resInd = OS.kernel.findRes(resId, resourseDesc);
                System.out.println("resurso vardas : " + OS.resourseDesc.get(resInd).getName());
                System.out.println("turimi resursai: " + OS.processDesc.get(index).getResource().getSize());
                System.out.println("turimi resursai: " + OS.processDesc.get(index).getResource().getList().get(0).processId);
                System.out.println("turimi resursai: " + OS.processDesc.get(index).getResource().getList().get(0).info);
                System.out.println("turimi resursai: " + OS.processDesc.get(index).getResource().getList().get(0).part_of_resourse);
                System.out.println("turimi resursai: " + OS.processDesc.get(index).getResource().getList().get(0).priority);
                OS.processDesc.get(index).setInfo(OS.processDesc.get(index).getResource().getList().get(0).info);
                int res = OS.kernel.findResName("ISVEDIMO_IRENGINYS", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, 1);
                OS.rmMemory[1].cell = "2";
                break;
            }
            case 2:
            {
                //vykdoma komanda xchng
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                outputStreamInUse = true;
                outputStream.add(OS.processDesc.get(index).getInfo());
                outputStreamInUse = false;
                OS.rmMemory[1].cell = "3";
                break;
            }
            case 3:
            {               
                int id = OS.kernel.findResName("ISVEDIMO_IRENGINYS", resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[1].cell = "4";
                break;
            }
            case 4:
            {
                int id = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[1].cell = "5";
                break;
            }
            case 5:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                if(OS.processDesc.get(index).getInfo().equals("DARBO_PABAIGA"))
                {
                    OS.rmMemory[1].cell = "6";
                }
                else OS.rmMemory[1].cell = "7";
                break;
            }
            case 6:
            {
                int res = OS.kernel.findResName("DARBO_PABAIGA", resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "");
                OS.rmMemory[1].cell = "0";
                break;
            }
            case 7:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                if(OS.processDesc.get(index).getInfo().equals("IVESK_PROGRAMA"))
                {
                    OS.rmMemory[1].cell = "8";
                }
                else OS.rmMemory[1].cell = "13";
                break;
            }
            case 8:
            {
                int res = OS.kernel.findResName("IVESK_PROGRAMA", resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "");
                OS.rmMemory[1].cell = "9";
                break;
            }
            case 9:
            {
                int res = OS.kernel.findResName("IVESK_PROGRAMA_END", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, 1);
                OS.rmMemory[1].cell = "10";
                break;
            }
            case 10:
            {
                int res = OS.kernel.findResName("IVESK_PROGRAMA", resourseDesc);
                OS.kernel.deaktyvuotiR(res);
                OS.rmMemory[1].cell = "11";
                break;
            }
            case 11:
            {
                int id = OS.kernel.findResName("IVESK_PROGRAMA_END", resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[1].cell = "12";
                break;
            }
            case 12:
            {
                int id = OS.kernel.findProcName("INPUT_PROGRAM", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[1].cell = "0";
                break;
            }
            case 13:
            {
                int res = OS.kernel.findResName("OUTPUT_TO_USER_END", resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "");
                OS.rmMemory[1].cell = "14";
                break;
            }
            case 14:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[1].cell = "15";
                break;
            }
            case 15:
            {
                int res = OS.kernel.findResName("OUTPUT_TO_USER_END", resourseDesc);
                OS.kernel.deaktyvuotiR(res);
                OS.rmMemory[1].cell = "0";
                break;
            }
            
        }
    }
    public static void inputProgram(int line)
    {
        switch(line)
        {
            case 0:
            {
                String res = "IVESK_PROGRAMA";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[2].cell = "1";
                break;
            }
            case 1:
            {
                String res = "ISORINE_ATMINTIS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 10);
                OS.rmMemory[2].cell = "2";
                break;
            }
            case 2:
            {
                String res = "ISORINIS_ATMINTIES_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[2].cell = "3";
                break;
            }
            case 3:
            {
                String res = "IVEDIMO_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[2].cell = "4";
                break;
            }
            case 4:
            {
                if(inputStreamOk)
                {
                    //xchng
                    ArrayList<Integer> blocks = new ArrayList<>();
                    String res = "ISORINE_ATMINTIS";
                    int idr = OS.kernel.findResName(res, resourseDesc);
                    int idp = OS.kernel.getProcDesc().getProcessName();
                    int index = OS.kernel.findProc(idr, processDesc);
                    for(int i = 0; i < OS.processDesc.get(index).getResource().getSize(); i++)
                    {
                        if(OS.processDesc.get(index).getResource().getList().get(i).processId == idr)
                        {
                            blocks.add(OS.processDesc.get(index).getResource().getList().get(i).part_of_resourse);
                        }
                    }
                    uzduotisIsorinejeAtmintyje.add(new ArrayList<>(blocks));
                    int j = -1;
                    int lastI = 0;
                    for(int i = 0; i < inputStream.size(); i++)
                    {
                        if(i%10 == 0)
                        {
                            j++;
                        }
                        externalMemory[blocks.get(j) + i].cell = inputStream.get(i);
                        lastI = i ;
                        
                    }
                    OS.rmMemory[2].cell = "5";
                    inputStarted = false;
                    inputStreamOk = false;
                }
                else
                {
                    int id = OS.kernel.getProcDesc().getProcessName();
                    blockedProcessId = id;
                    OS.kernel.stopProc(id);
                    OS.rmMemory[2].cell = "4";
                    startInput = true;
                }
                
                break;
            }
            case 5:
            {
                String res = "IVEDIMO_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[2].cell = "6";
                break;
            }
            case 6:
            {
                String res = "ISORINIS_ATMINTIES_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[2].cell = "7";
                break;
            }
            case 7:
            {
                String res = "IVESK_PROGRAMA";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[2].cell = "8";
                break;
            }
            case 8:
            {
                String res = "INPUT_PROGRAM_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[2].cell = "9";
                break;
            }
            case 9:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[2].cell = "10";
                break;
            }
            case 10:
            {
                String res = "INPUT_PROGRAM_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[2].cell = "11";
                break;
            }
            case 11:
            {
                String res = "UZDUOTIS_ISORINEJE_ATMINTYJE";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[2].cell = "0";
                break;
            }
            
        }
        
        
    }
    
    
    public static void inputData(int line)
    {
        switch(line)
        {
            case 0:
            {
                String res = "INPUT_DATA";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[3].cell = "1";
                break;
            }
            case 1:
            {
                String res = "IVEDIMO_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[3].cell = "2";
                break;
            }
            case 2:
            {
                if(inputStreamOk)
                {
                    //xchng
                    int registerR = Integer.valueOf(inputDataStrings.get(0));
                    int wordCount = registerR / 100;
                    int adr = registerR % 100;
                    int oldPtr = OS.realMachine.getRegisterPTR();
                    OS.realMachine.setRegisterPTR(inputDataPtr.get(0));
                    for(int i = 0; i < wordCount; i++)
                    {
                        int adrr = OS.paging.getRMadress(adr + 1);
                        rmMemory[adrr].setCell(inputStream.get(i));                        
                    }
                    inputDataPtr.remove(0);
                    inputDataStrings.remove(0);
                    OS.realMachine.setRegisterPTR(oldPtr);
                    OS.rmMemory[3].cell = "3";
                    inputStarted = false;
                    inputStreamOk = false;
                }
                else
                {
                    int id = OS.kernel.getProcDesc().getProcessName();
                    blockedProcessId = id;
                    OS.kernel.stopProc(id);
                    OS.rmMemory[3].cell = "2";
                    startInput = true;
                }
                //OS.rmMemory[3].cell = "3";
                break;
            }
            case 3:
            {
                String res = "IVEDIMO_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[3].cell = "4";
                break;
            }
            case 4:
            {
                String res = "INPUT_DATA";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[3].cell = "5";
                break;
            }
            case 5:
            {
                String res = "INPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[3].cell = "6";
                break;
            }
            case 6:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[3].cell = "7";
                break;  
            }
            case 7:
            {
                String res = "INPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[3].cell = "0";
                break;
            }
        }
        
        
    }
    public static void outpuData(int line)
    {
        switch(line)
        {
            case 0:
            {
                String res = "OUTPUT_DATA";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[4].cell = "1";
                break;
            }
            case 1:
            {
                String res = "ISVEDIMO_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[4].cell = "2";
                break;
            }
            case 2:
            {
                //xchng
                int registerR = Integer.valueOf(outputDataStrings.get(0));
                int wordCount = registerR / 100;
                int adr = registerR % 100;
                int oldPtr = OS.realMachine.getRegisterPTR();
                OS.realMachine.setRegisterPTR(outputDataPtr.get(0));
                for(int i = 0; i < wordCount; i++)
                {
                    int adrr = OS.paging.getRMadress(adr + 1);
                    String s = rmMemory[adrr].getCell();
                    outputStream.add(s);                        
                }
                outputDataPtr.remove(0);
                outputDataStrings.remove(0);
                
                OS.rmMemory[4].cell = "3";
                break;
            }
            case 3:
            {
                String res = "ISVEDIMO_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[4].cell = "4";
                break;
            }
            case 4:
            {
                String res = "OUTPUT_DATA";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[4].cell = "5";
                break;
            }
            case 5:
            {
                String res = "OUTPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[4].cell = "6";
                break;
            }
            case 6:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[4].cell = "7";
                break;  
            }
            case 7:
            {
                String res = "OUTPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[4].cell = "0";
                break;
            }   
        }
    }
    
    public static void additionalMemory(int line)
    {
        switch(line)
        {
            case 0:
            {
                String res = "ASK_MEMORY";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[5].cell = "1";
                break;
            }            
            case 1:
            {
                int registerR = Integer.valueOf(askMemoryR.get(0));
                int registerPtr = askMemoryPtr.get(0);
                int ptrSize = registerPtr - registerPtr%100;
                if(ptrSize / 100 + 1 - registerR > 0) 
                {
                    askMemorySize = ptrSize / 100 + 1 - registerR;
                    OS.rmMemory[5].cell = "2";
                }else OS.rmMemory[5].cell = "6";
                
                break;
            }
            case 2:
            {
                int res = OS.kernel.findResName("OPERATYVIOJI_ATMINTIS", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, askMemorySize);
                OS.rmMemory[5].cell = "3";
                break;
            }
            case 3:
            {
                //atminties priskyrimas virtualiai masinai
                int idp = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(idp, processDesc);
                int indexVm = OS.kernel.findProc(askMemoryVmId.get(0), processDesc);
                ArrList vmMem = OS.processDesc.get(indexVm).getOperating_memory();
                int res = OS.kernel.findResName("OPERATYVIOJI_ATMINTIS", resourseDesc);
                for(int i = 0; i < OS.processDesc.get(index).getOperating_memory().getSize(); i++)
                {
                    vmMem.addOa(res, OS.processDesc.get(index).getOperating_memory().getList().get(i).part_of_resourse);
                }
                OS.processDesc.get(indexVm).setOperating_memory(vmMem);
                int ptr = askMemoryPtr.get(0);
                int newPtr = ptr + askMemorySize * 100;
                int ptrAdr = ptr % 100;
                int oldSize = ptr - ptrAdr;
                oldSize = oldSize / 100 + 1;
                for(int i = 0; i < askMemorySize; i++)
                {
                    OS.rmMemory[ptrAdr * 10 + oldSize + i].setCell(String.valueOf(OS.processDesc.get(index).getOperating_memory().getList().get(i).part_of_resourse));
                    OS.rmMemory[ptrAdr * 10 + oldSize + i].setState(true);
                }
                OS.processDesc.get(indexVm).getCpu2().setRegisterPTR(newPtr);
                askMemoryPtr.remove(0);
                askMemoryR.remove(0);
                askMemorySize = 0;
                askMemoryVmId.remove(0);
                OS.rmMemory[5].cell = "4";
                break;
            }
            case 4:
            {
                int res = OS.kernel.findResName("MEMORY_GIVEN", resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "MEMORY_GIVEN");
                OS.rmMemory[5].cell = "5";
                break;
            }
            case 5:
            {
                int id = OS.kernel.findResName("ASK_MEMORY", resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[5].cell = "7";
                break;
            }
            case 6:
            {
                int res = OS.kernel.findResName("MEMORY_GIVEN", resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "MEMORY_NOT_GIVEN");
                OS.rmMemory[5].cell = "5";
                break;
            }
            case 7:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[5].cell = "8";
                break;
            }
            case 8:
            {
                int res = OS.kernel.findResName("MEMORY_GIVEN", resourseDesc);
                OS.kernel.deaktyvuotiR(res);
                OS.rmMemory[5].cell = "0";
                break;
            }
        }
    }
   public static void mainProc(int line)
    {
        //String info = "";
        switch(line)
        {
            case 0:
            {
                String res = "UZDUOTIS_ISORINEJE_ATMINTYJE";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[6].cell = "1";
                break;
            }
            case 1:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                if(OS.processDesc.get(index).getInfo().equals("FIKTYVUS_RESURSAS")){
                    OS.rmMemory[6].cell = "2";
                    break;
                } else {
                    OS.rmMemory[6].cell = "3";
                    break;
                }
            }
            case 2:
            {
                // naikinti job governor sukurusi fiktyvu resursa
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                int procId = OS.processDesc.get(index).getResource().getList().get(0).processId;
                OS.kernel.abortProcess(procId);
                OS.processDesc.get(index).getResource().getList().remove(0);
                OS.rmMemory[6].cell = "0";
                break;
            }
            case 3:
            {
                // SUKURTI JOB GOVERNOR
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                int ic = governorIc;
                governorIc++;
                if(governorIc == 20)
                {
                    governorIc = 10;
                }
                ArrList memory = new ArrList();
                ArrList resource = new ArrList();
                resource = OS.processDesc.get(index).getResource();
                CPU cpu = new CPU(false, 0, ic);
                int priority = 3;
                OS.kernel.createProcess(memory, resource, priority, cpu, "JOB_GOVERNOR");
                //OS.processDesc.get(index).setResource(new ArrList());
                rmMemory[ic].cell = "0";
                //rmMemory[0].cell = "2";
                //break;
                // perduoti UZDUOTIS_ISORINEJE_ATMINTYJE
                OS.rmMemory[6].cell = "4";
                break;
            }
            case 4:
            {
                String res = "UZDUOTIS_PAIMTA";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[6].cell = "5";
                break;
            }
            case 5:
            {
                String res = "UZDUOTIS_ISORINEJE_ATMINTYJE";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[6].cell = "6";
                break;
            }
            case 6:
            {
                String res = "UZDUOTIS_PAIMTA";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[6].cell = "7";
                break;
            }
            case 7:
            {
                //aktyvuoti JOB GOVERNOR
                //int index = OS.kernel.findProc(governotId, processDesc);
                OS.kernel.acivateProc(governorId);
                break;
            }
        }
    }
    
    public static void loader(int line)
    {
        switch(line)
        {
            case 0:
            {
                String res = "PAKROVIMO_PAKETAS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[7].cell = "1";
                break;
            }
            case 1:
            {
                String res = "ISORINIS_ATMINTIES_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[7].cell = "2";
                break;
            }
            case 2:
            {
                //XCHNG
                for(int i = 0; i < loaderOpreatingBlocks.get(0).size(); i++)
                {
                    rmMemory[vmIc + i].setCell(String.valueOf(loaderOpreatingBlocks.get(0).get(i)));
                }
                int ptr = 9*100+vmIc;
                governorPtr = ptr;
                for(int i = 0; i  < loaderExternalBlocks.get(0).size(); i++)
                {
                    int externalBlock = loaderExternalBlocks.get(0).get(i);
                    int operatingBlock = loaderOpreatingBlocks.get(0).get(i);
                    for(int j = 0; j < 10; j++)
                    {
                        rmMemory[operatingBlock + i].setCell(externalMemory[externalBlock + i].cell);
                        externalMemory[externalBlock + i].freeCell();
                    }
                }
                loaderExternalBlocks.remove(0);
                loaderOpreatingBlocks.remove(0);
                OS.rmMemory[7].cell = "3";
                break;
            }
            case 3:
            {
                String res = "ISORINIS_ATMINTIES_IRENGINYS";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[7].cell = "4";
                break;
            }
            case 4:
            {
                int res = OS.kernel.findResName("LOADER_END", resourseDesc);
                OS.kernel.aktyvuotiR(res, 1, "");
                OS.rmMemory[7].cell = "5";
                break;
            }
            case 5:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[7].cell = "6";
                break;
            }
            case 6:
            {
                String res = "LOADER_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[7].cell = "0";
                break;
            }       
        }
    }
    
    
    public static void idle(int line)
    {
        switch(line)
        {
            case 0:
            {
                int res = OS.kernel.findResName("LAUKIMAS", OS.resourseDesc);
                OS.kernel.prasytiResurso(res, 1);
                OS.rmMemory[8].cell = "1";
                //OS.kernel.prasytiResurso(id, 1);
                break;
            }
            case 1:
            {
                String res = "LAUKIMAS";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                rmMemory[8].cell = "0";
                break;
            }
            default:
            {
                
            }
        }
    }
    
    public static void jobGovernor( int line, int ic ){
        switch(line){
            case 0:
            {
                String res = "OPERATYVIOJI_ATMINTIS";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 10);
                OS.rmMemory[ic].cell = "1";
                ArrayList<Integer> opMemory = new ArrayList();
                for( int i = 0 ; i < OS.processDesc.get(id).getOperating_memory().getSize(); i++ ){
                    opMemory.add(OS.processDesc.get(id).getOperating_memory().getList().get(i).part_of_resourse);
                }
                loaderOpreatingBlocks.add(new ArrayList(opMemory));
                opMemory.clear();
                break;
            }
            case 1:
            {
                int id = OS.kernel.findResName("PAKROVIMO_PAKETAS", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[ic].cell = "2";
                ArrayList<Integer> exMemory = new ArrayList();
                for( int i = 0 ; i < OS.processDesc.get(id).getResource().getSize(); i++ ){
                    exMemory.add(OS.processDesc.get(id).getResource().getList().get(i).part_of_resourse);
                }
                loaderExternalBlocks.add(new ArrayList(exMemory));
                exMemory.clear();
                break;
            }
            case 2:
            {
                int id = OS.kernel.findResName("LOADER_END", resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "3";
                break;
            }
            case 3:
            {
                int id = OS.kernel.findResName("PAKROVIMO_PAKETAS", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "4";
                break;
            }
            case 4:
            {
                int id = OS.kernel.findProcName("LOADER", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "5";
                break;
            }
            case 5:
            {
                String res = "LOADER_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "6";
                break;
            }
            case 6:
            {
                int id = OS.kernel.findResName("UZDUOTIS_PAIMTA", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[ic].cell = "7";
                break;
            }
            case 7:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.stopProc(id);
                OS.rmMemory[ic].cell = "8";
                break;
            }
            case 8:
            {
                int id = OS.kernel.findResName("UZDUOTIS_PAIMTA", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "9";
                break;
            }
            case 9:
            {
                vmIc += 10;
                if(vmIc == 120)
                {
                    vmIc = 20;
                }
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                ArrList memory = new ArrList();
                ArrList resource = new ArrList();
                memory = OS.processDesc.get(index).getOperating_memory();
                CPU cpu = new CPU(true, Integer.valueOf(OS.rmMemory[vmIc-1].cell), 0);
                int priority = 4;
                OS.kernel.createProcess(memory, resource, priority, cpu, "VIRTUAL_MACHINE");
                OS.rmMemory[ic].cell = "10";
                break;
            }
            case 10:
            {
                String res = "VM_INTERRUPTED";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "11";
            }
            case 11:
            {
                int id = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(id, processDesc);
                OS.processDesc.get(index).setInfo(OS.processDesc.get(index).getResource().getList().get(0).info);
                
                if(OS.processDesc.get(index).getInfo().equals("IVEDIMAS")){
                    OS.rmMemory[ic].cell = "12";
                } else if(OS.processDesc.get(index).getInfo().equals("ISVEDIMAS")){
                    OS.rmMemory[ic].cell = "19";
                } else if(OS.processDesc.get(index).getInfo().equals("PAPILDOMA_ATMINTIS")){
                    OS.rmMemory[ic].cell = "26";
                } else if(OS.processDesc.get(index).getInfo().equals("BAIGIAMAS_DARBAS")){
                    OS.rmMemory[ic].cell = "32";
                } else {
                    OS.rmMemory[ic].cell = "39";
                }
                                
            }
            case 12:
            {
                String res = "VM_INTERRUPTED";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "13";
                break;
            }
            case 13:
            {
                int id = OS.kernel.findResName("INPUT_DATA", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[ic].cell = "14";
                break;  
            }
            case 14:
            {
                String res = "INPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "15";
            }
            case 15:
            {
                int id = OS.kernel.findResName("INPUT_DATA", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "16";
                break;
            }
            case 16:
            {
                String res = "INPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "17";
                break;
            }
            case 17:
            {
                int id = OS.kernel.findProcName("INPUT_PROGRAM", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "18";
                break;
            }
            case 18:
            {
                int id = OS.kernel.findProcName("VIRTUAL_MACHINE", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "10";
                break;
            }
            case 19:
            {
                String res = "VM_INTERRUPTED";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "20";
                break;
            }
            case 20:
            {
                int id = OS.kernel.findResName("OUTPUT_DATA", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[ic].cell = "21";
                break;  
            }
            case 21:
            {
                String res = "OUTPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "22";
                break;
            }
            case 22:
            {
                int id = OS.kernel.findResName("OUTPUT_DATA", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "23";
                break;
            }
            case 23:
            {
                String res = "OUTPUT_DATA_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "24";
                break;
            }
            case 24:
            {
                int id = OS.kernel.findProcName("OUTPUT_DATA", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "25";
                break;
            }
            case 25:
            {
                int id = OS.kernel.findProcName("VIRTUAL_MACHINE", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "10";
                break;
            }
            case 26:
            {
                String res = "VM_INTERRUPTED";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "27";
                break;
            }
            case 27:
            {
                int id = OS.kernel.findResName("ASK_MEMORY", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "");
                OS.rmMemory[ic].cell = "28";
                break;  
            }
            case 28:
            {
                String res = "MEMORY_GIVEN";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "29";
                break;
            }
            case 29:
            {
                int id = OS.kernel.findResName("ASK_MEMORY", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "30";
                break;
            }
            case 30:
            {
                int id = OS.kernel.findProcName("ADDITIONAL_MEMORY", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "31";
                break;
            }
            case 31:
            {
                int id = OS.kernel.findProcName("VIRTUAL_MACHINE", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "11";
                break;
            }
            case 32:
            {
                String res = "VM_INTERRUPTED";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "33";
                break;
            }
            case 33:
            {
                int id = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "SEKMINGA DARBO PABAIGA");
                OS.rmMemory[ic].cell = "34";
                break;  
            }
            case 34:
            {
                String res = "OUTPUT_TO_USER_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "35";
                break;
            }
            case 35:
            {
                int id = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "36";
                break;
            }
            case 36:
            {
                String res = "OUTPUT_TO_USER_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "37";
                break;
            }
            case 37:
            {
                int id = OS.kernel.findResName("OUTPUT_TO_USER", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "38";
                break;
            }
            case 38:
            {
                int id = OS.kernel.findProcName("OUTPUT_TO_USER", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "10";
                break;
            }
            case 39:
            {
                String res = "VM_INTERRUPTED";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "40";
                break;
            }
            case 40:
            {
                int id = OS.kernel.findResName("PRANESIMAS_VARTOTOJUI", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "NESEKMINGA DARBO PABAIGA");
                OS.rmMemory[ic].cell = "41";
                break;  
            }
            case 41:
            {
                String res = "OUTPUT_TO_USER_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                OS.kernel.prasytiResurso(id, 1);
                OS.rmMemory[ic].cell = "42";
                break;
            }
            case 42:
            {
                int id = OS.kernel.findResName("OUTPUT_TO_USER", resourseDesc);
                OS.kernel.deaktyvuotiR(id);
                OS.rmMemory[ic].cell = "43";
                break;
            }
            case 43:
            {
                String res = "OUTPUT_TO_USER_END";
                int id = OS.kernel.findResName(res, resourseDesc);
                int proc = OS.kernel.getProcDesc().getProcessName();
                OS.kernel.atlaisvintiResursa(id, 1, proc, "");
                OS.rmMemory[ic].cell = "44";
                break;
            }
            case 44:
            {
                int id = OS.kernel.findProcName("OUTPUT_TO_USER", processDesc);
                OS.kernel.acivateProc(id);
                OS.rmMemory[ic].cell = "45";
                break;
            }
            case 45:
            {
                int id = OS.kernel.findResName("UZDUOTIS_ISORINEJE_ATMINTYJE", resourseDesc);
                OS.kernel.aktyvuotiR(id, 1, "FIKTYVUS");
                OS.rmMemory[ic].cell = "46";
                break;  
            }
            case 46:
            {
                int id = OS.kernel.findProcName("VIRTUAL_MACHINE", processDesc);
                OS.kernel.abortProcess( id );
                OS.rmMemory[ic].cell = "0";
                break;  
            }
        }
    }
    
    public static void main(String[] args) {
        memoryInit();
        rmMemory[0].cell = "0";
        initializeSystem();
        for(int i = 0; i < OS.resourseDesc.size(); i++)
        {
            System.out.println("resurso vardas: " + OS.resourseDesc.get(i).getName());
            System.out.println("prieinamu resursu : " + OS.resourseDesc.get(i).getPrieinamu_resursu_sarasas().getSize());
        }
        OS.kernel.planuotojas();
        OS.plan = false;
        MachineThread machineThread = new MachineThread();
        machineThread.start();
        while(!osEnd)
        {
            while(outputStreamInUse);
                if(!outputStream.isEmpty()){
                    for( String output : outputStream ){
                        OS.gui.getOutputArea().append(output);
                    }
                    outputStream = new ArrayList<>();
            }
            try {    
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
            }
            OS.gui.refreshGUI();
            
        }
        //OS.realMachine.setRegisterIC(0);
        /*for(int i = 0; i < 40; i++)
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
        }*/
        //int zingsnis = -1;
        /*InputThread inputThread = new InputThread();
        inputThread.start();
        
        try
        {
            inputThread.join();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0; i<input.size(); i++)
        {
            System.out.println("atrodo : " + input.get(i));
        }
        startInput = true;
        while(true)
        {
            while(outputStreamInUse);
                if(!outputStream.isEmpty()){
                    for( String output : outputStream ){
                        OS.gui.getOutputArea().append(output);
                    }
                    outputStream = new ArrayList<>();
            }
            if(startInput)
            {
                InputThread inputThread = new InputThread();
                inputThread.start();
            }
            if(inputStreamOk)
            {
                OS.kernel.acivateProc(blockedProcessId);
            }
            zingsnis++;
            try
            {
                System.out.println("zingsnis: " + zingsnis);
                cpu();
                gui.refreshGUI();
                
                //rmMemory[0].setState(false);
                //rmMemory[80].setState(false);
                int idd = OS.kernel.getProcDesc().getProcessName();
                int index = OS.kernel.findProc(idd, processDesc);
                System.out.println("einamas procesas: " + OS.processDesc.get(index).getName());
                for(int i = 0; i < OS.kernel.getPps().getSize(); i++)
                {
                    int id = OS.kernel.getPps().getList().get(i).processId;
                    id = OS.kernel.findProc(id, processDesc);
                    String name = OS.processDesc.get(id).getName();
                    System.out.println(name + " prioritetas: " + OS.processDesc.get(id).getPriority());
                }
                for(int i = 0; i < OS.processDesc.size(); i++)
                {
                    System.out.println("proceso vardas: " + OS.processDesc.get(i).getName() + "proceso busena: " + OS.processDesc.get(i).getState());
                }
                Thread.sleep(500);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(OS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
    }
    
}
