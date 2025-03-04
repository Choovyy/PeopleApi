package com.example.PeopleApi_Hermosisima.Controller;

import com.example.PeopleApi_Hermosisima.Service.GoogleContactsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GoogleContactsController {

    private final GoogleContactsService contactsService;

    public GoogleContactsController(GoogleContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping("/contacts")
    public String getContacts(Model model, OAuth2User principal) {
        Map<String, Object> contacts = contactsService.getContacts(principal);
        model.addAttribute("connections", contacts.get("connections")); // Rename to "connections"
        return "contacts";
    }

    // 🔹 Create Contact Form
    @GetMapping("/contacts/new")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new HashMap<>());
        return "create-contact";
    }

        // 🔹 Handle Create Submission
    @PostMapping("/contacts")
    public String createContact(@RequestParam Map<String, String> formData, OAuth2User principal) {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("names", List.of(Map.of("givenName", formData.get("firstName"), "familyName", formData.get("lastName"))));
        newContact.put("emailAddresses", List.of(Map.of("value", formData.get("email"))));

        contactsService.createContact(principal, newContact);
        return "redirect:/contacts";
    }

     // 🔹 Delete Contact
     @PostMapping("/contacts/delete")
     public String deleteContact(@RequestParam String resourceName, OAuth2User principal) {
         contactsService.deleteContact(principal, resourceName);
         return "redirect:/contacts";
     }

     
}
