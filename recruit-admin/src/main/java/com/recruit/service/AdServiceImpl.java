package com.recruit.service;

import com.recruit.entity.AdContent;
import com.recruit.entity.AdPosition;
import com.recruit.mapper.AdContentMapper;
import com.recruit.mapper.AdPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdPositionMapper adPositionMapper;

    @Autowired
    private AdContentMapper adContentMapper;

    @Override
    public List<AdPosition> getAllPositions() {
        return adPositionMapper.selectAll();
    }

    @Override
    public List<AdContent> getContentByPositionKey(String positionKey) {
        return adContentMapper.selectByPositionKey(positionKey);
    }

    @Override
    public List<AdContent> getContentList() {
        return adContentMapper.selectAll();
    }

    @Override
    public AdContent getContentById(Long id) {
        return adContentMapper.selectById(id);
    }

    @Override
    public boolean addContent(AdContent content) {
        return adContentMapper.insert(content) > 0;
    }

    @Override
    public boolean updateContent(AdContent content) {
        return adContentMapper.update(content) > 0;
    }

    @Override
    public boolean deleteContent(Long id) {
        return adContentMapper.delete(id) > 0;
    }
}
