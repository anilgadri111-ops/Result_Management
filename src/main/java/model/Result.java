package model;

public class Result {
    private int resultId;
    private int studentId;
    private String studentName;
    private String rollNo;
    private String subjectName;
    private int marksObtained;
    private int maxMarks;
    private String grade;
    private String status;

    public Result() {}

    public Result(int studentId, String subjectName, int marksObtained, int maxMarks) {
        this.studentId = studentId;
        this.subjectName = subjectName;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.grade = calculateGrade(marksObtained, maxMarks);
        this.status = (marksObtained >= (maxMarks * 0.4)) ? "PASS" : "FAIL";
    }

    public static String calculateGrade(int marks, int maxMarks) {
        double percentage = (marks * 100.0) / maxMarks;
        if (percentage >= 90) return "A+";
        else if (percentage >= 75) return "A";
        else if (percentage >= 60) return "B";
        else if (percentage >= 45) return "C";
        else return "F";
    }

    public static String calculateStatus(int marks, int maxMarks) {
        double percentage = (marks * 100.0) / maxMarks;
        return percentage >= 40 ? "PASS" : "FAIL";
    }

    // Getters and Setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public int getMarksObtained() { return marksObtained; }
    public void setMarksObtained(int marksObtained) { this.marksObtained = marksObtained; }

    public int getMaxMarks() { return maxMarks; }
    public void setMaxMarks(int maxMarks) { this.maxMarks = maxMarks; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPercentage() {
        return (marksObtained * 100.0) / maxMarks;
    }
}
