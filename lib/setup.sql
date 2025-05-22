CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;


CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    class VARCHAR(20) NOT NULL,
    section VARCHAR(5) NOT NULL,
    dob DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    contact_number VARCHAR(20),
    address TEXT,
    enrollment_status VARCHAR(10) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_gender CHECK (gender IN ('Male', 'Female', 'Other')),
    CONSTRAINT chk_status CHECK (enrollment_status IN ('Active', 'Inactive'))
); 