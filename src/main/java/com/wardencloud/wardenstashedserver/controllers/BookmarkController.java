package com.wardencloud.wardenstashedserver.controllers;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.Bookmark;
import com.wardencloud.wardenstashedserver.services.BookmarkService;
import com.wardencloud.wardenstashedserver.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/bookmark")
public class BookmarkController {
    @Autowired
    TokenService tokenService;

    @Autowired
    @Qualifier("BookmarkServiceImpl")
    BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity getBookmarkByUserId(@RequestHeader("token") String token) {
        JSONObject bookmarkObjects = new JSONObject();
        JSONObject data = new JSONObject();
        int userId = tokenService.getUserIdFromToken(token);
        List<Bookmark> bookmarks = bookmarkService.getBookmarkByUserId(userId);
        bookmarkObjects.put("bookmarks", bookmarks);
        data.put("data", bookmarkObjects);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping
    public ResponseEntity addBookmarkByUserId(@RequestHeader("token") String token, @RequestBody Map<String, String> payload) {
        JSONObject bookmarkObject = new JSONObject();
        JSONObject data = new JSONObject();
        int userId = tokenService.getUserIdFromToken(token);
        int conceptCardId = Integer.parseInt(payload.get("concept_card_id"));
        int bookmarkId = bookmarkService.addBookmarkByUserId(userId, conceptCardId);
        bookmarkObject.put("bookmark_id", bookmarkId);
        data.put("data", bookmarkObject);
        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping
    public ResponseEntity deleteBookmarkByConceptCardId(@RequestHeader("token") String token, @RequestBody Map<String, String> payload) {
        JSONObject bookmarkObject = new JSONObject();
        JSONObject data = new JSONObject();
        int userId = tokenService.getUserIdFromToken(token);
        int conceptCardId = Integer.parseInt(payload.get("concept_card_id"));
        int deletedBookmarkId = bookmarkService.deleteBookmarkByConceptCardId(userId, conceptCardId);
        bookmarkObject.put("concept_card_id", deletedBookmarkId);
        data.put("data", bookmarkObject);
        return ResponseEntity.ok().body(data);
    }
}
