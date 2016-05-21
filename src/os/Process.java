/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

/**
 *
 * @author eimantas
 */
public class Process implements Comparable<Process>{
    int id;
    int part_of_resourse;
    int priority;
    String info;
    
    @Override
    public int compareTo(Process another) {
        if( this.priority <= another.priority ){
            return 1;
        } else
            return -1;
    }
}
