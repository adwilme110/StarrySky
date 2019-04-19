package com.itbomb.space.util;

import android.text.TextUtils;
import com.itbomb.space.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
/*
 * Copyright  2017 [AllenCoderr]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Create by Allen
 */

public class LogUtil {
	private static final int LENGTH = 5;
	private static final int V = 0x1;
	private static final int D = 0x2;
	private static final int I = 0x3;
	private static final int W = 0x4;
	private static final int E = 0x5;
	private static final int A = 0x6;
	private static final int JSON = 0x7;
	private static final int JSON_INDENT = 4;
	/**
	 * 行号分隔符
	 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String DEFAULT_MESSAGE = "execute";


	public static void v() {
		printLog(V, null, DEFAULT_MESSAGE);
	}

	public static void v(Object msg) {
		printLog(V, null, msg);
	}

	public static void v(String tag, String msg) {
		printLog(V, tag, msg);
	}

	public static void d() {
		printLog(D, null, DEFAULT_MESSAGE);
	}

	public static void d(Object msg) {
		printLog(D, null, msg);
	}
	public static void d(byte[] bytes) throws UnsupportedEncodingException {
		printLog(D, null, new String(bytes, "UTF-8"));
	}
	public static void d(String tag,byte[] bytes) throws UnsupportedEncodingException {
		printLog(D, tag, new String(bytes, "UTF-8"));
	}
	public static void d(String tag, Object msg) {
		printLog(D, tag, msg);
	}

	public static void i() {
		printLog(I, null, DEFAULT_MESSAGE);
	}

	public static void i(Object msg) {
		printLog(I, null, msg);
	}

	public static void i(String tag, Object msg) {
		printLog(I, tag, msg);
	}

	public static void w() {
		printLog(W, null, DEFAULT_MESSAGE);
	}

	public static void w(Object msg) {
		printLog(W, null, msg);
	}

	public static void w(String tag, Object msg) {
		printLog(W, tag, msg);
	}

	public static void e() {
		printLog(E, null, DEFAULT_MESSAGE);
	}

	public static void e(Object msg) {
		printLog(E, null, msg);
	}

	public static void e(String tag, Object msg) {
		printLog(E, tag, msg);
	}

	public static void a() {
		printLog(A, null, DEFAULT_MESSAGE);
	}

	public static void a(Object msg) {
		printLog(A, null, msg);
	}

	public static void a(String tag, Object msg) {
		printLog(A, tag, msg);
	}


	public static void json(String jsonFormat) {
		printLog(JSON, null, jsonFormat);
	}

	public static void json(String tag, String jsonFormat) {
		printLog(JSON, tag, jsonFormat);
	}


	public static int printMethodName(String TAG) {
		if (!BuildConfig.IS_DEBUG) {
			return -1;
		}
		String msg = "";
		StackTraceElement info = LogInfo.getInfoInternal(LENGTH);
		if (info != null) {
			msg = info.getMethodName() + " # Line " + info.getLineNumber();
		}

		return android.util.Log.i(TAG, msg);

	}

	public static int printStackTrace(String TAG) {
		if (!BuildConfig.IS_DEBUG) {
			return -1;
		}

		StackTraceElement[] stackTraceElements = new Exception()
				.getStackTrace();

		if (stackTraceElements != null) {
			android.util.Log.d(TAG, "printStackTrace:");
			for (int i = 1; i < stackTraceElements.length; i++) {
				android.util.Log.d(TAG, stackTraceElements[i].toString());
			}
		}

		return 0;
	}

	private static void printLog(int type, String tagStr, Object objectMsg) {
		String msg;
		if (!BuildConfig.IS_DEBUG) {
			return;
		}
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int index = 4;
		String className = stackTraceElements[index].getFileName();
		String methodName = stackTraceElements[index].getMethodName();
		int lineNumber = stackTraceElements[index].getLineNumber();
		String tag = (tagStr == null ? className : tagStr);
		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		StringBuilder builder = new StringBuilder();
		builder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ]");
		if (objectMsg == null) {
			msg = "log with null object";
		} else {
			msg = objectMsg.toString();
		}
		if (msg != null && type != JSON) {
			builder.append(msg);
		}
		String logStr = builder.toString();
		switch (type) {

			case V:
				android.util.Log.v(tag, logStr);
				break;
			case D:
				android.util.Log.d(tag, logStr);
				break;
			case I:
				android.util.Log.i(tag, logStr);
				break;
			case W:
				android.util.Log.w(tag, logStr);
				break;
			case E:
				android.util.Log.e(tag, logStr);
				break;
			case A:
				android.util.Log.d(tag, logStr);
				break;
			case JSON: {
				if (TextUtils.isEmpty(msg)) {
					android.util.Log.d(tag, "Empty or null json content");
					return;
				}
				String message = null;
				try {
					if (msg.startsWith("{")) {
						JSONObject jsonObject = new JSONObject(msg);
						message = jsonObject.toString(JSON_INDENT);
					} else if (msg.startsWith("[")) {
						JSONArray jsonArray = new JSONArray(msg);
						message = jsonArray.toString(JSON_INDENT);
					}
				} catch (JSONException e) {
					e(tag, e.getCause().getMessage() + "\n" + msg);
					return;
				}
				printLine(tag, true);
				message = logStr + LINE_SEPARATOR + message;
				String[] lines = message.split(LINE_SEPARATOR);
				StringBuilder jsonContent = new StringBuilder();
				for (String line : lines) {
					jsonContent.append("|| ").append(line).append(LINE_SEPARATOR);
				}
				android.util.Log.d(tag, jsonContent.toString());
				printLine(tag, false);

			}
			break;
			default:
				break;
		}
	}

	private static void printLine(String tag, boolean isTop) {
		if (isTop) {
			android.util.Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
		} else {
			android.util.Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
		}
	}

	public static void errorLog(Exception e) {
		if (e == null) {
			return;
		}

		printLog(E, null, e.getLocalizedMessage());
	}

	private static class LogInfo{
		private static final int LENGTH = 5;

		enum InfoKind{
			FILE_NAME,
			METHOD_NAME,
			CLASS_NAME,
			LINE_NUM
		}

		public static String getFileName() {
			return getInfo(InfoKind.FILE_NAME);
		}

		public static String getMethodName() {
			return getInfo(InfoKind.METHOD_NAME);
		}

		public static String getClassName() {
			return getInfo(InfoKind.CLASS_NAME);
		}

		public static  String getLineNumber() {
			return getInfo(InfoKind.LINE_NUM);
		}

		private static String getInfo(InfoKind kind) {
			String ret = "";

			if(!BuildConfig.IS_DEBUG) {
				return ret;
			}

			StackTraceElement[] stackTraceElements = Thread.currentThread()
					.getStackTrace();
			if (stackTraceElements != null && stackTraceElements.length >= LENGTH) {
				StackTraceElement stackTraceElement = stackTraceElements[LENGTH - 1];
				switch (kind) {
					case FILE_NAME:
						ret = stackTraceElement.getFileName();
						break;
					case METHOD_NAME:
						ret = stackTraceElement.getMethodName();
						break;
					case CLASS_NAME:
						ret = stackTraceElement.getClassName();
						break;
					case LINE_NUM:
						ret = Integer.toString(stackTraceElement.getLineNumber());
						break;

					default:
						break;
				}
			}
			return ret;
		}

		static StackTraceElement getInfoInternal(int length) {
			StackTraceElement ret = null;

			if(!BuildConfig.IS_DEBUG) {
				return ret;
			}

			StackTraceElement[] stackTraceElements = Thread.currentThread()
					.getStackTrace();
			if (stackTraceElements != null && stackTraceElements.length >= length) {
				ret = stackTraceElements[length - 1];

			}
			return ret;
		}
	}
}