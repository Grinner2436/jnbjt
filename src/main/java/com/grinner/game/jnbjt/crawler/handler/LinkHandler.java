package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;

public interface LinkHandler {
    void handle(String link, JSONObject context);
}
