import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.HttpURLConnectionUtil;

import java.io.*;
import java.util.Scanner;

public class DownLoad {

    //静态变量域名
    private static String baseUrl = "http://doc.canglaoshi.org";
    private static final String basePathl;

    //默认创建jsd文件夹
    static {
        File baseDir = new File("./jsd");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        basePathl = baseDir.getPath();
    }

    //主方法
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入要下载的文件");
        String uriPath = scan.nextLine();
        if ("".equals(uriPath.trim()) || uriPath != null) {
            uriPath = "jsd";
        } else {
            uriPath = "jsd/" + uriPath;
        }
        String dirPath = "./" + uriPath;
        DownLoad d = new DownLoad();
        d.downloadCase(uriPath, dirPath);

    }

    //请求url获取数据
    public JSONArray getData(String url) {
        HttpURLConnectionUtil requests = new HttpURLConnectionUtil();
        requests.doGet(url);
        String message = requests.streamString();
        JSONArray messageJsona = (JSONArray) JSONArray.parse(message);
        return messageJsona;
    }

    //下载事务
    public void downloadCase(String uriPath, String dirPath) {
        String url = baseUrl + "/" + uriPath + "/?j";
        //获取数据
        System.out.println(url);
        JSONArray messageJsona = getData(url);
        System.out.println(messageJsona);
        //遍历数据
        for (Object data : messageJsona) {
            //数据提取
            JSONObject message = (JSONObject) data;
            String type = message.getString("type");
            String fileName = message.getString("name");
            fileOperate(uriPath,dirPath,type,fileName);
        }
        return;
    }


    public void fileOperate(String urlPath, String filePath,String type,String fileName){
        String url;
        if ("directory".equals(type)) {
            url = urlPath +"/"+fileName;
            String path = makeDir(filePath + "/" + fileName);
            downloadCase(url, path);
        }else if("file".equals(type)){
            String downPath = filePath+"/"+fileName;
            url=baseUrl+"/"+urlPath+"/"+fileName;
            downloadFile(url, downPath);
        }
    }

    //创建文件夹
    public String makeDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    //下载文件并写入
    public void downloadFile(String url, String path) {
        System.out.println("下载来链接"+url);
        HttpURLConnectionUtil requests = new HttpURLConnectionUtil();
        File file = new File("./" + path);
        if (!file.exists()) {
            try (InputStream is = requests.doGet(url);
                 FileOutputStream fos = new FileOutputStream(file);) {
                int d;
                byte[] buffer = new byte[1024 * 1000];
                while ((d = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, d);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
