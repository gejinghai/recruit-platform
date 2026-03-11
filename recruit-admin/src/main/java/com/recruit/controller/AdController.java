package com.recruit.controller;

import com.recruit.dto.Result;
import com.recruit.entity.AdContent;
import com.recruit.entity.AdPosition;
import com.recruit.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AdController {

    @Autowired
    private AdService adService;

    @GetMapping("/home/banner")
    public Result<?> banner() {
        List<AdContent> list = adService.getContentByPositionKey("home_banner");
        return Result.success(list);
    }

    @GetMapping("/home/ads")
    public Result<?> ads() {
        List<AdPosition> positions = adService.getAllPositions();
        Map<String, List<AdContent>> adsMap = new HashMap<>();
        for (AdPosition position : positions) {
            List<AdContent> contents = adService.getContentByPositionKey(position.getPositionKey());
            adsMap.put(position.getPositionKey(), contents);
        }
        return Result.success(adsMap);
    }

    @GetMapping("/admin/ad/position/list")
    public Result<?> positions() {
        return Result.success(adService.getAllPositions());
    }

    @GetMapping("/admin/ad/content/list")
    public Result<?> contentList(@RequestParam(required = false) Long positionId) {
        if (positionId != null) {
            return Result.success(adService.getContentByPositionKey(
                adService.getAllPositions().stream()
                    .filter(p -> p.getId().equals(positionId))
                    .findFirst()
                    .map(AdPosition::getPositionKey)
                    .orElse("")
            ));
        }
        return Result.success(adService.getContentList());
    }

    @PostMapping("/admin/ad/content/save")
    public Result<?> saveContent(@RequestBody AdContent content) {
        boolean success;
        if (content.getId() != null) {
            success = adService.updateContent(content);
        } else {
            success = adService.addContent(content);
        }
        return success ? Result.success() : Result.error("保存失败");
    }

    @DeleteMapping("/admin/ad/content/{id}")
    public Result<?> deleteContent(@PathVariable Long id) {
        boolean success = adService.deleteContent(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}
