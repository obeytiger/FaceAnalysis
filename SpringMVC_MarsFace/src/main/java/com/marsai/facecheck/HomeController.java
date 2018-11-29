package com.marsai.facecheck;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loadimage");
		mav.addObject("pageTitle", "人面不知何处去,桃花依旧笑嘻嘻");
		mav.addObject("picFileName", "");
		mav.addObject("analysisResult", "");
		mav.addObject("errorMsg", "");
		mav.addObject("flag_ShowScore", "0");
		return mav;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView home_post(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loadimage");
		mav.addObject("pageTitle", CallFaceAI.getInstance().randomlySetTitle());
		mav.addObject("picFileName", "");
		mav.addObject("analysisResult", "");
		mav.addObject("errorMsg", "");
		mav.addObject("flag_ShowScore", "0");
		return mav;
	}
	
	
	@RequestMapping(value = "/doUploadImage", method = RequestMethod.GET)
	public ModelAndView home_upload_get(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loadimage");
		mav.addObject("pageTitle", CallFaceAI.getInstance().randomlySetTitle());
		mav.addObject("picFileName", "");
		mav.addObject("analysisResult", "");
		mav.addObject("errorMsg", "");
		mav.addObject("flag_ShowScore", "0");
		return mav;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */@ResponseBody
	@RequestMapping(value = "/doUploadImage", method = RequestMethod.POST, produces="html/text;charset=UTF-8")
	public String home2(HttpServletRequest request) throws IllegalStateException, IOException {

		ModelAndView mav = new ModelAndView();

		MultipartFile selPicture = null;
		
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			try {
				selPicture = multiRequest.getFileMap().get("files");
			}catch(Exception e) {
				mav.setViewName("loadimage");
				mav.addObject("pageTitle", CallFaceAI.getInstance().randomlySetTitle());
				mav.addObject("picFileName", "");
				mav.addObject("errorMsg", "");
				mav.addObject("flag_ShowScore", "1");
				//return mav;
				
			}
		}else {
			mav.setViewName("loadimage");
			mav.addObject("pageTitle", CallFaceAI.getInstance().randomlySetTitle());
			mav.addObject("picFileName", "");
			mav.addObject("errorMsg", "");
			mav.addObject("flag_ShowScore", "1");
			//return mav;
		}

		String newFileName = "";
		if (!selPicture.isEmpty()) {
            String path = "C:\\faceai\\image\\";
            String originalFileName = selPicture.getOriginalFilename();
            // 新的图片名称
            newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
            
            File newFile = new File(path + newFileName);
            // 将内存中的数据写入磁盘
            selPicture.transferTo(newFile);
            if(checkFileSize(newFile, 4, "M") == false) {
            	newFile.delete();
            	mav.setViewName("loadimage");
            	mav.addObject("errorMsg", "上传文件大小不可超过4M");
        		mav.addObject("flag_ShowScore", "0");
    	        //return mav;
            }
            
            
        	CallFaceAI.getInstance().analyzeFace(mav, path + newFileName);
        	if(newFile.isFile()) {newFile.delete();}
        	

            
	}
		JSONArray array =new JSONArray();
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("resultColor", mav.getModel().get("resultColor"));
        obj.put("faceGender", mav.getModel().get("faceGender"));
        obj.put("faceRace", mav.getModel().get("faceRace"));
        obj.put("faceAge", mav.getModel().get("faceAge"));
        obj.put("faceShape", mav.getModel().get("faceShape"));
        obj.put("faceSmile", mav.getModel().get("faceSmile"));
        obj.put("faceGlass", mav.getModel().get("faceGlass"));
        obj.put("faceBeauty", mav.getModel().get("faceBeauty"));
        obj.put("faceBeautyScore", mav.getModel().get("beauty"));
        String randomTitle = CallFaceAI.getInstance().randomlySetTitle();
        obj.put("pageTitle", randomTitle);
        obj.put("pageTitle", randomTitle);
        array.add(obj);
        return array.toString();
	}
 


	/**
     * 判断文件大小
     *
     * @param file
     *            文件
     * @param size
     *            限制大小
     * @param unit
     *            限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(File file, int size, String unit) {
        long len = file.length();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }
    
    
	@RequestMapping(value = "/testdrag", method = RequestMethod.GET)
	public ModelAndView testDrag(Locale locale, Model model) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("TestDrag");
		return mav;
	}

	
}
