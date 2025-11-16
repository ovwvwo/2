package com.example;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Scanner;

public class Main {
    protected static Scanner scan = new Scanner(System.in);
    protected static String tablename;
    protected static String mySQLURL = "jdbc:mysql://localhost:3306/homework2";

    public static void main(String[] args) throws SQLException {
        System.out.println("Добро пожаловать в программу! \nВведите будущее название таблицы: ");
        tablename = scan.nextLine();
        int x = 0;
        String s = "";

        int id1 = 0, id2 = 0; // ID двух строк

        while (!"0".equals(s)) {
            System.out.println("""
                    Выберите действие:
                    1. Вывести все таблицы из MySQL
                    2. Создать таблицу в MySQL
                    3. Ввести две строки и сохранить в MySQL
                    4. Подсчитать длину строк и сохранить
                    5. Объединить строки и сохранить
                    6. Сравнить строки и сохранить
                    7. Вывести данные на экран
                    0. Выход
                    """);
            s = scan.next();
            scan.nextLine();

            try {
                x = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода");
            }

            switch (x) {
                case 1 -> {
                    DriverManager.registerDriver(new Driver());
                    try (Connection con = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery("SHOW TABLES");
                        System.out.println("Таблицы из текущей БД: ");
                        while (rs.next()) {
                            System.out.println(rs.getString(1));
                        }
                    }
                }

                case 2 -> {
                    DriverManager.registerDriver(new Driver());
                    try (Connection con = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        Statement stmt = con.createStatement();
                        String query = "CREATE TABLE IF NOT EXISTS " + tablename +
                                " (id INT AUTO_INCREMENT PRIMARY KEY, " +
                                "stroka VARCHAR(255), " +
                                "quantity INT, " +
                                "concat VARCHAR(255), " +
                                "comparison VARCHAR(255))";
                        stmt.executeUpdate(query);
                        System.out.println("Таблица создана или уже существует");
                    }
                }

                case 3 -> {
                    DriverManager.registerDriver(new Driver());
                    try (Connection con = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        System.out.println("Введите строку номер 1: ");
                        String stroka1 = scan.nextLine();
                        System.out.println("Введите строку номер 2: ");
                        String stroka2 = scan.nextLine();

                        String insertQuery = "INSERT INTO " + tablename + " (stroka) VALUES (?)";
                        PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

                        ps.setString(1, stroka1);
                        ps.executeUpdate();
                        ResultSet rs1 = ps.getGeneratedKeys();
                        if (rs1.next()) id1 = rs1.getInt(1);

                        ps.setString(1, stroka2);
                        ps.executeUpdate();
                        ResultSet rs2 = ps.getGeneratedKeys();
                        if (rs2.next()) id2 = rs2.getInt(1);

                        System.out.println("Две строки успешно внесены!");
                    }
                }

                case 4 -> {
                    if (id1 == 0 || id2 == 0) {
                        System.out.println("Сначала выполните пункт 3 — введите строки!");
                        break;
                    }
                    DriverManager.registerDriver(new Driver());
                    try (Connection con = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        String selectQuery = "SELECT id, stroka FROM " + tablename + " WHERE id IN (?, ?)";
                        PreparedStatement psSelect = con.prepareStatement(selectQuery);
                        psSelect.setInt(1, id1);
                        psSelect.setInt(2, id2);
                        ResultSet rs = psSelect.executeQuery();

                        int q1 = 0, q2 = 0;
                        while (rs.next()) {
                            if (rs.getInt("id") == id1) q1 = rs.getString("stroka").length();
                            if (rs.getInt("id") == id2) q2 = rs.getString("stroka").length();
                        }

                        String updateQuery = "UPDATE " + tablename + " SET quantity=? WHERE id=?";
                        PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                        psUpdate.setInt(1, q1);
                        psUpdate.setInt(2, id1);
                        psUpdate.executeUpdate();

                        psUpdate.setInt(1, q2);
                        psUpdate.setInt(2, id2);
                        psUpdate.executeUpdate();

                        System.out.println("Длины строк успешно сохранены!");
                    }
                }

                case 5 -> {
                    if (id1 == 0 || id2 == 0) {
                        System.out.println("Сначала выполните пункт 3 — введите строки!");
                        break;
                    }
                    DriverManager.registerDriver(new Driver());
                    try (Connection con = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        String selectQuery = "SELECT id, stroka FROM " + tablename + " WHERE id IN (?, ?)";
                        PreparedStatement psSelect = con.prepareStatement(selectQuery);
                        psSelect.setInt(1, id1);
                        psSelect.setInt(2, id2);
                        ResultSet rs = psSelect.executeQuery();

                        String s1 = "", s2 = "";
                        while (rs.next()) {
                            if (rs.getInt("id") == id1) s1 = rs.getString("stroka");
                            if (rs.getInt("id") == id2) s2 = rs.getString("stroka");
                        }

                        String concatenated = s1 + s2;
                        String updateQuery = "UPDATE " + tablename + " SET concat=? WHERE id=?";
                        PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                        psUpdate.setString(1, concatenated);
                        psUpdate.setInt(2, id1);
                        psUpdate.executeUpdate();
                        psUpdate.setInt(2, id2);
                        psUpdate.executeUpdate();

                        System.out.println("Строки успешно объединены!");
                    }
                }

                case 6 -> {
                    if (id1 == 0 || id2 == 0) {
                        System.out.println("Сначала выполните пункт 3 — введите строки!");
                        break;
                    }
                    DriverManager.registerDriver(new Driver());
                    try (Connection con = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        String selectQuery = "SELECT id, stroka FROM " + tablename + " WHERE id IN (?, ?)";
                        PreparedStatement psSelect = con.prepareStatement(selectQuery);
                        psSelect.setInt(1, id1);
                        psSelect.setInt(2, id2);
                        ResultSet rs = psSelect.executeQuery();

                        String s1 = "", s2 = "";
                        while (rs.next()) {
                            if (rs.getInt("id") == id1) s1 = rs.getString("stroka");
                            if (rs.getInt("id") == id2) s2 = rs.getString("stroka");
                        }

                        String comparison = s1.equals(s2) ? "Строки совпадают" : "Строки НЕ совпадают";
                        String updateQuery = "UPDATE " + tablename + " SET comparison=? WHERE id=?";
                        PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                        psUpdate.setString(1, comparison);
                        psUpdate.setInt(2, id1);
                        psUpdate.executeUpdate();
                        psUpdate.setInt(2, id2);
                        psUpdate.executeUpdate();

                        System.out.println("Сравнение строк выполнено!");
                    }
                }

                case 7 -> {
                    DriverManager.registerDriver(new Driver());
                    try (Connection con7 = DriverManager.getConnection(mySQLURL, "root", "MyNewPass123!")) {
                        System.out.println("Успешно подключились к БД");

                        System.out.println("Введите будущее название файла: ");
                        String name = scan.nextLine();
                        String q = "SELECT * FROM " + tablename + " INTO OUTFILE 'D:/" + name + "' CHARACTER SET CP1251";


                        Statement stmt7 = con7.createStatement();
                        try {
                            stmt7.executeQuery(q);
                            System.out.println("Данные успешно экспортированы в файл!");
                        } catch (SQLException e) {
                            System.out.println("Ошибка экспорта: " + e.getMessage());
                        }
                        String query = "SELECT * FROM " + tablename + " WHERE id IN (?, ?)";
                        PreparedStatement ps = con7.prepareStatement(query);
                        ps.setInt(1, id1);
                        ps.setInt(2, id2);
                        ResultSet rs = ps.executeQuery();

                        System.out.println("Все данные из таблицы:");
                        while (rs.next()) {
                            System.out.println(
                                    "ID: " + rs.getInt("id") +
                                            ", Строка: " + rs.getString("stroka") +
                                            ", Размер: " + rs.getInt("quantity") +
                                            ", Объединение: " + rs.getString("concat") +
                                            ", Сравнение: " + rs.getString("comparison")
                            );
                        }
                    }
                }

                case 0 -> System.out.println("Выход из программы.");
            }
        }
    }
}
