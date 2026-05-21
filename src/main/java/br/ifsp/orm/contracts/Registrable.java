package br.ifsp.orm.contracts;

import java.sql.SQLException;

public interface Registrable <T>{
    void save(T t) throws SQLException;
}
