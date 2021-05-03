package pojo;
import cn.afterturn.easypoi.excel.annotation.Excel ;
/**
 * @author penn
 * @date 2021/4/4 - 20:03
 * 柠檬班学习
 */
public class ExcelPojo {
    @Excel(name="caseId")
    private int caseId;

    @Excel(name="interfaceModule")
    private String interfaceName;

    @Excel(name="caseTitle")
    private String title;

    @Excel(name="requestHeader")
    private String requestHeader;

    @Excel(name="requestMethod")
    private String method;

    @Excel(name="interfaceUrl")
    private String url;

    @Excel(name="inputParams")
    private String inputParams;

    @Excel(name="expectedResult")
    private String expected;

    @Excel(name="extractField")
    private String extractResult;

    @Excel(name="databaseVerify")
    private String dbAssert;

    public String getDbAssert() {
        return dbAssert;
    }

    public void setDbAssert(String dbAssert) {
        this.dbAssert = dbAssert;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getExtractResult() {
        return extractResult;
    }

    public void setExtractResult(String extractResult) {
        this.extractResult = extractResult;
    }

    @Override
    public String toString() {
        return "ExcelPojo{" +
                "caseId=" + caseId +
                ", interfaceName='" + interfaceName + '\'' +
                ", title='" + title + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expected='" + expected + '\'' +
                ", extractResult='" + extractResult + '\'' +
                ", dbAssert='" + dbAssert + '\'' +
                '}';
    }
}
