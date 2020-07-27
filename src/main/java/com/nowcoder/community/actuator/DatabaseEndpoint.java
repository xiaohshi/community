package com.nowcoder.community.actuator;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.nowcoder.community.util.CommunityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "database")
@Slf4j
public class DatabaseEndpoint {

    @Autowired
    private DataSource dataSource;

    @ReadOperation
    public String checkConnection() {
        try (
                Connection connection = dataSource.getConnection()
        ) {
            return CommunityUtil.getJsonString(0, "获取连接成功");
        } catch (SQLException e) {
            log.error("获取连接失败");
            return CommunityUtil.getJsonString(0, "获取连接失败");
        }
    }

}
