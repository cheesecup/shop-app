package com.chiz.shop.dto.request.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemImgCreateDto {

    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

}
