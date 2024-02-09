package coms309.items;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;
/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Dhairya Kachalia
 */
@RestController
public class ItemsController {

    HashMap<String, Items> itemsList = new HashMap<>();

    @GetMapping("/items")
    public @ResponseBody HashMap<String,Items> getAllItems() {
        return this.itemsList;
    }

    @PostMapping("/items")
    public @ResponseBody String createItem(@RequestBody Items item) {
        System.out.println(item);
        itemsList.put(item.getItemName(), item);
        return "New item "+ item.getItemName() + " Saved";
    }

    @GetMapping("/items/{itemName}")
    public @ResponseBody Items getItem(@PathVariable String itemName) {
        Items item = itemsList.get(itemName);
        return item;
    }

    @PutMapping("/items/{itemName}")
    public @ResponseBody Items updateItem(@PathVariable String itemName, @RequestBody Items item) {
        itemsList.replace(itemName, item);
        return itemsList.get(itemName);
    }

    @DeleteMapping("/items/{itemName}")
    public @ResponseBody HashMap<String, Items> deleteItem(@PathVariable String itemName) {
        itemsList.remove(itemName);
        return itemsList;
    }
}
/*

@RestController
public class ItemsController {

    // Create a HashMap to store the list of items.
    private HashMap<Integer, Items> itemsList = new HashMap<>();

    // Create operation to add an item to the list.
    @PostMapping("/items")
    public @ResponseBody String createItem(@RequestBody Items item) {
        System.out.println(item);
        itemsList.put(item.getItemName(), item);
        return "New person "+ item.getItemName() + " Saved";

        // Add the item to the list using the barcode as the key.
        itemsList.put(barcode, item);

        return "New item added with barcode: " + barcode;
    }

    // Read operation to get an item by barcode.
    @GetMapping("/items/{barcode}")
    public @ResponseBody Items getItemByBarcode(@PathVariable int barcode) {
        return itemsList.get(barcode);
    }

    // Update operation to modify an item by barcode.
    @PutMapping("/items/{barcode}")
    public @ResponseBody Items updateItemByBarcode(@PathVariable int barcode, @RequestBody Items updatedItem) {
        // Check if the item with the given barcode exists.
        if (itemsList.containsKey(barcode)) {
            Items item = itemsList.get(barcode);
            item.setItemName(updatedItem.getItemName());
            item.setPrice(updatedItem.getPrice());
            return item;
        } else {
            // Handle the case where the item with the given barcode is not found.
            return null;
        }
    }

    // Delete operation to remove an item by barcode.
    @DeleteMapping("/items/{barcode}")
    public @ResponseBody String deleteItemByBarcode(@PathVariable int barcode) {
        // Check if the item with the given barcode exists.
        if (itemsList.containsKey(barcode)) {
            itemsList.remove(barcode);
            return "Item with barcode " + barcode + " deleted.";
        } else {
            // Handle the case where the item with the given barcode is not found.
            return "Item with barcode " + barcode + " not found.";
        }
    }

    // Generate a unique barcode (you may implement your own logic here).
    private int generateUniqueBarcode() {
        // Replace this with your logic to generate a unique barcode.
        // For simplicity, let's increment a counter.
        return itemsList.size() + 1;
    }
}
*/
