import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        CourseManager manager = new CourseManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== COURSE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Course");
            System.out.println("2. Register Student");
            System.out.println("3. View Courses");
            System.out.println("4. Enroll Student in Course");
            System.out.println("5. View Student Courses");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    manager.addCourse();
                    break;
                case 2:
                    manager.addStudent();
                    break;
                case 3:
                    manager.showCourses();
                    break;
                case 4:
                    manager.enrollStudent();
                    break;
                case 5:
                    manager.viewStudentCourses();
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
