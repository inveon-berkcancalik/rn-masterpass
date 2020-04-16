//
//  MfsEditText.h
//  MfsIOSLibrary
//
//  Created by ercu on 09/04/15.
//  Copyright (c) 2015 Phaymobile. All rights reserved.
//

#ifndef MfsIOSLibrary_MfsEditText_h
#define MfsIOSLibrary_MfsEditText_h

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol MfsTextFieldDelegate <NSObject>

@optional

- (void) mfsTextFieldGetCardType:(NSInteger)cardType;

- (void) mfsTextFieldGetFirst6Chars:(NSString*)first6Chars;

- (void) mfsTextFieldCancelInstallment;

- (void) mfsTextFieldShouldBeginEditing:(NSInteger)typeId;

- (void) mfsTextFieldDidBeginEditing:(NSInteger)typeId;

- (void) mfsTextFieldDidEndEditing:(NSInteger)typeId;

- (void) mfsTextFieldEditingChange:(NSInteger)typeId;

- (void) mfsTextFieldDidClear:(NSInteger)typeId;

@end

@interface MfsTextField : UITextField
- (void)setSystemId:(NSString*)systemId;
- (void)setMaxLength:(NSUInteger)maxLength;
- (void)setType:(NSInteger)typeId;
- (BOOL)isEqualTo:(MfsTextField*)textField2;
- (void)clear;
- (BOOL)validateInput;
- (BOOL)isEmpty;
- (NSString*)getFirst6Chars;
- (void)setSystemIdforGet:(NSString*)system_Id;
- (void)setTextAlignment:(NSTextAlignment)textAlignment;
- (void)setFirst6Digit:(NSString*)first6Digit;
- (NSInteger)getTextLength;

@property (nonatomic, weak) id<MfsTextFieldDelegate> mfsDelegate;

@property (nonatomic, assign) UIEdgeInsets edgeInsets;
@property (nonatomic, assign) NSDictionary *attrDictionary;

@property (nonatomic)  NSUInteger maxLength;


@end


#endif
