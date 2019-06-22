package org.katheer.jdbc;

/*
STORED PROCEDURE:
-----------------
DELIMITER $$
CREATE PROCEDURE getSalary
(IN eid VARCHAR(10), OUT sal FLOAT)
BEGIN
    SELECT salary INTO sal FROM employee WHERE id = eid;
END $$
DELIMITER ;

STORED FUNCTION:
----------------
DELIMITER $$
CREATE FUNCTION getAvg
(eid1 VARCHAR(10), eid2 VARCHAR(10))
RETURNS FLOAT
DETERMINISTIC
BEGIN
    DECLARE sal1, sal2 FLOAT;
    SELECT salary INTO sal1 FROM employee WHERE id=eid1;
    SELECT salary INTO sal2 FROM employee WHERE id=eid2;
    RETURN (sal1 + sal2)/2;
END $$
DELIMITER ;
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;

public class Client {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql:" +
                "//localhost:3306/spring", "root", "katheer");

        //Stored Procedure
        CallableStatement procedure = connection.prepareCall("{CALL getSalary" +
                "(?,?)}");
        procedure.setString(1,"E-103");
        procedure.registerOutParameter(2, Types.FLOAT);
        procedure.execute();

        System.out.println("Salary : " + procedure.getFloat(2));
        procedure.close();

        //Stored Function
        CallableStatement function = connection.prepareCall("{? = CALL getAvg" +
                "(?,?)}");
        function.registerOutParameter(1, Types.FLOAT);
        function.setString(2, "E-103");
        function.setString(3, "E-104");
        function.execute();

        System.out.println("Average Salary : " + function.getFloat(1));
        function.close();

        connection.close();

    }
}
