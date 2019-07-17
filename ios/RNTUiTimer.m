#import <UIKit/UIKit.h>
#import <React/RCTView.h>
#import <React/UIView+React.h>
#import <React/RCTLog.h>
#import "RNTUiTimer.h"
#import <math.h>

@implementation RNTUiTimer
{
    double start;
    double speed;
    double tail_length_min;
    double tail_length_max;
}

#pragma mark - UIViewHierarchy methods

- (instancetype)init
{
    if ((self = [super init]))
    {
        start = [[NSDate date] timeIntervalSince1970];
        speed = 2;
        tail_length_min = M_PI / 16;
        tail_length_max = M_PI * 3 / 2;
        
        [NSTimer scheduledTimerWithTimeInterval:0.03 target:self selector:@selector(redrawTimer) userInfo:nil repeats:YES];
    }
    return self;
}

- (void)layoutSubviews
{
    [super layoutSubviews];
}

- (void)redrawTimer
{
    [self setNeedsDisplay];
}

- (void) drawCenteredString: (NSString*) s withFont: (UIFont*) font inRect: (CGRect) contextRect
{
    NSMutableParagraphStyle *paragraphStyle = [[NSParagraphStyle defaultParagraphStyle] mutableCopy];
    paragraphStyle.lineBreakMode = NSLineBreakByTruncatingTail;
    paragraphStyle.alignment = NSTextAlignmentCenter;
    
    NSDictionary *attributes = @{ NSFontAttributeName: font, NSForegroundColorAttributeName: [UIColor whiteColor], NSParagraphStyleAttributeName: paragraphStyle };
    CGSize size = [s sizeWithAttributes:attributes];
    CGRect textRect = CGRectMake(contextRect.origin.x + floorf((contextRect.size.width - size.width) / 2), contextRect.origin.y + floorf((contextRect.size.height - size.height) / 2), size.width, size.height);
    
    [s drawInRect:textRect withAttributes:attributes];
}

- (void)drawRect:(CGRect)rect
{
    double elapsed = MIN([self.timeout doubleValue] * 1000, ([[NSDate date] timeIntervalSince1970] - start) * 1000);
    CGFloat size = MIN( rect.size.width, rect.size.height );
    CGFloat centerX = rect.size.width / 2;
    CGFloat centerY = rect.size.height / 2;
    CGFloat second_fraction = fmodf( elapsed, 1000 ) / 1000;
    CGFloat velocity = ( second_fraction < 0.5 ? pow(second_fraction * 2, speed) / 2 : pow( ( 1 - second_fraction ) * 2, speed) / 2 );
    
    CGFloat second_end_angle = 2 * M_PI * ( second_fraction < 0.5 ? velocity : 1 - velocity ) + tail_length_min + 3 * M_PI / 2;
    CGFloat second_start_angle = second_end_angle - tail_length_min - ( tail_length_max - tail_length_min ) * velocity;
    CGFloat total_start_angle = 3 * M_PI / 2;
    CGFloat total_end_angle = total_start_angle + 2 * M_PI * ( elapsed / ( [self.timeout doubleValue] * 1000 ) );
    
    CGContextRef c = UIGraphicsGetCurrentContext();
    CGContextClearRect(c, rect);
    
    CGContextBeginPath(c);
    CGContextSetLineWidth(c, 1.0);
    CGContextAddArc(c, centerX, centerY, size / 2 - 2, second_start_angle, second_end_angle, NO);
    CGContextSetStrokeColorWithColor(c, [UIColor whiteColor].CGColor);
    CGContextDrawPath(c, kCGPathStroke);
    
    CGContextBeginPath(c);
    CGContextSetLineWidth(c, 3.0);
    CGContextAddArc(c, centerX, centerY, size / 2 - 2, total_start_angle, total_end_angle, NO);
    CGContextSetStrokeColorWithColor(c, [UIColor whiteColor].CGColor);
    CGContextDrawPath(c, kCGPathStroke);
    
    [self drawCenteredString:
     [NSString stringWithFormat:@"%d", (int)ceil((( [self.timeout doubleValue] * 1000 ) - elapsed)/1000)]
                    withFont: [UIFont fontWithName: @"Helvetica" size: size / 2.8]
                      inRect:rect];
}

@end
