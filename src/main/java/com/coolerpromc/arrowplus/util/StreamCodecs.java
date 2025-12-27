package com.coolerpromc.arrowplus.util;

import com.mojang.datafixers.util.Function10;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class StreamCodecs {
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> p_425768_, final Function<C, T1> p_426137_, final StreamCodec<? super B, T2> p_425680_, final Function<C, T2> p_425767_, final StreamCodec<? super B, T3> p_425700_, final Function<C, T3> p_426226_, final StreamCodec<? super B, T4> p_425792_, final Function<C, T4> p_426330_, final StreamCodec<? super B, T5> p_426249_, final Function<C, T5> p_425789_, final StreamCodec<? super B, T6> p_426248_, final Function<C, T6> p_425689_, final StreamCodec<? super B, T7> p_425685_, final Function<C, T7> p_425579_, final StreamCodec<? super B, T8> p_426135_, final Function<C, T8> p_425766_, final StreamCodec<? super B, T9> p_425708_, final Function<C, T9> p_425801_, final StreamCodec<? super B, T10> p_425715_, final Function<C, T10> p_425687_, final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> p_426154_) {
        return new StreamCodec<B, C>() {
            public C decode(B p_397196_) {
                T1 t1 = (T1)p_425768_.decode(p_397196_);
                T2 t2 = (T2)p_425680_.decode(p_397196_);
                T3 t3 = (T3)p_425700_.decode(p_397196_);
                T4 t4 = (T4)p_425792_.decode(p_397196_);
                T5 t5 = (T5)p_426249_.decode(p_397196_);
                T6 t6 = (T6)p_426248_.decode(p_397196_);
                T7 t7 = (T7)p_425685_.decode(p_397196_);
                T8 t8 = (T8)p_426135_.decode(p_397196_);
                T9 t9 = (T9)p_425708_.decode(p_397196_);
                T10 t10 = (T10)p_425715_.decode(p_397196_);
                return (C)p_426154_.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            public void encode(B p_397968_, C p_397982_) {
                p_425768_.encode(p_397968_, p_426137_.apply(p_397982_));
                p_425680_.encode(p_397968_, p_425767_.apply(p_397982_));
                p_425700_.encode(p_397968_, p_426226_.apply(p_397982_));
                p_425792_.encode(p_397968_, p_426330_.apply(p_397982_));
                p_426249_.encode(p_397968_, p_425789_.apply(p_397982_));
                p_426248_.encode(p_397968_, p_425689_.apply(p_397982_));
                p_425685_.encode(p_397968_, p_425579_.apply(p_397982_));
                p_426135_.encode(p_397968_, p_425766_.apply(p_397982_));
                p_425708_.encode(p_397968_, p_425801_.apply(p_397982_));
                p_425715_.encode(p_397968_, p_425687_.apply(p_397982_));
            }
        };
    }
}