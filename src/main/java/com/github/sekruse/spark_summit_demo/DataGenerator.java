package com.github.sekruse.spark_summit_demo;

import java.io.File;
import java.sql.*;
import java.util.Random;

/**
 * This utility generates test data.
 */
public class DataGenerator {

    /**
     * Generates a new k-means dataset.
     *
     * @param path      to the SQLite DB
     * @param k         the number of clusters in the generated dataset
     * @param numPoints the number of points to create
     */
    public static void generateSqliteDbForKMeans(String path, int k, int numPoints) {
        final double xRange = 180;
        final double yRange = 180;
        final double variance = 2;


        try (Connection connection = createJdbcSqliteConnection(path)) {
            // Create the table.
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS locations;");
            statement.execute("CREATE TABLE locations (lat REAL, lon REAL, name TEXT);");
            statement.close();

            // Initialize the data generators.
            Random random = new Random();
            double[] xCenters = new double[k];
            double[] yCenters = new double[k];
            for (int i = 0; i < k; i++) {
                xCenters[i] = random.nextDouble() * xRange;
                yCenters[i] = random.nextDouble() * yRange;
            }

            // Generate the data.
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO locations VALUES (?, ?, ?);");
            for (int i = 0; i < numPoints; i++) {
                int centerIndex = i % k;
                double xCenter = xCenters[centerIndex];
                double yCenter = yCenters[centerIndex];

                preparedStatement.setDouble(1, random.nextGaussian() * variance + xCenter);
                preparedStatement.setDouble(2, random.nextGaussian() * variance + yCenter);
                preparedStatement.setString(3, "This is a totally random string.");
                preparedStatement.addBatch();

                if (i % 1000 == 999) preparedStatement.executeBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Could not generate data.", e);
        }
    }

    private static Connection createJdbcSqliteConnection(String path) {
        File file = new File(path).getAbsoluteFile();
        try {
            Class.forName("org.sqlite.JDBC");
            String connString = String.format("jdbc:sqlite:%s", file.getAbsoluteFile());
            return DriverManager.getConnection(connString);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize JDBC connection.", e);
        }
    }

}
