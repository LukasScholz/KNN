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

public class Aufgabe3_4 {
    public static void main(String[] args) throws InterruptedException {
        int n = Integer.parseInt(args[1])*2;
        Network network = new Network(1, 1, n+1, System.out, true);
        // create Neurons
        Neuron endingNeuron = network.addEndingNeuron(in -> new NetEntries.sum().evaluate(in), (in1, in2, in3) -> new ActivationFunctions.identityNetEntry().evaluate(in1, in2, in3),
                in -> new OutgoingFunctions.isZero().evaluate(in), 0d, 0d);
        Neuron lastNeuron = endingNeuron;
        for(int i = 0; i < n-1; i++) {
            HashMap<Neuron, Double> targets = new HashMap<>();
            targets.put(lastNeuron, 1d);
            targets.put(endingNeuron, weight(i,n));
            lastNeuron = network.addInnerNeuron(targets, in -> new NetEntries.average().evaluate(in), (in1, in2, in3) -> new ActivationFunctions.identityNetEntry().evaluate(in1, in2, in3),
                    in -> new OutgoingFunctions.identity().evaluate(in), 0d, 0d, n-i-1);
        }
        HashMap<Neuron, Double> targets = new HashMap<>();
        targets.put(endingNeuron, weight(n-1, n));
        targets.put(lastNeuron, 1d);
        EingabeNeuron startingNeuron = network.addStartingNeuron(1, targets, in -> new NetEntries.andGate().evaluate(in), (in1, in2, in3) -> new ActivationFunctions.identityNetEntry().evaluate(in1, in2, in3),
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

    private static Double weight(int i, int n) {
        double result = -(n/2) + i;
        return result;
    }

}
