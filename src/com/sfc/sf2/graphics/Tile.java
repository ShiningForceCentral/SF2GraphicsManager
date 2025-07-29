/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class Tile extends JPanel {
    
    public static final int PIXEL_WIDTH = 8;
    public static final int PIXEL_HEIGHT = 8;
    
    private int id;
    private Palette palette;
    private int[][] pixels = new int[PIXEL_HEIGHT][PIXEL_WIDTH];
    private BufferedImage indexedColorImage = null;
    
    private boolean highPriority = false;
    private boolean hFlip = false;
    private boolean vFlip = false;
    
    private int occurrences = 0;

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
    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public IndexColorModel getIcm() {
        if (palette == null) {
            return null;
        } else {
            return palette.getIcm();
        }
    }

    public BufferedImage getIndexedColorImage(){
        if(indexedColorImage==null){
            IndexColorModel icm = palette.getIcm();
            indexedColorImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED, icm);
            byte[] data = ((DataBufferByte)(indexedColorImage.getRaster().getDataBuffer())).getData();
            int width = indexedColorImage.getWidth();
            for(int i=0;i<pixels.length;i++){
                for(int j=0;j<pixels[i].length;j++){
                    data[j*width+i] = (byte)(pixels[i][j]);
                }
            }
        }
        return indexedColorImage;        
    }
    
    public void clearIndexedColorImage() {
        indexedColorImage = null;
    }
    
    public void drawIndexedColorPixels(BufferedImage image, int[][] pixels, int x, int y){
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

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }
    
    public static Tile vFlip(Tile tile){
        Tile flippedTile = new Tile();
        flippedTile.setId(tile.getId());
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
        flippedTile.setId(tile.getId());
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
    
    public static Tile paletteSwap(Tile tile, Palette palette){
        Tile pltSwappedTile = new Tile();
        pltSwappedTile.setId(tile.getId());
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
    
    public boolean equalsWithPriority(Object obj){
        if(this==obj){
            return true;
        }
        if(obj==null || obj.getClass() != this.getClass()){
            return false;
        }
        Tile tile = (Tile) obj;
        if(this.highPriority!=tile.isHighPriority()){
            return false;
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(this.pixels[j][i]!=tile.pixels[j][i]){
                    return false;
                }
            }
        }
        return true;        
    }
    
    private Tile cloneTile() {
        Tile newTile = new Tile();
        newTile.setId(getId());
        newTile.setPalette(getPalette());
        newTile.setPixels(getPixels());
        newTile.setHighPriority(isHighPriority());
        newTile.sethFlip(ishFlip());
        newTile.setvFlip(isvFlip());
        return newTile;
    }
    
    public boolean isTileEmpty() {
        if (pixels == null) {
            return true;
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if (pixels[i][j] > 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static Tile EmptyTile(Palette palette) {
        Tile emptyTile = new Tile();
        emptyTile.setPalette(palette);
        emptyTile.setPixels(new int[8][8]);
        return emptyTile;
    }
}
