package heco.com.heco;

public class Account {

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 钱包地址
     */
    private String address;

    public Account(String privateKey, String address) {
        this.privateKey = privateKey;
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Account{" +
                "privateKey='" + privateKey + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
