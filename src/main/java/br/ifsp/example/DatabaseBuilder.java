package br.ifsp.example;

import br.ifsp.orm.ConnectionFactory;
import br.ifsp.orm.annotations.Column;
import br.ifsp.orm.annotations.OrmEntity;
import br.ifsp.orm.mappers.TypeMapper;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseBuilder {
    private static final String ENTITIES_PACKAGE = "br.ifsp.example";

    public static void main(String[] args) {
        DatabaseBuilder.createTableFromOrmEntities();
    }

    private static void createTableFromOrmEntities() {
        String packageName = DatabaseBuilder.class.getPackageName();
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> ormEntities = reflections.getTypesAnnotatedWith(OrmEntity.class);
        ormEntities.stream()
                .map(DatabaseBuilder::generateTable)
                .forEach(DatabaseBuilder::createTable);
    }

    private static String generateTable(Class<?> type) {
        final String tableName = type.getSimpleName().toUpperCase();
        final OrmEntity annotation = type.getAnnotation(OrmEntity.class);

        final Field[] fields = type.getDeclaredFields();

        String columns = Arrays.stream(fields)
                .map(f -> generateColumn(f, annotation.value()))
                .collect(Collectors.joining(",\n"));

        return String.format("CREATE TABLE IF NOT EXISTS %s (\n%s\n)", tableName, columns);
    }

    private static String generateColumn(Field field, OrmEntity.SGBD sgbd) {
        String columnTemplate = "\t%s %s";
        String mappedType = TypeMapper.fromSgbd(field.getType(), sgbd);

        if (!field.isAnnotationPresent(Column.class))
            return columnTemplate.formatted(field.getName(), mappedType);

        String columnName = field.getAnnotation(Column.class).value();
        return columnTemplate.formatted(columnName, mappedType);
    }

    private static void createTable(String tableSql) {
        try {
            final var stmt = ConnectionFactory.getStatement();
            stmt.execute(tableSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
