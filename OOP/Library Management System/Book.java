public class Book {
    String title;
    String author;

    Book(String t, String a){
        title = t;
        author = a;
    }

    public String toString(){
        return "📖 " + title + " — " + author;
    }
}
