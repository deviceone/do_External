//
//  TYPEID_SM.h
//  DoExt_API
//
//  Created by @userName on @time.
//  Copyright (c) 2015年 DoExt. All rights reserved.
//

#import "DoExt_External_ISM.h"

#import <MessageUI/MessageUI.h>
#import "doSingletonModule.h"

@interface DoExt_External_SM : doSingletonModule<DoExt_External_ISM,MFMessageComposeViewControllerDelegate>

@end
