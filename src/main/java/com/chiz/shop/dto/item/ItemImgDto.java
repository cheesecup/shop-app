package com.chiz.shop.dto.item;

import com.chiz.shop.domain.item.ItemImg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public ItemImgDto() {}

    public ItemImgDto(Long id, String imgName, String oriImgName, String imgUrl, String repImgYn) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;
    }

    public static ItemImgDto of(ItemImg itemImg) {
        return new ItemImgDto(itemImg.getId(),
                itemImg.getImgName(),
                itemImg.getOriImgName(),
                itemImg.getImgUrl(),
                itemImg.getRepImgYn());
    }
}
