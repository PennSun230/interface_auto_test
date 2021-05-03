package teststeps;

import com.alibaba.fastjson.JSONObject;
import common.BaseTest;
import data.EnvironmentConstant;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.ExcelPojo;
import util.PhoneRandomUtil;
import util.RSATool;

import java.util.List;
import java.util.Map;

/**
 * @author penn
 * @date 2021/4/5 --- 21:26
 * 柠檬班学习
 */
public class RechargeTest extends BaseTest {

    @BeforeClass
    public void setup() {
        String phone = PhoneRandomUtil.getUnregisteredPhone();
        EnvironmentConstant.environmentData.put("phone", phone);
        List<ExcelPojo> listExcelData = readDataToSpecificRow(2, 0, 2);
        ExcelPojo excelPojo = listExcelData.get(0);
        excelPojo = casesReplace(excelPojo);
        Response requestRegister = request(excelPojo, "Recharge");
        extractToEnvironment(excelPojo, requestRegister);
        casesReplace(listExcelData.get(1));
        Response loginResponse = request(listExcelData.get(1), "Recharge");
        extractToEnvironment(listExcelData.get(1), loginResponse);
    }

    @Test(dataProvider = "getRechargeData")
    public void testLogin(ExcelPojo excelPojo) throws Exception {
        RSATool.signRequest();
        excelPojo = casesReplace(excelPojo);
        Response response = request(excelPojo, "Recharge");
        Map<String, Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected(), Map.class);
        for (String key : expectedMap.keySet()) {
            Object expectedValue = expectedMap.get(key);
            Object actualValue = response.jsonPath().get(key);
            if (!key.equalsIgnoreCase("data.amount")) {
                Assert.assertEquals(expectedValue, actualValue);
            } else {
                expectedValue = Integer.parseInt(expectedValue.toString());
                actualValue = Integer.parseInt(actualValue.toString());
                Assert.assertEquals(expectedValue, actualValue);
            }
        }
    }

    @DataProvider
    public Object[] getRechargeData() {
        List<ExcelPojo> listExcelData = readDataFromSpecificRow(2, 2);
        return listExcelData.toArray();
    }
    @AfterTest
    public void teardown(){

    }
}



