//
//  TYPEID_SM.m
//  DoExt_API
//
//  Created by @userName on @time.
//  Copyright (c) 2015年 DoExt. All rights reserved.
//

#import "DoExt_External_SM.h"

#import "doScriptEngineHelper.h"
#import "doIScriptEngine.h"
#import "doInvokeResult.h"

#import <UIKit/UIKit.h>
#import "doIOHelper.h"
#import "doIPage.h"

@implementation DoExt_External_SM
#pragma mark -
#pragma mark - 同步异步方法的实现
-(void)openApp:(NSArray *)parms
{
    doJsonNode * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString* _wakeupid = [_dictParas GetOneText:@"wakeupid" :@""];
    doJsonNode *_data = [_dictParas GetOneNode:@"data"];
    NSMutableArray *_arr = [_data GetAllValues];
    NSMutableString *_openparms= [[NSMutableString alloc]init];
    for(doJsonValue *_entry  in _arr)
    {
        NSString *key = _entry.NodeName;
        NSString *value = [_entry GetText:@""];
        [_openparms appendString:[NSString stringWithFormat:@"%@&%@",key,value]];
    }
    [self openExternal:[NSString stringWithFormat:@"%@://%@",_wakeupid,_openparms]:_invokeResult];
}

//调用系统默认浏览器打开指定url
- (void) openURL:(NSArray*) parms
{
    doJsonNode * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString* callUrl = [_dictParas GetOneText:@"url" :@""];
    NSString *openStr;
    //打开系统浏览器
    if ([callUrl hasPrefix:@"http://"]||[callUrl hasPrefix:@"https://"]) {
        openStr = callUrl;
    } else if([callUrl hasPrefix:@"itms-services"])
    {
        openStr = callUrl;
    }
    else{
        openStr = [NSString stringWithFormat:@"http://%@",callUrl];
    }
    [self openExternal:openStr :_invokeResult];
}

//拨打指定电话号码
- (void) openDial:(NSArray*) parms
{
    doJsonNode * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString* _url = [_dictParas GetOneText:@"number" :@""];
    [self openExternal:[NSString stringWithFormat:@"tel://%@",_url] :_invokeResult];
}

//打开系统通讯录
- (void) openContact:(NSArray*) parms
{
    //iOS 不支持
}

- (void) openMail:(NSArray*) parms
{
    doJsonNode * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString *subject =[_dictParas GetOneText:@"subject" :@""];
    NSString *body = [_dictParas GetOneText:@"body" :@""];
    NSString *address = [_dictParas GetOneText:@"fromAddress" :@""];
    NSString *cc =[_dictParas GetOneText:@"toAddress" :@""];
    NSString *path = [NSString stringWithFormat:@"mailto:%@?cc=%@&subject=%@&body=%@", address, cc, subject, body];
    
    [self openExternal:path:_invokeResult];
}

- (void) openSMS:(NSArray*) parms
{
    doJsonNode * _dictParas =[parms objectAtIndex:0];
    id<doIScriptEngine> ii =[parms objectAtIndex:1];
    
    NSString *_number =[_dictParas GetOneText:@"number" :@""];
    NSString *_message = [_dictParas GetOneText:@"message" :@""];
    
    MFMessageComposeViewController * controller = [[MFMessageComposeViewController alloc]init]; //autorelease];
    
    controller.recipients = [NSArray arrayWithObject:_number];
    controller.body = _message;
    UIViewController *viewcontroller = (UIViewController *)ii.CurrentPage.PageView;
    controller.messageComposeDelegate = self;
    
    [viewcontroller presentViewController:controller animated:YES completion:^{
        
    }];
}

-(void)openExternal:(NSString *)_url : (doInvokeResult *)_invokeResult
{
    if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:[_url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]]) {
        
        // 成功回调
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[_url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]];
    }else {
        //失败回调
    }
}

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
    //没有回调  不做处理  必须实现 不然有警告
}

@end
