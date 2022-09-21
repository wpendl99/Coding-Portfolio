package rescources;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy Playground Class for Production (Provides no use to server)
 */
public class dummy {
    public static void main(String[] args) {
        String test1 = "/fill";
        String test2 = "/fill/susan";
        String test3 = "/fill/susan/3";

        String[] arr = test3.replaceAll("^/", "").split("/");
        ArrayList<String> list = new ArrayList<>(List.of(arr));

        System.out.println(list);

        if(list.size() == 1){
            System.out.println("Size 1");
        } else if (list.size() == 2){
            System.out.println("Size 2");
        } else if (list.size() == 3){
            System.out.println("Size 3");
        } else {
            System.out.println("Error");
        }

    }
}
