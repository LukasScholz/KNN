package model.Neurons;

import model.Network;
import model.functions.ActivationFunction;
import model.functions.NetEntry;
import model.functions.OutgoingFunction;

import java.util.HashMap;
import java.util.Set;

public class Neuron implements Runnable{
    public boolean active;
    Network network;
    public boolean destructed;
    public HashMap<Neuron, Double> nextNeurons;
    public NetEntry netEntry;
    public HashMap<Neuron, Double> Eingabe;
    public Double Ausgabe;
    public Double externalFactor;
    public ActivationFunction activationFunction;
    public OutgoingFunction outgoingFunction;
    public Double activationFactor;

    public Neuron(Network network, NetEntry netEntry, ActivationFunction activationFunction, OutgoingFunction outgoingFunction, Double externalFactor, Double activationFactor) {
        this.active = false;
        this.destructed = false;
        this.netEntry = netEntry;
        this.outgoingFunction = outgoingFunction;
        this.activationFunction = activationFunction;
        this.externalFactor = externalFactor;
        this.Eingabe = new HashMap<>();
        this.activationFactor = activationFactor;
        this.Ausgabe = 0d;
        this.network = network;
    }

    Neuron() {}

    public void activate() {this.active = true;}
    public void deactivate() {this.active = false;}


    @Override
    public void run() {
        try {
            if (!(this.destructed) && this.active) calculate();
        }
        catch (EvaluationException ignored) {}
        finally {network.reduceRemaindingNeurons();}
    }


    void calculate() throws EvaluationException{
        Double result = outgoingFunction.evaluate(activationFunction.evaluate(activationFactor, netEntry.evaluate(Eingabe.values()), externalFactor));
        if(result != null) {
            this.Ausgabe = result;
            nextNeurons.forEach((neuron, weight) -> neuron.Eingabe.put(this, Ausgabe*weight));
        }
        this.network.allValues.put(this, result);
    }

    public static class EvaluationException extends RuntimeException {
        public EvaluationException(String message) {
            super(message);
        }

    }
}
