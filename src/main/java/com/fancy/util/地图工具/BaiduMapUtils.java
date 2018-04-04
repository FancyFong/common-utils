package com.fancy.util.地图工具;

import com.alibaba.fastjson.JSON;
import com.fancy.util.网络请求.OkHttpUtils;

import java.io.IOException;
import java.util.List;

/**
 * 〈百度API工具〉<br>
 *  1.Okhttp3 网络请求
 *  2.fastjson JSON转Bean对象
 * @author fangdaji
 * @create 2018/4/4
 * @since 1.0.0
 */
public class BaiduMapUtils {

    private static String AK = "";

    public static void main(String[] args) throws Exception {

        //根据经纬度获取地址
        BaiduCity city = getCity("22.65554995170482", "113.99819929195738");

        //根据地址和城市获取经纬度
        BaiduCityLocation location = getLocation("宝安区名优采购中心", "深圳市");
    }

    /**
     * 根据经纬度获取地址
     * @param lat
     * @param lng
     * @throws Exception
     */
    public static BaiduCity getCity(String lat, String lng) throws Exception {

        String url = "http://api.map.baidu.com/geocoder/v2/?location=%s,%s&output=json&pois=1&ak=%s";
        url = String.format(url,lat,lng,AK);

        try {
            String str = OkHttpUtils.getMethod(url, OkHttpUtils.setHeaders(null));
            BaiduCity parse = JSON.parseObject(str, BaiduCity.class);
            return parse;
        } catch (IOException e) {
            throw new Exception("网络请求失败");
        }
    }

    /**
     * 根据地址和城市获取经纬度
     *
     * @param address
     * @param city
     */
    public static BaiduCityLocation getLocation(String city, String address) throws Exception {

        String url = "http://api.map.baidu.com/geocoder/v2/?address=%s&city=%s&output=json&ak=%s";
        url = String.format(url,address,city,AK);

        try {
            String str = OkHttpUtils.getMethod(url, OkHttpUtils.setHeaders(null));
            BaiduCityLocation parse = JSON.parseObject(str, BaiduCityLocation.class);
            return parse;
        } catch (IOException e) {
            throw new Exception("网络请求失败");
        }

    }
}


class BaiduCityLocation {
    /**
     * 返回结果状态值， 成功返回0，其他值请查看下方返回码状态表。
     */
    private int status;


    private Result result;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public Result getResult() {
        return result;
    }

}

class Result {

    /**
     * 经纬度坐标
     */
    private Location location;

    /**
     * 位置的附加信息，是否精确查找。1为精确查找，即准确打点；0为不精确，即模糊打点（模糊打点无法保证准确度，不建议使用）。
     */
    private int precise;

    /**
     * 可信度，描述打点准确度，大于80表示误差小于100m。该字段仅作参考，返回结果准确度主要参考precise参数。
     */
    private int confidence;

