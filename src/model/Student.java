package model;

import java.util.Date;

public class Student {
    // Maximum lengths as defined in database schema
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_ROLL_NUMBER_LENGTH = 20;
    private static final int MAX_CLASS_LENGTH = 20;
    private static final int MAX_SECTION_LENGTH = 5;
    private static final int MAX_GENDER_LENGTH = 10;
    private static final int MAX_CONTACT_LENGTH = 20;
    private static final int MAX_STATUS_LENGTH = 10;

    private String name;
    private String rollNumber;
    private String className;
    private String section;
    private Date dateOfBirth;
    private String gender;
    private String contactNumber;
    private String address;
    private String enrollmentStatus;

    // Constructor
    public Student(String name, String rollNumber, String className, String section,
                  Date dateOfBirth, String gender, String contactNumber, String address) {
        setName(name);
        setRollNumber(rollNumber);
        setClassName(className);
        setSection(section);
        setDateOfBirth(dateOfBirth);
        setGender(gender);
        setContactNumber(contactNumber);
        setAddress(address);
        this.enrollmentStatus = "Active";
    }

    // Getters and Setters with validation
    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }
        this.name = name.trim();
    }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) {
        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }
        if (rollNumber.length() > MAX_ROLL_NUMBER_LENGTH) {
            throw new IllegalArgumentException("Roll number cannot exceed " + MAX_ROLL_NUMBER_LENGTH + " characters");
        }
        this.rollNumber = rollNumber.trim();
    }

    public String getClassName() { return className; }
    public void setClassName(String className) {
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class cannot be empty");
        }
        if (className.length() > MAX_CLASS_LENGTH) {
            throw new IllegalArgumentException("Class cannot exceed " + MAX_CLASS_LENGTH + " characters");
        }
        this.className = className.trim();
    }

    public String getSection() { return section; }
    public void setSection(String section) {
        if (section == null || section.trim().isEmpty()) {
            throw new IllegalArgumentException("Section cannot be empty");
        }
        if (section.length() > MAX_SECTION_LENGTH) {
            throw new IllegalArgumentException("Section cannot exceed " + MAX_SECTION_LENGTH + " characters");
        }
        this.section = section.trim();
    }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() { return gender; }
    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be empty");
        }
        if (gender.length() > MAX_GENDER_LENGTH) {
            throw new IllegalArgumentException("Gender cannot exceed " + MAX_GENDER_LENGTH + " characters");
        }
        String validGender = gender.trim();
        if (!validGender.equals("Male") && !validGender.equals("Female") && !validGender.equals("Other")) {
            throw new IllegalArgumentException("Invalid gender value");
        }
        this.gender = validGender;
    }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) {
        if (contactNumber != null) {
            if (contactNumber.length() > MAX_CONTACT_LENGTH) {
                throw new IllegalArgumentException("Contact number cannot exceed " + MAX_CONTACT_LENGTH + " characters");
            }
            this.contactNumber = contactNumber.trim();
        } else {
            this.contactNumber = "";
        }
    }

    public String getAddress() { return address; }
    public void setAddress(String address) {
        this.address = (address != null) ? address.trim() : "";
    }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) {
        if (enrollmentStatus == null || enrollmentStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Enrollment status cannot be empty");
        }
        if (enrollmentStatus.length() > MAX_STATUS_LENGTH) {
            throw new IllegalArgumentException("Enrollment status cannot exceed " + MAX_STATUS_LENGTH + " characters");
        }
        String validStatus = enrollmentStatus.trim();
        if (!validStatus.equals("Active") && !validStatus.equals("Inactive")) {
            throw new IllegalArgumentException("Invalid enrollment status");
        }
        this.enrollmentStatus = validStatus;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", class='" + className + '\'' +
                ", section='" + section + '\'' +
                '}';
    }
} 