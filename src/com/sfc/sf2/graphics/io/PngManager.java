/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
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
    
    public static Tile[] importPng(String basepath){
        System.out.println("com.sfc.sf2.graphics.io.PngManager.importPng() - Importing PNG files ...");
        byte[][] graphicsChars = new byte[0][];
        Tile[] tiles = null;
        try{
            for(int i=0;i<100;i++){
                String index = String.format("%02d", i);
                Path path = Paths.get(basepath + CHARACTER_FILENAME.replace("XX.png", index+".png"));
                System.out.println("File "+path.toString()+" : ");
                BufferedImage img = ImageIO.read(path.toFile());
                byte[] graphicsChar = new byte[32];
                short width = (short)(img.getWidth() - 3);
                System.out.println("Width = "+width);
                graphicsChar[0] = (byte) ((width >> 8) & 0xff);
                graphicsChar[1] = (byte) (width & 0xff);
                for(int j=0;j<15;j++){
                    short row = 0;
                    for(int k=0;k<width+3;k++){
                        if(img.getRGB(k, j)==-1){
                            row = (short)(row | (0x8000 >> k));
                        }
                    }
                    System.out.println("\t" + String.format("%016d", Long.parseLong(Integer.toBinaryString(0xFFFF & row))));
                    graphicsChar[2+j*2] = (byte) ((row >> 8) & 0xff);
                    graphicsChar[2+j*2+1] = (byte) (row & 0xff);                    
                }
                graphicsChars = Arrays.copyOf(graphicsChars, graphicsChars.length + 1);
                graphicsChars[graphicsChars.length-1] = graphicsChar;
            }
        }catch(IOException e){
            System.out.println("No more character files to parse.");
        }catch(Exception e){
             System.err.println("com.sfc.sf2.text.io.DisassemblyManager.parseTextbank() - Error while parsing character data : "+e);
        }        
        System.out.println("com.sfc.sf2.graphics.io.PngManager.importPng() - PNG files imported.");        
        return tiles;                
    }
    
    public static void exportPng(Tile[] tiles, String filepath, String tilesPerRow){
        try {
            System.out.println("com.sfc.sf2.graphics.io.PngManager.exportPng() - Exporting PNG file ...");
            int imageTileWidth = Integer.parseInt(tilesPerRow,10);
            BufferedImage image = DefaultLayout.buildImage(tiles, imageTileWidth);
            File outputfile = new File(filepath);
            System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "png", outputfile);
            System.out.println("com.sfc.sf2.graphics.io.PngManager.exportPng() - PNG file exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    
}
