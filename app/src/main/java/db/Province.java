package db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lenovo on 2017/12/21.
 */

@Entity
public class Province {
    @Id(autoincrement = true)
    private Long id;
    private String ProvinceName;
    private String ProvinceCode;

    @Generated(hash = 1878631595)
    public Province(Long id, String ProvinceName, String ProvinceCode) {
        this.id = id;
        this.ProvinceName = ProvinceName;
        this.ProvinceCode = ProvinceCode;
    }

    @Generated(hash = 1309009906)
    public Province() {
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", ProvinceName='" + ProvinceName + '\'' +
                ", ProvinceCode='" + ProvinceCode + '\'' +
                '}';
    }

    public String getProvinceCode() {
        return this.ProvinceCode;
    }

    public void setProvinceCode(String ProvinceCode) {
        this.ProvinceCode = ProvinceCode;
    }

    public String getProvinceName() {
        return this.ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
