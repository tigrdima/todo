package ru.job4j.todo.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

import java.util.Date;

@ThreadSafe
@Controller
public class ItemController {
    private final ItemService itemService;
    private static final String TRUE_DONE = "Выполненная";
    private static final String FALSE_DONE = "Невыполненная";

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    private void getDone(Model model) {
        model.addAttribute("trueDone", TRUE_DONE);
        model.addAttribute("falseDone", FALSE_DONE);
    }

    @GetMapping("/allItems")
    public String index(Model model) {
        model.addAttribute("items", itemService.findAll());
        getDone(model);
        return "allItems";
    }

    @GetMapping("/formAddItem")
    public String addItem(Model model) {
        model.addAttribute("item", new Item());
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item) {
        item.setDone(false);
        item.setCreated(new Date().toString());
        itemService.add(item);
        return "redirect:allItems";
    }

    @GetMapping("/item/{itemId}")
    public String formUpdatePost(Model model, @PathVariable("itemId") int id) {
        model.addAttribute("item", itemService.findById(id));
        getDone(model);
        return "item";
    }

    @GetMapping("/formUpdateItem")
    public String formUpdateItem(Model model, @ModelAttribute Item item) {
        model.addAttribute("item", item);
        return "updateItem";
    }

    @PostMapping("/updateItem")
    public String updateItem(@ModelAttribute Item item) {
        item.setCreated(new Date().toString());
        item.setDone(false);
        itemService.update(item);
        return "redirect:newItems";
    }

    @PostMapping("/updateDoneItem")
    public String updateDoneItem(@ModelAttribute Item item) {
        item.setDone(true);
        itemService.updateDone(item);
        return "redirect:completedItems";
    }

    @PostMapping("/deleteItem")
    public String deleteItem(@ModelAttribute Item item) {
        itemService.delete(item);
        return "redirect:allItems";
    }

    @GetMapping("/completedItems")
    public String completedItems(Model model) {
        model.addAttribute("items", itemService.findAllCompleted());
        getDone(model);
        return "/completedItems";
    }

    @GetMapping("/newItems")
    public String notCompletedItems(Model model) {
        model.addAttribute("items", itemService.findAllNewItems());
        getDone(model);
        return "newItems";
    }
}
