package gui;

import dao.StudentDAO;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class StudentManagementSystem extends JFrame {
    private StudentDAO studentDAO;
    private JTextField nameField, rollNumberField, contactField, addressField;
    private JComboBox<String> classCombo, sectionCombo, genderCombo, statusCombo;
    private JTextField dobField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public StudentManagementSystem() {
        studentDAO = new StudentDAO();
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create main panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel tablePanel = createTablePanel();
        JPanel searchPanel = createSearchPanel();

        // Add panels to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.EAST);

        add(mainPanel);
        refreshTable();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        // Initialize components
        nameField = new JTextField(20);
        rollNumberField = new JTextField(20);
        contactField = new JTextField(20);
        addressField = new JTextField(20);
        dobField = new JTextField(10);

        String[] classes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] sections = {"A", "B", "C", "D"};
        String[] genders = {"Male", "Female", "Other"};
        String[] statuses = {"Active", "Inactive"};

        classCombo = new JComboBox<>(classes);
        sectionCombo = new JComboBox<>(sections);
        genderCombo = new JComboBox<>(genders);
        statusCombo = new JComboBox<>(statuses);

        // Add components to panel
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Roll Number:"));
        panel.add(rollNumberField);
        panel.add(new JLabel("Class:"));
        panel.add(classCombo);
        panel.add(new JLabel("Section:"));
        panel.add(sectionCombo);
        panel.add(new JLabel("Date of Birth (dd/MM/yyyy):"));
        panel.add(dobField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton resetButton = new JButton("Reset Fields");

        addButton.addActionListener(_ -> addStudent());
        updateButton.addActionListener(_ -> updateStudent());
        deleteButton.addActionListener(_ -> deleteStudent());
        resetButton.addActionListener(_ -> resetFields());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(resetButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Records"));

        String[] columns = {"Name", "Roll Number", "Class", "Section", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Search/Filter"));

        // Search components
        searchField = new JTextField(20);
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"Name", "Roll Number"});
        
        // Filter components
        JComboBox<String> filterClassCombo = new JComboBox<>(new String[]{"All Classes", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        JComboBox<String> filterSectionCombo = new JComboBox<>(new String[]{"All Sections", "A", "B", "C", "D"});
        JButton resetFilterButton = new JButton("Reset Filters");

        // Add document listener for live search
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateResults(); }

            private void updateResults() {
                String searchTerm = searchField.getText().trim();
                String searchType = searchTypeCombo.getSelectedItem().toString().toLowerCase().replace(" ", "_");
                String classFilter = filterClassCombo.getSelectedItem().toString();
                String sectionFilter = filterSectionCombo.getSelectedItem().toString();
                
                searchAndFilterStudents(searchTerm, searchType, classFilter, sectionFilter);
            }
        });

        // Add item listeners for live filter updates
        searchTypeCombo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateSearchResults(searchTypeCombo, filterClassCombo, filterSectionCombo);
            }
        });

        filterClassCombo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateSearchResults(searchTypeCombo, filterClassCombo, filterSectionCombo);
            }
        });

        filterSectionCombo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateSearchResults(searchTypeCombo, filterClassCombo, filterSectionCombo);
            }
        });

        resetFilterButton.addActionListener(_ -> {
            searchField.setText("");
            searchTypeCombo.setSelectedIndex(0);
            filterClassCombo.setSelectedIndex(0);
            filterSectionCombo.setSelectedIndex(0);
            refreshTable();
        });

        // Layout components
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Class:"));
        filterPanel.add(filterClassCombo);
        filterPanel.add(new JLabel("Section:"));
        filterPanel.add(filterSectionCombo);
        filterPanel.add(resetFilterButton);

        // Add panels to main search panel
        panel.setLayout(new BorderLayout());
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updateSearchResults(JComboBox<String> searchTypeCombo, 
                                   JComboBox<String> filterClassCombo, 
                                   JComboBox<String> filterSectionCombo) {
        String searchTerm = searchField.getText().trim();
        String searchType = searchTypeCombo.getSelectedItem().toString().toLowerCase().replace(" ", "_");
        String classFilter = filterClassCombo.getSelectedItem().toString();
        String sectionFilter = filterSectionCombo.getSelectedItem().toString();
        
        searchAndFilterStudents(searchTerm, searchType, classFilter, sectionFilter);
    }

    private void searchAndFilterStudents(String searchTerm, String searchType, String classFilter, String sectionFilter) {
        // Use SwingWorker to perform search in background
        new SwingWorker<List<Student>, Void>() {
            @Override
            protected List<Student> doInBackground() {
                List<Student> students;
                
                if (searchTerm.isEmpty()) {
                    students = studentDAO.getAllStudents();
                } else {
                    students = studentDAO.searchStudents(searchTerm, searchType);
                }

                // Apply filters
                List<Student> filteredStudents = new ArrayList<>();
                for (Student student : students) {
                    boolean classMatch = classFilter.equals("All Classes") || student.getClassName().equals(classFilter);
                    boolean sectionMatch = sectionFilter.equals("All Sections") || student.getSection().equals(sectionFilter);

                    if (classMatch && sectionMatch) {
                        filteredStudents.add(student);
                    }
                }

                return filteredStudents;
            }

            @Override
            protected void done() {
                try {
                    List<Student> filteredStudents = get();
                    updateTableWithStudents(filteredStudents);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void addStudent() {
        try {
            Student student = getStudentFromFields();
            if (student != null && studentDAO.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                resetFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateStudent() {
        try {
            Student student = getStudentFromFields();
            if (student != null && studentDAO.updateStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                resetFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row != -1) {
            String rollNumber = tableModel.getValueAt(row, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this student?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (studentDAO.deleteStudent(rollNumber)) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    resetFields();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete student!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!");
        }
    }

    private void refreshTable() {
        List<Student> students = studentDAO.getAllStudents();
        updateTableWithStudents(students);
    }

    private void updateTableWithStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {
                student.getName(),
                student.getRollNumber(),
                student.getClassName(),
                student.getSection(),
                student.getEnrollmentStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedStudent() {
        int row = studentTable.getSelectedRow();
        if (row != -1) {
            String rollNumber = tableModel.getValueAt(row, 1).toString();
            List<Student> students = studentDAO.searchStudents(rollNumber, "roll_number");
            if (!students.isEmpty()) {
                Student student = students.get(0);
                nameField.setText(student.getName());
                rollNumberField.setText(student.getRollNumber());
                classCombo.setSelectedItem(student.getClassName());
                sectionCombo.setSelectedItem(student.getSection());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dobField.setText(sdf.format(student.getDateOfBirth()));
                genderCombo.setSelectedItem(student.getGender());
                contactField.setText(student.getContactNumber());
                addressField.setText(student.getAddress());
                statusCombo.setSelectedItem(student.getEnrollmentStatus());
            }
        }
    }

    private Student getStudentFromFields() throws ParseException {
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String className = classCombo.getSelectedItem().toString();
        String section = sectionCombo.getSelectedItem().toString();
        String dobString = dobField.getText().trim();
        String gender = genderCombo.getSelectedItem().toString();
        String contact = contactField.getText().trim();
        String address = addressField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();

        // Validation
        if (name.isEmpty() || rollNumber.isEmpty() || dobString.isEmpty()) {
            throw new IllegalArgumentException("Required fields cannot be empty!");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dob = sdf.parse(dobString);

        Student student = new Student(name, rollNumber, className, section, dob, gender, contact, address);
        student.setEnrollmentStatus(status);  // Set the enrollment status
        return student;
    }

    private void resetFields() {
        nameField.setText("");
        rollNumberField.setText("");
        classCombo.setSelectedIndex(0);
        sectionCombo.setSelectedIndex(0);
        dobField.setText("");
        genderCombo.setSelectedIndex(0);
        contactField.setText("");
        addressField.setText("");
        statusCombo.setSelectedIndex(0);
        studentTable.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new StudentManagementSystem().setVisible(true);
        });
    }
} 