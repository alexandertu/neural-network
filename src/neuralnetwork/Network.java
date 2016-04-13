package neuralnetwork;

import java.util.ArrayList;
import java.util.Random;

public class Network implements java.io.Serializable {
    
    public int[] sizes;
    int num_layers;    
    double learningRate;

    ArrayList<double[]> trainingSetInput = new ArrayList();
    ArrayList<double[]> trainingSetOutput = new ArrayList();   
    
    public ArrayList<double[]> validationSetInput = new ArrayList();
    public ArrayList<double[]> validationSetOutput = new ArrayList();   
   
    public ArrayList<double[]> testSetInput = new ArrayList();
    public ArrayList<double[]> testSetOutput = new ArrayList();   

    public ArrayList<ArrayList<ArrayList<Double>>> weight = new ArrayList(); //layer, node, prevLayerNode
    public ArrayList<ArrayList<Double>> bias = new ArrayList(); //layer, node
    
    ArrayList<ArrayList<ArrayList<Double>>> x = new ArrayList(); //layer, node, prevLayerNode
    
    ArrayList<ArrayList<Double>> o = new ArrayList(); //layer, node
    ArrayList<ArrayList<Double>> error = new ArrayList(); //layer, node
    
    Random random = new Random();
       
    Network(int[] sizes){  
        this.sizes = sizes;
        num_layers = sizes.length;
    }
    
    public void addTrainingData(double[] input, double[] output) {
        trainingSetInput.add(input);       
        trainingSetOutput.add(output);
    }
    
    public void addValidationData(double[] input, double[] output) {
        validationSetInput.add(input);       
        validationSetOutput.add(output);
    }
    
    public void addTestData(double[] input, double[] output) {
        testSetInput.add(input);       
        testSetOutput.add(output);
    }

    private void feedforward(int d, ArrayList<double[]> Input) {
        
        for(int node = 0; node < sizes[0]; node++) {
            o.get(0).set(node, Input.get(d)[node]);           
        }
                
        for(int layer = 1; layer < num_layers; layer++) {            
            //Set output
            for(int node = 0; node < sizes[layer]; node++) {
                o.get(layer).set(node, sigmoid(net(layer, node)));
            }   
        }    
    }
    
    public double sigmoid(double net) {
        return 1/(1+ Math.exp(-net));
    }
    
    private double net(int layer, int node) {
        double sum = bias.get(layer).get(node);
         
        for(int prevLayerNode = 0; prevLayerNode < sizes[layer-1]; prevLayerNode++) {
            sum += o.get(layer-1).get(prevLayerNode) * weight.get(layer).get(node).get(prevLayerNode);
        }
       
        return sum;
    }

    public void train(int maxEpoches, double learningRate) {
        this.learningRate = learningRate;

        initNetwork();                      
        int epoch = 0;
             
        System.out.println(getAccuracy(validationSetInput, validationSetOutput) +
        "/" + validationSetInput.size() + " ");
        
        while(epoch++ < maxEpoches) {
            backpropagate();
            
            System.out.println(getAccuracy(validationSetInput, validationSetOutput) +
            "/" + validationSetInput.size());
        }       
    }
    
