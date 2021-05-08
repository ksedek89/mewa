package com.kormoran.reader;


import org.apache.log4j.Logger;




public class Kormoran {
    private final static Logger logger = Logger.getLogger(Kormoran.class);

    public Kormoran() {
        super();
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        (new Reader(configuration)).initReader();
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
