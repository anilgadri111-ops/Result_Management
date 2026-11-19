# Student Result Management System

A desktop application built with Java Swing and JDBC/MySQL for managing student academic records, with role-based access for Admins and Students.

## Features

**Admin**
- Add, update, and delete student records
- Subject-wise marks entry with duplicate-entry prevention (same student + subject can't be entered twice)
- Live grade and percentage preview while entering marks
- View all student results in a color-coded, real-time table (grade + PASS/FAIL status)
- Search any student's result by roll number

**Student**
- Search and view their own result by roll number, with subject-wise breakdown and overall percentage

**Core Logic**
- Automatic grade calculation (A+, A, B, C, F) and pass/fail status based on percentage
- Role-based login and navigation — Admin and Student see different dashboards

## Tech Stack

- **Language:** Java
- **UI:** Java Swing (JFrame, JPanel, JTable with DefaultTableModel, GridBagLayout/BorderLayout, custom DefaultTableCellRenderer for color-coding, MouseAdapter/KeyAdapter for interactivity)
- **Database:** MySQL
- **Data Access:** JDBC — PreparedStatement for all queries, DriverManager for connections, ResultSet for retrieval

## Architecture

- **MVC + DAO pattern** — clean separation between UI (Swing frames), models (`Student.java`, `Result.java`), and data access (`StudentDAO`, `ResultDAO`, `UserDAO`)
- **Singleton `DBConnection`** class for centralized connection management
- **PreparedStatement** used throughout to prevent SQL injection
- **`ON DELETE CASCADE`** on foreign keys — deleting a student automatically removes their associated results

## Database Schema

3 tables:
- `students` — student records (name, roll number, department, semester)
- `results` — subject-wise marks per student, linked via foreign key with cascading delete
- `users` — login credentials and role (admin/student)

## Project Structure
src/
├── dao/ → ResultDAO, StudentDAO, UserDAO
├── db/ → DBConnection
├── model/ → Result, Student
├── ui/ → DashboardFrame, LoginFrame, ResultForm, SearchFrame, StudentForm, ViewResultsFrame
└── Main.java → application entry point

## Getting Started

1. Clone the repo: git clone https://github.com/Akshat-13-Soni/Student_Result_Management_System_Using_JDBC-JAVA-Swing.git
2. Import into Eclipse (or your preferred IDE) as an existing Java project — `.classpath` and `.project` files are included.
3. Set up a MySQL database and create the `students`, `results`, and `users` tables (matching the schema above).
4. Copy `config.properties.example` to a new file named `config.properties` in the project root, and fill in your own database URL, username, and password.
5. Make sure the MySQL JDBC driver `.jar` (in `/library`) is on the build path.
6. Run `Main.java` to launch the application (opens the login screen).

**Demo login credentials:**
- Admin: `admin` / `admin`
- Student: `student` / `student`

## Team

Built as a 4-member academic team project. My contributions: overall project structure, student management module, database schema design, and UI integration across all screens.

## Author

**Akshat Soni**
[LinkedIn](https://linkedin.com/in/akshatsoni1304) · [GitHub](https://github.com/Akshat-13-Soni)
