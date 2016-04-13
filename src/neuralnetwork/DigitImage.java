/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

public class DigitImage {
    public double[] pixels = new double[28*28];
    public byte label;
    
    public DigitImage (double[] pixels, byte label) {
        this.pixels = pixels;
        this.label = label;             
    } 
    
    @Override
    public String toString() {
        StringBuilder lines = new StringBuilder();
        int index = 0;
        for(int i = 0; i < 28; i++) {          
            for(int j = 0; j < 28; j++) {
                if(pixels[index] > 0)
                    lines.append("#");
                else
                    lines.append(" ");
                index++;
            }
            lines.append("\n");
        }
        return lines.toString();
    }
}
