/*
 * Copyright (c) 2016-2017 Sviataslau Apanasionak
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Redistributions of source code or in binary form for commercial purposes are prohibited without written permission of the author.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.slavick.macmonitor.model.networkentities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Created by apanasyonok on 01.11.2016.
 */
@Embeddable
@Access( AccessType.FIELD )
public class IpAddress implements Serializable{

    @Column(name="ip_version")
    private byte version;
    @Column(name = "ip_high")
    private long high;
    @Column(name = "ip_low")
    private long low;


    public IpAddress(String string) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(string);
        byte[]bytes = inetAddress.getAddress();
        version = inetAddress.getClass().equals(Inet4Address.class) ? (byte)4:6;

        if(version==4){
            low=bytes[0]&0xff;
            low<<=8;
            low+=bytes[1]&0xff;
            low<<=8;
            low+=bytes[2]&0xff;
            low<<=8;
            low+=bytes[3]&0xff;
        }
        else
        if(version==6){

            for(int i=0;i<=6;i++) {
                low+= (bytes[i+8] & 0xff);
                high+=(bytes[i]& 0xff);
                low<<=8;
                high<<=8;
            }

            low+=bytes[15]& 0xff;
            high+=bytes[7]&0xff;
        }
    }



    public IpVersion getIpVersion(){
        if(version==4)
            return IpVersion.IPV_4;
        if(version==6)
            return IpVersion.IPV_6;
        return null;
    }

    public IpAddress() {
    }

    public static void main(String[] args) throws Exception{
        IpAddress ip = new IpAddress("ffff:ffff:1111:ffff:ffff:ffff:ffff:0000");
        System.out.printf(ip.getString());
    }

    public String getString(){
        if(version==4){
            return String.format("%d.%d.%d.%d",
                    (low >> 24 & 0xff),
                    (low >> 16 & 0xff),
                    (low >> 8 & 0xff),
                    (low & 0xff));

        }
        if(version==6){
            return String.format("%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x",
                    high>>56  & 0xff,
                    high>>48  & 0xff,
                    high>>40  & 0xff,
                    high>>32  & 0xff,
                    high>>24  & 0xff,
                    high>>16  & 0xff,
                    high>>8  & 0xff,
                    high  & 0xff,
                    low>>56  & 0xff,
                    low>>48  & 0xff,
                    low>>40  & 0xff,
                    low>>32  & 0xff,
                    low>>24  & 0xff,
                    low>>16  & 0xff,
                    low>>8  & 0xff,
                    low  & 0xff
            );

        }

        return null;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpAddress)) return false;

        IpAddress ipAddress = (IpAddress) o;

        if (version != ipAddress.version) return false;
        if (high != ipAddress.high) return false;
        return low == ipAddress.low;

    }

    @Override
    public int hashCode() {
        return (int) (low ^ (low >>> 32));
    }

    @Override
    public String toString() {
        return   getString();

    }
}
