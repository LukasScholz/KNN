package model.functions;


public class ActivationFunctions {
    public static class identityNetEntry implements ActivationFunction {
        public Double evaluate(Double activationFactor, Double netEntry, Double external) {
            return netEntry;
        }
    }
    public static class toBooleanNetEntry implements ActivationFunction {
        public Double evaluate(Double activationFactor, Double netEntry, Double external) {
            return (netEntry >= 0.5) ? 1d : 0d;
        }
    }
}
