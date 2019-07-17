#ifndef RNTUiTimerManager_h
#define RNTUiTimerManager_h

#import <React/RCTViewManager.h>
#import "RNTUiTimer.h"

@interface RNTUiTimerManager : RCTViewManager

@property (strong) RNTUiTimer *timer;

@end

#endif /* RNTUiTimerManager_h */
