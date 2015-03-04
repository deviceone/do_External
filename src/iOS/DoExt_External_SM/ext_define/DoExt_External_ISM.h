//
//  TYPEID_IMethod.h
//  DoExt_API
//
//  Created by @userName on @time.
//  Copyright (c) 2015年 DoExt. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "doInvokeResult.h"

@protocol DoExt_External_ISM <NSObject>

@required
//实现同步或异步方法，parms中包含了所需用的属性
- (void)openApp:(NSArray *)parms;
- (void) openURL:(NSArray*) parms;
- (void) openContact:(NSArray*) parms;
- (void) openMail:(NSArray*) parms;
- (void) openSMS:(NSArray*) parms;
-(void)openExternal:(NSString *)_url : (doInvokeResult *)_invokeResult;

@end
