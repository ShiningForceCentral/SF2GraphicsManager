/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class Tile extends JPanel {
    
    private int id;
    private Color[] palette;
    private int[][] pixels = new int[8][8];
    
    private boolean highPriority = false;
    private boolean hFlip = false;
    private boolean vFlip = false;

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    
    public Color[] getPalette() {
        return palette;
    }

    public void setPalette(Color[] palette) {
        this.palette = palette;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public boolean ishFlip() {
        return hFlip;
    }

    public void sethFlip(boolean hFlip) {
        this.hFlip = hFlip;
    }

    public boolean isvFlip() {
        return vFlip;
    }

    public void setvFlip(boolean vFlip) {
        this.vFlip = vFlip;
    }  
    
    public Tile(){
        setSize(8,8);

    }
    
    public void setPixel(int x, int y, int colorIndex){
        this.pixels[x][y] = colorIndex;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(getImage(), 0, 0, this);       
    }    
    
    public BufferedImage getImage(){
        BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        for(int y=0;y<pixels.length;y++){
            for(int x=0;x<pixels[y].length;x++){
                int colorIndex = pixels[x][y];
                Color color = palette[colorIndex];
                int rgbValue = color.getRGB();
                image.setRGB(x,y,rgbValue);
            }
        } 
        return image;        
    }
    
    public static Tile vFlip(Tile tile){
        Tile flippedTile = new Tile();
        flippedTile.setHighPriority(tile.isHighPriority());
        flippedTile.sethFlip(tile.ishFlip());
        flippedTile.setvFlip(!tile.isvFlip());
        flippedTile.setPalette(tile.getPalette());
        for(int y=0;y<tile.getPixels().length;y++){
            for(int x=0;x<tile.getPixels()[y].length;x++){
                flippedTile.getPixels()[y][x] = tile.getPixels()[y][7-x];
            }
        } 
        return flippedTile;
    }
    
    public static Tile hFlip(Tile tile){
        Tile flippedTile = new Tile();
        flippedTile.setHighPriority(tile.isHighPriority());
        flippedTile.sethFlip(!tile.ishFlip());
        flippedTile.setvFlip(tile.isvFlip());
        flippedTile.setPalette(tile.getPalette());
        for(int y=0;y<tile.getPixels().length;y++){
            for(int x=0;x<tile.getPixels()[y].length;x++){
                flippedTile.getPixels()[y][x] = tile.getPixels()[7-y][x];
            }
        } 
        return flippedTile;
    }    
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Tile " + id +" :\n");
        for(int y=0;y<pixels.length;y++){
            for(int x=0;x<pixels[y].length;x++){
                sb.append("\t"+pixels[x][y]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static Tile paletteSwap(Tile tile, Color[] palette){
        Tile pltSwappedTile = new Tile();
        pltSwappedTile.setPixels(tile.getPixels());
        pltSwappedTile.setHighPriority(tile.isHighPriority());
        pltSwappedTile.sethFlip(!tile.ishFlip());
        pltSwappedTile.setvFlip(tile.isvFlip());
        pltSwappedTile.setPalette(palette);
        return pltSwappedTile;
    }
    
    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj==null || obj.getClass() != this.getClass()){
            return false;
        }
        Tile tile = (Tile) obj;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(this.pixels[j][i]!=tile.pixels[j][i]){
                    return false;
                }
            }
        }
        return true;        
    }
    
    
}
