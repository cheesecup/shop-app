package com.chiz.shop.domain.item;

import com.chiz.shop.domain.BaseEntity;
import com.chiz.shop.dto.request.item.ItemImgCreateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class ItemImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    private String imgName; // 저장된 이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 URL

    @Setter
    private String repImgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    protected ItemImg() {}

    private ItemImg(String imgName, String oriImgName, String imgUrl, String repImgYn, Item item) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;
        this.item = item;
    }

    private ItemImg(String imgName, String oriImgName, String imgUrl, String repImgYn) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;
    }

    private ItemImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }

    private ItemImg(String repImgYn, Item item) {
        this.repImgYn = repImgYn;
        this.item = item;
    }

    public static ItemImg of(String imgName, String oriImgName, String imgUrl, String repImgYn, Item item) {
        return new ItemImg(imgName, oriImgName, imgUrl, repImgYn, item);
    }

    public static ItemImg saveItemImg(String repImgYn, Item item) {
        return new ItemImg(repImgYn, item);
    }

    public void updateItemImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }

    public static ItemImg createItemImg(ItemImgCreateDto dto) {
        return new ItemImg(dto.getImgName(),
                dto.getOriImgName(),
                dto.getImgUrl(),
                dto.getRepImgYn());
    }

}
