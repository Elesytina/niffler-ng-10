package guru.qa.niffler.data.tpl;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JdbcConnectionHolders implements AutoCloseable {

    private final List<JdbcConnectionHolder> holders;

    @Override
    public void close() {
        holders.forEach(JdbcConnectionHolder::close);
    }
}
