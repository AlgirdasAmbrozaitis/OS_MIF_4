/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.System.out;
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
    public static String inputText;
    public void run()
    {
        System.out.println("ivedimo pradzia");
        OS.startInput = false;
        while(OS.inputStarted);
        //OS.inputStarted = true;
        OS.gui.getInputButton().addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                inputText = new String(OS.gui.getInputArea().getText());

                OS.gui.getInputArea().setText(null);
                OS.inputStream = new ArrayList<>();
                //while(inputText.isEmpty());
                while(!inputText.isEmpty()){
                      if(inputText.contains(" ")){
                          String word = inputText.substring(0,inputText.indexOf(' '));
                          System.out.println("___________>>> zodis: " + word);
                          OS.inputStream.add(new String(word));
                          inputText = new String(inputText.substring(inputText.indexOf(' ')+1,inputText.length()));
                      } else {
                          OS.inputStream.add(inputText);
                          inputText = new String();
                      }
                }
                System.out.println("---------------------------------------->>>>> nustatomas pozymis true");
                for(int i = 0; i < OS.inputStream.size(); i++)
                {
                    System.out.println("inputStream : " + OS.inputStream.get(i));
                }
                OS.inputStreamOk = true;
                //Thread.currentThread().interrupt();
                return;
            } 
          } );
    }
}
