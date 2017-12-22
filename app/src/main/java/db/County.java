package db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lenovo on 2017/12/21.
 */
@Entity
public class County {

    @Id(autoincrement = true)
    private Long id;
    private String countyName;
    private String countyCode;
    private String cityId;
    @Generated(hash = 1148138338)
    public County(Long id, String countyName, String countyCode, String cityId) {
        this.id = id;
        this.countyName = countyName;
        this.countyCode = countyCode;
        this.cityId = cityId;
    }
    @Generated(hash = 1991272252)
    public County() {
    }
    @Override
    public String toString() {
        return "County{" +
                "id=" + id +
                ", countyName='" + countyName + '\'' +
                ", countyCode='" + countyCode + '\'' +
                ", cityId='" + cityId + '\'' +
                '}';
    }
    public String getCityId() {
        return this.cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public String getCountyCode() {
        return this.countyCode;
    }
    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }
    public String getCountyName() {
        return this.countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }



}
