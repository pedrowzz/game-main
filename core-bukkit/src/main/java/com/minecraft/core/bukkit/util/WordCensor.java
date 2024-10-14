package com.minecraft.core.bukkit.util;

import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.regex.Pattern;

@Getter
public class WordCensor {

    private Pattern censorPattern;
    private final String replacement;

    public WordCensor(String replacement) {
        Validate.isTrue(!replacement.isEmpty(), "Word Censor replacement can't be empty.");
        this.replacement = replacement;
    }

    public WordCensor addCensure(String... words) {
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (sb.length() > 0) sb.append("|");
            sb.append(String.format("(?i)(?<=(?=%s).{0,%d}).", Pattern.quote(w), w.length() - 1));
        }
        this.censorPattern = Pattern.compile(sb.toString());
        return this;
    }

    public String filter(String msg) {
        return censorPattern.matcher(msg).replaceAll(replacement);
    }
}
