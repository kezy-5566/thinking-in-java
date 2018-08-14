package xyz.vimtool.chapter8.section2;

/**
 * 只有普通的方法调用可以是多态的
 * 类的属性及静态方法不可多态
 *
 * @author  zhangzheng
 * @version 1.0.0
 * @since   jdk1.8
 * @date    2018/8/14
 */
public class FieldAccess {

    public static void main(String[] args) {
        Super sup = new Sub();
        System.out.println("sup.field = " + sup.field + ", sup.getField() = " + sup.getField());

        Sub sub = new Sub();
        System.out.println("sub.field = " + sub.field + ", sub.getField() = " + sub.getField() +
                ", sub.getSuperField() = " + sub.getSuperField());
    }
}

class Super {

    public int field = 0;

    public int getField() {
        return field;
    }
}

class Sub extends Super {

    public int field = 1;

    @Override
    public int getField() {
        return field;
    }

    public int getSuperField() {
        return super.field;
    }
}