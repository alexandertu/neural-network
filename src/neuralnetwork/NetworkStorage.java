/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author local
 */
public class NetworkStorage {
    
    public NetworkStorage() {
        
    }
    public void saveWeights(Network n, String name) {
        try {
            
            StringBuilder content = new StringBuilder();
            int[] sizes = n.sizes;
            
            //Append weights
            for(int layer = 1; layer < sizes.length; layer++) {
               for(int node = 0;  node < sizes[layer]; node++){
                   for(int prevLayerNode = 0; prevLayerNode < sizes[layer-1]; prevLayerNode++) {
                       content.append(n.weight.get(layer).get(node).get(prevLayerNode)).append("\n");
                   }
               }
            }
            
            //Append biases
            for(int layer = 1; layer < sizes.length; layer++) {
                for(int node = 0; node < sizes[layer]; node++) {
                    content.append(n.bias.get(layer).get(node)).append("\n");
                }
            }
            
            
            File file = new File("./network-weights-" + name + ".data");
//            if(!file.exists())
//                file.createNewFile();

            //abs. pathname
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content.toString());
            bw.close();
            fw.close();
        
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void save(Network n, String name) {
        try {
            FileOutputStream file = new FileOutputStream("./network-saved-" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(file));
            out.writeObject(n);
            out.close(); 
            file.close();
        } catch (IOException e) {
            
        }
    }
    
    public Network load(String name) {
        Network n = null;
        try {
            FileInputStream file = new FileInputStream("./network-saved-" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(file));
            n = (Network) in.readObject();
            in.close();
            file.close();
            
        } catch(IOException | ClassNotFoundException e) {
                
        }
        return n;
    }
}

