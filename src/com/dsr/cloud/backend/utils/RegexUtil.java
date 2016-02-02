package com.dsr.cloud.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexUtil {
	
	public static boolean regex(String rule,String str) throws PatternSyntaxException{
			Pattern pattern = Pattern.compile(rule);
			Matcher matcher = pattern.matcher(str);
			if(matcher.find())
				return true;
			else
				return false;
			
	}
	

}
