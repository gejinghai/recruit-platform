package com.recruit.service;

import com.recruit.entity.AdContent;
import com.recruit.entity.AdPosition;
import java.util.List;

public interface AdService {
    List<AdPosition> getAllPositions();

    List<AdContent> getContentByPositionKey(String positionKey);

    List<AdContent> getContentList();

    AdContent getContentById(Long id);

    boolean addContent(AdContent content);

    boolean updateContent(AdContent content);

    boolean deleteContent(Long id);
}
