package heco.com.heco.entity;

public class Member {

    private int memberId;
    private int menberType;
    private int permType;

    public Member(int memberId, int menberType, int permType) {
        this.memberId = memberId;
        this.menberType = menberType;
        this.permType = permType;
    }

    public Member() {

    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getMenberType() {
        return menberType;
    }

    public void setMenberType(int menberType) {
        this.menberType = menberType;
    }

    public int getPermType() {
        return permType;
    }

    public void setPermType(int permType) {
        this.permType = permType;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", menberType=" + menberType +
                ", permType=" + permType +
                '}';
    }
}
