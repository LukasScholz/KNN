package model.Neurons;

import model.Network;
import model.functions.ActivationFunction;
import model.functions.NetEntry;
import model.functions.OutgoingFunction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;

public class EingabeNeuron extends Neuron {
    public final Neuron[] inputs;
    public final HashMap<Integer, Queue<Double>> signals;

    public EingabeNeuron(Network network, Integer inputSignals, NetEntry netEntry, ActivationFunction activationFunction, OutgoingFunction outgoingFunction, Double externalFactor, Double activatioFactor) {
        super(network, netEntry, activationFunction, outgoingFunction, externalFactor, activatioFactor);
        this.inputs = new Neuron[inputSignals];
        this.signals = new HashMap<>();
        for(int i=0; i<inputSignals; i++) {
            this.signals.put(i, null);
            Neuron input = new Neuron();
            this.Eingabe.put(input, null);
            inputs[i] = input;
        }
    }

    public void setSignals(@NotNull Integer signalPos, Queue<Double> signals) {
        assert signalPos < inputs.length;
        this.signals.put(signalPos, signals);
    }
    public void setSignal(@NotNull Integer signalPos, Double signal) {
        assert signalPos < inputs.length;
        inputs[signalPos].Ausgabe=signal;
        this.Eingabe.put(inputs[signalPos], signal);
    }

    @Override
    void calculate() throws EvaluationException{
        boolean end = true;
        for(int i=0; i<inputs.length; i++) {
            if (this.signals.get(i) != null) {
                end = false;
                Double input =this.signals.get(i).poll();
                inputs[i].Ausgabe=input;
                this.Eingabe.put(inputs[i], input);
                if (this.signals.get(i).isEmpty()) this.signals.put(i, null);
            }
            if (network.endOfInputs && !end) network.endOfInputs = false;
        }
        Double result = outgoingFunction.evaluate(activationFunction.evaluate(activationFactor, netEntry.evaluate(Eingabe.values()), externalFactor));
        if(result != null) {
            this.Ausgabe = result;
            nextNeurons.forEach((neuron, weight) -> neuron.Eingabe.put(this, Ausgabe*weight));
        }
        this.network.allValues.put(this, result);
    }
}
