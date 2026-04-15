package com.coolerpromc.arrowplus.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FabricArrowPlusConfig {

    public static FabricArrowPlusConfig CONFIG;
    public static CommentedFileConfig CONFIG_SPEC;

    private final List<String> removal;

    private static final String KEY_RESTRICTIONS = "Restrictions.restrictions";
    private static final String COMMENT_RESTRICTIONS = "A list of arrows to be disabled. Example: ['diamond', 'iron']";

    private FabricArrowPlusConfig(List<String> removal) {
        this.removal = removal;
    }

    public List<String> getRemoval() {
        return removal;
    }

    public static void load(Path path) {
        CONFIG_SPEC = CommentedFileConfig.builder(path).autosave().sync().preserveInsertionOrder().build();
        CONFIG_SPEC.load();
        CONFIG_SPEC.setComment(KEY_RESTRICTIONS, COMMENT_RESTRICTIONS);
        List<?> raw = CONFIG_SPEC.getOrElse(KEY_RESTRICTIONS, List.of());
        List<String> list = raw.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
        CONFIG_SPEC.set(KEY_RESTRICTIONS, list);
        CONFIG = new FabricArrowPlusConfig(list);
        CONFIG_SPEC.save();

        ArrowPlusConfig.sync(list);
    }
}