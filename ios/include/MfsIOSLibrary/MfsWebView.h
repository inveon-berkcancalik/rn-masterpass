//
//  OneWebView.h
//  OneClickiOSLibraryExampleObjC
//
//  Created by ercu on 23/10/15.
//  Copyright © 2015 Phaymobile. All rights reserved.
//

#ifndef OneWebView_h
#define OneWebView_h


#import <WebKit/WebKit.h>


@protocol MfsWebViewCallback <NSObject>
- (void)webViewStartLoading:(WKWebView *)webView;

- (void)webViewFinishLoading:(WKWebView *)webView;
@end


@interface MfsWebView : WKWebView

@property (nonatomic, strong) NSString *token;
@property (nonatomic, strong) NSString *url3D;
@property (nonatomic, strong) NSString *url3DSuccess;
@property (nonatomic, strong) NSString *url3DError;

@property (readwrite, nonatomic) SEL validateSelector;
@property (readwrite, nonatomic, strong) id validateHandler;

@property int startedJobs;

@property (nonatomic, weak) id<MfsWebViewCallback> delegateMfsWebView;

-(id)initWithScale;

@end

#endif /* OneWebView_h */
