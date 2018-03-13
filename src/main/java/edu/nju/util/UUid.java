package edu.nju.util;

import java.util.UUID;

public class UUid {
	public String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
