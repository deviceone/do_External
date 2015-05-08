//
//  TYPEID_SM.m
//  DoExt_API
//
//  Created by @userName on @time.
//  Copyright (c) 2015年 DoExt. All rights reserved.
//

#import "do_External_SM.h"

#import "doScriptEngineHelper.h"
#import "doIScriptEngine.h"
#import "doInvokeResult.h"

#import <UIKit/UIKit.h>
#import "doIOHelper.h"
#import "doIPage.h"
#import "doJsonHelper.h"

@implementation do_External_SM
#pragma mark -
#pragma mark - 同步异步方法的实现
-(void)openApp:(NSArray *)parms
{
    NSDictionary * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString* _wakeupid = [doJsonHelper GetOneText:_dictParas :@"wakeupid" :@""];
    
    NSDictionary *_data = [doJsonHelper GetOneNode:_dictParas :@"data"];

    NSArray *_arr = [_data allKeys];
    NSMutableString *_openparms= [[NSMutableString alloc]init];
    for(NSString *_entry  in _arr)
    {
        NSString *key = _entry;
        NSString *value = [_data objectForKey:_entry];
        [_openparms appendString:[NSString stringWithFormat:@"%@&%@",key,value]];
    }
    [self openExternal:[NSString stringWithFormat:@"%@://%@",_wakeupid,_openparms]:_invokeResult];
}

//调用系统默认浏览器打开指定url
- (void) openURL:(NSArray*) parms
{
    NSDictionary * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString* callUrl = [doJsonHelper GetOneText:_dictParas :@"url" :@""];
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
    NSDictionary * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString* _url = [doJsonHelper GetOneText:_dictParas :@"number" :@""];
    [self openExternal:[NSString stringWithFormat:@"tel://%@",_url] :_invokeResult];
}

//打开系统通讯录
- (void) openContact:(NSArray*) parms
{
    //iOS 不支持
}

- (void) openMail:(NSArray*) parms
{
    NSDictionary * _dictParas =[parms objectAtIndex:0];
    doInvokeResult * _invokeResult = [parms objectAtIndex:2];
    NSString *subject =[doJsonHelper GetOneText:_dictParas :@"subject" :@""];
    NSString *body = [doJsonHelper GetOneText:_dictParas :@"body" :@""];
    NSString *address = [doJsonHelper GetOneText:_dictParas :@"fromAddress" :@""];
    NSString *cc =[doJsonHelper GetOneText:_dictParas :@"toAddress" :@""];
    NSString *path = [NSString stringWithFormat:@"mailto:%@?cc=%@&subject=%@&body=%@", address, cc, subject, body];
    
    [self openExternal:path:_invokeResult];
}

- (void) openSMS:(NSArray*) parms
{
    NSDictionary * _dictParas =[parms objectAtIndex:0];
    id<doIScriptEngine> ii =[parms objectAtIndex:1];
    
    NSString *_number =[doJsonHelper GetOneText:_dictParas :@"number" :@""];
    NSString *_message = [doJsonHelper GetOneText:_dictParas :@"message" :@""];
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
