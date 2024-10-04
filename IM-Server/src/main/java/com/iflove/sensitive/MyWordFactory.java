package com.iflove.sensitive;

import com.iflove.common.algorithm.sensitiveWord.WordFactory;
import com.iflove.sensitive.dao.SensitiveWordDao;
import com.iflove.sensitive.domain.SensitiveWord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Component
public class MyWordFactory implements WordFactory {
    @Resource
    private SensitiveWordDao sensitiveWordDao;

    @Override
    public List<String> getWordList() {
        return sensitiveWordDao.list()
                .stream()
                .map(SensitiveWord::getWord)
                .collect(Collectors.toList());
    }
}
