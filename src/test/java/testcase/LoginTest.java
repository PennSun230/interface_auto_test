package testcase;

import com.alibaba.fastjson.JSONObject;
import common.BaseTest;
import data.Constants;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.ExcelPojo;

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
        List<ExcelPojo> listExcelData=readDataToSpecificRow(2,0,1) ;
        request(listExcelData.get(0),"Login");

    }

    @Test(dataProvider = "getLoginData")
    public void testLogin(ExcelPojo excelPojo){
          Response response=request(excelPojo,"Login") ;
          Map<String,Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected(),Map.class);
          for(String key:expectedMap.keySet()){
              Object expectValue =expectedMap.get(key) ;
              Object actualValue =response.jsonPath().get(key);
              Assert.assertEquals(expectValue,actualValue) ;

          }
    }

    @DataProvider
    public Object[] getLoginData(){
        List<ExcelPojo> listExcelData=readDataFromSpecificRow(2,1);
        return  listExcelData.toArray() ;

    }


}
