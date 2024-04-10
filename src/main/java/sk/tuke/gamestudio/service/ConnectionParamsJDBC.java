package sk.tuke.gamestudio.service;

public interface ConnectionParamsJDBC {
    /** JDBC URL for connecting to the database. */
    String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";

    /** Username for JDBC connection. */
    String JDBC_USER = "postgres";

    /** Password for JDBC connection. */
    String JDBC_PASSWORD = "iiii";
}