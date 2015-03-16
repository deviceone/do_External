package extimplement;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import core.helper.jsonparse.DoJsonNode;
import core.helper.jsonparse.DoJsonValue;
import core.interfaces.DoIScriptEngine;
import core.object.DoInvokeResult;
import core.object.DoSingletonModule;
import extdefine.do_External_IMethod;

/**
 * 自定义扩展SM组件Model实现，继承DoSingletonModule抽象类，并实现Do_Notification_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式
 * new DoInvokeResult(this.getUniqueKey());
 */
public class do_External_Model extends DoSingletonModule implements do_External_IMethod {

	public do_External_Model() throws Exception {
		super();
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("openApp".equals(_methodName)) {
			openApp(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("openURL".equals(_methodName)) {
			openURL(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("openDial".equals(_methodName)) {
			openDial(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("openContact".equals(_methodName)) {
			openContact(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}

		if ("openMail".equals(_methodName)) {
			openMail(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		if ("openSMS".equals(_methodName)) {
			openSMS(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @throws Exception
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * 启动其他应用；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void openApp(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String _wakeupid = _dictParas.getOneText("wakeupid", "");
		DoJsonNode _data = _dictParas.getOneNode("data");
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		List<DoJsonValue> _dataList = _data.getAllValues();
		Intent _intent = new Intent();
		for (DoJsonValue _entry : _dataList) {
			String _key = _entry.getNodeName();
			String _value = _entry.getText("");
			_intent.putExtra(_key, _value);
		}

		try {
			// 通过包名启动其他应用
			PackageInfo _pi = _activity.getPackageManager().getPackageInfo(_wakeupid, 0);
			Intent _resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			_resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			_resolveIntent.setPackage(_pi.packageName);
			List<ResolveInfo> _apps = _activity.getPackageManager().queryIntentActivities(_resolveIntent, 0);
			ResolveInfo _ri = _apps.iterator().next();
			if (_ri != null) {
				ComponentName _cn = new ComponentName(_ri.activityInfo.packageName, _ri.activityInfo.name);
				_intent.setAction(Intent.ACTION_MAIN);
				_intent.addCategory(Intent.CATEGORY_LAUNCHER);
				_intent.setComponent(_cn);
				_activity.startActivity(_intent);
			}
		} catch (Exception _ex) {
			_intent.setAction(_wakeupid);
			_activity.startActivity(_intent);
		}
	}

	/**
	 * 打开系统通讯录界面；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void openContact(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		Intent _intent = new Intent(Intent.ACTION_VIEW);
		_intent.setType("vnd.android.cursor.dir/contact");
		_activity.startActivity(_intent);
	}

	/**
	 * 打开拨号界面；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void openDial(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		String _number = _dictParas.getOneText("number", "");
		Intent _intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _number));
		_activity.startActivity(_intent);
	}

	/**
	 * 打开发送邮件界面；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void openMail(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		String _subject = _dictParas.getOneText("subject", "");
		String _content = _dictParas.getOneText("body", "");
		String _to = _dictParas.getOneText("to", "");

		Uri _uri = Uri.parse("mailto:" + _to);
		Intent _intent = new Intent(Intent.ACTION_SENDTO, _uri);
		_intent.putExtra(Intent.EXTRA_SUBJECT, _subject);
		_intent.putExtra(Intent.EXTRA_TEXT, _content);
		_activity.startActivity(_intent);
	}

	/**
	 * 打开发送短信界面；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void openSMS(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		String _number = _dictParas.getOneText("number", "");
		String _body = _dictParas.getOneText("body", "");
		Uri _smsToUri = Uri.parse("smsto:" + _number);
		Intent _intent = new Intent(Intent.ACTION_SENDTO, _smsToUri);
		_intent.putExtra("sms_body", _body);
		_activity.startActivity(_intent);
	}

	/**
	 * 调用系统默认浏览器；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void openURL(DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		Activity _activity = (Activity) _scriptEngine.getCurrentPage().getPageView();
		String _url = _dictParas.getOneText("url", "");
		Intent _intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
		_activity.startActivity(_intent);
	}

}