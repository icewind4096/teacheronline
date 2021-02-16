package com.windvalley.guli.service.edu.entity.vo;

import com.windvalley.guli.service.edu.entity.Subject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVO implements Serializable {
    private String id;
    private String title;
    private int sort;

    private List<Subject> children = new ArrayList<Subject>();
}
