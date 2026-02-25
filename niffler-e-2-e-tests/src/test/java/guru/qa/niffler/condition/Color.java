package guru.qa.niffler.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {

    skyBlue("rgba(99, 181, 226, 1)"),
    green("rgba(53, 173, 123, 1)"),
    yellow("rgba(255, 183, 3, 1)"),
    orange("rgba(251, 133, 0, 1)"),
    red("rgba(247, 89, 67, 1)"),
    black("rgba(36, 37, 39, 1)");

    private final String rgba;

    public static Color getColor(String rgba) {
        for (Color c : Color.values()) {
            if (c.rgba.equals(rgba)) {
                return c;
            }
        }
        throw new IllegalArgumentException("There is no such a color: " + rgba);
    }
}