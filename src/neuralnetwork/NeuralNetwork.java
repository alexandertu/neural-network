package neuralnetwork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NeuralNetwork {   
    public static void main(String[] args) {

        int setup_HU = 30;
        double setup_eta = 0.35;    
        int setup_epoches = 8;       
        int setup_trainingSetSize = 500;
        
        System.out.println("Loading images... ");
        Network n = new Network(new int[] {784,setup_HU,10});
                    
        MNISTReader reader = new MNISTReader("train");
        ArrayList<DigitImage> digitImages = reader.getImages();
        System.out.println(digitImages.size());
        
        int index = setup_trainingSetSize;
        ArrayList<DigitImage> validationDigitImages = new ArrayList<>(digitImages.subList(index, index +10000));
        
        for(int d = 0; d < index; d++) {
            double[] target = reader.getTargetVector(digitImages.get(d).label);
            n.addTrainingData(digitImages.get(d).pixels, target);
        }       
        for(int d = 0; d < validationDigitImages.size(); d++) {
            double[] target = reader.getTargetVector(validationDigitImages.get(d).label);
            n.addValidationData(validationDigitImages.get(d).pixels, target);
        }
        
        System.out.println("Training... ");
        
        MNISTReader readerTestSet = new MNISTReader("t10k");
        ArrayList<DigitImage> testSet = readerTestSet.getImages();
        
        for(int d = 0; d < testSet.size(); d++) {
            double[] target = readerTestSet.getTargetVector(testSet.get(d).label);
            n.addTestData(testSet.get(d).pixels, target);                      
        }
        
        n.train(setup_epoches, setup_eta);
        
        double accuracy  = n.getAccuracy(n.testSetInput, n.testSetOutput);
        System.out.println("Accuracy test data: " + accuracy + "/ " + n.testSetInput.size() + " => " +
                accuracy/n.testSetInput.size());

        System.out.println("Saving weights..");
        NetworkStorage storage = new NetworkStorage();   
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-DD-HH-SS");
        Date date = new Date();
        
        storage.saveWeights(n, "tew_" + setup_HU + "hu_" + setup_eta + "eta_" + setup_epoches + 
                "epoches_" + setup_trainingSetSize + "set_" + dateFormat.format(date));

    }
}
