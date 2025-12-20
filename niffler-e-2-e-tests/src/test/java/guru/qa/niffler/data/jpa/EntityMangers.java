package guru.qa.niffler.data.jpa;

import guru.qa.niffler.data.tpl.DataSources;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityMangers {

    private final static Map<String, EntityManagerFactory> ems = new ConcurrentHashMap<>();

    public static EntityManager em(String jdbcUrl) {

        return ems.computeIfAbsent(jdbcUrl,
                key -> {
                    DataSources.getDataSource(jdbcUrl);
                    return Persistence.createEntityManagerFactory(jdbcUrl);
                }).createEntityManager();
    }
}