    /**
     * 地址类型
     */
    private String level;

    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    public void setPrecise(int precise) {
        this.precise = precise;
    }
    public int getPrecise() {
        return precise;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
    public int getConfidence() {
        return confidence;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLevel() {
        return level;
    }

}

class Location {

    /**
     * 纬度值
     */
    private double lng;

    /**
     * 经度值
     */
    private double lat;

    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLat() {
        return lat;
    }
}

class BaiduCity {

    /**
     * 返回结果状态值， 成功返回0，其他值请查看下方返回码状态表。
     */
    private int status;
    private CityResult result;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setResult(CityResult result) {
        this.result = result;
    }
    public CityResult getResult() {
        return result;
    }

}

class CityResult {

    /**
     * 经纬度坐标
     */
    private Location location;

    /**
     * 结构化地址信息
     */
    private String formatted_address;

    /**
     * 坐标所在商圈信息，如 "人民大学,中关村,苏州街"。最多返回3个。
     */
    private String business;

    /**
     * 注意，国外行政区划，字段仅代表层级
     */
    private AddressComponent addressComponent;

    /**
     * 周边poi数组
     */
    private List<Pois> pois;

    /**
     * 结构化地址信息
     */
    private List<String> roads;


    private List<String> poiRegions;

    /**
     * 当前位置结合POI的语义化结果描述
     */
    private String sematic_description;

    /**
     * 城市id（不再更新）
     */
    private int cityCode;
    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
    public String getFormatted_address() {
        return formatted_address;
    }

    public void setBusiness(String business) {
        this.business = business;
    }
    public String getBusiness() {
        return business;
    }

    public void setAddressComponent(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }
    public AddressComponent getAddressComponent() {
        return addressComponent;
    }

    public void setPois(List<Pois> pois) {
        this.pois = pois;
    }
    public List<Pois> getPois() {
        return pois;
    }

    public void setRoads(List<String> roads) {
        this.roads = roads;
    }
    public List<String> getRoads() {
        return roads;
    }

    public void setPoiRegions(List<String> poiRegions) {
        this.poiRegions = poiRegions;
    }
    public List<String> getPoiRegions() {
        return poiRegions;
    }

    public void setSematic_description(String sematic_description) {
        this.sematic_description = sematic_description;
    }
    public String getSematic_description() {
        return sematic_description;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
    public int getCityCode() {
        return cityCode;
    }

}

class AddressComponent {

    /**
     * 国家
     */
    private String country;
    /**
     * 国家代码
     */
    private int country_code;

    private String country_code_iso;

    private String country_code_iso2;
    /**
     * 省名
     */
    private String province;
    /**
     * 城市名
     */
    private String city;
    private int city_level;
    /**
     * 区县名
     */
    private String district;
    /**
     * 乡镇名
     */
    private String town;
    /**
     * 行政区划代码 adcode映射表
     */
    private String adcode;
    /**
     * 街道名（行政区划中的街道层级）
     */
    private String street;
    /**
     * 街道门牌号
     */
    private String street_number;
    /**
     * 区县名
     */
    private String direction;
    /**
     * 相对当前坐标点的距离，当有门牌号的时候返回数据
     */
    private String distance;
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }
    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code_iso(String country_code_iso) {
        this.country_code_iso = country_code_iso;
    }
    public String getCountry_code_iso() {
        return country_code_iso;
    }

    public void setCountry_code_iso2(String country_code_iso2) {
        this.country_code_iso2 = country_code_iso2;
    }
    public String getCountry_code_iso2() {
        return country_code_iso2;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setCity_level(int city_level) {
        this.city_level = city_level;
    }
    public int getCity_level() {
        return city_level;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    public String getDistrict() {
        return district;
    }

    public void setTown(String town) {
        this.town = town;
    }
    public String getTown() {
        return town;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }
    public String getAdcode() {
        return adcode;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }
    public String getStreet_number() {
        return street_number;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {
        return direction;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDistance() {
        return distance;
    }

}

class Pois {

    private String addr;
    private String cp;
    private String direction;
    private String distance;
    private String name;
    private String poiType;
    private Point point;
    private String tag;
    private String tel;
    private String uid;
    private String zip;
    private Parent_poi parent_poi;
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getAddr() {
        return addr;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
    public String getCp() {
        return cp;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {
        return direction;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDistance() {
        return distance;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPoiType(String poiType) {
        this.poiType = poiType;
    }
    public String getPoiType() {
        return poiType;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
    public Point getPoint() {
        return point;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getTag() {
        return tag;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getTel() {
        return tel;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getZip() {
        return zip;
    }

    public void setParent_poi(Parent_poi parent_poi) {
        this.parent_poi = parent_poi;
    }
    public Parent_poi getParent_poi() {
        return parent_poi;
    }

}

class Parent_poi {

    private String name;
    private String tag;
    private String addr;
    private Point point;
    private String direction;
    private String distance;
    private String uid;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getTag() {
        return tag;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getAddr() {
        return addr;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
    public Point getPoint() {
        return point;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {
        return direction;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDistance() {
        return distance;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

}

class Point {

    private int x;
    private int y;
    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }

}