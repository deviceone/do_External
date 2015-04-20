using doCore.Helper.JsonParse;
using doCore.Interface;
using doCore.Object;
using do_External.extdefine;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using doCore;
using doCore.Helper;
using Windows.ApplicationModel.Contacts;

namespace do_External.extimplement
{
    /// <summary>
    /// 自定义扩展API组件Model实现，继承DoSingletonModule抽象类，并实现@TYPEID_IMethod接口方法；
    /// #如何调用组件自定义事件？可以通过如下方法触发事件：
    /// this.model.getEventCenter().fireEvent(_messageName, jsonResult);
    /// 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象；
    /// 获取DoInvokeResult对象方式this.model.getCurrentPage().getScriptEngine().createInvokeResult(model.getUniqueKey());
    /// </summary>
    public class do_External_Model : doSingletonModule, do_External_IMethod
    {
        public do_External_Model()
            : base()
        {

        }
        public override bool InvokeSyncMethod(string _methodName, doCore.Helper.JsonParse.doJsonNode _dictParas, doCore.Interface.doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            if ("openApp".Equals(_methodName))
            {
                this.openApp(_dictParas, _scriptEngine, _invokeResult);
                return true;
            }
            if ("openURL".Equals(_methodName))
            {
                this.openURL(_dictParas, _scriptEngine, _invokeResult);
                return true;
            }
            if ("openDial".Equals(_methodName))
            {
                this.openDial(_dictParas, _scriptEngine, _invokeResult);
                return true;
            }
            if ("openContact".Equals(_methodName))
            {
                this.openContact(_dictParas, _scriptEngine, _invokeResult);
                return true;
            }
            if ("openMail".Equals(_methodName))
            {
                this.openMail(_dictParas, _scriptEngine, _invokeResult);
                return true;
            }
            if ("openSMS".Equals(_methodName))
            {
                this.openSMS(_dictParas, _scriptEngine, _invokeResult);
                return true;
            }
            return base.InvokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
        }
        private async void openApp(doJsonNode _dictParas, doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            try
            {
                string wakeupid = _dictParas.GetOneText("wakeupid", "");
                if (!string.IsNullOrEmpty(wakeupid))
                {
                    await Windows.System.Launcher.LaunchUriAsync(new Uri(wakeupid));
                }
            }
            catch (Exception _err)
            {
                doServiceContainer.LogEngine.WriteError("doExternal openApp \n", _err);
            }
        }
        private async void openURL(doJsonNode _dictParas, doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            try
            {
                string url = _dictParas.GetOneText("url", "");
                await Windows.System.Launcher.LaunchUriAsync(new Uri(url));
            }
            catch (Exception _err)
            {
                doServiceContainer.LogEngine.WriteError("doExternal openURL \n", _err);
            }
        }
        private async void openDial(doJsonNode _dictParas, doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            try
            {
                string number = _dictParas.GetOneText("number", "");
                if (doMyTools.EnvironmentOs == "WINDOWS_PHONE_APP")
                {
                    await Windows.System.Launcher.LaunchUriAsync(new Uri("callto:" + number));
                }
            }
            catch (Exception _err)
            {
                doServiceContainer.LogEngine.WriteError("doExternal openDial \n", _err);
            }
        }
        private async void openContact(doJsonNode _dictParas, doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            try
            {
                var contactPicker = new Windows.ApplicationModel.Contacts.ContactPicker();
                contactPicker.DesiredFieldsWithContactFieldType.Add(ContactFieldType.PhoneNumber);
                await contactPicker.PickContactAsync();
            }
            catch (Exception _err)
            {
                doServiceContainer.LogEngine.WriteError("doExternal openContact \n", _err);
            }
        }
        private async void openMail(doJsonNode _dictParas, doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            try
            {
                string to = _dictParas.GetOneText("to", "");
                string subject = _dictParas.GetOneText("subject", "");
                string content = _dictParas.GetOneText("body", "");
                List<string> ls = new List<string>();
                ls.Add(to);
                ls.Add(subject);
                ls.Add(content);
                await (doMyTools.lsAsync["Url"].FuncDo)(ls);
            }
            catch (Exception _err)
            {
                doServiceContainer.LogEngine.WriteError("doExternal openMail \n", _err);
            }
        }
        private void openSMS(doJsonNode _dictParas, doIScriptEngine _scriptEngine, doInvokeResult _invokeResult)
        {
            try
            {
                string _content = _dictParas.GetOneText("body", "HelloWorld");
                string _number = _dictParas.GetOneText("number", "10086");
                List<string> ls = new List<string>();
                ls.Add(_content);
                ls.Add(_number);
                doMyTools.lsSync["sms"].FuncDo(ls);
            }
            catch (Exception _err)
            {
                doServiceContainer.LogEngine.WriteError("doExternal openSMS \n", _err);
            }
        }

        public override async Task<bool> InvokeAsyncMethod(string _methodName, doCore.Helper.JsonParse.doJsonNode _dictParas, doCore.Interface.doIScriptEngine _scriptEngine, string _callbackFuncName)
        {
            doInvokeResult _invokeResult = new doInvokeResult(this.UniqueKey);

            return await base.InvokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
        }
    }
    
}
