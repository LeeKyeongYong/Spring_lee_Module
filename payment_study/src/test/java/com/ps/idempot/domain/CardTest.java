package com.ps.idempot.domain;


import com.ps.idempot.domain.vo.PayMoney;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CardTest {

    @Test
    void 만료일이_지나면_예외_발생한다() {
        // when & then
        assertThatThrownBy(() -> new PayMoney(1234, 1999, 02, 01))
                .isInstanceOf(IllegalArgumentException.class);
    }
}