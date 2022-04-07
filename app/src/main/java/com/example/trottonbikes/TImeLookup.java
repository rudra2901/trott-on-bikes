package com.example.trottonbikes;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.net.InetAddress;
import java.sql.Time;
import java.util.Date;

public class TImeLookup {
    private Date currTime;
    private static final String SERVER_NAME = "pool.ntp.org";
    private volatile TimeInfo timeInfo;
    private volatile Long offset;

    public static final TImeLookup instance = new TImeLookup();
    public static TImeLookup getInstance() {
        return instance;
    }

    private TImeLookup() {}

    public Date getCurrTime() throws Exception {
        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 10 seconds
        client.setDefaultTimeout(10_000);

        InetAddress inetAddress = InetAddress.getByName(SERVER_NAME);
        TimeInfo timeInfo = client.getTime(inetAddress);
        timeInfo.computeDetails();
        if (timeInfo.getOffset() != null) {
            this.timeInfo = timeInfo;
            this.offset = timeInfo.getOffset();
        }

        // Calculate the remote server NTP time
        long currentTime = System.currentTimeMillis();
        TimeStamp atomicNtpTime = TimeStamp.getNtpTime(currentTime + offset);

        System.out.println("Atomic time:\t" + atomicNtpTime + "  " + atomicNtpTime.toDateString());

        return currTime;
    }

    public boolean isComputed()
    {
        return timeInfo != null && offset != null;
    }
}
