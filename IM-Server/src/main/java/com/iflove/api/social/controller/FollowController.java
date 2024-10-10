package com.iflove.api.social.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Validated
@RestController
@RequestMapping("api/social")
@Tag(name = "社交模块")
public class FollowController {

}
