package com.iflove.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Getter
public class VideoScoreComputeEvent extends ApplicationEvent {
    private final Long videoId;
    public VideoScoreComputeEvent(Object source, Long videoId) {
        super(source);
        this.videoId = videoId;
    }
}
