package os;

import java.util.*;
/**
 *
 * @author eimantas
 */
public class ResourseDescriptor {
    
    private static int resource_id_counter = 0; 
    private final int rs = resource_id_counter++;; // Resurso ID
    
    private int res_dist_addr;  // resursų paskirstytojo adresas
    private boolean pnr; // Pakartotinio ar vienkartinio naudojimo požymis
    private int k; // sukūrusio proceso ID
    private ArrayList   pa, // prieinamų resursų sąrašas
                        su, // suvartotų resursų sąrašas
                        lps; // laukiančių procesų sąrašas
    private String info; // informacinė resurso dalis
    
    ResourseDescriptor( int rs, int proc_id, boolean pnr, ArrayList res_list, 
                        ArrayList used_res,  ArrayList lps, String info ){
        this.pnr = pnr;
        this.k = proc_id;
        this.pa = new ArrayList();
        this.pa.addAll(res_list);
        this.su = new ArrayList();
        this.su.addAll(used_res);
        this.lps = new ArrayList();
        this.lps.addAll(lps);
        this.info = info;
        
    }
    
    public int getRS(){
        return this.rs;
    }
    
    public int getRESDISTADDR(){
        return this.res_dist_addr;
    }
    
    public boolean isPNR(){
        return this.pnr;
    }
    
    public int getK(){
        return this.k;
    }
    
    public ArrayList getPA(){
        return this.pa;
    }
    
    public ArrayList getSU(){
        return this.su;
    }
    
    public ArrayList getLPS(){
        return this.lps;
    }
    
    public String getINFO(){
        return this.info;
    }
}
