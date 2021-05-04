package com.kormoran.reader;

public class Nmea0183 {
    public Nmea0183() {
        super();
    }
    
    public static int calculateCheckSum(String value){
        int checksum = 0;
        for(int i=1;i<value.length()-1;i++){
            checksum = checksum ^ value.charAt(i);
        }    
        return checksum;
    }
}
