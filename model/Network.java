package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Network {
    Set<Neuron<?>> startingNeurons;
    Set<Neuron<?>> innerNeurons;
    Set<Neuron<?>> endingNeurons;

    public void addNeuron(@Nullable Set<Neuron<?>> targets, NeuronEvaluation Evaluator, boolean startingNeuron, boolean endingNeuron) {
        Neuron<?> neuron = new Neuron<>(startingNeuron, endingNeuron, Evaluator);
        if (!endingNeuron) {
            assert targets != null;
            neuron.nextNeurons = targets;
            targets.forEach(target -> target.Eingabe.put(neuron, null));
        }
        else neuron.nextNeurons = new HashSet<>();
        if (startingNeuron) startingNeurons.add(neuron);
        if (endingNeuron) endingNeurons.add(neuron);
        if (!startingNeuron && !endingNeuron) innerNeurons.add(neuron);
    }

    public void sendSignal(@NotNull Neuron<?> startingNeuron, Byte signal) {
        assert startingNeuron.Eingabeneuron;
        startingNeuron.Eingabe.put(null, signal);
    }

    public Byte[] getResult() {
        Byte[] result = new Byte[endingNeurons.size()];
        Object[] neurons = endingNeurons.toArray();
        Arrays.setAll(result, i -> ((Neuron<?>) neurons[i]).Ausgabe);
        return result;
    }

    public void destruct() {
        endingNeurons.forEach(neuron -> neuron.destructed=true);
        innerNeurons.forEach(neuron -> neuron.destructed=true);
        startingNeurons.forEach(neuron -> neuron.destructed=true);
    }

}
