package com.kormoran.sensors;

public class Rs485 {

    private int baudRate;
    private int dataBits;
    private int stopBit;
    private int parityBit;

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setStopBit(int stopBit) {
        this.stopBit = stopBit;
    }

    public int getStopBit() {
        return stopBit;
    }

    public void setParityBit(int parityBit) {
        this.parityBit = parityBit;
    }

    public int getParityBit() {
        return parityBit;
    }

    public Rs485() {
        super();
    }

    public static float ieee24Format(byte[] data) {
/*        int bits =
            (data[0] & 0xff) | (data[1] & 0xff) << 8 | (data[2] & 0xff) << 16 |
            (data[3] & 0xff) << 24;
        return Float.intBitsToFloat(bits);*/
        return -1;
    }

    public static byte[] ModRTU_CRC(byte[] buf, int len) {
/*        int crc = 0xFFFF;

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

        return new byte[] { (byte)crc, (byte)(crc >>> 8) };*/
        return null;
    }


}
