package com.marsai.facecheck;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

public class CallFaceAI {
	
	public static void main(String[] args) {
		/****************/
		String Filepath = "D:\\Picture\\2016-10-30-backuped\\CZ2A0041_small.JPG";
		Filepath = "D:\\mars\\learning\\Face\\Test\\test03.JPG";
		Filepath = "D:\\mars\\learning\\Face\\Test\\test01.JPG";
		Filepath = "D:\\mars\\learning\\Face\\Test\\test02.JPG";
		String image = Image4Base64.GetImageStr(Filepath);
		//String image = Base64Util.encode(Filepath);
		String url = "https://aip.baidubce.com/rest/2.0/face/v1/detect?access_token="+APIConstants.TOKEN;
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		bodys.put("image", image);
		bodys.put("face_fields", "age,beauty,expression,gender,glasses,race,qualities");
		try {
			HttpResponse response = HttpUtils.doPostBD(url,headers,bodys);
			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private CallFaceAI() {}
	private static CallFaceAI callFaceAI_Single = null;
	public static CallFaceAI getInstance() {
		if(callFaceAI_Single == null) {
			callFaceAI_Single = new CallFaceAI();
		}
		return callFaceAI_Single;
	}
	
	public String analyzeFace(ModelAndView mav, String Filepath) {

		String retValue = "";
		String image = Image4Base64.GetImageStr(Filepath);
		//String image = Base64Util.encode(Filepath);
		String url = "https://aip.baidubce.com/rest/2.0/face/v1/detect?access_token="+APIConstants.TOKEN;
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		bodys.put("image", image);
		bodys.put("face_fields", "age,beauty,expression,faceshape,type,gender,gender_probability,glasses,race,qualities");
		try {
			HttpResponse response = HttpUtils.doPostBD(url,headers,bodys);
			HttpEntity entity = response.getEntity();
			testDetect(mav, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retValue;

	}

	
	
	
	

	
	
    /**
     * 人脸探测调用方法
     */
    private void testDetect(ModelAndView mav, HttpEntity entity){
    	
        String expressionStr = "";
        String glassesStr = "";
    	
    	try {

            if (entity != null) {
               String retSrc = EntityUtils.toString(entity); 
               // parsing JSON
               //JSONObject jsonObject = JSONObject.fromObject(retSrc); //Convert String to JSON Object
               JSONObject jsonObject = new JSONObject(retSrc);
               //JSONArray myJsonArray = JSONArray.fromObject(retSrc);
               /**
                * 获取年龄、颜值分数、微笑程度、是否戴眼镜
                */
               int faceNumber = ((Integer)jsonObject.get("result_num")).intValue();
               if(faceNumber == 0) {
            	   mav.addObject("resultColor", "red");
                   mav.addObject("faceGender", "探测不到人脸");               
                   mav.addObject("faceRace", "探测不到人脸");    
                   mav.addObject("faceShape", "探测不到人脸");
                   mav.addObject("faceAge", "探测不到人脸");
                   mav.addObject("faceSmile", "探测不到人脸");
                   mav.addObject("faceGlass", "探测不到人脸");
                   mav.addObject("beauty", "探测不到人脸");
                   mav.addObject("faceBeauty", "探测不到人脸");
                   return;
               }
               JSONArray jal = (JSONArray)jsonObject.get("result");
           	   JSONObject job = jal.getJSONObject(0); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
               
               String gender = job.optString("gender");
               String genderStr = "";
               double gender_probability = job.optDouble("gender_probability");
               
               
               //String type = job.optString("type");
               String[] faceType = new String[5];
               double[] faceTypeTrust = new double[5];
               JSONArray faceShape = (JSONArray)job.get("faceshape");
               
               //---------------------------
               JSONObject faceShape_01 = faceShape.getJSONObject(0);
               faceType[0]=faceShape_01.optString("type");faceTypeTrust[0] = faceShape_01.optDouble("probability");
               //---------------------------
               JSONObject faceShape_02 = faceShape.getJSONObject(1);
               faceType[1]=faceShape_02.optString("type");faceTypeTrust[1] = faceShape_02.optDouble("probability");
               //---------------------------
               JSONObject faceShape_03 = faceShape.getJSONObject(2);
               faceType[2]=faceShape_03.optString("type");faceTypeTrust[2] = faceShape_03.optDouble("probability");
               //---------------------------
               JSONObject faceShape_04 = faceShape.getJSONObject(3);
               faceType[3]=faceShape_04.optString("type");faceTypeTrust[3] = faceShape_04.optDouble("probability");
               //---------------------------
               JSONObject faceShape_05 = faceShape.getJSONObject(4);
               faceType[4]=faceShape_05.optString("type");faceTypeTrust[4] = faceShape_05.optDouble("probability");              
               //---------------------------
               String firstFaceType = faceType[0];
               double firstTrust = faceTypeTrust[0];
               for(int i = 1; i < faceType.length; i++) {
            	   if(faceTypeTrust[i] > firstTrust) {
            		   firstFaceType = faceType[i];
            		   firstTrust = faceTypeTrust[i];
            	   }
               }
               
               String race = job.optString("race");
               double age = job.optDouble("age");
               double beauty = job.optDouble("beauty");
               int expression = job.optInt("expression");
               int glasses = job.optInt("glasses");
               
               //BigDecimal b = new BigDecimal((new Double(gender_probability)).toString());
               String gender_probability_str = "" + new BigDecimal("" + gender_probability * 100).setScale(4, BigDecimal.ROUND_HALF_UP);
               //double f1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
               if(gender_probability < 0.7) {
            	   genderStr = "你猜" + " (性别可信度  " + gender_probability_str + "%)";
        		   mav.addObject("resultColor", "white");
               }else {
            	   if("male".equalsIgnoreCase(gender)) {
            		   genderStr = "帅哥" + " (性别可信度  " + gender_probability_str + "%)";
            		   mav.addObject("resultColor", "lightblue");
            	   }else if("female".equalsIgnoreCase(gender)) {
            		   genderStr = "美女" + " (性别可信度  " + gender_probability_str + "%)";
            		   mav.addObject("resultColor", "pink");
            	   }else {
            		   genderStr = "异形";
            		   mav.addObject("resultColor", "white");
            	   }
               }
               
        	   if("yellow".equalsIgnoreCase(race)) {
        		   race = "小黄人";
        	   }else if("white".equalsIgnoreCase(race)) {
        		   race = "小白兔";
        	   }else if("black".equalsIgnoreCase(race)){
        		   race = "小黑豹";
        	   }else if("arabs".equalsIgnoreCase(race)){
        		   race = "阿拉伯";
        	   }else {
        		   race = "母鸡呀";
        	   }
        	   
        	   
               //square/triangle/oval/heart/round
        	   String faceTypeStr = "";
        	   if("female".equalsIgnoreCase(gender)) {
            	   if("square".equalsIgnoreCase(firstFaceType)) {
            		   faceTypeStr = "大饼脸";
            	   }else if("triangle".equalsIgnoreCase(firstFaceType)) {
            		   faceTypeStr = "百慕大三角";
            	   }else if("oval".equalsIgnoreCase(firstFaceType)){
            		   faceTypeStr = "鹅蛋形";
            	   }else if("heart".equalsIgnoreCase(firstFaceType)){
            		   faceTypeStr = "爱心形";
            	   }else if("round".equalsIgnoreCase(firstFaceType)){
            		   faceTypeStr = "圆嘟嘟";
            	   }else {
            		   faceTypeStr = "母鸡呀";
            	   }
        	   }else {
            	   if("square".equalsIgnoreCase(firstFaceType)) {
            		   faceTypeStr = "国字脸";
            	   }else if("triangle".equalsIgnoreCase(firstFaceType)) {
            		   faceTypeStr = "尖脸猴腮";
            	   }else if("oval".equalsIgnoreCase(firstFaceType)){
            		   faceTypeStr = "鸭蛋形";
            	   }else if("heart".equalsIgnoreCase(firstFaceType)){
            		   faceTypeStr = "大心肝形";
            	   }else if("round".equalsIgnoreCase(firstFaceType)){
            		   faceTypeStr = "南瓜球";
            	   }else {
            		   faceTypeStr = "母鸡呀";
            	   }
        		   
        	   }
        	   

               switch (expression){
                   case 0 : expressionStr = "不笑";
                       break;
                   case 1 :  expressionStr = "微笑";
                       break;
                   case 2 :  expressionStr = "大笑";
                       break;
                   default: expressionStr = "无法识别";
               }

               switch (glasses){
                   case 0 : glassesStr = "无眼镜";
                       break;
                   case 1 :  glassesStr = "普通眼镜";
                       break;
                   case 2 :  glassesStr = "墨镜";
                       break;
                   default: glassesStr = "无法识别";
               }
               /**
                * 控制台打印输出探测结果
                * Tips：年龄默认为double，需用Math.round()四舍五入取整
                */
               System.out.println("年龄：" + Math.round(age));
               System.out.println("微笑程度：" + expressionStr);
               System.out.println("眼镜：" + glassesStr);
               System.out.println("颜值打分：" + beauty);
               
               mav.addObject("faceGender", genderStr);               
               mav.addObject("faceRace", race);    
               mav.addObject("faceShape", faceTypeStr);
               mav.addObject("faceAge", ""+Math.round(age));
               mav.addObject("faceSmile", expressionStr);
               mav.addObject("faceGlass", glassesStr);
               mav.addObject("beauty", ""+ beauty);
               mav.addObject("faceBeauty", ""+ giveADescForBeauty(gender, expression, beauty));
            }
    }
     catch (Exception e) {
      }
    	



    }
    
    
    private String giveADescForBeauty(String genderType, int smileOrNot, double beautyFigure) {
    	
    	String retValue = "";
    	java.util.Random random=new java.util.Random();
    	int result=random.nextInt(10);
    	//result = result+1; 
    	
    	if(genderType.equalsIgnoreCase("female")) {
    		if(beautyFigure < 35) {
    			retValue = APIConstants.female_level4[result];
    		}else if(beautyFigure >= 35 && beautyFigure < 50) {
    			retValue = APIConstants.female_level3[result];
    		}else if(beautyFigure >= 50 && beautyFigure < 70) {
    			if(smileOrNot == 1 | smileOrNot == 2) {
    				retValue = APIConstants.female_level2_smile[result];
    			}else {
    				retValue = APIConstants.female_level2[result];
    			}
    		}else {
    			if(smileOrNot == 1 | smileOrNot == 2) {
    				retValue = APIConstants.female_level1_smile[result];
    			}else {
       				retValue = APIConstants.female_level1[result];  				
    			}
    		}
    	}else {
    		if(beautyFigure < 35) {
    			retValue = APIConstants.male_level4[result];
    		}else if(beautyFigure >= 35 && beautyFigure < 50) {
    			retValue = APIConstants.male_level3[result];
    		}else if(beautyFigure >= 50 && beautyFigure < 70) {
    			retValue = APIConstants.male_level2[result];
    		}else {
    			retValue = APIConstants.male_level1[result];
    		}
    	}
    	
    	return retValue;
    }
    
    
    public String randomlySetTitle() {
    	java.util.Random random=new java.util.Random();
    	int result=random.nextInt(6);
    	return APIConstants.titlePoem[result];
    }
    
    
}
