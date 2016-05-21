package os;

import java.awt.List;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author eimantas
 */

/*
    Laukiančių procesų sąrašas
*/
public class ArrList {
        
    private ArrayList<Struct> list = new <Process>ArrayList();
    
    public void addLps( int id, int res, String info, int prior ){
        Struct struct = new Struct();
        struct.processId = id;
        struct.part_of_resourse = res;
        struct.priority = prior;
        list.add(struct);
        Collections.sort(list);
    }
    public void addPps( int id, int prior){
        Struct struct = new Struct();
        struct.processId = id;
        struct.priority = prior;
        //struct.part_of_resourse = res;
        list.add(struct);
        Collections.sort(list);
    }
    public void addSu( int id, int res, String info){
        Struct struct = new Struct();
        struct.processId = id;
        struct.part_of_resourse = res;
        struct.info = info;
        //struct.part_of_resourse = res;
        list.add(struct);
        Collections.sort(list);
    }
    
    public void remove( int id ){
        for( Struct obj: list ){
            if(obj.processId == id){
                list.remove(obj);
                //Collections.sort(list);
            }
        }
    }
    public int removeFirst()
    {
        int value = list.get(0).processId;
        list.remove(0);
        return value;
    }
    
    public void findProcessResourse( int id ){
        for( Struct obj: list ){
            if(obj.processId == id ){
                out.println("Process have :" + obj.part_of_resourse + "resourse");
            }
        }
    }
}