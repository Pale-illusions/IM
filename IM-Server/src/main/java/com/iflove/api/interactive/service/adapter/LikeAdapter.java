package com.iflove.api.interactive.service.adapter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import com.iflove.api.interactive.domain.vo.response.ActionListResp;
import com.iflove.common.domain.vo.response.CursorPageBaseResp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class LikeAdapter {

    public static List<ActionListResp> buildActionListResp(List<Pair<Long, Double>> pairList) {
        return pairList.stream()
                .map(pair -> ActionListResp.builder()
                        .videoId(pair.getKey())
                        .date(DateUtil.date(pair.getValue().longValue()))
                        .build())
                .collect(Collectors.toList());
    }

    public static CursorPageBaseResp<ActionListResp> buildPageResp(CursorPageBaseResp<Pair<Long, Double>> page) {
        CursorPageBaseResp<ActionListResp> result = new CursorPageBaseResp<>();
        result.setCursor(page.getCursor());
        result.setIsLast(page.getIsLast());
        result.setList(buildActionListResp(page.getList()));
        return result;
    }
}
