package model.Neurons;

import model.Network;
import model.functions.ActivationFunction;
import model.functions.NetEntry;
import model.functions.OutgoingFunction;

public class AusgabeNeuron extends Neuron{
    public AusgabeNeuron(Network network, NetEntry netEntry, ActivationFunction activationFunction, OutgoingFunction outgoingFunction, Double externalFactor, Double activationFactor) {
        super(network, netEntry, activationFunction, outgoingFunction, externalFactor, activationFactor);
    }
    @Override
    void calculate() throws EvaluationException{
        Double result = outgoingFunction.evaluate(activationFunction.evaluate(activationFactor, netEntry.evaluate(Eingabe.values()), externalFactor));
        if(result != null) {
            this.Ausgabe = result;
        }
        this.network.allValues.put(this, result);
    }
}
