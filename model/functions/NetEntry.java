package model.functions;

import model.Neurons.Neuron;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface NetEntry {
    @Nullable Double evaluate(Collection<Double> input) throws Neuron.EvaluationException;
}