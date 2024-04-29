package com.fly.clstudy.sur.data;

import com.fly.clstudy.sur.dto.SurlDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SurlGetItemsRespBody {
    private List<SurlDto> items;
}
