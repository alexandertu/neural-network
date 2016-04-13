/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author local
 */
public class MNISTReader {
    
    ArrayList<DigitImage> digitImages = new ArrayList();

    public MNISTReader(String name) {
        
        reader("./" + name + "-images.idx3-ubyte",
                "./" + name + "-labels.idx1-ubyte");
    }
    
    private final void reader(String images, String labels) {
        try {
            
            DataInputStream ioImage = 
                new DataInputStream(new BufferedInputStream(new FileInputStream(images)));
            DataInputStream ioLabel =  
                new DataInputStream(new BufferedInputStream(new FileInputStream(labels)));
            
            int magic1 = ioImage.readInt();
            int numImages = ioImage.readInt();
            int numRows = ioImage.readInt();
            int numColums = ioImage.readInt();
           
            int magic2 = ioLabel.readInt();
            int numLabels = ioLabel.readInt();

            for(int d = 0; d < numImages; d++) {
                double[] pixels = new double[784];
                for(int i = 0; i < 784; i++) {
                    pixels[i] = normalize((int) ioImage.readUnsignedByte());
                }
                byte label = ioLabel.readByte();
                if(label < 11){
                    digitImages.add(new DigitImage(pixels, label));
                }
                
            }
            
   
        } catch(IOException ex) {
            
        }        
        
    }
        
    public final double normalize(int value) {
        return value/255;
    }
    
    public double[] getTargetVector(byte value) {
        double[] target = new double[10];
        target[value] = 1;
        return target;
    }
    
    public ArrayList<DigitImage>  getImages() {
        Collections.shuffle(digitImages);
        return digitImages;
    }
     
}
