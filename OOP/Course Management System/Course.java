import java.util.ArrayList;

public class Course {
    String courseId;
    String courseName;
    ArrayList<Student> students;

    public Course(String id, String name) {
        this.courseId = id;
        this.courseName = name;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public void displayCourse() {
        System.out.println(courseId + " - " + courseName);
    }

    public void showEnrolledStudents() {
        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }

        System.out.println("Students in " + courseName + ":");
        for (Student s : students) {
            System.out.println("- " + s.name);
        }
    }
}
