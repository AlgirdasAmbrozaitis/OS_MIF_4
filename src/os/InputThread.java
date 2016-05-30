/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.util.ArrayList;
import java.util.Scanner;
//import static os.OS.input;
//import static os.OS.inputStarted;
//import static os.OS.inputStreamOk;
//import static os.OS.startInput;

/**
 *
 * @author Algirdas
 */
public class InputThread extends Thread
{
    public void run()
    {
        OS.startInput = false;
        Scanner in = new Scanner(System.in);
        while(OS.inputStarted);
        OS.inputStream = new ArrayList<>();
        OS.inputStarted = true;
        String s = "";
        while(true)
        {
            s = in.nextLine();
            System.out.println("ivesta: " + s);
            if(s.equals("END"))
            {
                break;
            }
            OS.inputStream.add(s);
        }
        OS.inputStreamOk = true;
    }
}
