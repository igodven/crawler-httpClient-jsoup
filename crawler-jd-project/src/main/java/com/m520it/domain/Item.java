package com.m520it.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "jd_item")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long itemId;
    @Column(name ="sku")
    private Long itemSku;
    @Column(name = "spu")
    private Long itemSpu;
    @Column(name = "title")
    private String itemTitle;
    @Column(name = "price")
    private Long itemPrice;
    @Column(name = "pic")
    private String itemPic;
    @Column(name = "url")
    private String itemUrl;
    @Column(name = "created")
    private Date itemCreatTime;
    @Column(name = "updated")
    private Date itemUpdateTime;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemSku() {
        return itemSku;
    }

    public void setItemSku(Long itemSku) {
        this.itemSku = itemSku;
    }

    public Long getItemSpu() {
        return itemSpu;
    }

    public void setItemSpu(Long itemSpu) {
        this.itemSpu = itemSpu;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Long getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public Date getItemCreatTime() {
        return itemCreatTime;
    }

    public void setItemCreatTime(Date itemCreatTime) {
        this.itemCreatTime = itemCreatTime;
    }

    public Date getItemUpdateTime() {
        return itemUpdateTime;
    }

    public void setItemUpdateTime(Date itemUpdateTime) {
        this.itemUpdateTime = itemUpdateTime;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemSku=" + itemSku +
                ", itemSpu=" + itemSpu +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemPic='" + itemPic + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                ", itemCreatTime=" + itemCreatTime +
                ", itemUpdateTime=" + itemUpdateTime +
                '}';
    }
}
