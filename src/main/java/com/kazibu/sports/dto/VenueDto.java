package com.kazibu.sports.dto;

import java.util.List;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.entity.VenuePricing;
import com.kazibu.sports.entity.VenueCourt;
import com.kazibu.sports.entity.VenueImage;

public class VenueDto {

  // 场馆请求类（用于增删改查）
  public static class VenueRequest {
    private Long id;
    private String name;
    private String description;
    private Long sportsTypeId; // 体育类型ID
    private String province; // 省
    private String city; // 市
    private String district; // 区
    private String address;
    private Double latitude;
    private Double longitude;
    private String contactType;
    private String contactInfo;
    private Double rating;
    private Integer order;
    private String enabled;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Long getSportsTypeId() {
      return sportsTypeId;
    }

    public void setSportsTypeId(Long sportsTypeId) {
      this.sportsTypeId = sportsTypeId;
    }

    public String getProvince() {
      return province;
    }

    public void setProvince(String province) {
      this.province = province;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getDistrict() {
      return district;
    }

    public void setDistrict(String district) {
      this.district = district;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public Double getLatitude() {
      return latitude;
    }

    public void setLatitude(Double latitude) {
      this.latitude = latitude;
    }

    public Double getLongitude() {
      return longitude;
    }

    public void setLongitude(Double longitude) {
      this.longitude = longitude;
    }

    public String getContactType() {
      return contactType;
    }

    public void setContactType(String contactType) {
      this.contactType = contactType;
    }

    public String getContactInfo() {
      return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
      this.contactInfo = contactInfo;
    }

    public Double getRating() {
      return rating;
    }

    public void setRating(Double rating) {
      this.rating = rating;
    }

    public Integer getOrder() {
      return order;
    }

    public void setOrder(Integer order) {
      this.order = order;
    }

    public String getEnabled() {
      return enabled;
    }

    public void setEnabled(String enabled) {
      this.enabled = enabled;
    }
  }

  // 场馆详情响应类（包含收费标准、场地信息和图片列表）
  public static class VenueDetailResponse {
    private Venue venue;
    private List<VenuePricing> pricings; // 收费标准列表
    private List<VenueCourt> courts; // 场地列表
    private List<VenueImage> images; // 图片列表

    public VenueDetailResponse() {
    }

    public VenueDetailResponse(Venue venue, List<VenuePricing> pricings, List<VenueCourt> courts,
        List<VenueImage> images) {
      this.venue = venue;
      this.pricings = pricings;
      this.courts = courts;
      this.images = images;
    }

    public Venue getVenue() {
      return venue;
    }

    public void setVenue(Venue venue) {
      this.venue = venue;
    }

    public List<VenuePricing> getPricings() {
      return pricings;
    }

    public void setPricings(List<VenuePricing> pricings) {
      this.pricings = pricings;
    }

    public List<VenueCourt> getCourts() {
      return courts;
    }

    public void setCourts(List<VenueCourt> courts) {
      this.courts = courts;
    }

    public List<VenueImage> getImages() {
      return images;
    }

    public void setImages(List<VenueImage> images) {
      this.images = images;
    }
  }

  // 场馆列表项（用于 /sports_venue/list 返回）
  public static class VenueListItem {
    private Long id;
    private String name;
    private String description;
    private Long sportsTypeId;
    private String sportsTypeName;
    private String province; // 省
    private String city; // 市
    private String district; // 区
    private String address;
    private Double latitude;
    private Double longitude;
    private String contactType;
    private String contactInfo;
    private Double rating;
    private Integer order;
    private String enabled;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Long getSportsTypeId() {
      return sportsTypeId;
    }

    public void setSportsTypeId(Long sportsTypeId) {
      this.sportsTypeId = sportsTypeId;
    }

    public String getSportsTypeName() {
      return sportsTypeName;
    }

    public void setSportsTypeName(String sportsTypeName) {
      this.sportsTypeName = sportsTypeName;
    }

    public String getProvince() {
      return province;
    }

    public void setProvince(String province) {
      this.province = province;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getDistrict() {
      return district;
    }

    public void setDistrict(String district) {
      this.district = district;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public Double getLatitude() {
      return latitude;
    }

    public void setLatitude(Double latitude) {
      this.latitude = latitude;
    }

    public Double getLongitude() {
      return longitude;
    }

    public void setLongitude(Double longitude) {
      this.longitude = longitude;
    }

    public String getContactType() {
      return contactType;
    }

    public void setContactType(String contactType) {
      this.contactType = contactType;
    }

    public String getContactInfo() {
      return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
      this.contactInfo = contactInfo;
    }

    public Double getRating() {
      return rating;
    }

    public void setRating(Double rating) {
      this.rating = rating;
    }

    public Integer getOrder() {
      return order;
    }

    public void setOrder(Integer order) {
      this.order = order;
    }

    public String getEnabled() {
      return enabled;
    }

    public void setEnabled(String enabled) {
      this.enabled = enabled;
    }
  }
}
