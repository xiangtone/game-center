package com.x.publics.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private JsonUtil() {

	}

	/** 
	 * 将对象转换成json格式 
	 *  
	 * @param ts 
	 * @return 
	 */
	public static String objectToJson(Object ts) {
		String jsonStr = null;
		try {
			if (gson != null) {
				jsonStr = gson.toJson(ts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	/** 
	 * 将对象转换成json格式(并自定义日期格式) 
	 *  
	 * @param ts 
	 * @return 
	 */
	public static String objectToJsonDateSerializer(Object ts, final String dateformat) {
		String jsonStr = null;
		try {
			gson = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
				public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
					SimpleDateFormat format = new SimpleDateFormat(dateformat);
					return new JsonPrimitive(format.format(src));
				}
			}).setDateFormat(dateformat).create();
			if (gson != null) {
				jsonStr = gson.toJson(ts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	/** 
	 * 将json格式转换成list对象 
	 *  
	 * @param jsonStr 
	 * @return 
	 */
	public static List<?> jsonToList(String jsonStr) {
		List<?> objList = null;
		try {
			if (gson != null) {
				java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
				}.getType();
				objList = gson.fromJson(jsonStr, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objList;
	}

	/** 
	 * 将json格式转换成list对象，并准确指定类型 
	 * @param jsonStr 
	 * @param type 
	 * @return 
	 */
	public static List<?> jsonToList(String jsonStr, java.lang.reflect.Type type) {
		List<?> objList = null;
		try {
			if (gson != null) {
				objList = gson.fromJson(jsonStr, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objList;
	}

	/** 
	 * 将json格式转换成map对象 
	 *  
	 * @param jsonStr 
	 * @return 
	 */
	public static Map<?, ?> jsonToMap(String jsonStr) {
		Map<?, ?> objMap = null;
		try {
			if (gson != null) {
				java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
				}.getType();
				objMap = gson.fromJson(jsonStr, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objMap;
	}

	/** 
	 * 将json转换成bean对象 
	 *  
	 * @param jsonStr 
	 * @return 
	 */
	public static Object jsonToBean(JSONObject json, Class<?> cl) {
		Object obj = null;
		try {
			if (gson != null) {
				obj = gson.fromJson(json.toString(), cl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/** 
	 * 将json转换成bean对象 
	 *  
	 * @param jsonStr 
	 * @return 
	 */
	public static Object jsonToBean(String jsonStr, Class<?> cl) {
		Object obj = null;
		try {
			if (gson != null) {
				obj = gson.fromJson(jsonStr, cl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/** 
	 * 将json转换成bean对象 
	 *  
	 * @param jsonStr 
	 * @param cl 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl, final String pattern) {
		Object obj = null;
		try {
			gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					SimpleDateFormat format = new SimpleDateFormat(pattern);
					String dateStr = json.getAsString();
					try {
						return format.parse(dateStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return null;
				}
			}).setDateFormat(pattern).create();
			if (gson != null) {
				obj = gson.fromJson(jsonStr, cl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) obj;
	}

	/** 
	 * 根据 
	 *  
	 * @param jsonStr 
	 * @param key 
	 * @return 
	 */
	public static Object getJsonValue(String jsonStr, String key) {
		Object rulsObj = null;
		try {
			Map<?, ?> rulsMap = jsonToMap(jsonStr);
			if (rulsMap != null && rulsMap.size() > 0) {
				rulsObj = rulsMap.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rulsObj;
	}

	/**
	 * 将集合类型的对象转化为JSON
	 * 
	 * @param collection
	 *            <a href="http://my.oschina.net/u/556800" target="_blank"
	 *            rel="nofollow">@return</a>
	 */
	public static String collectionToJson(Collection<?> collection) {
		StringBuilder json = new StringBuilder();
		try {
			json.append("[");
			if (collection != null && collection.size() > 0) {
				for (Object object : collection) {
					json.append(objectToJson(object));
					json.append(",");
				}
				json.setCharAt(json.length() - 1, ']');
			} else {
				json.append("]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 将一个Map类型的对象转化为JSON
	 * 
	 * @param map
	 *            <a href="http://my.oschina.net/u/556800" target="_blank"
	 *            rel="nofollow">@return</a>
	 */
	public static String mapToJson(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		try {
			Iterator it = map.keySet().iterator();
			json.append("{");
			if (map != null && map.size() > 0) {
				while (it.hasNext()) {
					Object object = it.next();
					json.append(objectToJson(object));
					json.append(":");
					json.append(objectToJson(map.get(object)));
					json.append(",");

				}
				json.setCharAt(json.length() - 1, '}');// 将最后一个逗号改为"}"
			} else {
				json.append("}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 将一个Bean类型的对象转化为JSON
	 * 
	 * @param bean
	 *            <a href="http://my.oschina.net/u/556800" target="_blank"
	 *            rel="nofollow">@return</a>
	 */
	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		try {
			Class cl = bean.getClass();
			json.append("{");
			Field[] fields = cl.getDeclaredFields();
			for (Field f : fields) {
				try {
					StringBuilder fieldName = new StringBuilder(f.getName());
					fieldName.setCharAt(0, Character.toUpperCase(fieldName.charAt(0)));
					Method method = cl.getDeclaredMethod("get" + fieldName.toString(), null);// 获取类中的get方法
					json.append(objectToJson(method.invoke(bean, null)));
					String value = objectToJson(method.invoke(bean, "nu"));
					if (value != null && !value.equals("")) {
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			json.setCharAt(json.length() - 1, '}');// 将最后一个逗号改为"}"
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	/**
	 * 将数组转化为JSON
	 * 
	 * @param array
	 *            <a href="http://my.oschina.net/u/556800" target="_blank"
	 *            rel="nofollow">@return</a>
	 */
	public static String arrayToJson(Object[] array) {
		StringBuilder json = new StringBuilder();
		try {
			json.append("[");
			if (array != null && array.length > 0) {
				for (Object object : array) {
					json.append(objectToJson(object));
					json.append(",");
				}
				json.setCharAt(json.length() - 1, ']');
			} else {
				json.append("]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	/**
	 * 将json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> toMap(String json) {
		JSONObject jsonObject = null;
		Map<String, String> result = new HashMap<String, String>();
		try {
			jsonObject = new JSONObject(json);
			Iterator<String> iterator = jsonObject.keys();
			String key = null;
			String value = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				try {
					value = jsonObject.getString(key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				result.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将String类型的对象转化为JSON
	 * 
	 * @param s
	 *            <a href="http://my.oschina.net/u/556800" target="_blank"
	 *            rel="nofollow">@return</a>
	 */
	public static String stringToJson(String string) {
		StringBuilder json = new StringBuilder();
		try {
			if (string == null) {
				return "";
			}
			for (int i = 0; i < string.length(); i++) {
				char ch = string.charAt(i);
				switch (ch) {
				case '\\':
					json.append("\\\\");
					break;
				case '\b':
					json.append("\\b");
					break;
				case '\f':
					json.append("\\f");
					break;
				case '\n':
					json.append("\\n");
					break;
				case '\r':
					json.append("\\r");
					break;
				case '\t':
					json.append("\\t");
					break;
				default:
					if (ch >= '\u0000' && ch <= '\u001F') {
						String ss = Integer.toHexString(ch);
						json.append("\\u");
						for (int k = 0; k < 4 - ss.length(); k++) {
							json.append('0');
						}
						json.append(ss.toUpperCase());
					} else {
						json.append(ch);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}
}
