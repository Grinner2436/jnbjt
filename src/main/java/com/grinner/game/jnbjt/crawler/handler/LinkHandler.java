package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;

public interface LinkHandler<Result> {
    Result handle(String link, JSONObject context);
}
