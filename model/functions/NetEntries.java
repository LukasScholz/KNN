package model.functions;

import model.Neurons.Neuron;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class NetEntries {

    public static class sum implements NetEntry {
        public @Nullable Double evaluate(Collection<Double> input) throws Neuron.EvaluationException {
            AtomicReference<Double> result = new AtomicReference<>(0d);
            input.forEach(value -> result.updateAndGet(v -> v + value));
            return result.get();
        }
    }
    public static class andGate implements NetEntry {
        public Double evaluate(Collection<Double> input) throws Neuron.EvaluationException {
            if (input.contains(null)) return null;
            return (input.stream().allMatch(value -> value==1d)) ? 1d : 0d;
        }
    }

    public static class NotGate implements NetEntry {
        public Double evaluate(Collection<Double> input) throws Neuron.EvaluationException {
            if (input.contains(null)) return null;
            return (input.stream().allMatch(value -> value==0d)) ? 1d : 0d;
        }
    }

    public static class allEqual implements NetEntry {
        public Double evaluate(Collection<Double> input) throws Neuron.EvaluationException {
            Double val = input.iterator().next();
            return (input.stream().allMatch(value -> Objects.equals(value, val))) ? 1d : 0d;
        }
    }

    public static class orGate implements NetEntry {
        public Double evaluate(Collection<Double> input) throws Neuron.EvaluationException {
            if (input.contains(null)) return null;
            return (input.stream().allMatch(value -> value==0d)) ? 0d : 1d;
        }
    }
    public static class average implements NetEntry {
        public Double evaluate(Collection<Double> input) throws Neuron.EvaluationException {
            AtomicReference<Double> result = new AtomicReference<>(0d);
            input.forEach(value -> result.updateAndGet(v -> v + value));
            return (result.get() / input.size());
        }
    }

}
