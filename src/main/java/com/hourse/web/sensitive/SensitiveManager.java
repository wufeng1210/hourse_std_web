package com.hourse.web.sensitive;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SensitiveManager {

    /**
     * 脱敏处理器
     */
    private Map<String,ISensitiveInfo> sensitiveInfo=new ConcurrentHashMap<String,ISensitiveInfo>();

    /**
     * 注册脱敏处理
     * @param iSensitiveInfos
     */
    public void regeisterSensitive(ISensitiveInfo... iSensitiveInfos){
        if(iSensitiveInfos!=null){
            for(ISensitiveInfo iSensitiveInfo : iSensitiveInfos){
                sensitiveInfo.put(iSensitiveInfo.getKey(),iSensitiveInfo);
            }
        }
    }

    /**
     * 删除脱敏处理
     * @param key
     */
    public void removeSensitive(String key){
        sensitiveInfo.remove(key);
    }

    /**
     * 判断是否注册脱敏处理
     * @param key
     * @return
     */
    public boolean contains(String key){
        return sensitiveInfo.containsKey(key);
    }

    /**
     * 数据脱敏
     * @param key
     * @param source
     * @return
     */
    public Object sensitive(String key,Object source){
        if(contains(key)){
            return sensitiveInfo.get(key).sensitive(source);
        }else{
            return source;
        }
    }

    /**
     * 初始化数据脱敏
     */
    @PostConstruct
    private void init(){
        //注册身份证脱敏
        regeisterSensitive(new ISensitiveInfo(){
            StringBuffer stringBuffer=new StringBuffer();

            @Override
            public Object sensitive(Object source) {
                stringBuffer.setLength(0);
                String idNo=source.toString().trim();
                if(idNo.length()==15){
                    stringBuffer.append(idNo.substring(0,5)).append("*****").append(idNo.substring(10));
                    idNo=stringBuffer.toString();
                }else if(idNo.length()==18){
                    stringBuffer.append(idNo.substring(0,7)).append("*******").append(idNo.substring(14));
                    idNo=stringBuffer.toString();
                }
                return idNo;
            }

            @Override
            public String getKey() {
                return "id_no";
            }
        });
    }
}
