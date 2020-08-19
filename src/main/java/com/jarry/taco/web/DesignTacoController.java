package com.jarry.taco.web;

import com.jarry.taco.Ingredient;
import com.jarry.taco.Order;
import com.jarry.taco.Taco;
import com.jarry.taco.User;
import com.jarry.taco.data.IngredientRepository;
import com.jarry.taco.data.TacoRepository;
import com.jarry.taco.data.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jarry.taco.Ingredient.Type;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@RequiredArgsConstructor
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private final TacoRepository tacoRepo;
    private final UserRepository userRepository;

    @GetMapping
    public String showDesignForm(Model model, Principal principal) {

        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredients::add);

        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                          .filter(x -> x.getType().equals(type))
                          .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }

        Taco saved = tacoRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }

}
