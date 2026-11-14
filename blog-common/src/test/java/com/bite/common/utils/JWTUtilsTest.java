package com.bite.common.utils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilsTest {

    @org.junit.jupiter.api.Test
    void genJwt() {
        Map<String,Object> claim = new HashMap<>();
        claim.put("id",1);
        claim.put("name","zhansan");
        System.out.print(JWTUtils.genJwt(claim));


    }
}