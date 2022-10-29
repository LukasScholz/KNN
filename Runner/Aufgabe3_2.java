package Runner;

import model.Network;
import model.Neurons.EingabeNeuron;
import model.Neurons.Neuron;
import model.functions.ActivationFunctions;
import model.functions.NetEntries;
import model.functions.OutgoingFunctions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Aufgabe3_2 {
    public static void main(String[] args) throws InterruptedException {
        Network network = new Network(1, 1, 3, System.out, true);
        // create Neurons
        Neuron endingNeuron = network.addEndingNeuron(in -> new NetEntries.allEqual().evaluate(in), (in1, in2, in3) -> new ActivationFunctions.identityNetEntry().evaluate(in1, in2, in3),
                in -> new OutgoingFunctions.toBoolean().evaluate(in), 0d, 0d);
        Neuron middleNeuron = network.addInnerNeuron(new HashMap<>() {{put(endingNeuron, 1d);}}, in -> new NetEntries.sum().evaluate(in), (in1, in2, in3) -> new ActivationFunctions.identityNetEntry().evaluate(in1, in2, in3),
                in -> new OutgoingFunctions.identity().evaluate(in), 0d, 0d, 1);
        EingabeNeuron startingNeuron = network.addStartingNeuron(1, new HashMap<>() {{put(middleNeuron, 1d); put(endingNeuron, 1d);}}, in -> new NetEntries.andGate().evaluate(in), (in1, in2, in3) -> new ActivationFunctions.identityNetEntry().evaluate(in1, in2, in3),
                in -> new OutgoingFunctions.toBoolean().evaluate(in), 0d, 0d);

        char[] chars = args[0].toCharArray();
        network.activate();
        Queue<Double> signal = new LinkedList<>();
        for(char chr : chars) {
            Double val = Double.parseDouble(String.valueOf(chr));
            signal.offer(val);
        }
        network.sendSignals(startingNeuron, 0, signal);
        network.run();
    }
}
