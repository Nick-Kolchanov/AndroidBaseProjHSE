package com.example.baseproject;

import java.time.Year;

public class GroupEntry {

    public GroupEntry(String programName, Year admissionYear, int groupsCount){
        this.programName = programName;
        this.admissionYear = admissionYear;
        this.groupsCount = groupsCount;
    }

    private String programName;
    private Year admissionYear;
    private int groupsCount;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Year getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(Year admissionYear) {
        this.admissionYear = admissionYear;
    }

    public int getGroupsCount() {
        return groupsCount;
    }

    public void setGroupsCount(int groupsCount) {
        this.groupsCount = groupsCount;
    }
}
