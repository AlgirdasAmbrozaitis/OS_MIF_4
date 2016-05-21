/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.awt.List;
import static java.lang.System.out;
import java.util.ArrayList;

/**
 *
 * @author eimantas
 */

/*
    Laukiančių procesų sąrašas
*/
public class WaitingProcessList {
        
    private ArrayList<Process> waitingList = new <Process>ArrayList();
    
    public void add( int id, int res ){
        Process newProcess = new Process();
        newProcess.id = id;
        newProcess.part_of_resourse = res;
        waitingList.add(newProcess);
    }
    
    public void remove( int id ){
        for( Process obj: waitingList ){
            if(obj.id == id){
                waitingList.remove(obj);
            }
        }
    }
    
    public void findProcessResourse( int id ){
        for( Process obj: waitingList ){
            if(obj.id == id ){
                out.println("Process have :" + obj.part_of_resourse + "resourse");
            }
        }
    }
}
