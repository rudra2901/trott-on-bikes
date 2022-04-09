package com.example.trottonbikes;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.net.InetAddress;
import java.sql.Time;
import java.util.Date;

public class TImeLookup {
    private static final String SERVER_NAME = "pool.ntp.org";

    public static final TImeLookup instance = new TImeLookup();

    public static TImeLookup getInstance() {
        return instance;
    }

    private TImeLookup() {
    }

    public long getCurrTime() throws Exception {
        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 5 seconds
        client.setDefaultTimeout(5000);

        // TODO: find a way to return epoch time instead of timestamp

        InetAddress inetAddress = InetAddress.getByName(SERVER_NAME);
        TimeInfo timeInfo = client.getTime(inetAddress);
        Date currTime = new Date(timeInfo.getMessage().getTransmitTimeStamp().getTime());

        //client.close();

        return currTime.getTime();
    }
}
