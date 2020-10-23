package com.hst.ii.flow.po;

import com.hst.core.annotation.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodePropConfig {
    private String Id;
    private String name;
    private String editor;
    private String ext;
}