    private void backpropagate() {

        int sizeMini = 1;
        for(int dd = 0; dd < trainingSetInput.size()/sizeMini; dd++) {   

            for(int mini = 0; mini < sizeMini; mini++) {
                int d = sizeMini*dd + mini;
                
                //1. Propagate the input forward through the network
                feedforward(d, trainingSetInput);

                //2. Calculate error term for each output neuron in final layer
                for(int node = 0; node < sizes[num_layers-1]; node++) {
                    double t = trainingSetOutput.get(d)[node];
                    double output = o.get(num_layers-1).get(node);
                    error.get(num_layers-1).set(node, error.get(num_layers-1).get(node) + output * (1 - output) * (t - output));
                }

                //3. Calculate error term for each hidden neuron
                for(int layer = num_layers -2; layer > 0; layer--) {

                    for(int node = 0; node < sizes[layer]; node++) {
                        double sum = 0; 

                        for(int nextLayerNode = 0; nextLayerNode < sizes[layer+1]; nextLayerNode++) {
                            sum += weight.get(layer+1).get(nextLayerNode).get(node) * error.get(layer+1).get(nextLayerNode);
                        }
                        double output = o.get(layer).get(node);
                        error.get(layer).set(node, error.get(layer).get(node) + output * (1 - output) * sum);
                    }
                }
            }
            //4. Update each weight
            for(int layer = 1; layer < num_layers; layer++) {
                for(int node = 0; node < sizes[layer]; node++) {
                     
                    bias.get(layer).set(node, bias.get(layer).get(node) + learningRate * error.get(layer).get(node));
                    
                    for(int prevLayerNode = 0; prevLayerNode < sizes[layer-1]; prevLayerNode++) {
                        double deltaWeight = learningRate * error.get(layer).get(node) * o.get(layer-1).get(prevLayerNode);
                      
                        weight.get(layer).get(node).set(prevLayerNode, weight.get(layer).get(node).get(prevLayerNode) + deltaWeight);
                    } 
                    error.get(layer).set(node, 0.0);
                }
            }
        }
    }
    
    //[-0.5, 0.5]
    private double getRandomWeight() {
        return (-0.5 + random.nextDouble());
    }
    
    public void initNetwork() {
        weight = new ArrayList(); //layer, node, prevLayerNode
        bias = new ArrayList(); //layer, node
        x = new ArrayList(); //layer, node, prevLayerNode
        o = new ArrayList(); //layer, node
        error = new ArrayList();
        
        //layer 0
        weight.add(new ArrayList());
        x.add(new ArrayList());   
       
               
        for(int layer = 1; layer < num_layers; layer++) {
            ArrayList<ArrayList<Double>> initW = new ArrayList();
            ArrayList<ArrayList<Double>> initX = new ArrayList();
            
            for(int node = 0; node < sizes[layer]; node++) {
                            
                ArrayList<Double> initWLayer = new ArrayList();
                ArrayList<Double> initXLayer = new ArrayList();
                
                for(int prevLayerNode = 0; prevLayerNode < sizes[layer-1]; prevLayerNode++) {
                    initWLayer.add(getRandomWeight());
                    initXLayer.add(0.0);
                    
                }
                initW.add(initWLayer);
                initX.add(initXLayer);
            }  
            
            weight.add(initW);
            x.add(initX);

        }
        
        for(int layer = 0; layer < num_layers; layer++) {
            ArrayList<Double> initO = new ArrayList();
            ArrayList<Double> initE = new ArrayList();
            ArrayList<Double> initB = new ArrayList();
            for(int node = 0; node < sizes[layer]; node++) {
                initO.add(0.0);
                initE.add(0.0);
                initB.add(getRandomWeight());
            }
            o.add(initO);
            error.add(initE);
            bias.add(initB);
        }

    } 
    
    public int getAccuracy(ArrayList<double[]> input, ArrayList<double[]> output) {
        int correct = 0;
        for(int d = 0; d < input.size(); d++) {
            feedforward(d, input);
            double best = 0.0;
            int number = 0;
            
            for(int node = 0; node < 10; node++) {
                if(o.get(num_layers-1).get(node) > best){
                    best = o.get(num_layers-1).get(node);
                    number = node;
                }     
            }
            if(output.get(d)[number] == 1.0)
                correct++;
        }
        return correct;
    }
      
    
    public double leastMeanSquareError(ArrayList<double[]> inputSet, ArrayList<double[]> outputSet) {
        double sum = 0;
        
        for(int d = 0; d < inputSet.size(); d++) {
            feedforward(d, inputSet);
            
            for(int node = 0; node < sizes[num_layers-1]; node++) {
                double output = o.get(num_layers-1).get(node);
                double target = outputSet.get(d)[node];
                
                sum += (target - output) * (target - output);
            }          
        }
        return 0.5 * sum/inputSet.size();
    }    
}