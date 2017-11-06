// ============================================================================
// file: Ipv6Client.java
// ============================================================================
// Programmers: David Shin
// Date: 11/5/2017
// Class: CS 380 ("Computer Networks")
// Time: T/TH 3:00 - 4:50pm
// Instructor: Mr. Davarpanah
// Project: 4
//
// Description:
//      
//
// ============================================================================
import java.io.IOException;
import java.net.Socket;
import java.lang.Math;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;

public class Ipv6Client
{
    public static void main(String[] args) {
	Socket socket;
	byte[] data; 
	    
	try{
	    socket = new Socket("18.221.102.182", 38004);
	    System.out.println("Connected to server");
	    
	    //setting up input stream
	    InputStream is = socket.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
	    BufferedReader br = new BufferedReader(isr);
	    
	    //setting up output stream
	    OutputStream os = socket.getOutputStream();
	    PrintStream ps = new PrintStream(os);
	    
	    for(int i = 1; i <= 12; i++)
		{
		    //creates a new byte array for data of length 2,4,8,16,32,64,128,256,512,1024,2048
		    data = new byte[(int)Math.pow(2.0, i) + 40];
		    //FrameFill returns a byte array in IPv4 format
		    data = FrameFill(data);
		    ps.write(data);

		    byte[] code = new byte[4];
		    is.read(code);
		    System.out.println("packet size: " + (data.length - 40));
		    System.out.print("0x");
		    for(byte e: code)
			System.out.printf("%X", e);
		    System.out.println(); 
		}

	} catch(IOException e) {
		e.printStackTrace();
	}  
	}//end main

    public static byte[] FrameFill(byte[] data) {
	short length = (short) data.length;
	data[0] = 0x60; //version 6, traffic class
	data[1] = 0x0; //traffic class and flow label
	data[2] = 0x0; //flow label
	data[3] = 0x0; //flow label

	data[4] = (byte)(((length - 40) & 0xFF00) >> 8); //first byte of payload
        data[5] = (byte)((length - 40) & 0x00FF); //second byte of payload
	data[6] = 17; //next header
	data[7] = 20; //hop limit

	for(int j = 8; j < 18; j++) 
	    data[j] = 0;
	for(int j = 18; j < 20; j++) 
	    data[j] = (byte)0xFF;
	data[20] = (byte) 72; //random source address
	data[21] = (byte) 182;
	data[22] = (byte) 13;
	data[23] = (byte) 171;
	for(int j = 24; j < 34; j++) 
	    data[j] = 0;
	for(int j = 34; j < 36; j++)
	    data[j] = (byte)0xFF;
	data[36] = (byte) 18;//destination inet address: 18.221.102.182
	data[37] = (byte) 221;
	data[38] = (byte) 102;
	data[39] = (byte) 182;
	// Data section is 0
	for (int i = 40; i < length; ++i)
	    data[i] = 0x0;
	return data;
    }
    
}
