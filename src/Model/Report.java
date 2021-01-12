package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author andrew.daiza
 */
public class Report {

    private String reportType;
    private String reportNum;
    private String reportStart;
    private String reportEnd;

    public Report(String reportType, String reportStart, String reportEnd) {
        this.reportType = reportType;
        this.reportNum = reportNum;
        this.reportStart = reportStart;
        this.reportEnd = reportEnd;
    }

    public static ObservableList<String> monthTypeReport = FXCollections.observableArrayList();
    public static ObservableList<String> consultantScheduleReport = FXCollections.observableArrayList();

    public void setReportStart(String reportStart) {
        this.reportStart = reportStart;
    }

    public void setReportEnd(String reportEnd) {
        this.reportEnd = reportEnd;
    }

    public static void setMonthTypeReport(ObservableList<String> monthTypeReport) {
        Report.monthTypeReport = monthTypeReport;
    }

    public String getReportStart() {
        return reportStart;
    }

    public String getReportEnd() {
        return reportEnd;
    }

    public static ObservableList<String> getMonthTypeReport() {
        return monthTypeReport;
    }

    public static void setConsultantScheduleReport(ObservableList<String> consultantScheduleReport) {
        Report.consultantScheduleReport = consultantScheduleReport;
    }

    public static ObservableList<String> getConsultantScheduleReport() {
        return consultantScheduleReport;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setReportNum(String reportNum) {
        this.reportNum = reportNum;
    }

    public String getReportType() {
        return reportType;
    }

    public String getReportNum() {
        return reportNum;
    }
}
