package com.artaeum.profile.controller;

import com.artaeum.profile.config.Constants;
import com.artaeum.profile.controller.error.InternalServerException;
import com.artaeum.profile.controller.error.NotFoundException;
import com.artaeum.profile.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/images")
public class ProfileImagesController {

    private ImageService imageService;

    public ProfileImagesController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/avatar/{userId:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String userId) throws IOException {
        return this.prepareResponseEntity(Constants.AVATAR_IMAGE_DIR, userId);
    }

    @GetMapping("/background/{userId:.+}")
    public ResponseEntity<Resource> getBackground(@PathVariable String userId) throws IOException {
        return this.prepareResponseEntity(Constants.BACKGROUND_IMAGE_DIR, userId);
    }

    @PostMapping("/avatar")
    public void saveAvatarForUser(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        this.imageService.save(image, Constants.AVATAR_IMAGE_DIR, principal.getName());
    }

    @PostMapping("/background")
    public void saveBackgroundForUser(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        this.imageService.save(image, Constants.BACKGROUND_IMAGE_DIR, principal.getName());
    }

    @ExceptionHandler(IOException.class)
    public void handleIOException() {
        throw new InternalServerException();
    }

    private ResponseEntity<Resource> prepareResponseEntity(String type, String userId) throws IOException {
        Resource image = this.imageService.load(type, userId);
        if (!image.exists()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
