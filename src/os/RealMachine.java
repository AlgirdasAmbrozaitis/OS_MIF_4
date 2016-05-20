package os;

/**
 *
 * @author eimantas
 */
public class RealMachine {
    
    private String registerR;
    
    
    private int registerPTR,
                registerIC,
                registerSP,
                registerINT,
                registerCT,
                registerPI,
                registerSI,
                registerTI;
    
    private int SB,
                DB,
                ST,
                DT,
                SZ;
    
    private boolean registerC,
                    registerMOD;
    
    String registers[] = {"CT", "IC", "SP", "C", "R", "MOD", "PTR", "TI","SI","PI",
                          "INT"}; 

    public RealMachine() {
        
        this.registerR = "0";
        this.registerPTR = 0;
        this.registerIC = 0;
        this.registerSP = 999;
        this.registerINT = 0;
        this.registerCT = 0;
        this.registerPI = 0;
        this.registerSI = 0;
        this.registerTI = 10;
        this.SB = 0;
        this.DB = 0;
        this.ST = 0;
        this.DT = 0;
        this.SZ = 0;
        this.registerC = false;
        this.registerMOD = false;
        
    }
    
    public void changeR()
    {
        this.registerR = OS.rmMemory[this.registerIC].getCell();
        this.registerIC = this.registerIC + 1;
    }
    public void loadR(int memCell)
    {
        this.registerR = OS.rmMemory[memCell].getCell();
    }
    public void saveR(int memCell)
    {
        OS.rmMemory[memCell].setCell(this.registerR);
    }
     
