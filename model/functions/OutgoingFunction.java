package model.functions;

import org.jetbrains.annotations.Nullable;

public interface OutgoingFunction {
    @Nullable Double evaluate(Double signal);
}
