package guru.qa.niffler.model;

import guru.qa.niffler.condition.Color;
import org.jetbrains.annotations.NotNull;


public record Bubble(Color color, String text) implements Comparable<Bubble> {

    @Override
    public int compareTo(@NotNull Bubble o) {
        int enumCompare = this.color.compareTo(o.color);
        if (enumCompare != 0) {
            return enumCompare;
        }

        return this.text.compareTo(o.text);
    }
}
