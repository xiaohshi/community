package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaSxh")
public class AlphaDaoSxhImpl implements AlphaDao{
    @Override
    public String select() {
        return "sxh";
    }
}
