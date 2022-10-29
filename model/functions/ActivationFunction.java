package model.functions;

import org.jetbrains.annotations.Nullable;

public interface ActivationFunction {
    @Nullable Double evaluate(Double activationFactor, Double netEntry, Double external);
}
