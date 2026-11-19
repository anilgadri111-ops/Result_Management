package model;

public class Student {
    private int studentId;
    private String name;
    private String rollNo;
    private String department;
    private int semester;

    public Student() {}

    public Student(int studentId, String name, String rollNo, String department, int semester) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.semester = semester;
    }

    public Student(String name, String rollNo, String department, int semester) {
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.semester = semester;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    @Override
    public String toString() {
        return name + " (" + rollNo + ")";
    }
}
