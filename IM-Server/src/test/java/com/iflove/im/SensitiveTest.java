package com.iflove.im;

import com.iflove.sensitive.algorithm.sensitiveWord.filter.ACFilter;
import com.iflove.sensitive.algorithm.sensitiveWord.filter.DFAFilter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
public class SensitiveTest {



    @Test
    public void DFA3() {
        List<String> sensitiveList = List.of("你", "你是", "我", "ok", "HELLO");
        DFAFilter instance = DFAFilter.getInstance();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("hElLo, ,bu, he,.llo"));
    }

    @Test
    public void DFA4() {
        List<String> sensitiveList = List.of("老婆");
        DFAFilter instance = DFAFilter.getInstance();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("老婆……\uD83E\uDD24嘿嘿………\n" +
                "\uD83E\uDD24……好可爱……嘿嘿……老婆\n" +
                "\uD83E\uDD24……老婆……我的\uD83E\uDD24……嘿嘿……\n" +
                "\uD83E\uDD24………亲爱的……赶紧让我抱一抱……啊啊啊老婆软软的脸蛋\n" +
                "\uD83E\uDD24还有软软的小手手……\n" +
                "\uD83E\uDD24…老婆……不会有人来伤害你的…\n" +
                "\uD83E\uDD24你就让我保护你吧嘿嘿嘿嘿嘿嘿嘿嘿\n" +
                "\uD83E\uDD24……太可爱了……\n" +
                "\uD83E\uDD24……美丽可爱的老婆……像珍珠一样……\n" +
                "\uD83E\uDD24嘿嘿……老婆……\uD83E\uDD24嘿嘿……" +
                "\uD83E\uDD24……好想一口吞掉……\uD83E\uDD24……但是舍不得啊……我的老婆\n" +
                "\uD83E\uDD24……嘿嘿……\uD83E\uDD24我的宝贝……我最可爱的老婆……\n" +
                "\uD83E\uDD24没有老婆……我就要死掉了呢……\uD83E\uDD24我的……\n" +
                "\uD83E\uDD24嘿嘿……可爱的老婆……嘿嘿\uD83E\uDD24……可爱的老婆……嘿嘿[爱心]\n" +
                "\uD83E\uDD24……可爱的老婆……[爱心]……嘿嘿\uD83E\uDD24……\n" +
                "可爱的老婆…（吸）身上的味道……好好闻～[爱心]…嘿嘿\n" +
                "\uD83E\uDD24……摸摸～……可爱的老婆……再贴近我一点嘛……（蹭蹭）嘿嘿\n" +
                "\uD83E\uDD24……可爱的老婆……嘿嘿\uD83E\uDD24……～亲一口～……可爱的老婆……嘿嘿\n" +
                "\uD83E\uDD24……抱抱你～可爱的老婆～（舔）喜欢～真的好喜欢～……（蹭蹭）脑袋要融化了呢～已经……除了老婆以外～什么都不会想了呢～[爱心]嘿嘿\n" +
                "\uD83E\uDD24……可爱的老婆……嘿嘿\uD83E\uDD24……可爱的老婆……我的～……嘿嘿\uD83E\uDD24……\n"));
    }

    @Test
    public void test5() {
        List<String> sensitiveList = Arrays.asList("abc", "ab", "a", "b", "bwd");
        DFAFilter instance = DFAFilter.getInstance();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("a,b,c,aa,b,bwd"));
    }

    @Test
    public void AC1() {
        List<String> sensitiveList = List.of("你", "你是", "我", "ok", "HELLo");
        ACFilter instance = new ACFilter();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("hElLo, ,bu, he,.llo"));
    }

    @Test
    public void DFAMulti() {
        List<String> sensitiveList = Arrays.asList("白痴", "你是白痴", "白痴吗");
        DFAFilter instance = DFAFilter.getInstance();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("你是白痴吗"));
    }


    @Test
    public void ACMulti() {
        List<String> sensitiveList = Arrays.asList("白痴", "你是白痴", "白痴吗");
        ACFilter instance = new ACFilter();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("你是白痴吗"));
    }

}
