package ru.job4j.todo.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ItemService;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
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

    private void userSession(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }

    @GetMapping("/allItems")
    public String index(Model model, HttpSession session) {
        userSession(model, session);
        model.addAttribute("items", itemService.findAll());
        getDone(model);
        return "allItems";
    }

    @GetMapping("/formAddItem")
    public String addItem(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("item", new Item(user));
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        item.setUser(user);
        item.setDone(false);
        item.setCreated(new Date().toString());
        itemService.add(item);
        return "redirect:allItems";
    }

    @GetMapping("/item/{itemId}")
    public String formUpdatePost(Model model, HttpSession session, @PathVariable("itemId") int id) {
        userSession(model, session);
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
    public String completedItems(Model model, HttpSession session) {
        userSession(model, session);
        model.addAttribute("items", itemService.findAllCompleted());
        getDone(model);
        return "/completedItems";
    }

    @GetMapping("/newItems")
    public String notCompletedItems(Model model, HttpSession session) {
        userSession(model, session);
        model.addAttribute("items", itemService.findAllNewItems());
        getDone(model);
        return "newItems";
    }
}
