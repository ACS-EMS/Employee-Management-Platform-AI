package com.ems.controller;

import com.ems.entity.Settings;
import com.ems.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    // Get current settings
    @GetMapping("/current")
    public ResponseEntity<?> getSettings() {

        Settings settings = settingsService.getSettings();

        if (settings == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(settings);
    }

    // Save or update settings
    @PutMapping("/save")
    public ResponseEntity<Settings> saveSettings(
            @RequestBody Settings settings) {

        Settings savedSettings =
                settingsService.saveSettings(settings);

        return ResponseEntity.ok(savedSettings);
    }
}