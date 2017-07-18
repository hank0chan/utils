package cn.hankchan.ext;

/**
 * @author hankChan
 *         2017/7/18 0018.
 */
public class AESConfig {

    private String password;

    public String getPassword() {
        return password;
    }
    private static class AESConfigHolder {
        private static AESConfig instance = new AESConfig();
    }
    public static AESConfig getInstance() {
        return AESConfigHolder.instance;
    }
    private AESConfig() {}

    /**
     * 设置AES加密工具的算法种子字符串
     * @param password 加密种子字符串（仅支持英文字母及数字组合）
     */
    public void setAESPassword(String password) {
        this.password = password;
    }
}
