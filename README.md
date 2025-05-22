# Student Management System

A Java Swing application for managing student records with MySQL database integration.

## Features

- Add new students with detailed information
- View all students in a sortable and filterable table
- Update existing student information
- Delete student records
- Search students by name or roll number
- Filter students by class, section, or enrollment status

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher
- MySQL Connector/J (JDBC driver)

## Setup Instructions

1. **Database Setup**
   - Install MySQL Server if not already installed
   - Run the SQL script in `database/setup.sql` to create the database and tables:
     ```bash
     mysql -u root -p < database/setup.sql
     ```

2. **Configure Database Connection**
   - Open `src/util/DatabaseConnection.java`
   - Update the following constants with your MySQL credentials:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/student_management";
     private static final String USER = "your_username";
     private static final String PASSWORD = "your_password";
     ```

3. **Compile and Run**
   - Compile the Java files:
     ```bash
     javac -d bin src/**/*.java
     ```
   - Run the application:
     ```bash
     java -cp bin:mysql-connector-java.jar App
     ```

## Usage

1. **Adding a Student**
   - Fill in the student details in the input fields
   - Click "Add Student" to save the record
   - Required fields: Name, Roll Number, Date of Birth

2. **Viewing Students**
   - All students are displayed in the table
   - Click column headers to sort
   - Use the search field to find specific students

3. **Updating Student Information**
   - Select a student from the table
   - Modify the fields as needed
   - Click "Update Student" to save changes

4. **Deleting a Student**
   - Select a student from the table
   - Click "Delete Student"
   - Confirm the deletion

5. **Searching and Filtering**
   - Use the search field to find students by name
   - Use the combo boxes to filter by class or status

## Contributing

Feel free to fork this project and submit pull requests for any improvements.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
