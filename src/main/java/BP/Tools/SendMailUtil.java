package BP.Tools;

import org.apache.commons.mail.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendMailUtil {
    private static Map<String, String> hostMap = new HashMap<String, String>();
    private static final String userAddr = "XXX@XX.com";//发件人地址
    private static final String password = "XXX"; //发件人密码发件邮箱为qq邮箱。且开启ssl服务时，要使用授权码发送邮件，所以一般不推荐QQ邮箱

    static {
        // 126
        hostMap.put ( "smtp.126", "smtp.126.com" );
        // qq
        hostMap.put ( "smtp.qq", "smtp.qq.com" );

        // 163
        hostMap.put ( "smtp.163", "smtp.163.com" );

        // sina
        hostMap.put ( "smtp.sina", "smtp.sina.com.cn" );

        // tom
        hostMap.put ( "smtp.tom", "smtp.tom.com" );

        // 263
        hostMap.put ( "smtp.263", "smtp.263.net" );

        // yahoo
        hostMap.put ( "smtp.yahoo", "smtp.mail.yahoo.com" );

        // hotmail
        hostMap.put ( "smtp.hotmail", "smtp.live.com" );

        // gmail
        hostMap.put ( "smtp.gmail", "smtp.gmail.com" );

        //buaa
        hostMap.put ( "smtp.buaa", "smtp.buaa.edu.cn");

    }
    //正则验证发件邮箱并提取组成邮箱服务器信息
    public static String getemail ( String email) throws Exception {
        Pattern pattern = Pattern.compile ( "\\w+@(\\w+)(\\.\\w+){1,2}" );//邮箱正则
        Matcher matcher = pattern.matcher ( email );
        String key = "unSupportEmail";
        if (matcher.find ()) {
            key = "smtp." + matcher.group ( 1 );//匹配第一个括号内的关键词，如buaa，gmail
        }
        if (hostMap.containsKey ( key )) {
            System.out.println(matcher.group ( 1 ));
            return hostMap.get ( key );
        } else {
            throw new Exception ( "unSupportEmail" );
        }
    }
    //最基本的发件形式，发送主题与正文，实际使用可被附件发送代替，附件地址为空与普邮功能相同
    public static void SendTextEmail(String toMailAddr, String subject, String message) throws Exception {
        SimpleEmail email = new SimpleEmail();
        email.setHostName (getemail(userAddr)) ;
        try {
            // 收件人邮箱
            email.addTo ( toMailAddr );
            // 邮箱服务器身份验证
            email.setAuthentication(userAddr, password);
            // 发件人邮箱
            email.setFrom(userAddr);
            // 邮件主题
            email.setSubject ( subject );
            // 邮件内容
            email.setMsg( message );
            // 发送邮件
            email.send();
            System.out.println("邮件发送成功");
    } catch (EmailException ex) {
        ex.printStackTrace();
        }
    }
    //附件发送
    public static void SendFileEmail(String toMailAddr, String subject, String message, String FilePath) throws Exception {
        MultiPartEmail email = new MultiPartEmail();
        email.setDebug(true);
        email.setHostName( getemail(userAddr) );
        email.setAuthenticator(new DefaultAuthenticator(userAddr,password));
        //附件
        EmailAttachment attachment = new EmailAttachment();
        //路径
        attachment.setPath( FilePath );
        try {
            email.setFrom( userAddr );
            email.addTo( toMailAddr );
            email.setCharset("utf-8");
            email.setSubject( subject );
            email.setMsg( message );
            email.attach(attachment);//添加附件
            email.send();
            System.out.println("邮件发送成功");
        } catch (EmailException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //目前qq邮箱开启ssl服务时，只需要通过授权码作为密码就可以以前面两种方式发送，若在实际使用中出现ssl协议问题，这里提供了一个填写ssl端口进行发送的方式，实际使用中尚未遇到ssl安全问题
    public static void SSLsendEmail(String toMailAddr, String subject, String message, String emailport, String FilePath) throws Exception {
        try {
            //创建会话
            Properties props = new Properties();
            props.put("mail.smtp.host",getemail (userAddr));//发件人使用发邮件的电子信箱服务器
            props.put("mail.password", password);
            props.put("mail.transport.protocol", "smtp");
            props.setProperty("mail.debug", "true");
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.port", emailport);//发件邮箱ssl端口，例如QQ邮箱可以使用465
            props.put("mail.smtp.socketFactory.port", emailport);
            Session mailSession = Session.getInstance(props);
            mailSession.setDebug(true);
            // 构建消息
            MimeMessage mimeMessage = new MimeMessage(mailSession);
            InternetAddress from=new InternetAddress(userAddr);
            mimeMessage.setFrom(from);
            InternetAddress to=new InternetAddress(toMailAddr); //设置收件人地址并规定其类型
            mimeMessage.setRecipient(Message.RecipientType.TO,to);
            mimeMessage.setSubject(subject); //主题
            mimeMessage.setText(message); //正文

            //给消息对象设置内容
            BodyPart bodyPart=new MimeBodyPart();					//新建一个存放信件内容的BodyPart对象
            bodyPart.setContent(message, "text/html;charset= GB2312");	//设置 发送邮件内容为HTML类型,并为中文编码
            Multipart multipart=new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            // 添加附件
            if (FilePath != null && !"".equals(FilePath)) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
            // 根据附件路径获取文件,
                FileDataSource dataSource = new FileDataSource(FilePath);
                attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
            //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(dataSource.getFile().getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            mimeMessage.setContent(multipart);
            mimeMessage.saveChanges();

            // 4. 根据 Session 获取邮件传输对象
            Transport transport=mailSession.getTransport("smtp");
            transport.connect(getemail (userAddr),userAddr,password);
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();

        } catch (EmailException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
          //SendFileEmail("neins_drc@163.com","测试","测试","C:\\Users\\neins\\Desktop\\140303ncgcrr06zx000rc2.jpg");
          //SSLsendEmail("1136148849@qq.com","测试","测试","465","");
          //SendTextEmail("neins_drc@163.com","测试","测试");
    }

}
