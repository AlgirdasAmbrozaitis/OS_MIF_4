/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author eimantas
 */
public class ReadyProcessList {
    
    private ArrayList<Struct> rpl = Structnew <Process>ArrayList();
    
    void add( int id, int priority ){
        
        Struct newProcess = new Struct();
        newProcess.id = id;
        newProcess.priority = priority;
        rpl.add(newProcess);
        Collections.sort(rpl);
        
    }
    
    void remove( int id ){
        for( Struct obj: rpl ){
            if( obj.id == id ){
                rpl.remove(obj);
            }
        }
        Collections.sort(rpl);
    }
    
}
