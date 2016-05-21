/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import static java.lang.System.out;

/**
 *
 * @author Algirdas
 */
public class Paging {
    private int blockNumber;
    public static int blocks = 0;
    public void findFreeBlock()
    {
        int count = 0;
        this.blockNumber = -1;
       // int i = 0;
        for( int i = 0 + Paging.blocks; i < (OS.RM_MEMORY_SIZE / 10); i++)
        {
            //out.println(i);
            for(int j = 0; j < 10; j++)
            {
                if (OS.rmMemory[i*10 + j].isState())
                {
                    count = 0;
                    break;
                }
                count = count + 1;
            }
            if (count == 10)
            {
                blockNumber = i;
                Paging.blocks = i + 1;
                return;
            }
        }
    }
    public void findPTR()
    {
        int ptrBlock;
        findFreeBlock();
        if(this.blockNumber == -1)
        {
            OS.realMachine.setRegisterPI(3);
            return;
        }
        else
        {
            ptrBlock = this.blockNumber;
        }
        int blocksToFind = Integer.valueOf(OS.realMachine.getRegisterR());
        int blocksFinded[] = new int[blocksToFind];
        int blocksFound = 0;
        for (int i = 0; i < blocksToFind; i++)
        {
            findFreeBlock();
            if(this.blockNumber == -1)
            {
                OS.realMachine.setRegisterPI(3);
                return;
            }
            else
            {
                blocksFinded[i] = this.blockNumber;
                blocksFound = blocksFound + 1;
            }
        }
        if(blocksFound == blocksToFind)
        {
            for(int i = 0; i < blocksToFind; i++)
            {
                OS.rmMemory[ptrBlock * 10 + i].setCell(String.valueOf(blocksFinded[i]));
                OS.rmMemory[ptrBlock * 10 + i].setState(true);
            }
        }
        else
        {
            OS.realMachine.setRegisterPI(3);
            return;
        }
        OS.realMachine.setRegisterPTR(((blocksToFind - 1) * 100) + ptrBlock);
    }
    public int getRMadress(int virtAdress)
    {
        //int blockAddr = 
        return ((10 * Integer.valueOf(OS.rmMemory[((10*(OS.realMachine.getRegisterPTR() % 100)) + virtAdress / 10)].getCell())) + virtAdress % 10);
    }
    /*public void addMore()
    {
        if((OS.realMachine.getRegisterPTR()/100) == 9)
        {
            OS.realMachine.setRegisterPI(3);
            return;
        }
        else if (((OS.realMachine.getRegisterPTR()/100) + 1 + Integer.valueOf(OS.virtualMachine.getRegisterR())) >= 10)
        {
            OS.realMachine.setRegisterPI(3);
            return;
        }
        else
        {
            int blocksToFind = Integer.valueOf(OS.virtualMachine.getRegisterR());
            int blocksFound = 0;
            int blocksFinded[] = new int[blocksToFind];
            for(int i = 0; i < blocksToFind; i++)
            {               
                findFreeBlock();
                if(this.blockNumber == -1)
                {
                    OS.realMachine.setRegisterPI(3);
                    return;
                }
                else
                {
                    blocksFinded[i] = this.blockNumber;
                    blocksFound = blocksFound + 1;
                }
            }
            if (blocksFound == blocksToFind)
            {
                int ptr = (OS.realMachine.getRegisterPTR() % 100);
                for(int i = 1; i <= blocksToFind; i++)
                {
                    OS.rmMemory[ptr + i].setCell(String.valueOf(blocksFinded[i - 1]));
                    OS.rmMemory[ptr + i].setState(true);
                }
                OS.realMachine.setRegisterPTR((((OS.realMachine.getRegisterPTR() / 100) + blocksToFind) * 100) + ptr);
            }
            else
            {
                OS.realMachine.setRegisterPI(3);
                return;
            }
        }
    }  */
}
