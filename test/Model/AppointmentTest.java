/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author devve
 */
public class AppointmentTest {

    public AppointmentTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of StringtoTimeConversion method, of class Appointment.
     */
    @Test
    public void testStringtoTimeConversion() {
        System.out.println("StringtoTimeConversion");
        String dateString = "2020-12-18";
        LocalDate date = LocalDate.parse(dateString);
        String start = "08:30";
        String end = "09:00";
        String[] expectedResult = {"2020-12-18 16:30:00", "2020-12-18 17:00:00"};
        String[] result = Appointment.StringtoTimeConversion(date, start, end);

        assert (compareStringArrays(expectedResult, result));
    }

    public static boolean compareStringArrays(String[] A, String[] B) {

        if (A.length != B.length) {
            return false;
        }
        for (int i = 0; i < A.length; i++) {
            System.out.println(A[i] + "     " + B[i]);
            if (A[i].compareTo(B[i]) != 0) {
                return false;
            }
        }
        return true;
    }

}
