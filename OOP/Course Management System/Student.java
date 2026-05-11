import java.util.ArrayList;

public class Student {
    String studentId;
    String name;
    ArrayList<Course> enrolledCourses;

    public Student(String id, String name) {
        this.studentId = id;
        this.name = name;
        this.enrolledCourses = new ArrayList<>();
    }

    public void enrollCourse(Course c) {
        enrolledCourses.add(c);
    }

    public void viewCourses() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }

        System.out.println("Courses of " + name + ":");
        for (Course c : enrolledCourses) {
            System.out.println("- " + c.courseName);
        }
    }
}
