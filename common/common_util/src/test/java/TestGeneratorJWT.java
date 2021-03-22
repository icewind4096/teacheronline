import com.windvalley.guli.common.base.entry.JWTInfo;
import com.windvalley.guli.common.base.util.JWTUtils;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

public class TestGeneratorJWT {
    @Test
    public void TestGeneratorJWT() throws UnsupportedEncodingException {
        JWTInfo jwtInfo = new JWTInfo();
        jwtInfo.setId("id");
        jwtInfo.setNickName("nickName");
        jwtInfo.setAvatar("avatar");

        System.out.println(JWTUtils.generatorJWT(jwtInfo));

        System.out.println(JWTUtils.checkJWTToken(JWTUtils.generatorJWT(jwtInfo)));

        char a = 01;
        String sourceData = a + "A这B";
        byte[] bytes = sourceData.getBytes();

        // Base64编码
        String base64Encode = DatatypeConverter.printBase64Binary(bytes);
        System.out.println(base64Encode);//6L+Z5pivQmFzZTY057yW56CB5YmN55qE5Y6f5aeL5pWw5o2u77yB77yB77yB

        // Base64解码
        byte[] parseBase64Binary = DatatypeConverter.parseBase64Binary(base64Encode);
        String base64Dncode = new String(parseBase64Binary, "utf-8");
        System.out.println(base64Dncode);//这是Base64编码前的原始数据！！！
    }
}
