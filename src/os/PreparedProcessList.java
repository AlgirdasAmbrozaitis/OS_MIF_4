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
public class PreparedProcessList {
        
    private ArrayList<Struct> waitingList = Structnew <Process>ArrayList();
    
    public void add( int id, int res ){
        Struct newProcess = new Struct();
        newProcess.id = id;
        newProcess.part_of_resourse = res;
        waitingList.add(newProcess);
    }
    
    public void remove( int id ){
        for( Struct obj: waitingList ){
            if(obj.id == id){
                waitingList.remove(obj);
            }
        }
    }
    
    public void findProcessResourse( int id ){
        for( Struct obj: waitingList ){
            if(obj.id == id ){
                out.println("Process have :" + obj.part_of_resourse + "resourse");
            }
        }
    }
}