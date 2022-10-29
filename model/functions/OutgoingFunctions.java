package model.functions;

import org.jetbrains.annotations.Nullable;

public class OutgoingFunctions {
    public static class identity implements OutgoingFunction { public Double evaluate(Double signal) {return signal;} }
    public static class toBoolean implements OutgoingFunction {
        public Double evaluate(Double signal) {
            if (signal == null) return null;
            if (signal < 0 || signal > 1) return null;
            return (signal <= 0.5) ? 0d : 1d;}
    }
    public static class isZero implements OutgoingFunction {
        public Double evaluate(Double signal) {
            if (signal == null) return null;
            return (signal == 0d) ? 1d : 0d;}
    }

}
