package teststeps;

import com.alibaba.fastjson.JSONObject;
import common.BaseTest;
import data.EnvironmentConstant;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.ExcelPojo;
import util.PhoneRandomUtil;

import java.util.List;
import java.util.Map;

/**
 * @author penn
 * @date 2021/4/4 - 22:42
 * 柠檬班学习
 */
public class LoginTest extends BaseTest {

    @BeforeTest
    public void setup(){
        String phone = PhoneRandomUtil.getUnregisteredPhone();
        EnvironmentConstant.environmentData .put("phone",phone);
        List<ExcelPojo> listExcelData=readDataToSpecificRow(1,0,1) ;
        ExcelPojo excelPojo = listExcelData.get(0);
        excelPojo =casesReplace(excelPojo);
        Response res = request(excelPojo,"login");
        extractToEnvironment(excelPojo,res);

    }

    @Test(dataProvider = "getLoginData")
    public void testLogin(ExcelPojo excelPojo){
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"login");
        assertResponse(excelPojo,res);
    }


    @DataProvider
    public Object[] getLoginData(){
        List<ExcelPojo> listExcelData=readDataFromSpecificRow(1,1);
        return  listExcelData.toArray() ;

    }

<<<<<<< HEAD
//    @AfterTest
//    public void teardown(){
//
//    }
=======
    @AfterTest
    public void teardown(){

    }
>>>>>>> d98b6b91323e63e11f7b2afef7f37797bab36148

}
