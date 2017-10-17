import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String path = "C:\\Users\\wille\\Documents\\Set.txt";
        new Parser( path ) ;
        new Parser( System.in );
    }
}