    public void loadRR(String reg)
    {
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: this.registerR = String.valueOf(this.registerCT);
                            break;
                    case 1: this.registerR = String.valueOf(this.registerIC);
                            break;
                    case 2: this.registerR = String.valueOf(this.registerSP);
                            break;
                    case 3: this.registerR = String.valueOf(this.registerC);
                            break;
                    case 4: this.registerR = String.valueOf(this.registerR);
                            break;
                    case 5: this.registerR = String.valueOf(this.registerMOD);
                            break;
                    case 6: this.registerR = String.valueOf(this.registerPTR);
                            break;
                    case 7: this.registerR = String.valueOf(this.registerTI);
                            break;
                    case 8: this.registerR = String.valueOf(this.registerSI);
                            break;
                    case 9: this.registerR = String.valueOf(this.registerPI);
                            break;
                    case 10: this.registerR = String.valueOf(this.registerINT);
                            break;
                }
            }
        }
    }
    
    public void addR(int memCell)
    {
        this.registerR = String.valueOf(Integer.valueOf(this.registerR) + Integer.valueOf(OS.rmMemory[memCell].getCell()));
    }
    
    public void subR(int memCell)
    {
        this.registerR = String.valueOf(Integer.valueOf(this.registerR) - Integer.valueOf(OS.rmMemory[memCell].getCell()));
    }
    
    public void mulR(int memCell)
    {
                this.registerR = String.valueOf(Integer.valueOf(this.registerR) * Integer.valueOf(OS.rmMemory[memCell].getCell()));
    }
    
    public void divR(int memCell)
    {
                this.registerR = String.valueOf(Integer.valueOf(this.registerR) / Integer.valueOf(OS.rmMemory[memCell].getCell()));
    }
    
    public void cmpR(int memCell)
    {
        this.registerC = (Integer.valueOf(this.registerR) == Integer.valueOf(OS.rmMemory[memCell].getCell()));
    }
    
    public void cmpRL(int memCell)
    {
        this.registerC = (Integer.valueOf(this.registerR) < Integer.valueOf(OS.rmMemory[memCell].getCell()));
    }
    
    public void cmpRG(int memCell)
    {
        this.registerC = (Integer.valueOf(this.registerR) > Integer.valueOf(OS.rmMemory[memCell].getCell()));
    } 
    
    public void cmpZ(String reg)
    {
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: this.registerC = (this.registerCT == 0 );
                            return;
                    case 1: this.registerC = (this.registerIC == 0 );
                            return;
                    case 2: this.registerC = (this.registerSP == 0 );
                            return;
                    case 3: this.registerC = (this.registerC == false );
                            return;
                    case 4: this.registerC = (Integer.valueOf(this.registerR) == 0 );
                            return;
                    case 5: this.registerC = (this.registerMOD == false );
                            return;
                    case 6: this.registerC = (this.registerPTR == 0 );
                            return;
                    case 7: this.registerC = (this.registerTI == 0 );
                            return;
                    case 8: this.registerC = (this.registerSI == 0 );
                            return;
                    case 9: this.registerC = (this.registerPI == 0 );
                            return;
                    case 10: this.registerC = (this.registerINT == 0 );
                            return;
                }
            }
        }
        //this.registerC = (Integer.valueOf(this.registerR) == 0 );
    }
    
    public void jumpIf(int memCell)
    {
        if(this.registerC)
        {
            this.registerIC = memCell;
        }
    }
    
    public void jump(int memCell)
    {
        this.registerIC = memCell;
    }
    
    public void call(int memCell)
    {
        push("MOD");
        push("PTR");
        push("C");
        push("R");
        push("IC");
        push("SP");
        push("CT");
        this.registerIC = Integer.valueOf(OS.rmMemory[memCell].getCell());
        jump(memCell);
    }
    
    public void push(String reg)
    {
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: OS.rmMemory[this.registerSP].setCell(String.valueOf(this.registerCT));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 1: OS.rmMemory[this.registerSP].setCell(String.valueOf(this.registerIC));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 2: OS.rmMemory[this.registerSP].setCell(String.valueOf(this.registerSP));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 3: OS.rmMemory[this.registerSP].setCell(String.valueOf(this.registerC));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 4: OS.rmMemory[this.registerSP].setCell(this.registerR);
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 5: OS.rmMemory[this.registerSP].setCell(Boolean.toString(this.registerMOD));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 6: OS.rmMemory[this.registerSP].setCell(Integer.toString(this.registerPTR));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 7: OS.rmMemory[this.registerSP].setCell(Integer.toString(this.registerTI));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 8: OS.rmMemory[this.registerSP].setCell(Integer.toString(this.registerSI));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 9: OS.rmMemory[this.registerSP].setCell(Integer.toString(this.registerPI));
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 10: OS.rmMemory[this.registerSP].setCell(Integer.toString(this.registerINT));
                            this.registerSP = this.registerSP - 1;
                            return;
                }
            }
        }
    }
    // {"CT", "IC", "SP", "C", "R", "MOD", "PTR", "TI","SI","PI", "INT"}; 
    public void pop(String reg)
    {
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: this.registerSP = this.registerSP + 1;
                            this.registerCT = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 1: this.registerSP = this.registerSP + 1;
                            this.registerIC = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 2: this.registerSP = this.registerSP + 1;
                            this.registerSP = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 3: this.registerSP = this.registerSP + 1;
                            this.registerC = Boolean.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 4: this.registerSP = this.registerSP + 1;
                            this.registerR = OS.rmMemory[this.registerSP].getCell();
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 5: this.registerSP = this.registerSP + 1;
                            this.registerMOD = Boolean.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 6: this.registerSP = this.registerSP + 1;
                            this.registerPTR = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 7: this.registerSP = this.registerSP + 1;
                            this.registerTI = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 8: this.registerSP = this.registerSP + 1;
                            this.registerSI = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 9: this.registerSP = this.registerSP + 1;
                            this.registerPI = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                    case 10:this.registerSP = this.registerSP + 1;
                            this.registerINT = Integer.valueOf(OS.rmMemory[this.registerSP].getCell());
                            OS.rmMemory[this.registerSP].freeCell();
                            return;
                }
            }
        }
    }
    public void returnC ()
    {          
        pop("CT");
        pop("SP");
        pop("IC");
        pop("R");
        pop("C");
        pop("PTR");
        pop("MOD");
    }
    
    public void systemCall(int number)
    {
        OS.realMachine.setRegisterSI(number);
    }
    
    public void loop(int memCell)
    {
        if(this.registerCT != 0)
        {
            this.registerCT = this.registerCT - 1;
            jump(memCell);
        }
    }
    
    public void doCommand(int number, String value)
    {        
        if(value.matches("\\d+"))
        {
            if(Integer.valueOf(value) < 0 && Integer.valueOf(value) >= OS.RM_MEMORY_SIZE)
            {
                OS.realMachine.setRegisterPI(1);
                return;
            }
        }
        if(OS.realMachine.isRegisterMOD())
        {
            switch(number)
            { 
                case 0 : changeR();
                         return;
                case 1 : loadR(Integer.valueOf(value));
                         return;
                case 2 : saveR(Integer.valueOf(value));
                         return;
                case 3 : loadRR(value);
                         return;            
                case 4 : addR(Integer.valueOf(value));
                         return;
                case 5 : subR(Integer.valueOf(value));
                         return;
                case 6 : mulR(Integer.valueOf(value));
                         return;
                case 7 : divR(Integer.valueOf(value));
                         return;
                case 8 : cmpR(Integer.valueOf(value));
                         return;
                case 9 : cmpRL(Integer.valueOf(value));
                         return;
                case 10: cmpRG(Integer.valueOf(value));
                         return;
                case 11: cmpZ(value);
                         return;
                case 12: jumpIf(Integer.valueOf(value));
                         return;
                case 13: jump(Integer.valueOf(value));
                         return;
                case 14: call(Integer.valueOf(value));
                         return;
                case 15: push(value);
                         return;
                case 16: pop(value);
                         return;
                case 17: returnC();
                         return;
                case 18: systemCall(Integer.valueOf(value));
                         return;
                case 19: loop(Integer.valueOf(value));
                         return;
                default: OS.realMachine.setRegisterPI(2);
            }
        }
        else
        {
            switch(number)
            { 
                case 0 : changeR();
                         break;
                case 1 : loadR(Integer.valueOf(value));
                         break;
                case 2 : saveR(Integer.valueOf(value));
                         break;
                case 3 : loadRR(value);
                         break;
                case 4 : addR(Integer.valueOf(value));
                         break;
                case 5 : subR(Integer.valueOf(value));
                         break;
                case 6 : mulR(Integer.valueOf(value));
                         break;
                case 7 : divR(Integer.valueOf(value));
                         break;
                case 8 : cmpR(Integer.valueOf(value));
                         break;
                case 9 : cmpRL(Integer.valueOf(value));
                         break;
                case 10: cmpRG(Integer.valueOf(value));
                         break;
                case 11: cmpZ(value);
                         break;
                case 12: jumpIf(Integer.valueOf(value));
                         break;
                case 13: jump(Integer.valueOf(value));
                         break;
                case 14: call(Integer.valueOf(value));
                         break;
                case 15: push(value);
                         break;
                case 16: pop(value);
                         break;
                case 17: returnC();
                         break;
                case 18: systemCall(Integer.valueOf(value));
                         break;
                case 19: loop(Integer.valueOf(value));
                         break;
                case 20: chnge();
                         break;
                case 21: sepi( Integer.parseInt(value) );
                         break;
                case 22: seti( Integer.parseInt(value) );
                         break;
                case 23: ptr( Integer.parseInt(value) );
                         break;
                case 24: sp( Integer.parseInt(value) );
                         break;
                case 25: in( Integer.parseInt(value) );
                         break;
                case 26: start();
                         break;
                case 27: calli();
                         break;
                case 28: iretn();
                         break;
                case 29: sb( Integer.parseInt(value) );
                         break;
                case 30: db( Integer.parseInt(value) );
                         break;
                case 31: st( Integer.parseInt(value) );
                         break;
                case 32: dt( Integer.parseInt(value) );
                         break;
                case 33: sz( Integer.parseInt(value) );
                         break;
                case 34: xchgn();   
                default: // throw exception
            }
        }
    }
    
    public void chnge(){
        this.registerMOD = !this.registerMOD;
    } 
    
    public void sepi( int x ){
        if( x >= 0 && x <= 999 ){
            this.registerPI = x;
        } 
    }
    
    public void seti( int x ){ 
        if( x >= 0 && x <= 999 ){
            this.registerTI = x;
        } else; // throw exception   
    }
    
    public void ptr( int block_number ){
        if( block_number >= 0 && block_number <= 99 ){
            this.registerPTR = block_number;
        } else; // throw exception
    }
    
    public void sp( int cell_adress ){
        if( cell_adress >= 0 && cell_adress <= 999 ){
            this.registerSP = cell_adress;
        } else; // throw exception
        
    }
    
    public void in( int cell_adress ){
        if( cell_adress >= 0 && cell_adress <= 999 ){
            this.registerINT = cell_adress;
        } else; // throw exception 
    }
    
    public void start()
    {
       push("MOD");
       push("PTR");
       push("C");
       push("R");
       push("IC");
       push("CT");
       push("INT");
       push("SP");
       OS.virtualMachine.setRegisterIC(0);
       OS.virtualMachine.setRegisterSP(99);
       OS.paging.findPTR();
       OS.realMachine.setRegisterMOD(true);
    }
    
    public void calli()
    {
       
       push("MOD");
       push("PTR");
       push("C");
       push("R");
       push("IC");
       push("SP");
       push("CT");
       this.registerIC = this.registerINT;    
    }

    public void iretn(){
       pop("CT");
       pop("SP");
       pop("IC");
       pop("R");
       pop("C");
       pop("PTR");
       pop("MOD");
    }

    public void sb( int object_number ){
        if( object_number >= 0 && object_number <= 999 ){
        this.SB = object_number;
        } else ; // throw exception
    }

    public void db( int object_number ){
        if( object_number >= 0 && object_number <= 999 ){
        this.DB = object_number;
        } else ; // throw exception
    }
    
    public void st( int object_number ){
        if( object_number >= 0 && object_number <= 999 ){
        this.ST = object_number;
        } else ; // throw exception
    }
    
    public void dt( int object_number ){
        if( object_number >= 0 && object_number <= 999 ){
        this.DT = object_number;
        } else ; // throw exception
    }
    
    public void sz( int object_number ){
        if( object_number >= 0 && object_number <= 999 ){
        this.SZ = object_number;
        } else ; // throw exception
    }
  
    public void xchgn()
    {
        OS.cd.DataExchange();
    }
    
     
    public String getRegisterR() {
        return registerR;
    }

    public int getRegisterPTR() {
        return registerPTR;
    }

    public int getRegisterIC() {
        return registerIC;
    }

    public int getRegisterSP() {
        return registerSP;
    }

    public int getRegisterINT() {
        return registerINT;
    }

    public int getRegisterCT() {
        return registerCT;
    }

    public int getRegisterPI() {
        return registerPI;
    }

    public int getRegisterSI() {
        return registerSI;
    }

    public int getRegisterTI() {
        return registerTI;
    }

    public int getSB() {
        return SB;
    }

    public int getDB() {
        return DB;
    }

    public int getST() {
        return ST;
    }

    public int getDT() {
        return DT;
    }

    public int getSZ() {
        return SZ;
    }

    public boolean isRegisterC() {
        return registerC;
    }

    public boolean isRegisterMOD() {
        return registerMOD;
    }

    public void setRegisterR(String registerR) {
        this.registerR = registerR;
    }

    public void setRegisterPTR(int registerPTR) {
        this.registerPTR = registerPTR;
    }

    public void setRegisterIC(int registerIC) {
        this.registerIC = registerIC;
    }

    public void setRegisterSP(int registerSP) {
        this.registerSP = registerSP;
    }

    public void setRegisterINT(int registerINT) {
        this.registerINT = registerINT;
    }

    public void setRegisterCT(int registerCT) {
        this.registerCT = registerCT;
    }

    public void setRegisterPI(int registerPI) {
        this.registerPI = registerPI;
    }

    public void setRegisterSI(int registerSI) {
        this.registerSI = registerSI;
    }

    public void setRegisterTI(int registerTI) {
        this.registerTI = registerTI;
    }

    public void setSB(int SB) {
        this.SB = SB;
    }

    public void setDB(int DB) {
        this.DB = DB;
    }

    public void setST(int ST) {
        this.ST = ST;
    }

    public void setDT(int DT) {
        this.DT = DT;
    }

    public void setSZ(int SZ) {
        this.SZ = SZ;
    }

    public void setRegisterC(boolean registerC) {
        this.registerC = registerC;
    }

    public void setRegisterMOD(boolean registerMOD) {
        this.registerMOD = registerMOD;
    }
    
    @Override
    public String toString() {
        return "RealMachine{" + "registerR=" + registerR + ", registerPTR=" + registerPTR + ", registerIC=" + registerIC + ", registerSP=" + registerSP + ", registerINT=" + registerINT + ", registerCT=" + registerCT + ", registerPI=" + registerPI + ", registerSI=" + registerSI + ", registerTI=" + registerTI + ", SB=" + SB + ", DB=" + DB + ", ST=" + ST + ", DT=" + DT + ", SZ=" + SZ + ", registerC=" + registerC + ", registerMOD=" + registerMOD + '}';
    }
    
    //virtual machine commands
    public void vchangeR()
    {
        int adr = OS.paging.getRMadress(this.registerIC);
        this.registerR = OS.rmMemory[adr].getCell();
        this.registerIC = this.registerIC + 1;
    }
    public void vloadR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerR = OS.rmMemory[adr].getCell();
    }
    public void vsaveR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        if(OS.rmMemory[adr].isState())
        {
            OS.realMachine.setRegisterPI(1);
            return;
        }
        else
        {
            OS.rmMemory[adr].setCell(this.registerR);
            //memory[memCell].setState(true);
            //changeRMmem(memCell, this.registerR);
        }
    }
    public void vloadRR(String reg)
    {
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: this.registerR = String.valueOf(this.registerCT);
                            return;
                    case 1: this.registerR = String.valueOf(this.registerIC);
                            return;
                    case 2: this.registerR = String.valueOf(this.registerSP);
                            return;
                    case 3: this.registerR = String.valueOf(this.registerC);
                            return;
                    case 4: this.registerR = String.valueOf(this.registerR);
                            return;
                }
            }
        }
    }
    public void vaddR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerR = String.valueOf(Integer.valueOf(this.registerR) + Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vsubR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerR = String.valueOf(Integer.valueOf(this.registerR) - Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vmulR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerR = String.valueOf(Integer.valueOf(this.registerR) * Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vdivR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerR = String.valueOf(Integer.valueOf(this.registerR) / Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vcmpR(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerC = (Integer.valueOf(this.registerR) == Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vcmpRL(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerC = (Integer.valueOf(this.registerR) < Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vcmpRG(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerC = (Integer.valueOf(this.registerR) > Integer.valueOf(OS.rmMemory[adr].getCell()));
    }
    public void vcmpZ(String reg)
    {
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: this.registerC = (this.registerCT == 0 );
                            return;
                    case 1: this.registerC = (this.registerIC == 0 );
                            return;
                    case 2: this.registerC = (this.registerSP == 0 );
                            return;
                    case 3: this.registerC = (this.registerC == false );
                            return;
                    case 4: this.registerC = (Integer.valueOf(this.registerR) == 0 );
                            return;
                }
            }
        }
    }
    public void vjumpIf(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        if(this.registerC)
        {
            this.registerIC = adr;
        }
    }
    public void vjump(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        this.registerIC = adr;
    }
    public void vcall(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        push("C");
        push("R");
        push("IC");
        push("SP");
        push("CT");
        jump(adr);
    }
    public void vpush(String reg)
    {
        int adr = OS.paging.getRMadress(this.registerSP);
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: //changeRMmem(this.registerSP,String.valueOf(this.registerCT));
                            OS.rmMemory[adr].setCell(String.valueOf(this.registerCT));
                            //memory[this.registerSP].setState(true);
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 1: //changeRMmem(this.registerSP,String.valueOf(this.registerIC));
                            OS.rmMemory[adr].setCell(String.valueOf(this.registerIC));
                            //memory[this.registerSP].setState(true);
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 2: //changeRMmem(this.registerSP,String.valueOf(this.registerSP));
                            OS.rmMemory[adr].setCell(String.valueOf(this.registerSP));
                            //memory[this.registerSP].setState(true);
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 3: //changeRMmem(this.registerSP,String.valueOf(this.registerC));
                            OS.rmMemory[adr].setCell(String.valueOf(this.registerC));
                            //memory[this.registerSP].setState(true);
                            this.registerSP = this.registerSP - 1;
                            return;
                    case 4: //changeRMmem(this.registerSP,String.valueOf(this.registerR));
                            OS.rmMemory[adr].setCell(this.registerR);
                            //memory[this.registerSP].setState(true);
                            this.registerSP = this.registerSP - 1;
                            return;
                }
            }
        }
    }
 public void vpop(String reg)
    {
        int adr = OS.paging.getRMadress(this.registerSP);
        for(char i = 0 ; i < registers.length; i++ )
        {
            if ( reg.contains(registers[i]) )
            {
                switch(i)
                {
                    case 0: this.registerSP = this.registerSP + 1;
                            this.registerCT = Integer.valueOf(OS.rmMemory[adr].getCell()); 
                            OS.rmMemory[adr].freeCell();
                            return;
                    case 1: this.registerSP = this.registerSP + 1;
                            this.registerIC = Integer.valueOf(OS.rmMemory[adr].getCell());
                            OS.rmMemory[adr].freeCell();
                            return;
                    case 2: this.registerSP = this.registerSP + 1;
                            this.registerSP = Integer.valueOf(OS.rmMemory[adr].getCell());
                            OS.rmMemory[adr].freeCell();
                            return;
                    case 3: this.registerSP = this.registerSP + 1;
                            this.registerC = Boolean.valueOf(OS.rmMemory[adr].getCell());
                            OS.rmMemory[adr].freeCell();
                            return;
                    case 4: this.registerSP = this.registerSP + 1;
                            this.registerR = OS.rmMemory[adr].getCell();
                            OS.rmMemory[adr].freeCell();
                            return;
                }
            }
        }
    }
    public void vreturnC ()
    {          
        pop("CT");
        pop("SP");
        pop("IC");
        pop("R");
        pop("C");
    }
    public void vsystemCall(int number)
    {
        OS.realMachine.setRegisterSI(number);
    }
    public void vloop(int memCell)
    {
        int adr = OS.paging.getRMadress(memCell);
        if(this.registerCT != 0)
        {
            this.registerCT = this.registerCT - 1;
            jump(adr);
        }
    }
}
