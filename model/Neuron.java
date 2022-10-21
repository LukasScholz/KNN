package model;

import java.util.HashMap;
import java.util.Set;

public class Neuron<Evaluation extends NeuronEvaluation> implements Runnable{
    public boolean Eingabeneuron;
    public boolean active;
    public boolean destructed;
    public boolean Ausgabeneuron;
    public Set<Neuron<?>> nextNeurons;
    public Evaluation Evaluator;
    public HashMap<Neuron<?>, Byte> Eingabe;
    public Byte Ausgabe;

    public Neuron(boolean Eingabeneuron, boolean Ausgabeneuron, Evaluation Evaluator) {
        this.Ausgabeneuron = Ausgabeneuron;
        this.Eingabeneuron = Eingabeneuron;
        this.active = false;
        this.destructed = false;
        this.Evaluator = Evaluator;
    }

    public void activate() {this.active = true;}
    public void deactivate() {this.active = false;}


    @Override
    public void run() {
        try {
            while (!destructed)
                if (this.active) calculate();
        }
        catch (EvaluationException ignored) {}
    }

    private void calculate() throws EvaluationException{
        Byte result = Evaluator.evaluate(Eingabe.values());
        if(result != null) {
            this.Ausgabe = result;
            nextNeurons.forEach(neuron -> neuron.Eingabe.put(this, Ausgabe));
        }
    }

    public static class EvaluationException extends RuntimeException {
        public EvaluationException(String message) {
            super(message);
        }

    }

}
