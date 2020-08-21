package com.grinner.game.jnbjt.crawler.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;

public class HtmlUtils {
    public static List<Node> trim(List<Node> nodes){
        List<Node> result = new ArrayList<>();
        boolean isNew = true;
        StringBuffer buffer = new StringBuffer();
        for (Node node : nodes) {
            if(node instanceof TextNode){
                buffer.append(((TextNode)node).getWholeText());
                isNew = false;
            }else {
                if(!isNew){
                    String text = buffer.toString()
                            .replaceAll("&nbsp;","")
                            .replaceAll("\\p{Zs}","")
                            ;
                    if(StringUtils.isNotBlank(text)){
                        TextNode textNode = new TextNode(text);
                        result.add(textNode);
                        isNew = true;
                        buffer = new StringBuffer();
                    }
                }
                result.add(node);
            }
        }
        if(!isNew){
            String text = buffer.toString()
                    .replaceAll("&nbsp;","")
                    .replaceAll("\\p{Zs}","")
                    ;
            if(StringUtils.isNotBlank(text)){
                TextNode textNode = new TextNode(text);
                result.add(textNode);
            }
        }
        return result;
    }
}
