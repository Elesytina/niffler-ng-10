package guru.qa.niffler.model;

import guru.qa.niffler.condition.Color;

import java.util.Objects;


public record Bubble(Color color, String text) {

    @Override
    public String toString() {
        return "Bubble{" + color.getRgba() + ", " + text + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bubble bubble = (Bubble) o;
        return Objects.equals(color, bubble.color) && Objects.equals(text, bubble.text);
    }
}
