package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateProductCategory {
    public UUID id;
    public String name;
    public String textTip;
    public String image;
    public UUID parentId;
}
