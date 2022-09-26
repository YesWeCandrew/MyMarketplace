package Entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class CSVReader {

    public static void createItems() throws IOException {
        String splitBy = ",";
        String line = "";
        String path = "java/data/";

        BufferedReader br = new BufferedReader(new FileReader(path+"Items.csv"));

        Items items = Items.getInstance();

        while ((line = br.readLine()) != null) {
            String[] itemArray = line.split(splitBy);

            Items.Item item;
            item = new Items.Item(
                    Integer.parseInt(itemArray[0]),
                    itemArray[1],
                    itemArray[2],
                    Integer.parseInt(itemArray[3]),
                    itemArray[4],
                    Double.parseDouble(itemArray[5]),
                    itemArray[6],
                    itemArray[7],
                    itemArray[8],
                    itemArray[9]
            );

            items.addItem(item);

        }
    }

}
