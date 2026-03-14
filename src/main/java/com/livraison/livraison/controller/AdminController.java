package com.livraison.livraison.controller;

import com.livraison.livraison.model.Livraison;
import com.livraison.livraison.repository.LivraisonRepository;
import com.livraison.livraison.service.ExcelImportService;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class AdminController {

    private final LivraisonRepository repo;

    public AdminController(LivraisonRepository repo){
        this.repo = repo;
    }

    // 📋 PAGE ADMIN
    @GetMapping("/admin")
    public String admin(Model model){

        List<Livraison> livraisons = repo.findAll();
        model.addAttribute("livraisons", livraisons);

        return "admin";
    }

    // ➕ AJOUTER LIVRAISON
    @PostMapping("/admin/ajouter")
    public String ajouter(Livraison livraison){

        livraison.setStatut("EN_ATTENTE");

        repo.save(livraison);

        return "redirect:/admin";
    }

    // ❌ SUPPRIMER
    @GetMapping("/admin/supprimer/{id}")
    public String supprimer(@PathVariable Long id){

        repo.deleteById(id);

        return "redirect:/admin";
    }

    // ✏ MODIFIER STATUT
    @GetMapping("/admin/edit/{id}")
public String editLivraison(@PathVariable Long id, Model model){

    Livraison livraison = repo.findById(id).orElse(null);

    model.addAttribute("livraison", livraison);

    return "modifier";
}
@PostMapping("/admin/update")
public String updateLivraison(Livraison livraison){

    repo.save(livraison);

    return "redirect:/admin";
}
    // 📥 IMPORT EXCEL
    @PostMapping("/admin/import")
    public String importExcel(@RequestParam("file") MultipartFile file){

        try {

            List<Livraison> list = ExcelImportService.importExcel(file.getInputStream());

            repo.saveAll(list);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/admin";
    }
}