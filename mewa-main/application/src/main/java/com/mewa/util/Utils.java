package com.mewa.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final long NANO = 1000000000;

    public static int getFactorCompareToNano(String factor){
        if(factor.equalsIgnoreCase("m")){
            return 1000000;
        }
        if(factor.equalsIgnoreCase("u")){
            return 1000;
        }
        if(factor.equalsIgnoreCase("n")){
            return 1;
        }
        return 1;
    }

    public static double calculateToMicro(String factor){
        if(factor.equalsIgnoreCase("m")){
            return 1000;
        }
        if(factor.equalsIgnoreCase("u")){
            return 1;
        }
        if(factor.equalsIgnoreCase("n")){
            return 0.001;
        }
        return 1;
    }

    public static int getNumericValueFromByte(byte[] bytes){
        return Character.getNumericValue((char)bytes[2]);
    }

   public static String getCurrentDateForSiu(){
           SimpleDateFormat sdf = new SimpleDateFormat("HHmmss.SSS");
           Date now = new Date();
           String strDate = sdf.format(now);
           return strDate.substring(0, strDate.length() - 1).concat(",");

   }

    public static String calculateCheckSumForSiu(String value){
        int checksum = 0;
        for(int i=1;i<value.length()-1;i++){
            checksum = checksum ^ value.charAt(i);
        }
        return Integer.toHexString(checksum);
    }

    public static double ieee32Format(byte[] data) {
        int bits =
            (data[0] & 0xff) | (data[1] & 0xff) << 8 | (data[2] & 0xff) << 16 | (data[3] & 0xff) << 24;
        return Float.intBitsToFloat(bits);
    }

    public static byte[] ModRtuCrc(byte[] buf, int len) {
        int crc = 0xFFFF;

        for (int pos = 0; pos < len; pos++) {
            crc ^= (int)(0x00ff & buf[pos]); // XOR byte into least sig. byte of crc
            for (int i = 8; i != 0; i--) { // Loop over each bit
                if ((crc & 0x0001) != 0) { // If the LSB is set
                    crc >>= 1; // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else // Else LSB is not set
                    crc >>= 1; // Just shift right
            }
        }

        return new byte[] { (byte)crc, (byte)(crc >>> 8) };
    }

    public static int calculateCheckSum(String value){
        int checksum = 0;
        for(int i=1;i<value.length()-1;i++){
            checksum = checksum ^ value.charAt(i);
        }
        return checksum;
    }
}
