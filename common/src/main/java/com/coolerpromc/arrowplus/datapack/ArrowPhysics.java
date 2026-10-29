package com.coolerpromc.arrowplus.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public final class ArrowPhysics {
    public static final Codec<Double> GRAVITY = bounded("gravity", 0, 1);
    public static final Codec<Double> VELOCITY = bounded("velocity", 0.01, 4);
    public static final Codec<Double> DRAG = bounded("drag", 0, 1);
    public static final Codec<Double> ACCURACY = bounded("accuracy", 0, 10);
    public static final Codec<Double> GRAVITY_MULTIPLIER = bounded("gravityMultiplier", 0, 4);

    private ArrowPhysics() {
    }

    private static Codec<Double> bounded(String name, double min, double max) {
        return Codec.DOUBLE.validate(value -> Double.isFinite(value) && value >= min && value <= max
                ? DataResult.success(value)
                : DataResult.error(() -> name + " must be finite and between " + min + " and " + max));
    }
}
