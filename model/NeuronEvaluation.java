package model;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface NeuronEvaluation {
    @Nullable Byte evaluate(Collection<Byte> input) throws Neuron.EvaluationException;
}