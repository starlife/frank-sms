/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chinamobile.cmpp3_0.protocol.util;

public class TypeConvert {

    public static byte[] short2byte(short i) {
        byte[] b = new byte[2];
        b[0] = (byte) (i >> 8);
        b[1] = (byte) i;
        return b;
    }

    public static void short2byte(short n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 8);
        buf[offset + 1] = (byte) n;
    }

    public static void int2byte(int n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 24);
        buf[offset + 1] = (byte) (n >> 16);
        buf[offset + 2] = (byte) (n >> 8);
        buf[offset + 3] = (byte) n;
    }

    public static byte[] int2byte(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n >> 24);
        b[1] = (byte) (n >> 16);
        b[2] = (byte) (n >> 8);
        b[3] = (byte) n;
        return b;
    }

    public static void long2byte(long n, byte[] buf, int offset) {

        buf[offset] = (byte) (n >> 56);
        buf[offset + 1] = (byte) (n >> 48);
        buf[offset + 2] = (byte) (n >> 40);
        buf[offset + 3] = (byte) (n >> 32);
        buf[offset + 4] = (byte) (n >> 24);
        buf[offset + 5] = (byte) (n >> 16);
        buf[offset + 6] = (byte) (n >> 8);
        buf[offset + 7] = (byte) n;

    }

    public static byte[] long2byte(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n >> 56);
        b[1] = (byte) (n >> 48);
        b[2] = (byte) (n >> 40);
        b[3] = (byte) (n >> 32);
        b[4] = (byte) (n >> 24);
        b[5] = (byte) (n >> 16);
        b[6] = (byte) (n >> 8);
        b[7] = (byte) n;
        return b;
    }

    public static int byte2int(byte b[]) {
        return byte2int(b, 0);
    }

    public static int byte2int(byte b[], int offset) {
        return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8 | (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
    }

    public static short byte2short(byte b[]) {
        return byte2short(b, 0);
    }

    public static short byte2short(byte b[], int offset) {
        return (short) (b[offset + 1] & 0xff | (b[offset + 0] & 0xff) << 8);
    }

    /*拷贝字符串src的len个字节的长度，如果len<src.lenght()则拷贝部分，如果len>=src.length()全拷*/
    public static byte[] getBytes(String src, int len) {

        byte[] buf = new byte[len];
        byte[] temp = src.getBytes();
        if (len > temp.length) {
            System.arraycopy(temp, 0, buf, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, buf, 0, len);
        }
        return buf;
    }

    public static byte[] getBytes(String[] src, int len) {

        byte[] buf = new byte[len * src.length];
        for (int i = 0; i < src.length; i++) {
            byte[] temp = src[i].getBytes();
            System.arraycopy(temp, 0, buf, i * len, temp.length);
        }
        return buf;
    }
   
}
