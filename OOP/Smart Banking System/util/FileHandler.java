package util;

import java.io.FileReader;
import java.io.IOException;

public class FileHandler {

    public static void readCustomerData()
            throws IOException {

        FileReader reader =
                new FileReader("customer.txt");

        reader.close();
    }
}
