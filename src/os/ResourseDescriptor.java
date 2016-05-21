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
    private boolean repeated_use; // Pakartotinio ar vienkartinio naudojimo požymis
    private int father_resource; // sukūrusio proceso ID
    private String info; // informacinė resurso dalis
    /*
    private ArrayList   available_resourse, // prieinamų resursų sąrašas
                        used_resourse; // suvartotų resursų sąrašas
                      // laukiančių procesų sąrašas
    */

    public int getRes_dist_addr() {
        return res_dist_addr;
    }

    public void setRes_dist_addr(int res_dist_addr) {
        this.res_dist_addr = res_dist_addr;
    }

    public boolean isRepeated_use() {
        return repeated_use;
    }

    public void setRepeated_use(boolean repeated_use) {
        this.repeated_use = repeated_use;
    }

    public int getFather_resource() {
        return father_resource;
    }

    public void setFather_resource(int father_resource) {
        this.father_resource = father_resource;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
}
