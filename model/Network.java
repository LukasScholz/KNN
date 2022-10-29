package model;

import model.Neurons.AusgabeNeuron;
import model.Neurons.EingabeNeuron;
import model.Neurons.Neuron;
import model.functions.ActivationFunction;
import model.functions.ActivationFunctions;
import model.functions.NetEntry;
import model.functions.OutgoingFunction;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Stream;

public class Network implements Runnable {
    Neuron[] startingNeurons;
    public boolean debug = false;
    public boolean sleeping=false;
    int startingNeuronsPos;
    HashMap<Integer, Set<Neuron>> innerNeurons;
    Neuron[] endingNeurons;
    int endingNeuronsPos;
    Integer maxdepth;
    boolean reverseFeed;
    public boolean endOfInputs;
    private Integer remaindingNeurons;
    PrintStream output;
    public HashMap<Neuron, Double> allValues;

    public Network(int entrypoints, int exitpoints, int depth, PrintStream output, boolean reverseFeed) {
        this.startingNeurons = new Neuron[entrypoints];
        this.endingNeurons = new Neuron[exitpoints];
        this.maxdepth = depth;
        startingNeuronsPos = 0;
        endingNeuronsPos = 0;
        this.endOfInputs = true;
        this.remaindingNeurons = 0;
        this.innerNeurons = new HashMap<>();
        this.output = output;
        this.allValues = new HashMap<>();
        this.reverseFeed = reverseFeed;

        for(int i=0; i<depth; i++) {
            innerNeurons.put(i, new HashSet<>());
        }
    }

    public void reduceRemaindingNeurons() {
        if (this.remaindingNeurons<=0) throw new NetworkException("Neurons ended but not started");
        this.remaindingNeurons--;
    }

    public Neuron addInnerNeuron(@NotNull HashMap<Neuron, Double> targets, NetEntry netEntry, ActivationFunction activationFunction, OutgoingFunction outgoingFunction, Double externalFactor, Double activationFactor, int depth) {
        if (maxdepth <= depth) throw new NetworkException("Neuron depth exceeds Network depth!");
        Neuron neuron = new Neuron(this, netEntry, activationFunction, outgoingFunction, externalFactor, activationFactor);
        neuron.nextNeurons = targets;
        targets.forEach((target, weight) -> target.Eingabe.put(neuron, neuron.Ausgabe));
        innerNeurons.get(depth).add(neuron);
        return neuron;
    }

    public EingabeNeuron addStartingNeuron(Integer inputSignals, @NotNull HashMap<Neuron, Double> targets, NetEntry netEntry, ActivationFunction activationFunction, OutgoingFunction outgoingFunction, Double externalFactor, Double activationFactor) {
        EingabeNeuron neuron = new EingabeNeuron(this, inputSignals, netEntry, activationFunction, outgoingFunction, externalFactor, activationFactor);
        neuron.nextNeurons = targets;
        targets.forEach((target, weight) -> target.Eingabe.put(neuron, neuron.Ausgabe));
        if (startingNeuronsPos == startingNeurons.length) throw new NetworkException("Too many Startingneurons!");
        startingNeurons[startingNeuronsPos] = neuron;
        startingNeuronsPos++;
        return neuron;

    }

    public AusgabeNeuron addEndingNeuron(NetEntry netEntry, ActivationFunction activationFunction, OutgoingFunction outgoingFunction, Double externalFactor, Double activationFactor) {
        AusgabeNeuron neuron = new AusgabeNeuron(this, netEntry, activationFunction, outgoingFunction, externalFactor, activationFactor);
        neuron.nextNeurons = null;
        if (endingNeuronsPos == endingNeurons.length) throw new NetworkException("Too many Endingneurons!");
        endingNeurons[endingNeuronsPos] = neuron;
        endingNeuronsPos++;
        return neuron;
    }

    public void sendSignal(@NotNull EingabeNeuron startingNeuron, Integer signalPos, Double signal) {
        startingNeuron.setSignal(signalPos, signal);
    }
    public void sendSignals(@NotNull EingabeNeuron startingNeuron, Integer signalPos, Queue<Double> signal) {
        startingNeuron.setSignals(signalPos, signal);
    }

    public Double[] getResult() {
        Double[] result = new Double[endingNeurons.length];
        Arrays.setAll(result, i -> (endingNeurons[i]).Ausgabe);
        return result;
    }

    public void destruct() {
        Arrays.stream(endingNeurons).forEach(neuron -> neuron.destructed=true);
        innerNeurons.values().forEach(neuronSet -> neuronSet.forEach(neuron -> neuron.destructed=true));
        Arrays.stream(startingNeurons).forEach(neuron -> neuron.destructed=true);
    }
    public void activate() {
        Arrays.stream(endingNeurons).forEach(Neuron::activate);
        innerNeurons.values().forEach(neuronSet -> neuronSet.forEach(Neuron::activate));
        Arrays.stream(startingNeurons).forEach(Neuron::activate);
    }
    public void deactivate() {
        Arrays.stream(endingNeurons).forEach(Neuron::deactivate);
        innerNeurons.values().forEach(neuronSet -> neuronSet.forEach(Neuron::activate));
        Arrays.stream(startingNeurons).forEach(Neuron::deactivate);
    }

    @Override
    public void run() {
        Queue<String> results = new LinkedList();
        while (true) {
            endOfInputs = true;
            Map<Neuron, Double> oldResults = Collections.unmodifiableMap(allValues);
            for (int i = maxdepth - 1; i >= 0; i--) {
                int currentdepth = i;
                if (!reverseFeed) currentdepth = (maxdepth - i-1);
                Object[] neurons;
                if (currentdepth == 0) neurons = startingNeurons;
                else if (currentdepth == maxdepth - 1) neurons = endingNeurons;
                else neurons = innerNeurons.get(currentdepth).toArray();
                this.remaindingNeurons += neurons.length;
                // feed forward network
                for (Object neuron : neurons) {
                    Neuron target = (Neuron) neuron;
                    Thread thread = new Thread(target, target.toString());
                    thread.start();
                }
                // waiting for neurons to finish
                while (remaindingNeurons > 0) sleep(10);
            }
            results.add(Arrays.toString(this.getResult()));
            if(debug) output.println((Collections.unmodifiableMap(allValues)));
            if(endOfInputs && oldResults.equals(Collections.unmodifiableMap(allValues))) break;
        }
        output.println(results.toString());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static class NetworkException extends RuntimeException {
        public NetworkException(String message) {
            super(message);
        }

    }

}
