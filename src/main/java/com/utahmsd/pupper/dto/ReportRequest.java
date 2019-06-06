package com.utahmsd.pupper.dto;

public class ReportRequest {

    private ReportReason reportType;
    private String description;


    public ReportReason getReportType() { return reportType; }

    public void setReportType(ReportReason reportType) { this.reportType = reportType; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }


    enum ReportReason {
        SPAM,
        MISUSE, //Violation of policies or terms of use
        ABUSE; //Harrassment

        public String value() { return name(); }

        public static ReportReason fromValue(String v) { return valueOf(v); }
    }

}
