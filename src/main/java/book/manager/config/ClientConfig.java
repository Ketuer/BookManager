package book.manager.config;

import com.alibaba.fastjson.JSONObject;
import dandelion.ui.color.ColorConfig;
import dandelion.ui.color.ColorSwitch;

import java.io.*;

public class ClientConfig {
    private static JSONObject settings;

    public static void readLocalSetting() throws IOException {
        File settingFile = new File("settings.json");
        if(settingFile.exists()){
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(settingFile));
            String tmp;
            while ((tmp = reader.readLine()) != null) builder.append(tmp);
            settings = JSONObject.parseObject(builder.toString());
            reader.close();
        }else {
            settings = getDefaultSettings();
            saveLocalSetting();
        }
    }

    public static void saveLocalSetting() throws IOException{
        File settingFile = new File("settings.json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(settingFile));
        writer.write(settings.toString());
        writer.flush();
        writer.close();
    }

    public static void put(String key, Object value) {
        settings.put(key, value);
        try {
            saveLocalSetting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getInt(String key){
        Integer i = settings.getInteger(key);
        if(i == null) return 0;
        return i;
    }

    public static boolean getBoolean(String key){
        Boolean b = settings.getBoolean(key);
        if(b == null) return false;
        return b;
    }

    public static double getDouble(String key){
        return settings.getDouble(key);
    }

    public static String getString(String key){
        return settings.getString(key);
    }

    private static JSONObject getDefaultSettings(){
        JSONObject object = new JSONObject();
        object.put("language", "setting.language.zh_cn");
        object.put("themeColor", "setting.color.light");
        object.put("savePassword", false);
        object.put("password", "");
        object.put("userName", "");
        return object;
    }
}
