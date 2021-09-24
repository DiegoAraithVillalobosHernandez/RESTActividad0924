package mx.edu.utez.database;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMysql {
    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/ventas?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
        //jdbc:mysql://@ip:@puerto/@baseDatos,@usuario,@contraseña
        //localhost = 127.0.0.1 es una dirección de ip local
    }

    public static void main(String[] args) {
        try {
            Connection con = ConnectionMysql.getConnection();
            System.out.println("Conexion exitosa");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}