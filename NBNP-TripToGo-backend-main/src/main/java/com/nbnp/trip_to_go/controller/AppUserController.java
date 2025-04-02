package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.AppUserWithTripsDTO;
import com.nbnp.trip_to_go.service.AppUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{userId}")
    public AppUserDTO getAppUser(@PathVariable int userId) {
        return appUserService.getAppUser(userId);
    }

    @GetMapping("/me")
    public AppUserWithTripsDTO getCurrentAppUser() {
        return appUserService.getCurrentAppUser();
    }

    @PutMapping("/me")
    public AppUserWithTripsDTO updateAppUser(@RequestBody AppUserDTO appUserDTO) {
        return appUserService.updateAppUser(appUserDTO);
    }
}
