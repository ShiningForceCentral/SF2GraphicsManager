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
    
}
