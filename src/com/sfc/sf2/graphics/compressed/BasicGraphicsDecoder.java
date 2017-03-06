/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.compressed;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.uncompressed.*;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wiz
 */
public class BasicGraphicsDecoder {
    
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(List<Byte> bytes) {
        char[] hexChars = new char[bytes.size() * 2];
        for ( int j = 0; j < bytes.size(); j++ ) {
            int v = bytes.get(j) & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Tile[] decodeBasicGraphics(byte[] input, Color[] palette){
        System.out.println("com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder.decodeBasicGraphics() - Decoding basic graphics ...");
        System.out.println("com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder.decodeBasicGraphics() - Data length = " + input.length + " bytes.");
        List<Byte> output = new ArrayList();
        boolean done = false;
        int pointer = 0;
        Tile[] tiles = null;
        try{
            while(!done){
                ByteBuffer bbCommand = ByteBuffer.allocate(2);
                bbCommand.order(ByteOrder.LITTLE_ENDIAN);
                bbCommand.put(input[pointer+1]);
                bbCommand.put(input[pointer]);
                short commands = bbCommand.getShort(0);
                pointer+=2;
                //System.out.println("commands = " + Integer.toHexString(commands&0xFFFF)); 
                for(int i=0;i<16;i++){
                    if((commands & (1<<15-i)) != 0){
                        // apply repeat
                        ByteBuffer bbRepeat = ByteBuffer.allocate(2);
                        bbRepeat.order(ByteOrder.LITTLE_ENDIAN);
                        bbRepeat.put(input[pointer+1]);
                        bbRepeat.put(input[pointer]);          
                        short repeatCommand = bbRepeat.getShort(0); 
                        pointer+=2;
                        //System.out.println("repeatCommand = " + Integer.toHexString(repeatCommand&0xFFFF)); 
                        if(repeatCommand==0){
                            done = true;
                            break;
                        }else{
                            byte repeats = (byte)(repeatCommand & 0x1F);
                            short wordIndex = (short)((repeatCommand - repeats)>>5);
                            //System.out.println("pointer = " + pointer);
                            //System.out.println("repeats = " + Integer.toHexString(repeats&0xFFFF) + ", wordIndex = " + Integer.toHexString(wordIndex&0xFFFF));
                            if(wordIndex==1){
                                // repeat last word (4 pixels)
                                byte firstByte = output.get(output.size()-2);
                                byte secondByte = output.get(output.size()-1);
                                for(int r=0;r<33-repeats;r++){
                                    output.add(firstByte);
                                    output.add(secondByte);
                                }
                            }else{
                                // repeat pointed 2 words (2 x 4 pixels)
                                int copyPointer = output.size()-wordIndex*2;
                                for(int r=0;r<33-repeats;r++){
                                    output.add(output.get(copyPointer));
                                    output.add(output.get(copyPointer+1));
                                    copyPointer+=2;
                                }
                            }
                            //System.out.println("output = " + bytesToHex(output));
                        }
                    }else{
                        // copy word
                        output.add(input[pointer]);
                        output.add(input[pointer+1]);
                        pointer+=2;
                        //System.out.println("output = " + bytesToHex(output));
                    }
                }
                
            }
        }catch(Exception e){
            System.out.println("com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder.decodeBasicGraphics() - Basic graphics decoding failed.");
            throw e;
        }finally{
            byte[] bytes = new byte[output.size()];
            for(int i=0;i<bytes.length;i++){
                bytes[i] = output.get(i);
            }
            tiles = UncompressedGraphicsDecoder.decodeUncompressedGraphics(bytes, palette);
        }
        System.out.println("com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder.decodeBasicGraphics() - Basic graphics decoded.");
        return tiles;
    }    
    
}
