# neural-network
Neural network implemented in Java for academic purposes. The implemented artificial neural network
achieved an error rate of 3.4% on the MNIST dataset.

#MNIST database
Link to MNIST dataset: http://yann.lecun.com/exdb/mnist.
The files should be placed in the same directory.

#Code example XOR
Here is an example on the XOR problem.

```java

package neuralnetwork;

public class Example {
    
    public static void main(String[] args) {
        
        //Creating a new feed-forward neural network with 3 layers
        Network n = new Network(new int[] {2,2,1});
        
        //Add training data
        n.addTrainingData(new double[] {0,0}, new double[] {0});
        n.addTrainingData(new double[] {1,0}, new double[] {1});
        n.addTrainingData(new double[] {0,1}, new double[] {1});
        n.addTrainingData(new double[] {1,1}, new double[] {0});
      
        //Train network for 500 epoches, 0.3 learning rate
        n.train(500,0.3);
    } 
}

```
