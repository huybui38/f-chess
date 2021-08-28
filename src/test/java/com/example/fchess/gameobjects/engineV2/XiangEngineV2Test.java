package com.example.fchess.gameobjects.engineV2;

import org.junit.Test;
import org.junit.Assert;

public class XiangEngineV2Test {
    @Test
    public void it_sould_count_all_moves() {
        XiangqiEngineV2 engineV2 = new XiangqiEngineV2("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR");
        Assert.assertEquals(44, engineV2.Perft(1));
        Assert.assertEquals(1920, engineV2.Perft(2));
//        Assert.assertEquals(79666, engineV2.Perft(3));
    }
}
