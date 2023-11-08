package Database;

import Controller.UserController;
import Init.Config;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DB {
    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String serverName = Config.db_server;
            String port = Config.db_port;
            String databaseName = Config.db_name;
            String username = Config.db_username;
            String password = Config.db_password;
            String url = "jdbc:sqlserver://" + serverName + ":" + port + ";databaseName=" + databaseName + ";trustServerCertificate=true;";
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean executeUpdate(String sql) {// insert update delete
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int row = preparedStatement.executeUpdate();
            connection.close();
            return row > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean executeUpdate(String sql, String[] fields) {// insert update delete
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < fields.length; i++) {
                preparedStatement.setString(i + 1, fields[i]);
            }
            int row = preparedStatement.executeUpdate();
            connection.close();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<MyObject> getData(String sql, String[] fields) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<MyObject> result = new ArrayList<>();
            List<MyObject> re ;
            while (resultSet.next()) {
                MyObject myObject = new MyObject();
                for (int i = 0; i < fields.length; i++) {
                    Field field = MyObject.class.getDeclaredField(fields[i]);
                    field.setAccessible(true);
                    field.set(myObject, resultSet.getString(fields[i]));
                }
                result.add(myObject);
            }
            connection.close();
            return result;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<MyObject> getData(String sql, String[] vars, String[] fields) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < vars.length; i++) {
                preparedStatement.setString(i + 1, vars[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<MyObject> result = new ArrayList<>();
            while (resultSet.next()) {
                MyObject myObject = new MyObject();
                for (int i = 0; i < fields.length; i++) {
                    Field field = MyObject.class.getDeclaredField(fields[i]);
                    field.setAccessible(true);
                    field.set(myObject, resultSet.getString(fields[i]));
                }
                result.add(myObject);
            }
            connection.close();
            return result;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    static String slurpStdin() {
        String input = "";
        Scanner scan = new Scanner(System.in);

        while (true) {
            input += scan.nextLine();
            if (scan.hasNextLine()) {
                input += "\n";
            } else {
                break;
            }
        }

        return input;
    }

    public int d,x,y;
    public void extendedEuclid(int A, int B){
        if (B == 0) {
            d = A;
            x = 1;
            y = 0;
        }
        else {
            extendedEuclid(B, A%B);
            int temp = x;
            x = y;
            y = temp - (A/B)*y;
        }
    }
    public static Date[] calculateDatesAfterXMonths(int x) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.MONTH, x);
        calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract 1 day to get the last day of the month
        Date futureDate = calendar.getTime();

        return new Date[]{currentDate, futureDate};
    }
    public static int daysBetweenDates(Date startDate, Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
    }
    public static void main(String[] args) throws SQLException {
        boolean check_phone = UserController.RegisterController.checkPhone("+84763416782");
        System.out.println(check_phone);
    }
}
