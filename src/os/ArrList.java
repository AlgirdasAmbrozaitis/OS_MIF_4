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
        
    private ArrayList<Struct> list = new <Struct>ArrayList();
    
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
    
    public void addPa( int part ){
        Struct struct = new Struct();
        struct.part_of_resourse = part;
        list.add(struct);
        //Collections.sort(list);
    }
    public void addR(int resource, int part ){
        Struct struct = new Struct();
        struct.processId = resource;
        struct.part_of_resourse = part;
        list.add(struct);
        //Collections.sort(list);
    }
    
    public void addOa(int resource, int part ){
        Struct struct = new Struct();
        struct.processId = resource;
        struct.part_of_resourse = part;
        list.add(struct);
    }
    
    public void remove( int id ){ // pasalinami visi elementai su vardu <id>
        for( Struct obj: list ){
            if(obj.processId == id){
                list.remove(obj);
                //Collections.sort(list);
            }
        }
    }
    
    public void removeR( int id, int part ){ //pasalinami visi elementa su vardu <id> turintys <part> resurso dali
        for( Struct obj: list ){
            if(obj.processId == id && obj.part_of_resourse == part){
                list.remove(obj);
                //Collections.sort(list);
            }
        }
    }
    public int removeFirst() //pasalinamas pirmas elementas, pps, lps
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
    public Struct get( int index ){
        for( Struct obj: list){
            if(obj.processId == index ){
                return obj;
            }
        }
        return null;
    }
    
    public ArrayList<Struct> getList(){
        return list;
    }
}