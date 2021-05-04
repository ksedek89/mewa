package com.kormoran.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;


public class TelnetClient implements Runnable {
    public TelnetClient() {
        super();
    }
    private final static Logger logger = Logger.getLogger(TelnetClient.class);


    private static String currentIp = "192.168.127.254";
    private static String expectedIp = "192.168.127.253";

    public void run() {
        while(true){
            try{
                 if(InetAddress.getByName(expectedIp).isReachable(3000)){
                    //nie zmienione ip - odczekujemy 10 minut
                    logger.debug("Znaleziono: " + expectedIp);

                }else if (InetAddress.getByName(currentIp).isReachable(3000)){
                    //zmieniono ip - resetujemy ustawienia
                    logger.debug("Znaleziono: " + currentIp);

                 
                
                    //inicjacja
                    Socket socket = new Socket(currentIp, 23);
                    BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter w = new PrintWriter(socket.getOutputStream(),true);
                    
                    init(w,r);
                    setIp(w,r);

                    setBaudRate(w,r);
                    setParityBit(w,r);
                    
                    setRs485(w,r);
                    restart(w, r);
                                     
                    
                    socket.close();
                    
                 }else{
                     logger.debug("Nie znaleziono");
                 }
            logger.debug("Go to sleep");
            Thread.sleep(100000);
            logger.debug("Out of sleepp");
           }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private void setBaudRate(PrintWriter w, BufferedReader r) throws Exception{
        //ustawienia baudrate
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);    
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);  
        
        //port nr 1
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);       

        //port  nr 2
        sendDownButton(w);
        
        //port nr3
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);             
        //port nr4
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);            
        //port nr5
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr6
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr7
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);            
        //port nr8
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr9
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);             
        //port nr10
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr11
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);             
        //port nr12
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);             
        //port nr13
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);          
        //port nr14
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr15
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr16
        sendDownButton(w);
        
        //port nr17
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);      
        //port nr18
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);      
        //port nr19
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);          
        //port nr20
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);          
        //port nr21
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);          
        //port nr22
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);         
        //port nr23
        sendDownButton(w);
        
        //port nr24
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);            
        //port nr25
        sendDownButton(w);
        
        //port nr26
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);           
        //port nr27
        sendDownButton(w);
        writeData(w,"\r\n"); 
        readData(r);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        sendUpButton(w);       
        writeData(w,"\r\n");     
        readData(r);         
        //port nr28
        sendDownButton(w);
        
        //port nr29
        sendDownButton(w);
        
        //port nr30
        sendDownButton(w);
        
        //port nr31
        sendDownButton(w);
        
        //port nr32
        sendDownButton(w);
        
            //wyjscie do menu wyzej
        w.write(27);
        w.flush();
        readData(r);
        w.write(27);
        w.flush();
        readData(r);    
            //wpisanie y
        w.write(121);
        w.flush();
        Thread.sleep(3000);
        readData(r);
        sendLeftButton(w);
        sendLeftButton(w);
        sendLeftButton(w);
        


    }
    
    private void setParityBit(PrintWriter w, BufferedReader r) throws Exception{
              //ustawienie port�w rs-485
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);    
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);  
        
        //ustawienie rs-485 portow
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);  
      
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       
        sendDownButton(w);       

            //ustawienie jednego portu
        writeData(w,"\r\n"); 
        readData(r);       
        sendDownButton(w);       
        sendDownButton(w);       
        writeData(w,"\r\n");     
        readData(r);       
 
            //wyj�cie i zapis
        w.write(27);
        w.flush();
        readData(r);
        w.write(27);
        w.flush();
        readData(r);    
            //wpisanie y
        w.write(121);
        w.flush();
        Thread.sleep(3000);
        readData(r);
        sendLeftButton(w);
        sendLeftButton(w);
        sendLeftButton(w); 
    }
    
    private void setRs485(PrintWriter w, BufferedReader r) throws Exception{
              //ustawienie port�w rs-485
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);    
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);  
        
        //ustawienie rs-485 portow
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);  
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);       
            //ustawienie jednego portu
        writeData(w,"\r\n"); 
        readData(r);       
        sendDownButton(w);       
        sendDownButton(w);       
        writeData(w,"\r\n");     
        readData(r);       
            //ustawienie kolejnych portow
        for(int i = 0;i<31;i++){
            sendDownButton(w);
            writeData(w,"\r\n"); 
            readData(r);       
            sendDownButton(w);       
            sendDownButton(w);       
            writeData(w,"\r\n");     
            readData(r);      
        }
        
        
            //wyj�cie i zapis
        w.write(27);
        w.flush();
        readData(r);
        w.write(27);
        w.flush();
        readData(r);    
            //wpisanie y
        w.write(121);
        w.flush();
        Thread.sleep(3000);
        readData(r);
        sendLeftButton(w);
        sendLeftButton(w);
        sendLeftButton(w); 
    }
    
    private void setIp(PrintWriter w, BufferedReader r) throws Exception{
                 //ustawienie ip
        sendRightButton(w);
        sendRightButton(w);
        writeData(w,"\r\n");    
        readData(r);
        writeData(w,"\r\n");    
        readData(r);
        sendDownButton(w);
        writeData(w,expectedIp);
                //escape
        w.write(27);
        w.flush();
        readData(r);
        w.write(27);
        w.flush();
        readData(r);
                //wpisanie y
        w.write(121);
        w.flush();
        Thread.sleep(3000);
        readData(r);     
        sendLeftButton(w);
        sendLeftButton(w); 
    }
    
    private void init(PrintWriter w, BufferedReader r) throws Exception{
        //init i logowanie
        readData(r);    
        writeData(w,"\r\n");
        readData(r);
        writeData(w,"admin\r\n\r\n");
        readData(r);
    }
    
    private void restart(PrintWriter w, BufferedReader r) throws Exception{
                //restart
        sendRightButton(w);
        sendRightButton(w);
        sendRightButton(w);        
        sendRightButton(w);        
        sendRightButton(w);
        sendRightButton(w);       
        sendRightButton(w);       
        writeData(w,"\r\n"); 
        readData(r);     
        writeData(w,"\r\n"); 
        readData(r);   
        writeData(w,"\r\n");         
    }

    
    private  void sendRightButton(PrintWriter w){
        w.write(27);
        w.write(91);
        w.write(67);      
    }
    
    private  void sendUpButton(PrintWriter w){
        w.write(27);
        w.write(91);
        w.write(65);      
    }   
    private  void sendLeftButton(PrintWriter w){
        w.write(27);
        w.write(91);
        w.write(68);      
    }
    
    private  void sendDownButton(PrintWriter w){
        w.write(27);
        w.write(91);
        w.write(66);      
    }  
    private  void readData(BufferedReader r) throws Exception{
        Thread.sleep(500);
        int c=0;      
        while (r.ready() && (c = r.read()) != -1){
            System.out.print((char)c);
        }
        System.out.println();
    }
    
    private  void writeData(PrintWriter w, String data){
        w.print(data); // also tried simply \n or \r
        w.flush();       
    
    }
    
}
