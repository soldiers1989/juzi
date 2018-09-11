package com.jzfq.retail.core.swagger.api.manage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jzfq.retail.core.api.service.RedisService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @描述：一次性校验码
 * @author
 */
@Slf4j
@Controller
public class VerificationCodeController{

    @Autowired
    RedisService redisService;

    // 渲染随机背景颜色
    private Color getRandColor(int fc, int bc){
        Random random = new Random();
        if(fc>255) fc=255;
        if(bc>255) bc=255;
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
    }

    //渲染固定背景颜色
    private Color getBgColor(){
        return new Color(200,200,200);
    }


    private BufferedImage  drawImg(String code){
        return drawImg(false, code);
    }

    /**
     * 画验证码图形
     * @param isDrawLine
     * @return
     */
    private BufferedImage  drawImg(boolean isDrawLine, String code){
        // 在内存中创建图象
        int width=86, height=33;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        Random random = new Random();
        // 设定背景色
        g.setColor(getBgColor());
        g.fillRect(0, 0, width, height);
        // 设定字体
        g.setFont(new Font("Times New Roman",Font.PLAIN,22));
        // 画边框
        //g.setColor(new Color());
        //g.drawRect(0,0,width-1,height-1);

        /**随机产生155条干扰线，使图象中的认证码不易被其它程序探测到*/

        if(isDrawLine){
            g.setColor(getRandColor(160,200));
            for (int i=0;i<155;i++){
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x,y,x+xl,y+yl);
            }
        }

        // 取随机产生的认证码(8位数字和字母混合)
          for (int i=0;i<code.length();i++){
            char cc = code.charAt(i);
            if((i+1)%2==0){
                g.setColor(new Color(255,0,0));
            }else{
                g.setColor(new Color(0,0,0));
            }
            // 将认证码随机打印不同的颜色显示出来
            //g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(cc+"",13*i+16,23);
        }
        // 图象生效
        g.dispose();
        return image;
    }

    private String generateSRand(){
        Random random = new Random();
        String sRand="";
        char[] seds = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'};
        for (int i=0;i<4;i++){
            int index = random.nextInt(seds.length);
            char cc = seds[index];
            sRand+=cc;
        }
        return sRand;
    }

    private String encodeBase64ImgCode(String code)throws ServletException, IOException {
        BufferedImage codeImg = drawImg(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(codeImg, "JPEG", out);
        byte[] b = out.toByteArray();
        String imgString = Base64.encodeBase64String(b);
        return "data:image/JPEG;base64," + imgString;
    }


    /**
     * @描述：生成校验码
     * @return
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "图形验证码", notes = "图形验证码")
    @RequestMapping(value="/pictureCheck",method={RequestMethod.POST,RequestMethod.GET})
    public @ResponseBody ModelMap generateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelMap model = new ModelMap();
        //生成验证码uuid
        String uuid = UUID.randomUUID().toString();
        String code = generateSRand();
        //Redis缓存
        redisService.setTimesData(uuid, code, 60 * 5 * 1);//5分钟后失效
        model.put("uuid", uuid);

        // 设置页面不缓存
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);

        model.put("img",encodeBase64ImgCode(code));//获取通过base64加密后图形码字符串
        return model;
    }


    @RequestMapping("/hello")
    public @ResponseBody String test() {
        return "hello, world! This com from spring!";
    }

    @ApiOperation(value = "显示图形验证码", notes = "显示图形验证码")
    @RequestMapping(value="/springmvc/getOutImgCode")
    public void getOutImgCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=utf-8");
        String code = generateSRand();
        out.println("<img src=\""+encodeBase64ImgCode(code)+"\">");
        out.close();
    }



}