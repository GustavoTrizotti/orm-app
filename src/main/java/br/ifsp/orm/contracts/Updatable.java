package br.ifsp.orm.contracts;

import java.sql.SQLException;

public interface Updatable<T> {
    void update(T t) throws SQLException;
}
