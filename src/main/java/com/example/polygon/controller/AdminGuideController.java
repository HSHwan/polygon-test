package com.example.polygon.controller;

import com.example.polygon.dto.GuideForm;
import com.example.polygon.service.GuideRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.guideregistry.GuideRegistry;

import java.util.List;

@Controller
@RequestMapping("/admin/guide")
@RequiredArgsConstructor
public class AdminGuideController {

    private final GuideRegistryService guideService;

    @GetMapping
    public String guidePage(Model model) {
        model.addAttribute("guideForm", new GuideForm());
        return "guide-admin";
    }

    @PostMapping("/register")
    public String registerGuide(@ModelAttribute GuideForm guideForm, Model model) {
        try {
            String txHash = guideService.registerGuide(
                    guideForm.getGuideAddress(), guideForm.getGuideId(), guideForm.getMetadata()
            );
            model.addAttribute("message", "Registered! Tx: " + txHash);
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return guidePage(model);
    }

    @PostMapping("/update")
    public String updateGuide(@ModelAttribute GuideForm guideForm, Model model) {
        try {
            String txHash = guideService.updateMetadata(
                    guideForm.getGuideAddress(), guideForm.getMetadata()
            );
            model.addAttribute("message", "Updated! Tx: " + txHash);
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return guidePage(model);
    }

    @GetMapping("/view")
    public String viewGuide(@RequestParam String guideAddress, Model model) {
        try {
            GuideRegistry.Guide guide = guideService.getGuide(guideAddress);
            model.addAttribute("guide", guide);
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return guidePage(model);
    }

    @GetMapping("/all")
    public String viewAllGuides(Model model) {
        try {
            List<GuideRegistry.Guide> guides = guideService.getAllGuides();
            model.addAttribute("guides", guides);
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        model.addAttribute("guideForm", new GuideForm()); // 폼 초기화 필요
        return "guide-admin";
    }

}

