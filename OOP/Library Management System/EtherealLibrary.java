import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EtherealLibrary extends JFrame {

    ArrayList<Book> books = new ArrayList<>();
    JTextArea display = new JTextArea();

    EtherealLibrary(){

        setTitle("🌌 Ethereal Library");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        add(panel);

        JLabel title = new JLabel("Ethereal Library Manager", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(Theme.getTextColor());
        panel.add(title, BorderLayout.NORTH);

        display.setFont(new Font("Consolas", Font.PLAIN, 16));
        display.setForeground(Theme.getTextColor());
        display.setBackground(Theme.getBg1());
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);

        GlowButton addBtn = new GlowButton("Add Book");
        GlowButton searchBtn = new GlowButton("Search");
        GlowButton viewBtn = new GlowButton("View All");
        GlowButton themeBtn = new GlowButton("Toggle Theme");
        
        buttons.add(addBtn);
        buttons.add(searchBtn);
        buttons.add(viewBtn);
        buttons.add(themeBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        // BUTTON ACTIONS ⭐

        addBtn.addActionListener(e -> addBook());
        searchBtn.addActionListener(e -> searchBook());
        viewBtn.addActionListener(e -> viewBooks());
        themeBtn.addActionListener(e -> toggleTheme());

        setVisible(true);
    }

    void addBook(){
        String title = JOptionPane.showInputDialog("Enter Book Title");
        String author = JOptionPane.showInputDialog("Enter Author");

        if(title != null && author != null){
            books.add(new Book(title, author));
            JOptionPane.showMessageDialog(this,"✨ Book Added!");
        }
    }

    void searchBook(){
        String key = JOptionPane.showInputDialog("Search title:");
        display.setText("");

        for(Book b : books){
            if(b.title.toLowerCase().contains(key.toLowerCase()))
                display.append(b + "\n");
        }
    }

    void viewBooks(){
        display.setText("");
        for(Book b : books)
            display.append(b + "\n");
    }
    void toggleTheme(){
    Theme.darkMode = !Theme.darkMode;

    display.setForeground(Theme.getTextColor());
    repaint();
}
}
