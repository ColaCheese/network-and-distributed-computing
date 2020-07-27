package bean;

/**
 * Used to represent a person
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class Person {

    /** 姓名 */
    private String name;
    /** 密码 */
    private String passWard;

    /**
     * 构造函数
     *
     * @param name     姓名
     * @param passWard 密码
     */
    public Person(String name, String passWard) {
        this.name = name;
        this.passWard = passWard;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getPersonName() {
        return this.name;
    }

    /**
     * 获取密码
     *
     * @return 用户密码
     */
    public String getPersonPassWard() {
        return this.passWard;
    }
}
