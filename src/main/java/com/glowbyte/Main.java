package com.glowbyte;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.*;
import com.glowbyte.connection.SimpleConnectionFactory;
import com.glowbyte.writing.SimpleFileWriter;


public class Main {

    public static void main(String[] args){
        try (Connection connection = SimpleConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM TABLE_LIST ";
            ResultSet resultSet = statement.executeQuery(sql);

            Map<String, Set<String>> tables = new HashMap<>();
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String pks = resultSet.getString("PK");

                if(tableName == null || pks == null) {
                    continue;
                }

                String[] pkArray = pks.replaceAll("\\s", "").split(",");

                Set<String> pkList = new HashSet<>(Arrays.asList(pkArray));

                tables.put(tableName, pkList);
            }

            sql = "SELECT * FROM TABLE_COLS";
            resultSet = statement.executeQuery(sql);


            List<String> resultStrings = new ArrayList<>();
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");

                if(tableName == null) {
                    continue;
                }

                if(tables.containsKey(tableName)) {
                    String columnName = resultSet.getString("COLUMN_NAME");

                    if(columnName == null) {
                        continue;
                    }

                    //разбито на 2 условия, так как исходя из примера становится понятно, что требуется
                    //выводить COLUMN_NAME с регистром таблицы TABLE_LIST и проверять, что
                    //в таблице TABLE_COLS либо такая же строка либо в противоположном регистре
                    if(tables.get(tableName).contains(columnName.toLowerCase())){
                        String columnType = resultSet.getString("COLUMN_TYPE");

                        if(columnType == null) {
                            continue;
                        }

                        columnName = columnName.toLowerCase();
                        resultStrings.add(String.join(", ", tableName, columnName, columnType));
                    }else if (tables.get(tableName).contains(columnName.toUpperCase())) {
                        String columnType = resultSet.getString("COLUMN_TYPE");

                        if(columnType == null) {
                            continue;
                        }

                        columnName = columnName.toUpperCase();
                        resultStrings.add(String.join(", ", tableName, columnName, columnType));
                    }
                }
            }

            SimpleFileWriter.writeToFile(resultStrings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}