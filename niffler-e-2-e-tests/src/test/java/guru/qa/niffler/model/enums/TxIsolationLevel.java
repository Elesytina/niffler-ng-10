package guru.qa.niffler.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TxIsolationLevel {
    NONE(0),
    READ_UNCOMMITTED(1),
    READ_COMMITED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    private final int level;
}
