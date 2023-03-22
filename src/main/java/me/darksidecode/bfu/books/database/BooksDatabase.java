package me.darksidecode.bfu.books.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.sql.Connection;

public class BooksDatabase {

    private final HikariDataSource dataSource;

    private final WritersRepository writersRepository = new WritersRepository(this);
    private final TextsRepository textsRepository = new TextsRepository(this);
    private final GenresRepository genresRepository = new GenresRepository(this);
    private final CountriesRepository countriesRepository = new CountriesRepository(this);
    private final KeeperTypesRepository keeperTypesRepository = new KeeperTypesRepository(this);
    private final PrizesRepository prizesRepository = new PrizesRepository(this);

    public BooksDatabase() {
        System.out.println("db: setting up");
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/bfu_db_lab");
        config.setUsername("root");
        config.setPassword("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public Connection getConnection() {
        System.out.println("db: connecting");
        return dataSource.getConnection();
    }

    public WritersRepository writers() {
        return writersRepository;
    }

    public TextsRepository texts() {
        return textsRepository;
    }

    public GenresRepository genres() {
        return genresRepository;
    }

    public CountriesRepository countries() {
        return countriesRepository;
    }

    public KeeperTypesRepository keeperTypes() {
        return keeperTypesRepository;
    }

    public PrizesRepository prizes() {
        return prizesRepository;
    }

}
