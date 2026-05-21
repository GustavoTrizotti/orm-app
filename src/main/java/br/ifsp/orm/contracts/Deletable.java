package br.ifsp.orm.contracts;

import java.sql.SQLException;

public interface Deletable<K> {
    void deleteByKey(K key) throws SQLException;
}
