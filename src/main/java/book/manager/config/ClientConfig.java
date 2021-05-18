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

    public static double getDouble(String key){
        return settings.getDouble(key);
    }

    public static String getString(String key){
        return settings.getString(key);
    }

    public static String getLanguage(String key){
        return settings.getString(key).replace("setting.language.", "");
    }

    public static ColorConfig getColor(String key){
        switch (settings.getString(key)){
            case "setting.color.dark":
                return ColorSwitch.DARK;
            case "setting.color.light":
            default:
                return ColorSwitch.LIGHT;
        }
    }

    private static JSONObject getDefaultSettings(){
        JSONObject object = new JSONObject();
        object.put("language", "setting.language.zh_cn");
        object.put("themeColor", "setting.color.light");
        return object;
    }
}
