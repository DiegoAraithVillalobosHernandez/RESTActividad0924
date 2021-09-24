package mx.edu.utez.controller;

import mx.edu.utez.database.ConnectionMysql;
import mx.edu.utez.model.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/employee")
public class Service {
    Connection con;
    PreparedStatement pstm;
    Statement statement;
    ResultSet rs;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employeeNumber,lastname,firstname,extension,email,officeCode,reportsTo,jobTitle" +
                    " FROM employees";
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                Employee employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastname"));
                employee.setFirstName(rs.getString("firstname"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
                employees.add(employee);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employees;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber){
        Employee employee = new Employee();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employeeNumber,lastname,firstname,extension,email,officeCode,reportsTo,jobTitle" +
                    " FROM employees where employeeNumber=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            rs = pstm.executeQuery();
            if (rs.next()) {
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastname"));
                employee.setFirstName(rs.getString("firstname"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employee;
    }
    @POST
    @Path("/{id}/{lastname}/{firstname}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createEmployee(@PathParam("id") int employeeNumber,@PathParam("lastname") String lastname,@PathParam("firstname") String firstname,@PathParam("extension") String extension,
                                 @PathParam("email") String email,@PathParam("officeCode") int officeCode,@PathParam("reportsTo") int reportsTo,@PathParam("jobTitle") String jobTittle){
        String message = null;
        try{
            con = ConnectionMysql.getConnection();
            String query = "INSERT INTO employees(employeeNumber,lastname,firstname,extension,email,officeCode,reportsTo,jobTitle) values(?,?,?,?,?,?,?,?);";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            pstm.setString(2,lastname);
            pstm.setString(3,firstname);
            pstm.setString(4,extension);
            pstm.setString(5,email);
            pstm.setInt(6,officeCode);
            pstm.setInt(7,reportsTo);
            pstm.setString(8,jobTittle);
            if (pstm.executeUpdate() == 1) {
                message = "Empleado registrado";
            }else{
                message = "Algo salio mal, verifica tus datos";
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return message;
    }
    @PUT
    @Path("/{id}/{lastname}/{firstname}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(@PathParam("id") int employeeNumber,@PathParam("lastname") String lastname,@PathParam("firstname") String firstname,@PathParam("reportsTo") int reportsTo,@PathParam("jobTitle") String jobTittle){
        String message = null;
        try {
            con = ConnectionMysql.getConnection();
            String query = "UPDATE employees SET  lastname=?, firstname=?,reportsTo=?,jobTitle=? WHERE employeeNumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setString(1,lastname);
            pstm.setString(2,firstname);
            pstm.setInt(3,reportsTo);
            pstm.setString(4,jobTittle);
            pstm.setInt(5,employeeNumber);
            if (pstm.executeUpdate() == 1) {
                message = "Empleado actualizado";
            }else{
                message = "Algo salio mal con la actualización, verifica tus datos";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return message;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePerson(@PathParam("id") int employeeNumber){
        String message = null;
        try {
            con = ConnectionMysql.getConnection();
            String query = "DELETE FROM employees WHERE employeeNumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            if (pstm.executeUpdate() == 1) {
                message = "Empleado eliminado";
            }else{
                message = "Algo salio mal con la eliminación, verifica tus datos";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return message;
    }

    public void closeConnection(){
        try {
            if (con!=null){
                con.close();
            }
            if (pstm != null){
                pstm.close();
            }
            if (rs != null){
                rs.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}