package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdatePerson {
    public UUID id;
    public String name;
    public UUID roleId;
    public String password;
    public String image;
}
