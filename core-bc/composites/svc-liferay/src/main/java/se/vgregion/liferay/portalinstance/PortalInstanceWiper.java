package se.vgregion.liferay.portalinstance;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Patrik Bergström
 */
public class PortalInstanceWiper {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");

        Properties properties = new Properties();

        properties.put("user", "hotell");
        properties.put("password", "hotell");

        for (int i = 1; i <= 3; i++) {

            System.out.println("=========================== Shard " + i + " =====================================");

            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5555/hotell62_" + i, properties);

            Statement statement = connection.createStatement();


//            ResultSet resultSet = statement.executeQuery("select c.table_name from information_schema.columns c where COLUMN_NAME like 'accountid'");
//            ResultSet resultSet = statement.executeQuery("select c.table_name from information_schema.columns c where COLUMN_NAME like 'classpk'");
            ResultSet resultSet = statement.executeQuery("select c.table_name from information_schema.columns c where COLUMN_NAME like 'companyid'");
//            ResultSet resultSet = statement.executeQuery("select c.table_name from information_schema.columns c where COLUMN_NAME like 'ddmstructure%'");
//            ResultSet resultSet = statement.executeQuery("select c.table_name from information_schema.columns c where COLUMN_NAME like 'classnameid'");
//            ResultSet resultSet = statement.executeQuery("select c.table_name from information_schema.columns c where COLUMN_NAME like 'shard%'");
            ResultSetMetaData metaData = resultSet.getMetaData();

            /*for (int j = 1; j <= metaData.getColumnCount(); j++) {
                System.out.println(metaData.getColumnName(j));
            }*/

            System.out.println();
            List<String> tablesWithCompanyId = new ArrayList<String>();
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                tablesWithCompanyId.add(tableName);
                System.out.print("Tablename: " + tableName);
            }
            System.out.println();

            for (String table : tablesWithCompanyId) {

//                ResultSet resultFromTable = statement.executeQuery("SELECT * FROM " + table + " where classnameid = '10043'");
//                ResultSet resultFromTable = statement.executeQuery("SELECT * FROM " + table + " where accountid = '1660810'");
//                ResultSet resultFromTable = statement.executeQuery("SELECT * FROM " + table + " where companyid = '1660808'");
                int noRows = statement.executeUpdate("delete FROM " + table + " where companyid = '1660808'");
//                int noRows = statement.executeUpdate("delete FROM " + table + " where classpk = '1660808'");
//                int noRows = statement.executeUpdate("delete FROM " + table + " where accountid = '1660810'");
//                connection.commit();

                if (noRows > 0) {
                    System.out.println(table + ": " + noRows);
                }

/*
                while (resultFromTable.next()) {
                    System.out.println(table + " - id: " + resultFromTable.getObject(1) + " - " */
/*+ resultFromTable.getString("classpk")*//*
);
                }
*/

            }

            statement.close();

            connection.close();
        }

    }
}
