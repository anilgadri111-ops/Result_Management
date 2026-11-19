
CREATE DATABASE IF NOT EXISTS student_result_db;

USE student_result_db;


CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(30) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    semester INT NOT NULL
);


CREATE TABLE IF NOT EXISTS results (
    result_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    marks_obtained INT NOT NULL,
    max_marks INT NOT NULL,
    grade VARCHAR(5),
    status VARCHAR(10),

    CONSTRAINT fk_student_result
    FOREIGN KEY (student_id)
    REFERENCES students(student_id)
    ON DELETE CASCADE,

    CONSTRAINT unique_student_subject
    UNIQUE (student_id, subject_name)
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);


INSERT IGNORE INTO users (username, password, role)
VALUES
('admin', 'admin', 'admin'),
('student', 'student', 'student');

-- VERIFY TABLES
SHOW TABLES;

SELECT * FROM users;
