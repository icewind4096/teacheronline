package com.windvalley.guli.service.edu.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterVO {
    private String id;
    private String title;
    private Integer sort;
    private List<VideoVO> children = new ArrayList<VideoVO>();
}
