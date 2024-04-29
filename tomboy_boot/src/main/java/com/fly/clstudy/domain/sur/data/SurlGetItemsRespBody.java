package com.fly.clstudy.domain.sur.data;

import com.fly.clstudy.domain.sur.dto.SurlDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SurlGetItemsRespBody {
    private List<SurlDto> items;
}
