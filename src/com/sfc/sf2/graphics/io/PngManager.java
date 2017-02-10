/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class PngManager {
    
    private static String CHARACTER_FILENAME = "symbolXX.png";
    
    public static byte[][] importPng(String basepath){
        System.out.println("com.sfc.sf2.vwfont.io.PngManager.importPng() - Importing PNG files ...");
        byte[][] vwfontChars = new byte[0][];
        try{
            for(int i=0;i<100;i++){
                String index = String.format("%02d", i);
                Path path = Paths.get(basepath + CHARACTER_FILENAME.replace("XX.png", index+".png"));
                System.out.println("File "+path.toString()+" : ");
                BufferedImage img = ImageIO.read(path.toFile());
                byte[] vwfontChar = new byte[32];
                short width = (short)(img.getWidth() - 3);
                System.out.println("Width = "+width);
                vwfontChar[0] = (byte) ((width >> 8) & 0xff);
                vwfontChar[1] = (byte) (width & 0xff);
                for(int j=0;j<15;j++){
                    short row = 0;
                    for(int k=0;k<width+3;k++){
                        if(img.getRGB(k, j)==-1){
                            row = (short)(row | (0x8000 >> k));
                        }
                    }
                    System.out.println("\t" + String.format("%016d", Long.parseLong(Integer.toBinaryString(0xFFFF & row))));
                    vwfontChar[2+j*2] = (byte) ((row >> 8) & 0xff);
                    vwfontChar[2+j*2+1] = (byte) (row & 0xff);                    
                }
                vwfontChars = Arrays.copyOf(vwfontChars, vwfontChars.length + 1);
                vwfontChars[vwfontChars.length-1] = vwfontChar;
            }
        }catch(IOException e){
            System.out.println("No more character files to parse.");
        }catch(Exception e){
             System.err.println("com.sfc.sf2.text.io.DisassemblyManager.parseTextbank() - Error while parsing character data : "+e);
        }        
        System.out.println("com.sfc.sf2.vwfont.io.PngManager.importPng() - PNG files imported.");        
        return vwfontChars;                
    }
    
    public static void exportPng(byte[][] vwfontChars, String filepath){
        try {
            System.out.println("com.sfc.sf2.vwfont.io.PngManager.exportPng() - Exporting PNG files ...");
            for(int i = 0;i<vwfontChars.length;i++){
                String index = String.format("%02d", i);
                System.out.println("Char "+i+" : ");
                int width = vwfontChars[i][1];
                System.out.println("Width = "+width);
                BufferedImage bi = new BufferedImage(width+3,15,BufferedImage.TYPE_BYTE_BINARY);
                for(int j = 0 ; j<15 ; j++){
                    ByteBuffer bb = ByteBuffer.allocate(2);
                    bb.order(ByteOrder.BIG_ENDIAN);
                    bb.put(vwfontChars[i][2+j*2]);
                    bb.put(vwfontChars[i][2+j*2+1]);
                    short shortVal = bb.getShort(0);
                    System.out.println("\t" + String.format("%016d", Long.parseLong(Integer.toBinaryString(0xFFFF & shortVal))));
                    for(int k=0;k<width+3;k++){
                        if(((shortVal<<k)&0x8000)!=0){
                            bi.setRGB(k, j, 0xFFFFFF);
                        }
                    }
                }
                File outputfile = new File(filepath + System.getProperty("file.separator") + CHARACTER_FILENAME.replace("XX.png", index+".png"));
                ImageIO.write(bi, "png", outputfile);                
                System.out.println("");
            }
            System.out.println("com.sfc.sf2.vwfont.io.PngManager.exportPng() - PNG files exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    
}
