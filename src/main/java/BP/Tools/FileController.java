package BP.Tools;

import BP.springCloud.tool.FeignTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-16 10:50
 **/
@Controller()
@RequestMapping("file")
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${hanlp.dir}")
    private String userFileDir;

    @RequestMapping(value="upload",method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(MultipartFile file){

            String fileName = + FeignTool.getSerialNumber("FileNumber")+file.getOriginalFilename() ;

            String path = userFileDir + "/uploadFile";
            String fileFullName=path + "/" + fileName;
            File dest = new File(fileFullName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        return fileFullName;
    }

}
