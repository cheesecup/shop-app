package com.chiz.shop.dto.response.item;

import com.chiz.shop.domain.item.ItemImg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgResponse {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    public ItemImgResponse() {
    }

    @Builder
    public ItemImgResponse(Long id, String imgName, String oriImgName, String imgUrl, String repImgYn) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;
    }

    public static ItemImgResponse of(ItemImg itemImg) {
        return ItemImgResponse.builder()
                .id(itemImg.getId())
                .imgName(itemImg.getImgName())
                .oriImgName(itemImg.getOriImgName())
                .imgUrl(itemImg.getImgUrl())
                .repImgYn(itemImg.getRepImgYn())
                .build();
    }
}
