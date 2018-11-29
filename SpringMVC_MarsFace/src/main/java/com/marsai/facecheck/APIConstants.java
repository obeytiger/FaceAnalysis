package com.marsai.facecheck;

public class APIConstants {
	
	//---  !!!!!! [以下四条非常重要] !!!!!!
	//--- 在百度AI网站注册你自己的应用，把你自己的应用的信息填入下方，TOKEN使用AITest.main()方法生成
	
	//百度人脸识别应用id，使用你的替代
	public static final String APPID = "";
	//百度人脸识别应用apikey，使用你的替代
	public static final String API_KEY = "";
	//百度人脸识别应用sercetkey，使用你的替代
	public static final String SERCET_KEY = "";
	//百度人脸识别token 有效期一个月，使用AITest.main()方法生成你的TOKEN
	public static final String TOKEN = "";
	
	//--- level 1: >= 70
	//--- level 2: >= 35, < 70
	//--- level 3: < 35
	public static final String[] male_level1 = {"玉树临风", "再世潘安", "风流倜傥", "品貌非凡", "目若朗星", "举世无双", "邪魅性感", "神采奕奕", "气宇轩昂", "宸宁之貌"};
	public static final String[] male_level2 = {"风度翩翩", "高大威猛", "帅得一逼", "英姿勃发", "面如冠玉", "文质彬彬", "眉清目秀", "英俊潇洒", "肤白貌美", "气度从容"};
	public static final String[] male_level3 = {"神勇威武", "票房保证", "安全着陆", "爱国敬业", "轮廓分明", "共产主义接班人", "可以抢救", "资本主义掘墓人", "智勇双全", "正义化身"};
	public static final String[] male_level4 = {"称霸宇宙", "惊世骇俗", "鬼斧神工", "内心强大", "盛世美颜", "建议绝育", "穿越时空", "基因突变", "濒临灭绝", "充值信仰"};
	//public static final String[] male_level4 = {"称霸宇宙", "惊世骇俗", "定海神针", "鬼斧神工", "内心强大", "充值信仰", "建议绝育", "穿越时空", "基因突变", "濒临灭绝", "盛世美颜", "地动山摇"};
	
	public static final String[] female_level1 = {"人间仙境", "梦露再世", "沉鱼落雁", "秀色可餐", "倾国倾城", "有颜任性", "闭月羞花", "国色天香", "玉洁冰清", "雪肤凝肌"};
	public static final String[] female_level1_smile = {"人间仙境", "再世海伦", "明眸皓齿", "回眸一笑百媚生", "倾国倾城", "国色天香", "迷倒众生", "勾魂摄魄", "玉洁冰清", "有颜任性"};
	public static final String[] female_level2 = {"清丽脱俗", "亭亭玉立", "窈窕淑女", "人淡如菊", "妩媚动人", "车见车载花见花开", "风姿绰约", "语笑嫣然", "美丽大方", "小家碧玉"};
	public static final String[] female_level2_smile = {"清丽脱俗", "亭亭玉立", "窈窕淑女", "笑靥迷人", "娇羞可爱", "人见人爱花见花开", "风姿绰约", "语笑嫣然", "梨涡浅笑", "小家碧玉"};
	public static final String[] female_level3 = {"大家闺秀", "小鲜姐姐", "票房保证", "共产主义接班人", "如花似玉", "妈妈之选", "挤挤就有", "可以抢救", "秀丽端庄", "智慧化身"};
	public static final String[] female_level4 = {"逢凶化吉", "国家保护", "安全第一", "此女必火", "鬼哭狼嚎", "雌雄莫辨", "车祸现场", "怀疑人生", "望风而逃", "天外飞仙"};

	public static final String[] titlePoem = {"人面不知何处去,桃花依旧笑嘻嘻", "雕栏玉砌应犹在,只是朱颜改",
			"江南风景秀,最忆在碧莲", "百尺朱楼闲倚遍,薄雨浓云,抵死遮人面", "绣面芙蓉一笑开,斜飞宝鸭衬香腮", "美人微笑转星眸,月华羞,捧金瓯"};
}
