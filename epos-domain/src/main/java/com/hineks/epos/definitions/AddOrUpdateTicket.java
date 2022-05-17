package com.hineks.epos.definitions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddOrUpdateTicket {
    public UUID id;
    public int status;
    public int type;
}
