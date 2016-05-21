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
    private ArrList prieinamu_resursu_sarasas = new ArrList();
    private ArrList used_resourse  = new ArrList(); // suvartotų resursų sąrašas
    private ArrList laukianciu_procesu_sarasas = new ArrList();

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

    public ArrList getPrieinamu_resursu_sarasas()
    {
        return prieinamu_resursu_sarasas;
    }

    public void setPrieinamu_resursu_sarasas(ArrList prieinamu_resursu_sarasas)
    {
        this.prieinamu_resursu_sarasas = prieinamu_resursu_sarasas;
    }

    public ArrList getUsed_resourse()
    {
        return used_resourse;
    }

    public void setUsed_resourse(ArrList used_resourse)
    {
        this.used_resourse = used_resourse;
    }

    public ArrList getLaukianciu_procesu_sarasas()
    {
        return laukianciu_procesu_sarasas;
    }

    public void setLaukianciu_procesu_sarasas(ArrList laukianciu_procesu_sarasas)
    {
        this.laukianciu_procesu_sarasas = laukianciu_procesu_sarasas;
    }

    public int getRs()
    {
        return rs;
    }
    
    
}
