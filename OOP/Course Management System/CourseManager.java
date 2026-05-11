import java.util.ArrayList;
import java.util.Scanner;

public class CourseManager {

    ArrayList<Course> courses = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    // Add course
    public void addCourse() {
        System.out.print("Enter Course ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Course Name: ");
        String name = sc.nextLine();

        courses.add(new Course(id, name));
        System.out.println("Course added successfully!");
    }

    // Register student
    public void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        students.add(new Student(id, name));
        System.out.println("Student registered!");
    }

    // Show courses
    public void showCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        for (Course c : courses) {
            c.displayCourse();
        }
    }

    // Enroll student in course
    public void enrollStudent() {
        System.out.print("Enter Student ID: ");
        String sid = sc.nextLine();

        Student foundStudent = null;
        for (Student s : students) {
            if (s.studentId.equals(sid)) {
                foundStudent = s;
                break;
            }
        }

        if (foundStudent == null) {
            System.out.println("Student not found!");
            return;
        }

        showCourses();
        System.out.print("Enter Course ID to enroll: ");
        String cid = sc.nextLine();

        for (Course c : courses) {
            if (c.courseId.equals(cid)) {
                c.addStudent(foundStudent);
                foundStudent.enrollCourse(c);
                System.out.println("Enrollment successful!");
                return;
            }
        }

        System.out.println("Course not found!");
    }

    // View student's courses
    public void viewStudentCourses() {
        System.out.print("Enter Student ID: ");
        String sid = sc.nextLine();

        for (Student s : students) {
            if (s.studentId.equals(sid)) {
                s.viewCourses();
                return;
            }
        }

        System.out.println("Student not found!");
    }
}
