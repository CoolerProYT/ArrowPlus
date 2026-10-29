package com.coolerpromc.arrowplus.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JavaOps;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrowPhysicsTest {
    @Test
    void rejectsNonFiniteNumbersOnReadAndWrite() {
        for (Codec<Double> codec : List.of(ArrowPhysics.GRAVITY, ArrowPhysics.VELOCITY, ArrowPhysics.DRAG, ArrowPhysics.ACCURACY, ArrowPhysics.GRAVITY_MULTIPLIER)){
            for (double value : new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}){
                assertTrue(codec.parse(JavaOps.INSTANCE, value).isError());
                assertTrue(codec.encodeStart(JavaOps.INSTANCE, value).isError());
            }
        }
    }

    @Test
    void rejectsAcceleratingDragAndInvalidLaunchSpeed() {
        assertTrue(ArrowPhysics.DRAG.parse(JavaOps.INSTANCE, 1.01).isError());
        assertTrue(ArrowPhysics.DRAG.parse(JavaOps.INSTANCE, -0.01).isError());
        assertTrue(ArrowPhysics.VELOCITY.parse(JavaOps.INSTANCE, 0.0).isError());
        assertTrue(ArrowPhysics.VELOCITY.parse(JavaOps.INSTANCE, 4.01).isError());
        assertTrue(ArrowPhysics.GRAVITY.parse(JavaOps.INSTANCE, -0.01).isError());
        assertTrue(ArrowPhysics.ACCURACY.parse(JavaOps.INSTANCE, -1.0).isError());
        assertTrue(ArrowPhysics.GRAVITY_MULTIPLIER.parse(JavaOps.INSTANCE, 4.01).isError());
    }

    @Test
    void acceptsVanillaStatsAndDeliberateZeroGravityOrSpread() {
        assertEquals(0.99, ArrowPhysics.DRAG.parse(JavaOps.INSTANCE, 0.99).getOrThrow());
        assertEquals(0.05, ArrowPhysics.GRAVITY.parse(JavaOps.INSTANCE, 0.05).getOrThrow());
        assertEquals(1.0, ArrowPhysics.VELOCITY.parse(JavaOps.INSTANCE, 1.0).getOrThrow());
        assertEquals(0.0, ArrowPhysics.GRAVITY_MULTIPLIER.parse(JavaOps.INSTANCE, 0.0).getOrThrow());
        assertEquals(0.0, ArrowPhysics.ACCURACY.parse(JavaOps.INSTANCE, 0.0).getOrThrow());
    }
}
