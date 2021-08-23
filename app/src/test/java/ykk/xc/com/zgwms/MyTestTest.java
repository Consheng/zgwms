package ykk.xc.com.zgwms;

import org.junit.Test;

import ykk.xc.com.zgwms.comm.Comm;
import ykk.xc.com.zgwms.util.BigdecimalUtil;

public class MyTestTest {

    @Test
    public void main() {
        String a = "河南省 开封市 祥符区 经一路与王白路交叉口（向东100米路北，祥云植保院内）";
        if(a.length() > 22) {
            System.out.println(a.substring(0,22));
            System.out.println(a.substring(22,a.length()));
        } else {
            System.out.println(a);
        }
    }
}