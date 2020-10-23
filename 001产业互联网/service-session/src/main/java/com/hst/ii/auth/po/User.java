package com.hst.ii.auth.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    @JsonIgnore
    private String sid;
    @JsonProperty("u")
    private String id;
    @JsonProperty("n")
    private String name;
    @JsonProperty("o")
    private String org;
    @JsonProperty("s")
    private String state;
    @JsonIgnore
    private String passwd;
}
