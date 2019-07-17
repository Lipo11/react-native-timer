#import <React/RCTLog.h>
#import "RNTUiTimerManager.h"
#import "RNTUiTimer.h"

@implementation RNTUiTimerManager

RCT_EXPORT_MODULE()

#pragma mark - Properties

RCT_EXPORT_VIEW_PROPERTY(timeout, NSNumber)

#pragma mark - Lifecycle

- (UIView *)view
{
    RNTUiTimer * view;
    view = [[RNTUiTimer alloc] init];
    return view;
}

@end
